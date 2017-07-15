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
import java.util.List;
import entidades.BvArtigo;
import entidades.SgObraArea;
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
        if (sgObraArea.getSgObraList() == null) {
            sgObraArea.setSgObraList(new ArrayList<SgObra>());
        }
        if (sgObraArea.getBvArtigoList() == null) {
            sgObraArea.setBvArtigoList(new ArrayList<BvArtigo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SgObra> attachedSgObraList = new ArrayList<SgObra>();
            for (SgObra sgObraListSgObraToAttach : sgObraArea.getSgObraList()) {
                sgObraListSgObraToAttach = em.getReference(sgObraListSgObraToAttach.getClass(), sgObraListSgObraToAttach.getIdlivro());
                attachedSgObraList.add(sgObraListSgObraToAttach);
            }
            sgObraArea.setSgObraList(attachedSgObraList);
            List<BvArtigo> attachedBvArtigoList = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListBvArtigoToAttach : sgObraArea.getBvArtigoList()) {
                bvArtigoListBvArtigoToAttach = em.getReference(bvArtigoListBvArtigoToAttach.getClass(), bvArtigoListBvArtigoToAttach.getIdartigo());
                attachedBvArtigoList.add(bvArtigoListBvArtigoToAttach);
            }
            sgObraArea.setBvArtigoList(attachedBvArtigoList);
            em.persist(sgObraArea);
            for (SgObra sgObraListSgObra : sgObraArea.getSgObraList()) {
                SgObraArea oldAreaOfSgObraListSgObra = sgObraListSgObra.getArea();
                sgObraListSgObra.setArea(sgObraArea);
                sgObraListSgObra = em.merge(sgObraListSgObra);
                if (oldAreaOfSgObraListSgObra != null) {
                    oldAreaOfSgObraListSgObra.getSgObraList().remove(sgObraListSgObra);
                    oldAreaOfSgObraListSgObra = em.merge(oldAreaOfSgObraListSgObra);
                }
            }
            for (BvArtigo bvArtigoListBvArtigo : sgObraArea.getBvArtigoList()) {
                SgObraArea oldAreaOfBvArtigoListBvArtigo = bvArtigoListBvArtigo.getArea();
                bvArtigoListBvArtigo.setArea(sgObraArea);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
                if (oldAreaOfBvArtigoListBvArtigo != null) {
                    oldAreaOfBvArtigoListBvArtigo.getBvArtigoList().remove(bvArtigoListBvArtigo);
                    oldAreaOfBvArtigoListBvArtigo = em.merge(oldAreaOfBvArtigoListBvArtigo);
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
            List<SgObra> sgObraListOld = persistentSgObraArea.getSgObraList();
            List<SgObra> sgObraListNew = sgObraArea.getSgObraList();
            List<BvArtigo> bvArtigoListOld = persistentSgObraArea.getBvArtigoList();
            List<BvArtigo> bvArtigoListNew = sgObraArea.getBvArtigoList();
            List<SgObra> attachedSgObraListNew = new ArrayList<SgObra>();
            for (SgObra sgObraListNewSgObraToAttach : sgObraListNew) {
                sgObraListNewSgObraToAttach = em.getReference(sgObraListNewSgObraToAttach.getClass(), sgObraListNewSgObraToAttach.getIdlivro());
                attachedSgObraListNew.add(sgObraListNewSgObraToAttach);
            }
            sgObraListNew = attachedSgObraListNew;
            sgObraArea.setSgObraList(sgObraListNew);
            List<BvArtigo> attachedBvArtigoListNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListNewBvArtigoToAttach : bvArtigoListNew) {
                bvArtigoListNewBvArtigoToAttach = em.getReference(bvArtigoListNewBvArtigoToAttach.getClass(), bvArtigoListNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoListNew.add(bvArtigoListNewBvArtigoToAttach);
            }
            bvArtigoListNew = attachedBvArtigoListNew;
            sgObraArea.setBvArtigoList(bvArtigoListNew);
            sgObraArea = em.merge(sgObraArea);
            for (SgObra sgObraListOldSgObra : sgObraListOld) {
                if (!sgObraListNew.contains(sgObraListOldSgObra)) {
                    sgObraListOldSgObra.setArea(null);
                    sgObraListOldSgObra = em.merge(sgObraListOldSgObra);
                }
            }
            for (SgObra sgObraListNewSgObra : sgObraListNew) {
                if (!sgObraListOld.contains(sgObraListNewSgObra)) {
                    SgObraArea oldAreaOfSgObraListNewSgObra = sgObraListNewSgObra.getArea();
                    sgObraListNewSgObra.setArea(sgObraArea);
                    sgObraListNewSgObra = em.merge(sgObraListNewSgObra);
                    if (oldAreaOfSgObraListNewSgObra != null && !oldAreaOfSgObraListNewSgObra.equals(sgObraArea)) {
                        oldAreaOfSgObraListNewSgObra.getSgObraList().remove(sgObraListNewSgObra);
                        oldAreaOfSgObraListNewSgObra = em.merge(oldAreaOfSgObraListNewSgObra);
                    }
                }
            }
            for (BvArtigo bvArtigoListOldBvArtigo : bvArtigoListOld) {
                if (!bvArtigoListNew.contains(bvArtigoListOldBvArtigo)) {
                    bvArtigoListOldBvArtigo.setArea(null);
                    bvArtigoListOldBvArtigo = em.merge(bvArtigoListOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoListNewBvArtigo : bvArtigoListNew) {
                if (!bvArtigoListOld.contains(bvArtigoListNewBvArtigo)) {
                    SgObraArea oldAreaOfBvArtigoListNewBvArtigo = bvArtigoListNewBvArtigo.getArea();
                    bvArtigoListNewBvArtigo.setArea(sgObraArea);
                    bvArtigoListNewBvArtigo = em.merge(bvArtigoListNewBvArtigo);
                    if (oldAreaOfBvArtigoListNewBvArtigo != null && !oldAreaOfBvArtigoListNewBvArtigo.equals(sgObraArea)) {
                        oldAreaOfBvArtigoListNewBvArtigo.getBvArtigoList().remove(bvArtigoListNewBvArtigo);
                        oldAreaOfBvArtigoListNewBvArtigo = em.merge(oldAreaOfBvArtigoListNewBvArtigo);
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
            List<SgObra> sgObraList = sgObraArea.getSgObraList();
            for (SgObra sgObraListSgObra : sgObraList) {
                sgObraListSgObra.setArea(null);
                sgObraListSgObra = em.merge(sgObraListSgObra);
            }
            List<BvArtigo> bvArtigoList = sgObraArea.getBvArtigoList();
            for (BvArtigo bvArtigoListBvArtigo : bvArtigoList) {
                bvArtigoListBvArtigo.setArea(null);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
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
