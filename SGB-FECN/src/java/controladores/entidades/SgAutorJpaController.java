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
 * @author Nobrega
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
        if (sgAutor.getSgObraAutorList() == null) {
            sgAutor.setSgObraAutorList(new ArrayList<SgObraAutor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SgObraAutor> attachedSgObraAutorList = new ArrayList<SgObraAutor>();
            for (SgObraAutor sgObraAutorListSgObraAutorToAttach : sgAutor.getSgObraAutorList()) {
                sgObraAutorListSgObraAutorToAttach = em.getReference(sgObraAutorListSgObraAutorToAttach.getClass(), sgObraAutorListSgObraAutorToAttach.getSgObraAutorPK());
                attachedSgObraAutorList.add(sgObraAutorListSgObraAutorToAttach);
            }
            sgAutor.setSgObraAutorList(attachedSgObraAutorList);
            em.persist(sgAutor);
            for (SgObraAutor sgObraAutorListSgObraAutor : sgAutor.getSgObraAutorList()) {
                SgAutor oldSgAutorOfSgObraAutorListSgObraAutor = sgObraAutorListSgObraAutor.getSgAutor();
                sgObraAutorListSgObraAutor.setSgAutor(sgAutor);
                sgObraAutorListSgObraAutor = em.merge(sgObraAutorListSgObraAutor);
                if (oldSgAutorOfSgObraAutorListSgObraAutor != null) {
                    oldSgAutorOfSgObraAutorListSgObraAutor.getSgObraAutorList().remove(sgObraAutorListSgObraAutor);
                    oldSgAutorOfSgObraAutorListSgObraAutor = em.merge(oldSgAutorOfSgObraAutorListSgObraAutor);
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
            List<SgObraAutor> sgObraAutorListOld = persistentSgAutor.getSgObraAutorList();
            List<SgObraAutor> sgObraAutorListNew = sgAutor.getSgObraAutorList();
            List<String> illegalOrphanMessages = null;
            for (SgObraAutor sgObraAutorListOldSgObraAutor : sgObraAutorListOld) {
                if (!sgObraAutorListNew.contains(sgObraAutorListOldSgObraAutor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SgObraAutor " + sgObraAutorListOldSgObraAutor + " since its sgAutor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SgObraAutor> attachedSgObraAutorListNew = new ArrayList<SgObraAutor>();
            for (SgObraAutor sgObraAutorListNewSgObraAutorToAttach : sgObraAutorListNew) {
                sgObraAutorListNewSgObraAutorToAttach = em.getReference(sgObraAutorListNewSgObraAutorToAttach.getClass(), sgObraAutorListNewSgObraAutorToAttach.getSgObraAutorPK());
                attachedSgObraAutorListNew.add(sgObraAutorListNewSgObraAutorToAttach);
            }
            sgObraAutorListNew = attachedSgObraAutorListNew;
            sgAutor.setSgObraAutorList(sgObraAutorListNew);
            sgAutor = em.merge(sgAutor);
            for (SgObraAutor sgObraAutorListNewSgObraAutor : sgObraAutorListNew) {
                if (!sgObraAutorListOld.contains(sgObraAutorListNewSgObraAutor)) {
                    SgAutor oldSgAutorOfSgObraAutorListNewSgObraAutor = sgObraAutorListNewSgObraAutor.getSgAutor();
                    sgObraAutorListNewSgObraAutor.setSgAutor(sgAutor);
                    sgObraAutorListNewSgObraAutor = em.merge(sgObraAutorListNewSgObraAutor);
                    if (oldSgAutorOfSgObraAutorListNewSgObraAutor != null && !oldSgAutorOfSgObraAutorListNewSgObraAutor.equals(sgAutor)) {
                        oldSgAutorOfSgObraAutorListNewSgObraAutor.getSgObraAutorList().remove(sgObraAutorListNewSgObraAutor);
                        oldSgAutorOfSgObraAutorListNewSgObraAutor = em.merge(oldSgAutorOfSgObraAutorListNewSgObraAutor);
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
            List<SgObraAutor> sgObraAutorListOrphanCheck = sgAutor.getSgObraAutorList();
            for (SgObraAutor sgObraAutorListOrphanCheckSgObraAutor : sgObraAutorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SgAutor (" + sgAutor + ") cannot be destroyed since the SgObraAutor " + sgObraAutorListOrphanCheckSgObraAutor + " in its sgObraAutorList field has a non-nullable sgAutor field.");
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
