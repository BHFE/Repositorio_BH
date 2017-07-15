/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.SgAutor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.SgObraAutor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class SgAutorJpaController implements Serializable {

    public SgAutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgAutor sgAutor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraAutor sgObraAutor = sgAutor.getSgObraAutor();
            if (sgObraAutor != null) {
                sgObraAutor = em.getReference(sgObraAutor.getClass(), sgObraAutor.getIdautor());
                sgAutor.setSgObraAutor(sgObraAutor);
            }
            em.persist(sgAutor);
            if (sgObraAutor != null) {
                SgAutor oldSgAutorOfSgObraAutor = sgObraAutor.getSgAutor();
                if (oldSgAutorOfSgObraAutor != null) {
                    oldSgAutorOfSgObraAutor.setSgObraAutor(null);
                    oldSgAutorOfSgObraAutor = em.merge(oldSgAutorOfSgObraAutor);
                }
                sgObraAutor.setSgAutor(sgAutor);
                sgObraAutor = em.merge(sgObraAutor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgAutor sgAutor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgAutor persistentSgAutor = em.find(SgAutor.class, sgAutor.getIdautor());
            SgObraAutor sgObraAutorOld = persistentSgAutor.getSgObraAutor();
            SgObraAutor sgObraAutorNew = sgAutor.getSgObraAutor();
            List<String> illegalOrphanMessages = null;
            if (sgObraAutorOld != null && !sgObraAutorOld.equals(sgObraAutorNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain SgObraAutor " + sgObraAutorOld + " since its sgAutor field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (sgObraAutorNew != null) {
                sgObraAutorNew = em.getReference(sgObraAutorNew.getClass(), sgObraAutorNew.getIdautor());
                sgAutor.setSgObraAutor(sgObraAutorNew);
            }
            sgAutor = em.merge(sgAutor);
            if (sgObraAutorNew != null && !sgObraAutorNew.equals(sgObraAutorOld)) {
                SgAutor oldSgAutorOfSgObraAutor = sgObraAutorNew.getSgAutor();
                if (oldSgAutorOfSgObraAutor != null) {
                    oldSgAutorOfSgObraAutor.setSgObraAutor(null);
                    oldSgAutorOfSgObraAutor = em.merge(oldSgAutorOfSgObraAutor);
                }
                sgObraAutorNew.setSgAutor(sgAutor);
                sgObraAutorNew = em.merge(sgObraAutorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sgAutor.getIdautor();
                if (findSgAutor(id) == null) {
                    throw new NonexistentEntityException("The sgAutor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgAutor sgAutor;
            try {
                sgAutor = em.getReference(SgAutor.class, id);
                sgAutor.getIdautor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgAutor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            SgObraAutor sgObraAutorOrphanCheck = sgAutor.getSgObraAutor();
            if (sgObraAutorOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SgAutor (" + sgAutor + ") cannot be destroyed since the SgObraAutor " + sgObraAutorOrphanCheck + " in its sgObraAutor field has a non-nullable sgAutor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(sgAutor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgAutor> findSgAutorEntities() {
        return findSgAutorEntities(true, -1, -1);
    }

    public List<SgAutor> findSgAutorEntities(int maxResults, int firstResult) {
        return findSgAutorEntities(false, maxResults, firstResult);
    }

    private List<SgAutor> findSgAutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgAutor.class));
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

    public SgAutor findSgAutor(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgAutor.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgAutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgAutor> rt = cq.from(SgAutor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
