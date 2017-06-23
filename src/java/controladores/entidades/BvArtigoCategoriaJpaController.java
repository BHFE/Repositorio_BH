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
import entidades.BvAvaliador;
import java.util.ArrayList;
import java.util.Collection;
import entidades.BvArtigo;
import entidades.BvArtigoCategoria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class BvArtigoCategoriaJpaController implements Serializable {

    public BvArtigoCategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BvArtigoCategoria bvArtigoCategoria) throws PreexistingEntityException, Exception {
        if (bvArtigoCategoria.getBvAvaliadorCollection() == null) {
            bvArtigoCategoria.setBvAvaliadorCollection(new ArrayList<BvAvaliador>());
        }
        if (bvArtigoCategoria.getBvArtigoCollection() == null) {
            bvArtigoCategoria.setBvArtigoCollection(new ArrayList<BvArtigo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<BvAvaliador> attachedBvAvaliadorCollection = new ArrayList<BvAvaliador>();
            for (BvAvaliador bvAvaliadorCollectionBvAvaliadorToAttach : bvArtigoCategoria.getBvAvaliadorCollection()) {
                bvAvaliadorCollectionBvAvaliadorToAttach = em.getReference(bvAvaliadorCollectionBvAvaliadorToAttach.getClass(), bvAvaliadorCollectionBvAvaliadorToAttach.getIdLeitor());
                attachedBvAvaliadorCollection.add(bvAvaliadorCollectionBvAvaliadorToAttach);
            }
            bvArtigoCategoria.setBvAvaliadorCollection(attachedBvAvaliadorCollection);
            Collection<BvArtigo> attachedBvArtigoCollection = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionBvArtigoToAttach : bvArtigoCategoria.getBvArtigoCollection()) {
                bvArtigoCollectionBvArtigoToAttach = em.getReference(bvArtigoCollectionBvArtigoToAttach.getClass(), bvArtigoCollectionBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollection.add(bvArtigoCollectionBvArtigoToAttach);
            }
            bvArtigoCategoria.setBvArtigoCollection(attachedBvArtigoCollection);
            em.persist(bvArtigoCategoria);
            for (BvAvaliador bvAvaliadorCollectionBvAvaliador : bvArtigoCategoria.getBvAvaliadorCollection()) {
                BvArtigoCategoria oldIdAreaOfBvAvaliadorCollectionBvAvaliador = bvAvaliadorCollectionBvAvaliador.getIdArea();
                bvAvaliadorCollectionBvAvaliador.setIdArea(bvArtigoCategoria);
                bvAvaliadorCollectionBvAvaliador = em.merge(bvAvaliadorCollectionBvAvaliador);
                if (oldIdAreaOfBvAvaliadorCollectionBvAvaliador != null) {
                    oldIdAreaOfBvAvaliadorCollectionBvAvaliador.getBvAvaliadorCollection().remove(bvAvaliadorCollectionBvAvaliador);
                    oldIdAreaOfBvAvaliadorCollectionBvAvaliador = em.merge(oldIdAreaOfBvAvaliadorCollectionBvAvaliador);
                }
            }
            for (BvArtigo bvArtigoCollectionBvArtigo : bvArtigoCategoria.getBvArtigoCollection()) {
                BvArtigoCategoria oldTipodocOfBvArtigoCollectionBvArtigo = bvArtigoCollectionBvArtigo.getTipodoc();
                bvArtigoCollectionBvArtigo.setTipodoc(bvArtigoCategoria);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
                if (oldTipodocOfBvArtigoCollectionBvArtigo != null) {
                    oldTipodocOfBvArtigoCollectionBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionBvArtigo);
                    oldTipodocOfBvArtigoCollectionBvArtigo = em.merge(oldTipodocOfBvArtigoCollectionBvArtigo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBvArtigoCategoria(bvArtigoCategoria.getCategoria()) != null) {
                throw new PreexistingEntityException("BvArtigoCategoria " + bvArtigoCategoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BvArtigoCategoria bvArtigoCategoria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BvArtigoCategoria persistentBvArtigoCategoria = em.find(BvArtigoCategoria.class, bvArtigoCategoria.getCategoria());
            Collection<BvAvaliador> bvAvaliadorCollectionOld = persistentBvArtigoCategoria.getBvAvaliadorCollection();
            Collection<BvAvaliador> bvAvaliadorCollectionNew = bvArtigoCategoria.getBvAvaliadorCollection();
            Collection<BvArtigo> bvArtigoCollectionOld = persistentBvArtigoCategoria.getBvArtigoCollection();
            Collection<BvArtigo> bvArtigoCollectionNew = bvArtigoCategoria.getBvArtigoCollection();
            List<String> illegalOrphanMessages = null;
            for (BvAvaliador bvAvaliadorCollectionOldBvAvaliador : bvAvaliadorCollectionOld) {
                if (!bvAvaliadorCollectionNew.contains(bvAvaliadorCollectionOldBvAvaliador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BvAvaliador " + bvAvaliadorCollectionOldBvAvaliador + " since its idArea field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<BvAvaliador> attachedBvAvaliadorCollectionNew = new ArrayList<BvAvaliador>();
            for (BvAvaliador bvAvaliadorCollectionNewBvAvaliadorToAttach : bvAvaliadorCollectionNew) {
                bvAvaliadorCollectionNewBvAvaliadorToAttach = em.getReference(bvAvaliadorCollectionNewBvAvaliadorToAttach.getClass(), bvAvaliadorCollectionNewBvAvaliadorToAttach.getIdLeitor());
                attachedBvAvaliadorCollectionNew.add(bvAvaliadorCollectionNewBvAvaliadorToAttach);
            }
            bvAvaliadorCollectionNew = attachedBvAvaliadorCollectionNew;
            bvArtigoCategoria.setBvAvaliadorCollection(bvAvaliadorCollectionNew);
            Collection<BvArtigo> attachedBvArtigoCollectionNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionNewBvArtigoToAttach : bvArtigoCollectionNew) {
                bvArtigoCollectionNewBvArtigoToAttach = em.getReference(bvArtigoCollectionNewBvArtigoToAttach.getClass(), bvArtigoCollectionNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollectionNew.add(bvArtigoCollectionNewBvArtigoToAttach);
            }
            bvArtigoCollectionNew = attachedBvArtigoCollectionNew;
            bvArtigoCategoria.setBvArtigoCollection(bvArtigoCollectionNew);
            bvArtigoCategoria = em.merge(bvArtigoCategoria);
            for (BvAvaliador bvAvaliadorCollectionNewBvAvaliador : bvAvaliadorCollectionNew) {
                if (!bvAvaliadorCollectionOld.contains(bvAvaliadorCollectionNewBvAvaliador)) {
                    BvArtigoCategoria oldIdAreaOfBvAvaliadorCollectionNewBvAvaliador = bvAvaliadorCollectionNewBvAvaliador.getIdArea();
                    bvAvaliadorCollectionNewBvAvaliador.setIdArea(bvArtigoCategoria);
                    bvAvaliadorCollectionNewBvAvaliador = em.merge(bvAvaliadorCollectionNewBvAvaliador);
                    if (oldIdAreaOfBvAvaliadorCollectionNewBvAvaliador != null && !oldIdAreaOfBvAvaliadorCollectionNewBvAvaliador.equals(bvArtigoCategoria)) {
                        oldIdAreaOfBvAvaliadorCollectionNewBvAvaliador.getBvAvaliadorCollection().remove(bvAvaliadorCollectionNewBvAvaliador);
                        oldIdAreaOfBvAvaliadorCollectionNewBvAvaliador = em.merge(oldIdAreaOfBvAvaliadorCollectionNewBvAvaliador);
                    }
                }
            }
            for (BvArtigo bvArtigoCollectionOldBvArtigo : bvArtigoCollectionOld) {
                if (!bvArtigoCollectionNew.contains(bvArtigoCollectionOldBvArtigo)) {
                    bvArtigoCollectionOldBvArtigo.setTipodoc(null);
                    bvArtigoCollectionOldBvArtigo = em.merge(bvArtigoCollectionOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoCollectionNewBvArtigo : bvArtigoCollectionNew) {
                if (!bvArtigoCollectionOld.contains(bvArtigoCollectionNewBvArtigo)) {
                    BvArtigoCategoria oldTipodocOfBvArtigoCollectionNewBvArtigo = bvArtigoCollectionNewBvArtigo.getTipodoc();
                    bvArtigoCollectionNewBvArtigo.setTipodoc(bvArtigoCategoria);
                    bvArtigoCollectionNewBvArtigo = em.merge(bvArtigoCollectionNewBvArtigo);
                    if (oldTipodocOfBvArtigoCollectionNewBvArtigo != null && !oldTipodocOfBvArtigoCollectionNewBvArtigo.equals(bvArtigoCategoria)) {
                        oldTipodocOfBvArtigoCollectionNewBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionNewBvArtigo);
                        oldTipodocOfBvArtigoCollectionNewBvArtigo = em.merge(oldTipodocOfBvArtigoCollectionNewBvArtigo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = bvArtigoCategoria.getCategoria();
                if (findBvArtigoCategoria(id) == null) {
                    throw new NonexistentEntityException("The bvArtigoCategoria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BvArtigoCategoria bvArtigoCategoria;
            try {
                bvArtigoCategoria = em.getReference(BvArtigoCategoria.class, id);
                bvArtigoCategoria.getCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bvArtigoCategoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<BvAvaliador> bvAvaliadorCollectionOrphanCheck = bvArtigoCategoria.getBvAvaliadorCollection();
            for (BvAvaliador bvAvaliadorCollectionOrphanCheckBvAvaliador : bvAvaliadorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BvArtigoCategoria (" + bvArtigoCategoria + ") cannot be destroyed since the BvAvaliador " + bvAvaliadorCollectionOrphanCheckBvAvaliador + " in its bvAvaliadorCollection field has a non-nullable idArea field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<BvArtigo> bvArtigoCollection = bvArtigoCategoria.getBvArtigoCollection();
            for (BvArtigo bvArtigoCollectionBvArtigo : bvArtigoCollection) {
                bvArtigoCollectionBvArtigo.setTipodoc(null);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
            }
            em.remove(bvArtigoCategoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BvArtigoCategoria> findBvArtigoCategoriaEntities() {
        return findBvArtigoCategoriaEntities(true, -1, -1);
    }

    public List<BvArtigoCategoria> findBvArtigoCategoriaEntities(int maxResults, int firstResult) {
        return findBvArtigoCategoriaEntities(false, maxResults, firstResult);
    }

    private List<BvArtigoCategoria> findBvArtigoCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BvArtigoCategoria.class));
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

    public BvArtigoCategoria findBvArtigoCategoria(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BvArtigoCategoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getBvArtigoCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BvArtigoCategoria> rt = cq.from(BvArtigoCategoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
