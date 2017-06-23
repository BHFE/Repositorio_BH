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
import java.util.Collection;
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
        if (sgAutor.getSgObraAutorCollection() == null) {
            sgAutor.setSgObraAutorCollection(new ArrayList<SgObraAutor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SgObraAutor> attachedSgObraAutorCollection = new ArrayList<SgObraAutor>();
            for (SgObraAutor sgObraAutorCollectionSgObraAutorToAttach : sgAutor.getSgObraAutorCollection()) {
                sgObraAutorCollectionSgObraAutorToAttach = em.getReference(sgObraAutorCollectionSgObraAutorToAttach.getClass(), sgObraAutorCollectionSgObraAutorToAttach.getSgObraAutorPK());
                attachedSgObraAutorCollection.add(sgObraAutorCollectionSgObraAutorToAttach);
            }
            sgAutor.setSgObraAutorCollection(attachedSgObraAutorCollection);
            em.persist(sgAutor);
            for (SgObraAutor sgObraAutorCollectionSgObraAutor : sgAutor.getSgObraAutorCollection()) {
                SgAutor oldSgAutorOfSgObraAutorCollectionSgObraAutor = sgObraAutorCollectionSgObraAutor.getSgAutor();
                sgObraAutorCollectionSgObraAutor.setSgAutor(sgAutor);
                sgObraAutorCollectionSgObraAutor = em.merge(sgObraAutorCollectionSgObraAutor);
                if (oldSgAutorOfSgObraAutorCollectionSgObraAutor != null) {
                    oldSgAutorOfSgObraAutorCollectionSgObraAutor.getSgObraAutorCollection().remove(sgObraAutorCollectionSgObraAutor);
                    oldSgAutorOfSgObraAutorCollectionSgObraAutor = em.merge(oldSgAutorOfSgObraAutorCollectionSgObraAutor);
                }
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
            Collection<SgObraAutor> sgObraAutorCollectionOld = persistentSgAutor.getSgObraAutorCollection();
            Collection<SgObraAutor> sgObraAutorCollectionNew = sgAutor.getSgObraAutorCollection();
            List<String> illegalOrphanMessages = null;
            for (SgObraAutor sgObraAutorCollectionOldSgObraAutor : sgObraAutorCollectionOld) {
                if (!sgObraAutorCollectionNew.contains(sgObraAutorCollectionOldSgObraAutor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SgObraAutor " + sgObraAutorCollectionOldSgObraAutor + " since its sgAutor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SgObraAutor> attachedSgObraAutorCollectionNew = new ArrayList<SgObraAutor>();
            for (SgObraAutor sgObraAutorCollectionNewSgObraAutorToAttach : sgObraAutorCollectionNew) {
                sgObraAutorCollectionNewSgObraAutorToAttach = em.getReference(sgObraAutorCollectionNewSgObraAutorToAttach.getClass(), sgObraAutorCollectionNewSgObraAutorToAttach.getSgObraAutorPK());
                attachedSgObraAutorCollectionNew.add(sgObraAutorCollectionNewSgObraAutorToAttach);
            }
            sgObraAutorCollectionNew = attachedSgObraAutorCollectionNew;
            sgAutor.setSgObraAutorCollection(sgObraAutorCollectionNew);
            sgAutor = em.merge(sgAutor);
            for (SgObraAutor sgObraAutorCollectionNewSgObraAutor : sgObraAutorCollectionNew) {
                if (!sgObraAutorCollectionOld.contains(sgObraAutorCollectionNewSgObraAutor)) {
                    SgAutor oldSgAutorOfSgObraAutorCollectionNewSgObraAutor = sgObraAutorCollectionNewSgObraAutor.getSgAutor();
                    sgObraAutorCollectionNewSgObraAutor.setSgAutor(sgAutor);
                    sgObraAutorCollectionNewSgObraAutor = em.merge(sgObraAutorCollectionNewSgObraAutor);
                    if (oldSgAutorOfSgObraAutorCollectionNewSgObraAutor != null && !oldSgAutorOfSgObraAutorCollectionNewSgObraAutor.equals(sgAutor)) {
                        oldSgAutorOfSgObraAutorCollectionNewSgObraAutor.getSgObraAutorCollection().remove(sgObraAutorCollectionNewSgObraAutor);
                        oldSgAutorOfSgObraAutorCollectionNewSgObraAutor = em.merge(oldSgAutorOfSgObraAutorCollectionNewSgObraAutor);
                    }
                }
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
            Collection<SgObraAutor> sgObraAutorCollectionOrphanCheck = sgAutor.getSgObraAutorCollection();
            for (SgObraAutor sgObraAutorCollectionOrphanCheckSgObraAutor : sgObraAutorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SgAutor (" + sgAutor + ") cannot be destroyed since the SgObraAutor " + sgObraAutorCollectionOrphanCheckSgObraAutor + " in its sgObraAutorCollection field has a non-nullable sgAutor field.");
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
