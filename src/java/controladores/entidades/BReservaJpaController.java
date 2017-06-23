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
import entidades.SgExemplar;
import entidades.SgEmprestimo;
import java.util.ArrayList;
import java.util.Collection;
import entidades.BNotificacao;
import entidades.BReserva;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class BReservaJpaController implements Serializable {

    public BReservaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BReserva BReserva) {
        if (BReserva.getSgEmprestimoCollection() == null) {
            BReserva.setSgEmprestimoCollection(new ArrayList<SgEmprestimo>());
        }
        if (BReserva.getBNotificacaoCollection() == null) {
            BReserva.setBNotificacaoCollection(new ArrayList<BNotificacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitor leitor = BReserva.getLeitor();
            if (leitor != null) {
                leitor = em.getReference(leitor.getClass(), leitor.getNrCartao());
                BReserva.setLeitor(leitor);
            }
            SgExemplar livro = BReserva.getLivro();
            if (livro != null) {
                livro = em.getReference(livro.getClass(), livro.getNrRegisto());
                BReserva.setLivro(livro);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollection = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimoToAttach : BReserva.getSgEmprestimoCollection()) {
                sgEmprestimoCollectionSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollection.add(sgEmprestimoCollectionSgEmprestimoToAttach);
            }
            BReserva.setSgEmprestimoCollection(attachedSgEmprestimoCollection);
            Collection<BNotificacao> attachedBNotificacaoCollection = new ArrayList<BNotificacao>();
            for (BNotificacao BNotificacaoCollectionBNotificacaoToAttach : BReserva.getBNotificacaoCollection()) {
                BNotificacaoCollectionBNotificacaoToAttach = em.getReference(BNotificacaoCollectionBNotificacaoToAttach.getClass(), BNotificacaoCollectionBNotificacaoToAttach.getIdNotificacao());
                attachedBNotificacaoCollection.add(BNotificacaoCollectionBNotificacaoToAttach);
            }
            BReserva.setBNotificacaoCollection(attachedBNotificacaoCollection);
            em.persist(BReserva);
            if (leitor != null) {
                leitor.getBReservaCollection().add(BReserva);
                leitor = em.merge(leitor);
            }
            if (livro != null) {
                livro.getBReservaCollection().add(BReserva);
                livro = em.merge(livro);
            }
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : BReserva.getSgEmprestimoCollection()) {
                BReserva oldReservaRefOfSgEmprestimoCollectionSgEmprestimo = sgEmprestimoCollectionSgEmprestimo.getReservaRef();
                sgEmprestimoCollectionSgEmprestimo.setReservaRef(BReserva);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
                if (oldReservaRefOfSgEmprestimoCollectionSgEmprestimo != null) {
                    oldReservaRefOfSgEmprestimoCollectionSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionSgEmprestimo);
                    oldReservaRefOfSgEmprestimoCollectionSgEmprestimo = em.merge(oldReservaRefOfSgEmprestimoCollectionSgEmprestimo);
                }
            }
            for (BNotificacao BNotificacaoCollectionBNotificacao : BReserva.getBNotificacaoCollection()) {
                BReserva oldIdReservaOfBNotificacaoCollectionBNotificacao = BNotificacaoCollectionBNotificacao.getIdReserva();
                BNotificacaoCollectionBNotificacao.setIdReserva(BReserva);
                BNotificacaoCollectionBNotificacao = em.merge(BNotificacaoCollectionBNotificacao);
                if (oldIdReservaOfBNotificacaoCollectionBNotificacao != null) {
                    oldIdReservaOfBNotificacaoCollectionBNotificacao.getBNotificacaoCollection().remove(BNotificacaoCollectionBNotificacao);
                    oldIdReservaOfBNotificacaoCollectionBNotificacao = em.merge(oldIdReservaOfBNotificacaoCollectionBNotificacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BReserva BReserva) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BReserva persistentBReserva = em.find(BReserva.class, BReserva.getIdagenda());
            BLeitor leitorOld = persistentBReserva.getLeitor();
            BLeitor leitorNew = BReserva.getLeitor();
            SgExemplar livroOld = persistentBReserva.getLivro();
            SgExemplar livroNew = BReserva.getLivro();
            Collection<SgEmprestimo> sgEmprestimoCollectionOld = persistentBReserva.getSgEmprestimoCollection();
            Collection<SgEmprestimo> sgEmprestimoCollectionNew = BReserva.getSgEmprestimoCollection();
            Collection<BNotificacao> BNotificacaoCollectionOld = persistentBReserva.getBNotificacaoCollection();
            Collection<BNotificacao> BNotificacaoCollectionNew = BReserva.getBNotificacaoCollection();
            if (leitorNew != null) {
                leitorNew = em.getReference(leitorNew.getClass(), leitorNew.getNrCartao());
                BReserva.setLeitor(leitorNew);
            }
            if (livroNew != null) {
                livroNew = em.getReference(livroNew.getClass(), livroNew.getNrRegisto());
                BReserva.setLivro(livroNew);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollectionNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimoToAttach : sgEmprestimoCollectionNew) {
                sgEmprestimoCollectionNewSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionNewSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollectionNew.add(sgEmprestimoCollectionNewSgEmprestimoToAttach);
            }
            sgEmprestimoCollectionNew = attachedSgEmprestimoCollectionNew;
            BReserva.setSgEmprestimoCollection(sgEmprestimoCollectionNew);
            Collection<BNotificacao> attachedBNotificacaoCollectionNew = new ArrayList<BNotificacao>();
            for (BNotificacao BNotificacaoCollectionNewBNotificacaoToAttach : BNotificacaoCollectionNew) {
                BNotificacaoCollectionNewBNotificacaoToAttach = em.getReference(BNotificacaoCollectionNewBNotificacaoToAttach.getClass(), BNotificacaoCollectionNewBNotificacaoToAttach.getIdNotificacao());
                attachedBNotificacaoCollectionNew.add(BNotificacaoCollectionNewBNotificacaoToAttach);
            }
            BNotificacaoCollectionNew = attachedBNotificacaoCollectionNew;
            BReserva.setBNotificacaoCollection(BNotificacaoCollectionNew);
            BReserva = em.merge(BReserva);
            if (leitorOld != null && !leitorOld.equals(leitorNew)) {
                leitorOld.getBReservaCollection().remove(BReserva);
                leitorOld = em.merge(leitorOld);
            }
            if (leitorNew != null && !leitorNew.equals(leitorOld)) {
                leitorNew.getBReservaCollection().add(BReserva);
                leitorNew = em.merge(leitorNew);
            }
            if (livroOld != null && !livroOld.equals(livroNew)) {
                livroOld.getBReservaCollection().remove(BReserva);
                livroOld = em.merge(livroOld);
            }
            if (livroNew != null && !livroNew.equals(livroOld)) {
                livroNew.getBReservaCollection().add(BReserva);
                livroNew = em.merge(livroNew);
            }
            for (SgEmprestimo sgEmprestimoCollectionOldSgEmprestimo : sgEmprestimoCollectionOld) {
                if (!sgEmprestimoCollectionNew.contains(sgEmprestimoCollectionOldSgEmprestimo)) {
                    sgEmprestimoCollectionOldSgEmprestimo.setReservaRef(null);
                    sgEmprestimoCollectionOldSgEmprestimo = em.merge(sgEmprestimoCollectionOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimo : sgEmprestimoCollectionNew) {
                if (!sgEmprestimoCollectionOld.contains(sgEmprestimoCollectionNewSgEmprestimo)) {
                    BReserva oldReservaRefOfSgEmprestimoCollectionNewSgEmprestimo = sgEmprestimoCollectionNewSgEmprestimo.getReservaRef();
                    sgEmprestimoCollectionNewSgEmprestimo.setReservaRef(BReserva);
                    sgEmprestimoCollectionNewSgEmprestimo = em.merge(sgEmprestimoCollectionNewSgEmprestimo);
                    if (oldReservaRefOfSgEmprestimoCollectionNewSgEmprestimo != null && !oldReservaRefOfSgEmprestimoCollectionNewSgEmprestimo.equals(BReserva)) {
                        oldReservaRefOfSgEmprestimoCollectionNewSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionNewSgEmprestimo);
                        oldReservaRefOfSgEmprestimoCollectionNewSgEmprestimo = em.merge(oldReservaRefOfSgEmprestimoCollectionNewSgEmprestimo);
                    }
                }
            }
            for (BNotificacao BNotificacaoCollectionOldBNotificacao : BNotificacaoCollectionOld) {
                if (!BNotificacaoCollectionNew.contains(BNotificacaoCollectionOldBNotificacao)) {
                    BNotificacaoCollectionOldBNotificacao.setIdReserva(null);
                    BNotificacaoCollectionOldBNotificacao = em.merge(BNotificacaoCollectionOldBNotificacao);
                }
            }
            for (BNotificacao BNotificacaoCollectionNewBNotificacao : BNotificacaoCollectionNew) {
                if (!BNotificacaoCollectionOld.contains(BNotificacaoCollectionNewBNotificacao)) {
                    BReserva oldIdReservaOfBNotificacaoCollectionNewBNotificacao = BNotificacaoCollectionNewBNotificacao.getIdReserva();
                    BNotificacaoCollectionNewBNotificacao.setIdReserva(BReserva);
                    BNotificacaoCollectionNewBNotificacao = em.merge(BNotificacaoCollectionNewBNotificacao);
                    if (oldIdReservaOfBNotificacaoCollectionNewBNotificacao != null && !oldIdReservaOfBNotificacaoCollectionNewBNotificacao.equals(BReserva)) {
                        oldIdReservaOfBNotificacaoCollectionNewBNotificacao.getBNotificacaoCollection().remove(BNotificacaoCollectionNewBNotificacao);
                        oldIdReservaOfBNotificacaoCollectionNewBNotificacao = em.merge(oldIdReservaOfBNotificacaoCollectionNewBNotificacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = BReserva.getIdagenda();
                if (findBReserva(id) == null) {
                    throw new NonexistentEntityException("The bReserva with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BReserva BReserva;
            try {
                BReserva = em.getReference(BReserva.class, id);
                BReserva.getIdagenda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The BReserva with id " + id + " no longer exists.", enfe);
            }
            BLeitor leitor = BReserva.getLeitor();
            if (leitor != null) {
                leitor.getBReservaCollection().remove(BReserva);
                leitor = em.merge(leitor);
            }
            SgExemplar livro = BReserva.getLivro();
            if (livro != null) {
                livro.getBReservaCollection().remove(BReserva);
                livro = em.merge(livro);
            }
            Collection<SgEmprestimo> sgEmprestimoCollection = BReserva.getSgEmprestimoCollection();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : sgEmprestimoCollection) {
                sgEmprestimoCollectionSgEmprestimo.setReservaRef(null);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
            }
            Collection<BNotificacao> BNotificacaoCollection = BReserva.getBNotificacaoCollection();
            for (BNotificacao BNotificacaoCollectionBNotificacao : BNotificacaoCollection) {
                BNotificacaoCollectionBNotificacao.setIdReserva(null);
                BNotificacaoCollectionBNotificacao = em.merge(BNotificacaoCollectionBNotificacao);
            }
            em.remove(BReserva);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BReserva> findBReservaEntities() {
        return findBReservaEntities(true, -1, -1);
    }

    public List<BReserva> findBReservaEntities(int maxResults, int firstResult) {
        return findBReservaEntities(false, maxResults, firstResult);
    }

    private List<BReserva> findBReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BReserva.class));
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

    public BReserva findBReserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BReserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getBReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BReserva> rt = cq.from(BReserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
