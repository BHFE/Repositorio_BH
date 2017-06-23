/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.SgObra;
import entidades.SgObraCategoria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class SgObraCategoriaJpaController implements Serializable {

    public SgObraCategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgObraCategoria sgObraCategoria) throws PreexistingEntityException, Exception {
        if (sgObraCategoria.getSgObraCollection() == null) {
            sgObraCategoria.setSgObraCollection(new ArrayList<SgObra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SgObra> attachedSgObraCollection = new ArrayList<SgObra>();
            for (SgObra sgObraCollectionSgObraToAttach : sgObraCategoria.getSgObraCollection()) {
                sgObraCollectionSgObraToAttach = em.getReference(sgObraCollectionSgObraToAttach.getClass(), sgObraCollectionSgObraToAttach.getIdlivro());
                attachedSgObraCollection.add(sgObraCollectionSgObraToAttach);
            }
            sgObraCategoria.setSgObraCollection(attachedSgObraCollection);
            em.persist(sgObraCategoria);
            for (SgObra sgObraCollectionSgObra : sgObraCategoria.getSgObraCollection()) {
                SgObraCategoria oldDominioOfSgObraCollectionSgObra = sgObraCollectionSgObra.getDominio();
                sgObraCollectionSgObra.setDominio(sgObraCategoria);
                sgObraCollectionSgObra = em.merge(sgObraCollectionSgObra);
                if (oldDominioOfSgObraCollectionSgObra != null) {
                    oldDominioOfSgObraCollectionSgObra.getSgObraCollection().remove(sgObraCollectionSgObra);
                    oldDominioOfSgObraCollectionSgObra = em.merge(oldDominioOfSgObraCollectionSgObra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSgObraCategoria(sgObraCategoria.getCategoria()) != null) {
                throw new PreexistingEntityException("SgObraCategoria " + sgObraCategoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgObraCategoria sgObraCategoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraCategoria persistentSgObraCategoria = em.find(SgObraCategoria.class, sgObraCategoria.getCategoria());
            Collection<SgObra> sgObraCollectionOld = persistentSgObraCategoria.getSgObraCollection();
            Collection<SgObra> sgObraCollectionNew = sgObraCategoria.getSgObraCollection();
            Collection<SgObra> attachedSgObraCollectionNew = new ArrayList<SgObra>();
            for (SgObra sgObraCollectionNewSgObraToAttach : sgObraCollectionNew) {
                sgObraCollectionNewSgObraToAttach = em.getReference(sgObraCollectionNewSgObraToAttach.getClass(), sgObraCollectionNewSgObraToAttach.getIdlivro());
                attachedSgObraCollectionNew.add(sgObraCollectionNewSgObraToAttach);
            }
            sgObraCollectionNew = attachedSgObraCollectionNew;
            sgObraCategoria.setSgObraCollection(sgObraCollectionNew);
            sgObraCategoria = em.merge(sgObraCategoria);
            for (SgObra sgObraCollectionOldSgObra : sgObraCollectionOld) {
                if (!sgObraCollectionNew.contains(sgObraCollectionOldSgObra)) {
                    sgObraCollectionOldSgObra.setDominio(null);
                    sgObraCollectionOldSgObra = em.merge(sgObraCollectionOldSgObra);
                }
            }
            for (SgObra sgObraCollectionNewSgObra : sgObraCollectionNew) {
                if (!sgObraCollectionOld.contains(sgObraCollectionNewSgObra)) {
                    SgObraCategoria oldDominioOfSgObraCollectionNewSgObra = sgObraCollectionNewSgObra.getDominio();
                    sgObraCollectionNewSgObra.setDominio(sgObraCategoria);
                    sgObraCollectionNewSgObra = em.merge(sgObraCollectionNewSgObra);
                    if (oldDominioOfSgObraCollectionNewSgObra != null && !oldDominioOfSgObraCollectionNewSgObra.equals(sgObraCategoria)) {
                        oldDominioOfSgObraCollectionNewSgObra.getSgObraCollection().remove(sgObraCollectionNewSgObra);
                        oldDominioOfSgObraCollectionNewSgObra = em.merge(oldDominioOfSgObraCollectionNewSgObra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = sgObraCategoria.getCategoria();
                if (findSgObraCategoria(id) == null) {
                    throw new NonexistentEntityException("The sgObraCategoria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraCategoria sgObraCategoria;
            try {
                sgObraCategoria = em.getReference(SgObraCategoria.class, id);
                sgObraCategoria.getCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgObraCategoria with id " + id + " no longer exists.", enfe);
            }
            Collection<SgObra> sgObraCollection = sgObraCategoria.getSgObraCollection();
            for (SgObra sgObraCollectionSgObra : sgObraCollection) {
                sgObraCollectionSgObra.setDominio(null);
                sgObraCollectionSgObra = em.merge(sgObraCollectionSgObra);
            }
            em.remove(sgObraCategoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgObraCategoria> findSgObraCategoriaEntities() {
        return findSgObraCategoriaEntities(true, -1, -1);
    }

    public List<SgObraCategoria> findSgObraCategoriaEntities(int maxResults, int firstResult) {
        return findSgObraCategoriaEntities(false, maxResults, firstResult);
    }

    private List<SgObraCategoria> findSgObraCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgObraCategoria.class));
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

    public SgObraCategoria findSgObraCategoria(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgObraCategoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgObraCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgObraCategoria> rt = cq.from(SgObraCategoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
