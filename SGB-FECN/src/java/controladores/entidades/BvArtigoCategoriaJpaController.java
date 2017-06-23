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
import java.util.List;
import entidades.BvArtigo;
import entidades.BvArtigoCategoria;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
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
        if (bvArtigoCategoria.getBvAvaliadorList() == null) {
            bvArtigoCategoria.setBvAvaliadorList(new ArrayList<BvAvaliador>());
        }
        if (bvArtigoCategoria.getBvArtigoList() == null) {
            bvArtigoCategoria.setBvArtigoList(new ArrayList<BvArtigo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<BvAvaliador> attachedBvAvaliadorList = new ArrayList<BvAvaliador>();
            for (BvAvaliador bvAvaliadorListBvAvaliadorToAttach : bvArtigoCategoria.getBvAvaliadorList()) {
                bvAvaliadorListBvAvaliadorToAttach = em.getReference(bvAvaliadorListBvAvaliadorToAttach.getClass(), bvAvaliadorListBvAvaliadorToAttach.getIdLeitor());
                attachedBvAvaliadorList.add(bvAvaliadorListBvAvaliadorToAttach);
            }
            bvArtigoCategoria.setBvAvaliadorList(attachedBvAvaliadorList);
            List<BvArtigo> attachedBvArtigoList = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListBvArtigoToAttach : bvArtigoCategoria.getBvArtigoList()) {
                bvArtigoListBvArtigoToAttach = em.getReference(bvArtigoListBvArtigoToAttach.getClass(), bvArtigoListBvArtigoToAttach.getIdartigo());
                attachedBvArtigoList.add(bvArtigoListBvArtigoToAttach);
            }
            bvArtigoCategoria.setBvArtigoList(attachedBvArtigoList);
            em.persist(bvArtigoCategoria);
            for (BvAvaliador bvAvaliadorListBvAvaliador : bvArtigoCategoria.getBvAvaliadorList()) {
                BvArtigoCategoria oldIdAreaOfBvAvaliadorListBvAvaliador = bvAvaliadorListBvAvaliador.getIdArea();
                bvAvaliadorListBvAvaliador.setIdArea(bvArtigoCategoria);
                bvAvaliadorListBvAvaliador = em.merge(bvAvaliadorListBvAvaliador);
                if (oldIdAreaOfBvAvaliadorListBvAvaliador != null) {
                    oldIdAreaOfBvAvaliadorListBvAvaliador.getBvAvaliadorList().remove(bvAvaliadorListBvAvaliador);
                    oldIdAreaOfBvAvaliadorListBvAvaliador = em.merge(oldIdAreaOfBvAvaliadorListBvAvaliador);
                }
            }
            for (BvArtigo bvArtigoListBvArtigo : bvArtigoCategoria.getBvArtigoList()) {
                BvArtigoCategoria oldTipodocOfBvArtigoListBvArtigo = bvArtigoListBvArtigo.getTipodoc();
                bvArtigoListBvArtigo.setTipodoc(bvArtigoCategoria);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
                if (oldTipodocOfBvArtigoListBvArtigo != null) {
                    oldTipodocOfBvArtigoListBvArtigo.getBvArtigoList().remove(bvArtigoListBvArtigo);
                    oldTipodocOfBvArtigoListBvArtigo = em.merge(oldTipodocOfBvArtigoListBvArtigo);
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
            List<BvAvaliador> bvAvaliadorListOld = persistentBvArtigoCategoria.getBvAvaliadorList();
            List<BvAvaliador> bvAvaliadorListNew = bvArtigoCategoria.getBvAvaliadorList();
            List<BvArtigo> bvArtigoListOld = persistentBvArtigoCategoria.getBvArtigoList();
            List<BvArtigo> bvArtigoListNew = bvArtigoCategoria.getBvArtigoList();
            List<String> illegalOrphanMessages = null;
            for (BvAvaliador bvAvaliadorListOldBvAvaliador : bvAvaliadorListOld) {
                if (!bvAvaliadorListNew.contains(bvAvaliadorListOldBvAvaliador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BvAvaliador " + bvAvaliadorListOldBvAvaliador + " since its idArea field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<BvAvaliador> attachedBvAvaliadorListNew = new ArrayList<BvAvaliador>();
            for (BvAvaliador bvAvaliadorListNewBvAvaliadorToAttach : bvAvaliadorListNew) {
                bvAvaliadorListNewBvAvaliadorToAttach = em.getReference(bvAvaliadorListNewBvAvaliadorToAttach.getClass(), bvAvaliadorListNewBvAvaliadorToAttach.getIdLeitor());
                attachedBvAvaliadorListNew.add(bvAvaliadorListNewBvAvaliadorToAttach);
            }
            bvAvaliadorListNew = attachedBvAvaliadorListNew;
            bvArtigoCategoria.setBvAvaliadorList(bvAvaliadorListNew);
            List<BvArtigo> attachedBvArtigoListNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListNewBvArtigoToAttach : bvArtigoListNew) {
                bvArtigoListNewBvArtigoToAttach = em.getReference(bvArtigoListNewBvArtigoToAttach.getClass(), bvArtigoListNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoListNew.add(bvArtigoListNewBvArtigoToAttach);
            }
            bvArtigoListNew = attachedBvArtigoListNew;
            bvArtigoCategoria.setBvArtigoList(bvArtigoListNew);
            bvArtigoCategoria = em.merge(bvArtigoCategoria);
            for (BvAvaliador bvAvaliadorListNewBvAvaliador : bvAvaliadorListNew) {
                if (!bvAvaliadorListOld.contains(bvAvaliadorListNewBvAvaliador)) {
                    BvArtigoCategoria oldIdAreaOfBvAvaliadorListNewBvAvaliador = bvAvaliadorListNewBvAvaliador.getIdArea();
                    bvAvaliadorListNewBvAvaliador.setIdArea(bvArtigoCategoria);
                    bvAvaliadorListNewBvAvaliador = em.merge(bvAvaliadorListNewBvAvaliador);
                    if (oldIdAreaOfBvAvaliadorListNewBvAvaliador != null && !oldIdAreaOfBvAvaliadorListNewBvAvaliador.equals(bvArtigoCategoria)) {
                        oldIdAreaOfBvAvaliadorListNewBvAvaliador.getBvAvaliadorList().remove(bvAvaliadorListNewBvAvaliador);
                        oldIdAreaOfBvAvaliadorListNewBvAvaliador = em.merge(oldIdAreaOfBvAvaliadorListNewBvAvaliador);
                    }
                }
            }
            for (BvArtigo bvArtigoListOldBvArtigo : bvArtigoListOld) {
                if (!bvArtigoListNew.contains(bvArtigoListOldBvArtigo)) {
                    bvArtigoListOldBvArtigo.setTipodoc(null);
                    bvArtigoListOldBvArtigo = em.merge(bvArtigoListOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoListNewBvArtigo : bvArtigoListNew) {
                if (!bvArtigoListOld.contains(bvArtigoListNewBvArtigo)) {
                    BvArtigoCategoria oldTipodocOfBvArtigoListNewBvArtigo = bvArtigoListNewBvArtigo.getTipodoc();
                    bvArtigoListNewBvArtigo.setTipodoc(bvArtigoCategoria);
                    bvArtigoListNewBvArtigo = em.merge(bvArtigoListNewBvArtigo);
                    if (oldTipodocOfBvArtigoListNewBvArtigo != null && !oldTipodocOfBvArtigoListNewBvArtigo.equals(bvArtigoCategoria)) {
                        oldTipodocOfBvArtigoListNewBvArtigo.getBvArtigoList().remove(bvArtigoListNewBvArtigo);
                        oldTipodocOfBvArtigoListNewBvArtigo = em.merge(oldTipodocOfBvArtigoListNewBvArtigo);
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
            List<BvAvaliador> bvAvaliadorListOrphanCheck = bvArtigoCategoria.getBvAvaliadorList();
            for (BvAvaliador bvAvaliadorListOrphanCheckBvAvaliador : bvAvaliadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BvArtigoCategoria (" + bvArtigoCategoria + ") cannot be destroyed since the BvAvaliador " + bvAvaliadorListOrphanCheckBvAvaliador + " in its bvAvaliadorList field has a non-nullable idArea field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<BvArtigo> bvArtigoList = bvArtigoCategoria.getBvArtigoList();
            for (BvArtigo bvArtigoListBvArtigo : bvArtigoList) {
                bvArtigoListBvArtigo.setTipodoc(null);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
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
