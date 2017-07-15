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
import entidades.BLeitor;
import entidades.BvArtigo;
import entidades.BvLeitura;
import entidades.BvLeituraPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class BvLeituraJpaController implements Serializable {

    public BvLeituraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BvLeitura bvLeitura) throws PreexistingEntityException, Exception {
        if (bvLeitura.getBvLeituraPK() == null) {
            bvLeitura.setBvLeituraPK(new BvLeituraPK());
        }
        bvLeitura.getBvLeituraPK().setLeitor(bvLeitura.getBLeitor().getNrCartao());
        bvLeitura.getBvLeituraPK().setObra(bvLeitura.getBvArtigo().getIdartigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitor BLeitor = bvLeitura.getBLeitor();
            if (BLeitor != null) {
                BLeitor = em.getReference(BLeitor.getClass(), BLeitor.getNrCartao());
                bvLeitura.setBLeitor(BLeitor);
            }
            BvArtigo bvArtigo = bvLeitura.getBvArtigo();
            if (bvArtigo != null) {
                bvArtigo = em.getReference(bvArtigo.getClass(), bvArtigo.getIdartigo());
                bvLeitura.setBvArtigo(bvArtigo);
            }
            em.persist(bvLeitura);
            if (BLeitor != null) {
                BLeitor.getBvLeituraList().add(bvLeitura);
                BLeitor = em.merge(BLeitor);
            }
            if (bvArtigo != null) {
                bvArtigo.getBvLeituraList().add(bvLeitura);
                bvArtigo = em.merge(bvArtigo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBvLeitura(bvLeitura.getBvLeituraPK()) != null) {
                throw new PreexistingEntityException("BvLeitura " + bvLeitura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BvLeitura bvLeitura) throws NonexistentEntityException, Exception {
        bvLeitura.getBvLeituraPK().setLeitor(bvLeitura.getBLeitor().getNrCartao());
        bvLeitura.getBvLeituraPK().setObra(bvLeitura.getBvArtigo().getIdartigo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BvLeitura persistentBvLeitura = em.find(BvLeitura.class, bvLeitura.getBvLeituraPK());
            BLeitor BLeitorOld = persistentBvLeitura.getBLeitor();
            BLeitor BLeitorNew = bvLeitura.getBLeitor();
            BvArtigo bvArtigoOld = persistentBvLeitura.getBvArtigo();
            BvArtigo bvArtigoNew = bvLeitura.getBvArtigo();
            if (BLeitorNew != null) {
                BLeitorNew = em.getReference(BLeitorNew.getClass(), BLeitorNew.getNrCartao());
                bvLeitura.setBLeitor(BLeitorNew);
            }
            if (bvArtigoNew != null) {
                bvArtigoNew = em.getReference(bvArtigoNew.getClass(), bvArtigoNew.getIdartigo());
                bvLeitura.setBvArtigo(bvArtigoNew);
            }
            bvLeitura = em.merge(bvLeitura);
            if (BLeitorOld != null && !BLeitorOld.equals(BLeitorNew)) {
                BLeitorOld.getBvLeituraList().remove(bvLeitura);
                BLeitorOld = em.merge(BLeitorOld);
            }
            if (BLeitorNew != null && !BLeitorNew.equals(BLeitorOld)) {
                BLeitorNew.getBvLeituraList().add(bvLeitura);
                BLeitorNew = em.merge(BLeitorNew);
            }
            if (bvArtigoOld != null && !bvArtigoOld.equals(bvArtigoNew)) {
                bvArtigoOld.getBvLeituraList().remove(bvLeitura);
                bvArtigoOld = em.merge(bvArtigoOld);
            }
            if (bvArtigoNew != null && !bvArtigoNew.equals(bvArtigoOld)) {
                bvArtigoNew.getBvLeituraList().add(bvLeitura);
                bvArtigoNew = em.merge(bvArtigoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BvLeituraPK id = bvLeitura.getBvLeituraPK();
                if (findBvLeitura(id) == null) {
                    throw new NonexistentEntityException("The bvLeitura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BvLeituraPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BvLeitura bvLeitura;
            try {
                bvLeitura = em.getReference(BvLeitura.class, id);
                bvLeitura.getBvLeituraPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bvLeitura with id " + id + " no longer exists.", enfe);
            }
            BLeitor BLeitor = bvLeitura.getBLeitor();
            if (BLeitor != null) {
                BLeitor.getBvLeituraList().remove(bvLeitura);
                BLeitor = em.merge(BLeitor);
            }
            BvArtigo bvArtigo = bvLeitura.getBvArtigo();
            if (bvArtigo != null) {
                bvArtigo.getBvLeituraList().remove(bvLeitura);
                bvArtigo = em.merge(bvArtigo);
            }
            em.remove(bvLeitura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BvLeitura> findBvLeituraEntities() {
        return findBvLeituraEntities(true, -1, -1);
    }

    public List<BvLeitura> findBvLeituraEntities(int maxResults, int firstResult) {
        return findBvLeituraEntities(false, maxResults, firstResult);
    }

    private List<BvLeitura> findBvLeituraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BvLeitura.class));
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

    public BvLeitura findBvLeitura(BvLeituraPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BvLeitura.class, id);
        } finally {
            em.close();
        }
    }

    public int getBvLeituraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BvLeitura> rt = cq.from(BvLeitura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
