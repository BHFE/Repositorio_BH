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
import java.util.Collection;
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
        if (bvAvaliador.getBvArtigoCollection() == null) {
            bvAvaliador.setBvArtigoCollection(new ArrayList<BvArtigo>());
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
            Collection<BvArtigo> attachedBvArtigoCollection = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionBvArtigoToAttach : bvAvaliador.getBvArtigoCollection()) {
                bvArtigoCollectionBvArtigoToAttach = em.getReference(bvArtigoCollectionBvArtigoToAttach.getClass(), bvArtigoCollectionBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollection.add(bvArtigoCollectionBvArtigoToAttach);
            }
            bvAvaliador.setBvArtigoCollection(attachedBvArtigoCollection);
            em.persist(bvAvaliador);
            if (BLeitor != null) {
                BLeitor.setBvAvaliador(bvAvaliador);
                BLeitor = em.merge(BLeitor);
            }
            if (idArea != null) {
                idArea.getBvAvaliadorCollection().add(bvAvaliador);
                idArea = em.merge(idArea);
            }
            for (BvArtigo bvArtigoCollectionBvArtigo : bvAvaliador.getBvArtigoCollection()) {
                BvAvaliador oldAvaliadorOfBvArtigoCollectionBvArtigo = bvArtigoCollectionBvArtigo.getAvaliador();
                bvArtigoCollectionBvArtigo.setAvaliador(bvAvaliador);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
                if (oldAvaliadorOfBvArtigoCollectionBvArtigo != null) {
                    oldAvaliadorOfBvArtigoCollectionBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionBvArtigo);
                    oldAvaliadorOfBvArtigoCollectionBvArtigo = em.merge(oldAvaliadorOfBvArtigoCollectionBvArtigo);
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
            Collection<BvArtigo> bvArtigoCollectionOld = persistentBvAvaliador.getBvArtigoCollection();
            Collection<BvArtigo> bvArtigoCollectionNew = bvAvaliador.getBvArtigoCollection();
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
            Collection<BvArtigo> attachedBvArtigoCollectionNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionNewBvArtigoToAttach : bvArtigoCollectionNew) {
                bvArtigoCollectionNewBvArtigoToAttach = em.getReference(bvArtigoCollectionNewBvArtigoToAttach.getClass(), bvArtigoCollectionNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollectionNew.add(bvArtigoCollectionNewBvArtigoToAttach);
            }
            bvArtigoCollectionNew = attachedBvArtigoCollectionNew;
            bvAvaliador.setBvArtigoCollection(bvArtigoCollectionNew);
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
                idAreaOld.getBvAvaliadorCollection().remove(bvAvaliador);
                idAreaOld = em.merge(idAreaOld);
            }
            if (idAreaNew != null && !idAreaNew.equals(idAreaOld)) {
                idAreaNew.getBvAvaliadorCollection().add(bvAvaliador);
                idAreaNew = em.merge(idAreaNew);
            }
            for (BvArtigo bvArtigoCollectionOldBvArtigo : bvArtigoCollectionOld) {
                if (!bvArtigoCollectionNew.contains(bvArtigoCollectionOldBvArtigo)) {
                    bvArtigoCollectionOldBvArtigo.setAvaliador(null);
                    bvArtigoCollectionOldBvArtigo = em.merge(bvArtigoCollectionOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoCollectionNewBvArtigo : bvArtigoCollectionNew) {
                if (!bvArtigoCollectionOld.contains(bvArtigoCollectionNewBvArtigo)) {
                    BvAvaliador oldAvaliadorOfBvArtigoCollectionNewBvArtigo = bvArtigoCollectionNewBvArtigo.getAvaliador();
                    bvArtigoCollectionNewBvArtigo.setAvaliador(bvAvaliador);
                    bvArtigoCollectionNewBvArtigo = em.merge(bvArtigoCollectionNewBvArtigo);
                    if (oldAvaliadorOfBvArtigoCollectionNewBvArtigo != null && !oldAvaliadorOfBvArtigoCollectionNewBvArtigo.equals(bvAvaliador)) {
                        oldAvaliadorOfBvArtigoCollectionNewBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionNewBvArtigo);
                        oldAvaliadorOfBvArtigoCollectionNewBvArtigo = em.merge(oldAvaliadorOfBvArtigoCollectionNewBvArtigo);
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
                idArea.getBvAvaliadorCollection().remove(bvAvaliador);
                idArea = em.merge(idArea);
            }
            Collection<BvArtigo> bvArtigoCollection = bvAvaliador.getBvArtigoCollection();
            for (BvArtigo bvArtigoCollectionBvArtigo : bvArtigoCollection) {
                bvArtigoCollectionBvArtigo.setAvaliador(null);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
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
