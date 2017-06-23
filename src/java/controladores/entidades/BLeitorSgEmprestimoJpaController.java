/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.BLeitorSgEmprestimo;
import entidades.BLeitorSgEmprestimoPK;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Migueljr
 */
public class BLeitorSgEmprestimoJpaController implements Serializable {

    public BLeitorSgEmprestimoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BLeitorSgEmprestimo BLeitorSgEmprestimo) throws PreexistingEntityException, Exception {
        if (BLeitorSgEmprestimo.getBLeitorSgEmprestimoPK() == null) {
            BLeitorSgEmprestimo.setBLeitorSgEmprestimoPK(new BLeitorSgEmprestimoPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(BLeitorSgEmprestimo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBLeitorSgEmprestimo(BLeitorSgEmprestimo.getBLeitorSgEmprestimoPK()) != null) {
                throw new PreexistingEntityException("BLeitorSgEmprestimo " + BLeitorSgEmprestimo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BLeitorSgEmprestimo BLeitorSgEmprestimo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitorSgEmprestimo = em.merge(BLeitorSgEmprestimo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BLeitorSgEmprestimoPK id = BLeitorSgEmprestimo.getBLeitorSgEmprestimoPK();
                if (findBLeitorSgEmprestimo(id) == null) {
                    throw new NonexistentEntityException("The bLeitorSgEmprestimo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BLeitorSgEmprestimoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitorSgEmprestimo BLeitorSgEmprestimo;
            try {
                BLeitorSgEmprestimo = em.getReference(BLeitorSgEmprestimo.class, id);
                BLeitorSgEmprestimo.getBLeitorSgEmprestimoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The BLeitorSgEmprestimo with id " + id + " no longer exists.", enfe);
            }
            em.remove(BLeitorSgEmprestimo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BLeitorSgEmprestimo> findBLeitorSgEmprestimoEntities() {
        return findBLeitorSgEmprestimoEntities(true, -1, -1);
    }

    public List<BLeitorSgEmprestimo> findBLeitorSgEmprestimoEntities(int maxResults, int firstResult) {
        return findBLeitorSgEmprestimoEntities(false, maxResults, firstResult);
    }

    private List<BLeitorSgEmprestimo> findBLeitorSgEmprestimoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BLeitorSgEmprestimo.class));
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

    public BLeitorSgEmprestimo findBLeitorSgEmprestimo(BLeitorSgEmprestimoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BLeitorSgEmprestimo.class, id);
        } finally {
            em.close();
        }
    }

    public int getBLeitorSgEmprestimoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BLeitorSgEmprestimo> rt = cq.from(BLeitorSgEmprestimo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
