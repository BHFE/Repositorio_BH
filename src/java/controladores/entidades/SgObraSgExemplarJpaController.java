/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.SgObraSgExemplar;
import entidades.SgObraSgExemplarPK;
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
public class SgObraSgExemplarJpaController implements Serializable {

    public SgObraSgExemplarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgObraSgExemplar sgObraSgExemplar) throws PreexistingEntityException, Exception {
        if (sgObraSgExemplar.getSgObraSgExemplarPK() == null) {
            sgObraSgExemplar.setSgObraSgExemplarPK(new SgObraSgExemplarPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(sgObraSgExemplar);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSgObraSgExemplar(sgObraSgExemplar.getSgObraSgExemplarPK()) != null) {
                throw new PreexistingEntityException("SgObraSgExemplar " + sgObraSgExemplar + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgObraSgExemplar sgObraSgExemplar) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            sgObraSgExemplar = em.merge(sgObraSgExemplar);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SgObraSgExemplarPK id = sgObraSgExemplar.getSgObraSgExemplarPK();
                if (findSgObraSgExemplar(id) == null) {
                    throw new NonexistentEntityException("The sgObraSgExemplar with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SgObraSgExemplarPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraSgExemplar sgObraSgExemplar;
            try {
                sgObraSgExemplar = em.getReference(SgObraSgExemplar.class, id);
                sgObraSgExemplar.getSgObraSgExemplarPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgObraSgExemplar with id " + id + " no longer exists.", enfe);
            }
            em.remove(sgObraSgExemplar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgObraSgExemplar> findSgObraSgExemplarEntities() {
        return findSgObraSgExemplarEntities(true, -1, -1);
    }

    public List<SgObraSgExemplar> findSgObraSgExemplarEntities(int maxResults, int firstResult) {
        return findSgObraSgExemplarEntities(false, maxResults, firstResult);
    }

    private List<SgObraSgExemplar> findSgObraSgExemplarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgObraSgExemplar.class));
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

    public SgObraSgExemplar findSgObraSgExemplar(SgObraSgExemplarPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgObraSgExemplar.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgObraSgExemplarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgObraSgExemplar> rt = cq.from(SgObraSgExemplar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
