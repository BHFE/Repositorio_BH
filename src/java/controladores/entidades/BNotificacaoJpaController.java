/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.BLeitor;
import entidades.BNotificacao;
import entidades.BReserva;
import entidades.BvArtigo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class BNotificacaoJpaController implements Serializable {

    public BNotificacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BNotificacao BNotificacao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitor idLeitor = BNotificacao.getIdLeitor();
            if (idLeitor != null) {
                idLeitor = em.getReference(idLeitor.getClass(), idLeitor.getNrCartao());
                BNotificacao.setIdLeitor(idLeitor);
            }
            BReserva idReserva = BNotificacao.getIdReserva();
            if (idReserva != null) {
                idReserva = em.getReference(idReserva.getClass(), idReserva.getIdagenda());
                BNotificacao.setIdReserva(idReserva);
            }
            BvArtigo idPublicacao = BNotificacao.getIdPublicacao();
            if (idPublicacao != null) {
                idPublicacao = em.getReference(idPublicacao.getClass(), idPublicacao.getIdartigo());
                BNotificacao.setIdPublicacao(idPublicacao);
            }
            em.persist(BNotificacao);
            if (idLeitor != null) {
                idLeitor.getBNotificacaoCollection().add(BNotificacao);
                idLeitor = em.merge(idLeitor);
            }
            if (idReserva != null) {
                idReserva.getBNotificacaoCollection().add(BNotificacao);
                idReserva = em.merge(idReserva);
            }
            if (idPublicacao != null) {
                idPublicacao.getBNotificacaoCollection().add(BNotificacao);
                idPublicacao = em.merge(idPublicacao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BNotificacao BNotificacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BNotificacao persistentBNotificacao = em.find(BNotificacao.class, BNotificacao.getIdNotificacao());
            BLeitor idLeitorOld = persistentBNotificacao.getIdLeitor();
            BLeitor idLeitorNew = BNotificacao.getIdLeitor();
            BReserva idReservaOld = persistentBNotificacao.getIdReserva();
            BReserva idReservaNew = BNotificacao.getIdReserva();
            BvArtigo idPublicacaoOld = persistentBNotificacao.getIdPublicacao();
            BvArtigo idPublicacaoNew = BNotificacao.getIdPublicacao();
            if (idLeitorNew != null) {
                idLeitorNew = em.getReference(idLeitorNew.getClass(), idLeitorNew.getNrCartao());
                BNotificacao.setIdLeitor(idLeitorNew);
            }
            if (idReservaNew != null) {
                idReservaNew = em.getReference(idReservaNew.getClass(), idReservaNew.getIdagenda());
                BNotificacao.setIdReserva(idReservaNew);
            }
            if (idPublicacaoNew != null) {
                idPublicacaoNew = em.getReference(idPublicacaoNew.getClass(), idPublicacaoNew.getIdartigo());
                BNotificacao.setIdPublicacao(idPublicacaoNew);
            }
            BNotificacao = em.merge(BNotificacao);
            if (idLeitorOld != null && !idLeitorOld.equals(idLeitorNew)) {
                idLeitorOld.getBNotificacaoCollection().remove(BNotificacao);
                idLeitorOld = em.merge(idLeitorOld);
            }
            if (idLeitorNew != null && !idLeitorNew.equals(idLeitorOld)) {
                idLeitorNew.getBNotificacaoCollection().add(BNotificacao);
                idLeitorNew = em.merge(idLeitorNew);
            }
            if (idReservaOld != null && !idReservaOld.equals(idReservaNew)) {
                idReservaOld.getBNotificacaoCollection().remove(BNotificacao);
                idReservaOld = em.merge(idReservaOld);
            }
            if (idReservaNew != null && !idReservaNew.equals(idReservaOld)) {
                idReservaNew.getBNotificacaoCollection().add(BNotificacao);
                idReservaNew = em.merge(idReservaNew);
            }
            if (idPublicacaoOld != null && !idPublicacaoOld.equals(idPublicacaoNew)) {
                idPublicacaoOld.getBNotificacaoCollection().remove(BNotificacao);
                idPublicacaoOld = em.merge(idPublicacaoOld);
            }
            if (idPublicacaoNew != null && !idPublicacaoNew.equals(idPublicacaoOld)) {
                idPublicacaoNew.getBNotificacaoCollection().add(BNotificacao);
                idPublicacaoNew = em.merge(idPublicacaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = BNotificacao.getIdNotificacao();
                if (findBNotificacao(id) == null) {
                    throw new NonexistentEntityException("The bNotificacao with id " + id + " no longer exists.");
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
            BNotificacao BNotificacao;
            try {
                BNotificacao = em.getReference(BNotificacao.class, id);
                BNotificacao.getIdNotificacao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The BNotificacao with id " + id + " no longer exists.", enfe);
            }
            BLeitor idLeitor = BNotificacao.getIdLeitor();
            if (idLeitor != null) {
                idLeitor.getBNotificacaoCollection().remove(BNotificacao);
                idLeitor = em.merge(idLeitor);
            }
            BReserva idReserva = BNotificacao.getIdReserva();
            if (idReserva != null) {
                idReserva.getBNotificacaoCollection().remove(BNotificacao);
                idReserva = em.merge(idReserva);
            }
            BvArtigo idPublicacao = BNotificacao.getIdPublicacao();
            if (idPublicacao != null) {
                idPublicacao.getBNotificacaoCollection().remove(BNotificacao);
                idPublicacao = em.merge(idPublicacao);
            }
            em.remove(BNotificacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BNotificacao> findBNotificacaoEntities() {
        return findBNotificacaoEntities(true, -1, -1);
    }

    public List<BNotificacao> findBNotificacaoEntities(int maxResults, int firstResult) {
        return findBNotificacaoEntities(false, maxResults, firstResult);
    }

    private List<BNotificacao> findBNotificacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BNotificacao.class));
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

    public BNotificacao findBNotificacao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BNotificacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getBNotificacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BNotificacao> rt = cq.from(BNotificacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
