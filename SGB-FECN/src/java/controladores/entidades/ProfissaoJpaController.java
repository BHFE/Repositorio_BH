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
import entidades.Estudante;
import entidades.Profissao;
import entidades.Provincia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
 */
public class ProfissaoJpaController implements Serializable {

    public ProfissaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profissao profissao) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Estudante estudanteOrphanCheck = profissao.getEstudante();
        if (estudanteOrphanCheck != null) {
            Profissao oldProfissaoOfEstudante = estudanteOrphanCheck.getProfissao();
            if (oldProfissaoOfEstudante != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Estudante " + estudanteOrphanCheck + " already has an item of type Profissao whose estudante column cannot be null. Please make another selection for the estudante field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudante estudante = profissao.getEstudante();
            if (estudante != null) {
                estudante = em.getReference(estudante.getClass(), estudante.getIdEstudante());
                profissao.setEstudante(estudante);
            }
            Provincia provinciapr = profissao.getProvinciapr();
            if (provinciapr != null) {
                provinciapr = em.getReference(provinciapr.getClass(), provinciapr.getIdProvincia());
                profissao.setProvinciapr(provinciapr);
            }
            em.persist(profissao);
            if (estudante != null) {
                estudante.setProfissao(profissao);
                estudante = em.merge(estudante);
            }
            if (provinciapr != null) {
                provinciapr.getProfissaoList().add(profissao);
                provinciapr = em.merge(provinciapr);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfissao(profissao.getIdEstudante()) != null) {
                throw new PreexistingEntityException("Profissao " + profissao + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profissao profissao) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profissao persistentProfissao = em.find(Profissao.class, profissao.getIdEstudante());
            Estudante estudanteOld = persistentProfissao.getEstudante();
            Estudante estudanteNew = profissao.getEstudante();
            Provincia provinciaprOld = persistentProfissao.getProvinciapr();
            Provincia provinciaprNew = profissao.getProvinciapr();
            List<String> illegalOrphanMessages = null;
            if (estudanteNew != null && !estudanteNew.equals(estudanteOld)) {
                Profissao oldProfissaoOfEstudante = estudanteNew.getProfissao();
                if (oldProfissaoOfEstudante != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Estudante " + estudanteNew + " already has an item of type Profissao whose estudante column cannot be null. Please make another selection for the estudante field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estudanteNew != null) {
                estudanteNew = em.getReference(estudanteNew.getClass(), estudanteNew.getIdEstudante());
                profissao.setEstudante(estudanteNew);
            }
            if (provinciaprNew != null) {
                provinciaprNew = em.getReference(provinciaprNew.getClass(), provinciaprNew.getIdProvincia());
                profissao.setProvinciapr(provinciaprNew);
            }
            profissao = em.merge(profissao);
            if (estudanteOld != null && !estudanteOld.equals(estudanteNew)) {
                estudanteOld.setProfissao(null);
                estudanteOld = em.merge(estudanteOld);
            }
            if (estudanteNew != null && !estudanteNew.equals(estudanteOld)) {
                estudanteNew.setProfissao(profissao);
                estudanteNew = em.merge(estudanteNew);
            }
            if (provinciaprOld != null && !provinciaprOld.equals(provinciaprNew)) {
                provinciaprOld.getProfissaoList().remove(profissao);
                provinciaprOld = em.merge(provinciaprOld);
            }
            if (provinciaprNew != null && !provinciaprNew.equals(provinciaprOld)) {
                provinciaprNew.getProfissaoList().add(profissao);
                provinciaprNew = em.merge(provinciaprNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = profissao.getIdEstudante();
                if (findProfissao(id) == null) {
                    throw new NonexistentEntityException("The profissao with id " + id + " no longer exists.");
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
            Profissao profissao;
            try {
                profissao = em.getReference(Profissao.class, id);
                profissao.getIdEstudante();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profissao with id " + id + " no longer exists.", enfe);
            }
            Estudante estudante = profissao.getEstudante();
            if (estudante != null) {
                estudante.setProfissao(null);
                estudante = em.merge(estudante);
            }
            Provincia provinciapr = profissao.getProvinciapr();
            if (provinciapr != null) {
                provinciapr.getProfissaoList().remove(profissao);
                provinciapr = em.merge(provinciapr);
            }
            em.remove(profissao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profissao> findProfissaoEntities() {
        return findProfissaoEntities(true, -1, -1);
    }

    public List<Profissao> findProfissaoEntities(int maxResults, int firstResult) {
        return findProfissaoEntities(false, maxResults, firstResult);
    }

    private List<Profissao> findProfissaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profissao.class));
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

    public Profissao findProfissao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profissao.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfissaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profissao> rt = cq.from(Profissao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
