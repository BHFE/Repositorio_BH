/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Funcionario;
import entidades.Tipochefia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
 */
public class TipochefiaJpaController implements Serializable {

    public TipochefiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipochefia tipochefia) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Funcionario funcionarioOrphanCheck = tipochefia.getFuncionario();
        if (funcionarioOrphanCheck != null) {
            Tipochefia oldTipochefiaOfFuncionario = funcionarioOrphanCheck.getTipochefia();
            if (oldTipochefiaOfFuncionario != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Funcionario " + funcionarioOrphanCheck + " already has an item of type Tipochefia whose funcionario column cannot be null. Please make another selection for the funcionario field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario funcionario = tipochefia.getFuncionario();
            if (funcionario != null) {
                funcionario = em.getReference(funcionario.getClass(), funcionario.getIdFuncionario());
                tipochefia.setFuncionario(funcionario);
            }
            em.persist(tipochefia);
            if (funcionario != null) {
                funcionario.setTipochefia(tipochefia);
                funcionario = em.merge(funcionario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipochefia(tipochefia.getIdfuncionario()) != null) {
                throw new PreexistingEntityException("Tipochefia " + tipochefia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipochefia tipochefia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipochefia persistentTipochefia = em.find(Tipochefia.class, tipochefia.getIdfuncionario());
            Funcionario funcionarioOld = persistentTipochefia.getFuncionario();
            Funcionario funcionarioNew = tipochefia.getFuncionario();
            List<String> illegalOrphanMessages = null;
            if (funcionarioNew != null && !funcionarioNew.equals(funcionarioOld)) {
                Tipochefia oldTipochefiaOfFuncionario = funcionarioNew.getTipochefia();
                if (oldTipochefiaOfFuncionario != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Funcionario " + funcionarioNew + " already has an item of type Tipochefia whose funcionario column cannot be null. Please make another selection for the funcionario field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (funcionarioNew != null) {
                funcionarioNew = em.getReference(funcionarioNew.getClass(), funcionarioNew.getIdFuncionario());
                tipochefia.setFuncionario(funcionarioNew);
            }
            tipochefia = em.merge(tipochefia);
            if (funcionarioOld != null && !funcionarioOld.equals(funcionarioNew)) {
                funcionarioOld.setTipochefia(null);
                funcionarioOld = em.merge(funcionarioOld);
            }
            if (funcionarioNew != null && !funcionarioNew.equals(funcionarioOld)) {
                funcionarioNew.setTipochefia(tipochefia);
                funcionarioNew = em.merge(funcionarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tipochefia.getIdfuncionario();
                if (findTipochefia(id) == null) {
                    throw new NonexistentEntityException("The tipochefia with id " + id + " no longer exists.");
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
            Tipochefia tipochefia;
            try {
                tipochefia = em.getReference(Tipochefia.class, id);
                tipochefia.getIdfuncionario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipochefia with id " + id + " no longer exists.", enfe);
            }
            Funcionario funcionario = tipochefia.getFuncionario();
            if (funcionario != null) {
                funcionario.setTipochefia(null);
                funcionario = em.merge(funcionario);
            }
            em.remove(tipochefia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipochefia> findTipochefiaEntities() {
        return findTipochefiaEntities(true, -1, -1);
    }

    public List<Tipochefia> findTipochefiaEntities(int maxResults, int firstResult) {
        return findTipochefiaEntities(false, maxResults, firstResult);
    }

    private List<Tipochefia> findTipochefiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipochefia.class));
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

    public Tipochefia findTipochefia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipochefia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipochefiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipochefia> rt = cq.from(Tipochefia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
