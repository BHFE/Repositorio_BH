/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.Docente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Funcionario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class DocenteJpaController implements Serializable {

    public DocenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Docente docente) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Funcionario funcionarioOrphanCheck = docente.getFuncionario();
        if (funcionarioOrphanCheck != null) {
            Docente oldDocenteOfFuncionario = funcionarioOrphanCheck.getDocente();
            if (oldDocenteOfFuncionario != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Funcionario " + funcionarioOrphanCheck + " already has an item of type Docente whose funcionario column cannot be null. Please make another selection for the funcionario field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario funcionario = docente.getFuncionario();
            if (funcionario != null) {
                funcionario = em.getReference(funcionario.getClass(), funcionario.getIdFuncionario());
                docente.setFuncionario(funcionario);
            }
            em.persist(docente);
            if (funcionario != null) {
                funcionario.setDocente(docente);
                funcionario = em.merge(funcionario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocente(docente.getIddocente()) != null) {
                throw new PreexistingEntityException("Docente " + docente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Docente docente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente persistentDocente = em.find(Docente.class, docente.getIddocente());
            Funcionario funcionarioOld = persistentDocente.getFuncionario();
            Funcionario funcionarioNew = docente.getFuncionario();
            List<String> illegalOrphanMessages = null;
            if (funcionarioNew != null && !funcionarioNew.equals(funcionarioOld)) {
                Docente oldDocenteOfFuncionario = funcionarioNew.getDocente();
                if (oldDocenteOfFuncionario != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Funcionario " + funcionarioNew + " already has an item of type Docente whose funcionario column cannot be null. Please make another selection for the funcionario field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (funcionarioNew != null) {
                funcionarioNew = em.getReference(funcionarioNew.getClass(), funcionarioNew.getIdFuncionario());
                docente.setFuncionario(funcionarioNew);
            }
            docente = em.merge(docente);
            if (funcionarioOld != null && !funcionarioOld.equals(funcionarioNew)) {
                funcionarioOld.setDocente(null);
                funcionarioOld = em.merge(funcionarioOld);
            }
            if (funcionarioNew != null && !funcionarioNew.equals(funcionarioOld)) {
                funcionarioNew.setDocente(docente);
                funcionarioNew = em.merge(funcionarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = docente.getIddocente();
                if (findDocente(id) == null) {
                    throw new NonexistentEntityException("The docente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente docente;
            try {
                docente = em.getReference(Docente.class, id);
                docente.getIddocente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docente with id " + id + " no longer exists.", enfe);
            }
            Funcionario funcionario = docente.getFuncionario();
            if (funcionario != null) {
                funcionario.setDocente(null);
                funcionario = em.merge(funcionario);
            }
            em.remove(docente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Docente> findDocenteEntities() {
        return findDocenteEntities(true, -1, -1);
    }

    public List<Docente> findDocenteEntities(int maxResults, int firstResult) {
        return findDocenteEntities(false, maxResults, firstResult);
    }

    private List<Docente> findDocenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Docente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Docente findDocente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Docente.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Docente> rt = cq.from(Docente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
