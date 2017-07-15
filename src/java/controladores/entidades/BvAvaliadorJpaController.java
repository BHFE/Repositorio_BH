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
import entidades.BLeitor;
import entidades.BvArtigoCategoria;
import entidades.BvArtigo;
import entidades.BvAvaliador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class BvAvaliadorJpaController implements Serializable {

    public BvAvaliadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BvAvaliador bvAvaliador) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (bvAvaliador.getBvArtigoList() == null) {
            bvAvaliador.setBvArtigoList(new ArrayList<BvArtigo>());
        }
        List<String> illegalOrphanMessages = null;
        BLeitor BLeitorOrphanCheck = bvAvaliador.getBLeitor();
        if (BLeitorOrphanCheck != null) {
            BvAvaliador oldBvAvaliadorOfBLeitor = BLeitorOrphanCheck.getBvAvaliador();
            if (oldBvAvaliadorOfBLeitor != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The BLeitor " + BLeitorOrphanCheck + " already has an item of type BvAvaliador whose BLeitor column cannot be null. Please make another selection for the BLeitor field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitor BLeitor = bvAvaliador.getBLeitor();
            if (BLeitor != null) {
                BLeitor = em.getReference(BLeitor.getClass(), BLeitor.getNrCartao());
                bvAvaliador.setBLeitor(BLeitor);
            }
            BvArtigoCategoria idArea = bvAvaliador.getIdArea();
            if (idArea != null) {
                idArea = em.getReference(idArea.getClass(), idArea.getCategoria());
                bvAvaliador.setIdArea(idArea);
            }
            List<BvArtigo> attachedBvArtigoList = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListBvArtigoToAttach : bvAvaliador.getBvArtigoList()) {
                bvArtigoListBvArtigoToAttach = em.getReference(bvArtigoListBvArtigoToAttach.getClass(), bvArtigoListBvArtigoToAttach.getIdartigo());
                attachedBvArtigoList.add(bvArtigoListBvArtigoToAttach);
            }
            bvAvaliador.setBvArtigoList(attachedBvArtigoList);
            em.persist(bvAvaliador);
            if (BLeitor != null) {
                BLeitor.setBvAvaliador(bvAvaliador);
                BLeitor = em.merge(BLeitor);
            }
            if (idArea != null) {
                idArea.getBvAvaliadorList().add(bvAvaliador);
                idArea = em.merge(idArea);
            }
            for (BvArtigo bvArtigoListBvArtigo : bvAvaliador.getBvArtigoList()) {
                BvAvaliador oldAvaliadorOfBvArtigoListBvArtigo = bvArtigoListBvArtigo.getAvaliador();
                bvArtigoListBvArtigo.setAvaliador(bvAvaliador);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
                if (oldAvaliadorOfBvArtigoListBvArtigo != null) {
                    oldAvaliadorOfBvArtigoListBvArtigo.getBvArtigoList().remove(bvArtigoListBvArtigo);
                    oldAvaliadorOfBvArtigoListBvArtigo = em.merge(oldAvaliadorOfBvArtigoListBvArtigo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBvAvaliador(bvAvaliador.getIdLeitor()) != null) {
                throw new PreexistingEntityException("BvAvaliador " + bvAvaliador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BvAvaliador bvAvaliador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BvAvaliador persistentBvAvaliador = em.find(BvAvaliador.class, bvAvaliador.getIdLeitor());
            BLeitor BLeitorOld = persistentBvAvaliador.getBLeitor();
            BLeitor BLeitorNew = bvAvaliador.getBLeitor();
            BvArtigoCategoria idAreaOld = persistentBvAvaliador.getIdArea();
            BvArtigoCategoria idAreaNew = bvAvaliador.getIdArea();
            List<BvArtigo> bvArtigoListOld = persistentBvAvaliador.getBvArtigoList();
            List<BvArtigo> bvArtigoListNew = bvAvaliador.getBvArtigoList();
            List<String> illegalOrphanMessages = null;
            if (BLeitorNew != null && !BLeitorNew.equals(BLeitorOld)) {
                BvAvaliador oldBvAvaliadorOfBLeitor = BLeitorNew.getBvAvaliador();
                if (oldBvAvaliadorOfBLeitor != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The BLeitor " + BLeitorNew + " already has an item of type BvAvaliador whose BLeitor column cannot be null. Please make another selection for the BLeitor field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (BLeitorNew != null) {
                BLeitorNew = em.getReference(BLeitorNew.getClass(), BLeitorNew.getNrCartao());
                bvAvaliador.setBLeitor(BLeitorNew);
            }
            if (idAreaNew != null) {
                idAreaNew = em.getReference(idAreaNew.getClass(), idAreaNew.getCategoria());
                bvAvaliador.setIdArea(idAreaNew);
            }
            List<BvArtigo> attachedBvArtigoListNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListNewBvArtigoToAttach : bvArtigoListNew) {
                bvArtigoListNewBvArtigoToAttach = em.getReference(bvArtigoListNewBvArtigoToAttach.getClass(), bvArtigoListNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoListNew.add(bvArtigoListNewBvArtigoToAttach);
            }
            bvArtigoListNew = attachedBvArtigoListNew;
            bvAvaliador.setBvArtigoList(bvArtigoListNew);
            bvAvaliador = em.merge(bvAvaliador);
            if (BLeitorOld != null && !BLeitorOld.equals(BLeitorNew)) {
                BLeitorOld.setBvAvaliador(null);
                BLeitorOld = em.merge(BLeitorOld);
            }
            if (BLeitorNew != null && !BLeitorNew.equals(BLeitorOld)) {
                BLeitorNew.setBvAvaliador(bvAvaliador);
                BLeitorNew = em.merge(BLeitorNew);
            }
            if (idAreaOld != null && !idAreaOld.equals(idAreaNew)) {
                idAreaOld.getBvAvaliadorList().remove(bvAvaliador);
                idAreaOld = em.merge(idAreaOld);
            }
            if (idAreaNew != null && !idAreaNew.equals(idAreaOld)) {
                idAreaNew.getBvAvaliadorList().add(bvAvaliador);
                idAreaNew = em.merge(idAreaNew);
            }
            for (BvArtigo bvArtigoListOldBvArtigo : bvArtigoListOld) {
                if (!bvArtigoListNew.contains(bvArtigoListOldBvArtigo)) {
                    bvArtigoListOldBvArtigo.setAvaliador(null);
                    bvArtigoListOldBvArtigo = em.merge(bvArtigoListOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoListNewBvArtigo : bvArtigoListNew) {
                if (!bvArtigoListOld.contains(bvArtigoListNewBvArtigo)) {
                    BvAvaliador oldAvaliadorOfBvArtigoListNewBvArtigo = bvArtigoListNewBvArtigo.getAvaliador();
                    bvArtigoListNewBvArtigo.setAvaliador(bvAvaliador);
                    bvArtigoListNewBvArtigo = em.merge(bvArtigoListNewBvArtigo);
                    if (oldAvaliadorOfBvArtigoListNewBvArtigo != null && !oldAvaliadorOfBvArtigoListNewBvArtigo.equals(bvAvaliador)) {
                        oldAvaliadorOfBvArtigoListNewBvArtigo.getBvArtigoList().remove(bvArtigoListNewBvArtigo);
                        oldAvaliadorOfBvArtigoListNewBvArtigo = em.merge(oldAvaliadorOfBvArtigoListNewBvArtigo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = bvAvaliador.getIdLeitor();
                if (findBvAvaliador(id) == null) {
                    throw new NonexistentEntityException("The bvAvaliador with id " + id + " no longer exists.");
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
            BvAvaliador bvAvaliador;
            try {
                bvAvaliador = em.getReference(BvAvaliador.class, id);
                bvAvaliador.getIdLeitor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bvAvaliador with id " + id + " no longer exists.", enfe);
            }
            BLeitor BLeitor = bvAvaliador.getBLeitor();
            if (BLeitor != null) {
                BLeitor.setBvAvaliador(null);
                BLeitor = em.merge(BLeitor);
            }
            BvArtigoCategoria idArea = bvAvaliador.getIdArea();
            if (idArea != null) {
                idArea.getBvAvaliadorList().remove(bvAvaliador);
                idArea = em.merge(idArea);
            }
            List<BvArtigo> bvArtigoList = bvAvaliador.getBvArtigoList();
            for (BvArtigo bvArtigoListBvArtigo : bvArtigoList) {
                bvArtigoListBvArtigo.setAvaliador(null);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
            }
            em.remove(bvAvaliador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BvAvaliador> findBvAvaliadorEntities() {
        return findBvAvaliadorEntities(true, -1, -1);
    }

    public List<BvAvaliador> findBvAvaliadorEntities(int maxResults, int firstResult) {
        return findBvAvaliadorEntities(false, maxResults, firstResult);
    }

    private List<BvAvaliador> findBvAvaliadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BvAvaliador.class));
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

    public BvAvaliador findBvAvaliador(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BvAvaliador.class, id);
        } finally {
            em.close();
        }
    }

    public int getBvAvaliadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BvAvaliador> rt = cq.from(BvAvaliador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
