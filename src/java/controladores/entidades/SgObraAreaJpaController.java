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
import java.util.ArrayList;
import java.util.Collection;
import entidades.BvArtigo;
import entidades.SgObraArea;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class SgObraAreaJpaController implements Serializable {

    public SgObraAreaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgObraArea sgObraArea) throws PreexistingEntityException, Exception {
        if (sgObraArea.getSgObraCollection() == null) {
            sgObraArea.setSgObraCollection(new ArrayList<SgObra>());
        }
        if (sgObraArea.getBvArtigoCollection() == null) {
            sgObraArea.setBvArtigoCollection(new ArrayList<BvArtigo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SgObra> attachedSgObraCollection = new ArrayList<SgObra>();
            for (SgObra sgObraCollectionSgObraToAttach : sgObraArea.getSgObraCollection()) {
                sgObraCollectionSgObraToAttach = em.getReference(sgObraCollectionSgObraToAttach.getClass(), sgObraCollectionSgObraToAttach.getIdlivro());
                attachedSgObraCollection.add(sgObraCollectionSgObraToAttach);
            }
            sgObraArea.setSgObraCollection(attachedSgObraCollection);
            Collection<BvArtigo> attachedBvArtigoCollection = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionBvArtigoToAttach : sgObraArea.getBvArtigoCollection()) {
                bvArtigoCollectionBvArtigoToAttach = em.getReference(bvArtigoCollectionBvArtigoToAttach.getClass(), bvArtigoCollectionBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollection.add(bvArtigoCollectionBvArtigoToAttach);
            }
            sgObraArea.setBvArtigoCollection(attachedBvArtigoCollection);
            em.persist(sgObraArea);
            for (SgObra sgObraCollectionSgObra : sgObraArea.getSgObraCollection()) {
                SgObraArea oldAreaOfSgObraCollectionSgObra = sgObraCollectionSgObra.getArea();
                sgObraCollectionSgObra.setArea(sgObraArea);
                sgObraCollectionSgObra = em.merge(sgObraCollectionSgObra);
                if (oldAreaOfSgObraCollectionSgObra != null) {
                    oldAreaOfSgObraCollectionSgObra.getSgObraCollection().remove(sgObraCollectionSgObra);
                    oldAreaOfSgObraCollectionSgObra = em.merge(oldAreaOfSgObraCollectionSgObra);
                }
            }
            for (BvArtigo bvArtigoCollectionBvArtigo : sgObraArea.getBvArtigoCollection()) {
                SgObraArea oldAreaOfBvArtigoCollectionBvArtigo = bvArtigoCollectionBvArtigo.getArea();
                bvArtigoCollectionBvArtigo.setArea(sgObraArea);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
                if (oldAreaOfBvArtigoCollectionBvArtigo != null) {
                    oldAreaOfBvArtigoCollectionBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionBvArtigo);
                    oldAreaOfBvArtigoCollectionBvArtigo = em.merge(oldAreaOfBvArtigoCollectionBvArtigo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSgObraArea(sgObraArea.getIdarea()) != null) {
                throw new PreexistingEntityException("SgObraArea " + sgObraArea + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgObraArea sgObraArea) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraArea persistentSgObraArea = em.find(SgObraArea.class, sgObraArea.getIdarea());
            Collection<SgObra> sgObraCollectionOld = persistentSgObraArea.getSgObraCollection();
            Collection<SgObra> sgObraCollectionNew = sgObraArea.getSgObraCollection();
            Collection<BvArtigo> bvArtigoCollectionOld = persistentSgObraArea.getBvArtigoCollection();
            Collection<BvArtigo> bvArtigoCollectionNew = sgObraArea.getBvArtigoCollection();
            Collection<SgObra> attachedSgObraCollectionNew = new ArrayList<SgObra>();
            for (SgObra sgObraCollectionNewSgObraToAttach : sgObraCollectionNew) {
                sgObraCollectionNewSgObraToAttach = em.getReference(sgObraCollectionNewSgObraToAttach.getClass(), sgObraCollectionNewSgObraToAttach.getIdlivro());
                attachedSgObraCollectionNew.add(sgObraCollectionNewSgObraToAttach);
            }
            sgObraCollectionNew = attachedSgObraCollectionNew;
            sgObraArea.setSgObraCollection(sgObraCollectionNew);
            Collection<BvArtigo> attachedBvArtigoCollectionNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionNewBvArtigoToAttach : bvArtigoCollectionNew) {
                bvArtigoCollectionNewBvArtigoToAttach = em.getReference(bvArtigoCollectionNewBvArtigoToAttach.getClass(), bvArtigoCollectionNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollectionNew.add(bvArtigoCollectionNewBvArtigoToAttach);
            }
            bvArtigoCollectionNew = attachedBvArtigoCollectionNew;
            sgObraArea.setBvArtigoCollection(bvArtigoCollectionNew);
            sgObraArea = em.merge(sgObraArea);
            for (SgObra sgObraCollectionOldSgObra : sgObraCollectionOld) {
                if (!sgObraCollectionNew.contains(sgObraCollectionOldSgObra)) {
                    sgObraCollectionOldSgObra.setArea(null);
                    sgObraCollectionOldSgObra = em.merge(sgObraCollectionOldSgObra);
                }
            }
            for (SgObra sgObraCollectionNewSgObra : sgObraCollectionNew) {
                if (!sgObraCollectionOld.contains(sgObraCollectionNewSgObra)) {
                    SgObraArea oldAreaOfSgObraCollectionNewSgObra = sgObraCollectionNewSgObra.getArea();
                    sgObraCollectionNewSgObra.setArea(sgObraArea);
                    sgObraCollectionNewSgObra = em.merge(sgObraCollectionNewSgObra);
                    if (oldAreaOfSgObraCollectionNewSgObra != null && !oldAreaOfSgObraCollectionNewSgObra.equals(sgObraArea)) {
                        oldAreaOfSgObraCollectionNewSgObra.getSgObraCollection().remove(sgObraCollectionNewSgObra);
                        oldAreaOfSgObraCollectionNewSgObra = em.merge(oldAreaOfSgObraCollectionNewSgObra);
                    }
                }
            }
            for (BvArtigo bvArtigoCollectionOldBvArtigo : bvArtigoCollectionOld) {
                if (!bvArtigoCollectionNew.contains(bvArtigoCollectionOldBvArtigo)) {
                    bvArtigoCollectionOldBvArtigo.setArea(null);
                    bvArtigoCollectionOldBvArtigo = em.merge(bvArtigoCollectionOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoCollectionNewBvArtigo : bvArtigoCollectionNew) {
                if (!bvArtigoCollectionOld.contains(bvArtigoCollectionNewBvArtigo)) {
                    SgObraArea oldAreaOfBvArtigoCollectionNewBvArtigo = bvArtigoCollectionNewBvArtigo.getArea();
                    bvArtigoCollectionNewBvArtigo.setArea(sgObraArea);
                    bvArtigoCollectionNewBvArtigo = em.merge(bvArtigoCollectionNewBvArtigo);
                    if (oldAreaOfBvArtigoCollectionNewBvArtigo != null && !oldAreaOfBvArtigoCollectionNewBvArtigo.equals(sgObraArea)) {
                        oldAreaOfBvArtigoCollectionNewBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionNewBvArtigo);
                        oldAreaOfBvArtigoCollectionNewBvArtigo = em.merge(oldAreaOfBvArtigoCollectionNewBvArtigo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sgObraArea.getIdarea();
                if (findSgObraArea(id) == null) {
                    throw new NonexistentEntityException("The sgObraArea with id " + id + " no longer exists.");
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
            SgObraArea sgObraArea;
            try {
                sgObraArea = em.getReference(SgObraArea.class, id);
                sgObraArea.getIdarea();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgObraArea with id " + id + " no longer exists.", enfe);
            }
            Collection<SgObra> sgObraCollection = sgObraArea.getSgObraCollection();
            for (SgObra sgObraCollectionSgObra : sgObraCollection) {
                sgObraCollectionSgObra.setArea(null);
                sgObraCollectionSgObra = em.merge(sgObraCollectionSgObra);
            }
            Collection<BvArtigo> bvArtigoCollection = sgObraArea.getBvArtigoCollection();
            for (BvArtigo bvArtigoCollectionBvArtigo : bvArtigoCollection) {
                bvArtigoCollectionBvArtigo.setArea(null);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
            }
            em.remove(sgObraArea);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgObraArea> findSgObraAreaEntities() {
        return findSgObraAreaEntities(true, -1, -1);
    }

    public List<SgObraArea> findSgObraAreaEntities(int maxResults, int firstResult) {
        return findSgObraAreaEntities(false, maxResults, firstResult);
    }

    private List<SgObraArea> findSgObraAreaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgObraArea.class));
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

    public SgObraArea findSgObraArea(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgObraArea.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgObraAreaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgObraArea> rt = cq.from(SgObraArea.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
