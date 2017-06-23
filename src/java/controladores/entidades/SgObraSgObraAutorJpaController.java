/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.SgObraSgObraAutor;
import entidades.SgObraSgObraAutorPK;
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
public class SgObraSgObraAutorJpaController implements Serializable {

    public SgObraSgObraAutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgObraSgObraAutor sgObraSgObraAutor) throws PreexistingEntityException, Exception {
        if (sgObraSgObraAutor.getSgObraSgObraAutorPK() == null) {
            sgObraSgObraAutor.setSgObraSgObraAutorPK(new SgObraSgObraAutorPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(sgObraSgObraAutor);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSgObraSgObraAutor(sgObraSgObraAutor.getSgObraSgObraAutorPK()) != null) {
                throw new PreexistingEntityException("SgObraSgObraAutor " + sgObraSgObraAutor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgObraSgObraAutor sgObraSgObraAutor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            sgObraSgObraAutor = em.merge(sgObraSgObraAutor);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SgObraSgObraAutorPK id = sgObraSgObraAutor.getSgObraSgObraAutorPK();
                if (findSgObraSgObraAutor(id) == null) {
                    throw new NonexistentEntityException("The sgObraSgObraAutor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SgObraSgObraAutorPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraSgObraAutor sgObraSgObraAutor;
            try {
                sgObraSgObraAutor = em.getReference(SgObraSgObraAutor.class, id);
                sgObraSgObraAutor.getSgObraSgObraAutorPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgObraSgObraAutor with id " + id + " no longer exists.", enfe);
            }
            em.remove(sgObraSgObraAutor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgObraSgObraAutor> findSgObraSgObraAutorEntities() {
        return findSgObraSgObraAutorEntities(true, -1, -1);
    }

    public List<SgObraSgObraAutor> findSgObraSgObraAutorEntities(int maxResults, int firstResult) {
        return findSgObraSgObraAutorEntities(false, maxResults, firstResult);
    }

    private List<SgObraSgObraAutor> findSgObraSgObraAutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgObraSgObraAutor.class));
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

    public SgObraSgObraAutor findSgObraSgObraAutor(SgObraSgObraAutorPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgObraSgObraAutor.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgObraSgObraAutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgObraSgObraAutor> rt = cq.from(SgObraSgObraAutor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
