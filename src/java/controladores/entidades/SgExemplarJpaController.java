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
import entidades.SgObra;
import entidades.Users;
import entidades.SgEmprestimo;
import java.util.ArrayList;
import java.util.Collection;
import entidades.BReserva;
import entidades.SgExemplar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class SgExemplarJpaController implements Serializable {

    public SgExemplarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgExemplar sgExemplar) {
        if (sgExemplar.getSgEmprestimoCollection() == null) {
            sgExemplar.setSgEmprestimoCollection(new ArrayList<SgEmprestimo>());
        }
        if (sgExemplar.getBReservaCollection() == null) {
            sgExemplar.setBReservaCollection(new ArrayList<BReserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObra obraRef = sgExemplar.getObraRef();
            if (obraRef != null) {
                obraRef = em.getReference(obraRef.getClass(), obraRef.getIdlivro());
                sgExemplar.setObraRef(obraRef);
            }
            Users agenteRegisto = sgExemplar.getAgenteRegisto();
            if (agenteRegisto != null) {
                agenteRegisto = em.getReference(agenteRegisto.getClass(), agenteRegisto.getUtilizador());
                sgExemplar.setAgenteRegisto(agenteRegisto);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollection = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimoToAttach : sgExemplar.getSgEmprestimoCollection()) {
                sgEmprestimoCollectionSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollection.add(sgEmprestimoCollectionSgEmprestimoToAttach);
            }
            sgExemplar.setSgEmprestimoCollection(attachedSgEmprestimoCollection);
            Collection<BReserva> attachedBReservaCollection = new ArrayList<BReserva>();
            for (BReserva BReservaCollectionBReservaToAttach : sgExemplar.getBReservaCollection()) {
                BReservaCollectionBReservaToAttach = em.getReference(BReservaCollectionBReservaToAttach.getClass(), BReservaCollectionBReservaToAttach.getIdagenda());
                attachedBReservaCollection.add(BReservaCollectionBReservaToAttach);
            }
            sgExemplar.setBReservaCollection(attachedBReservaCollection);
            em.persist(sgExemplar);
            if (obraRef != null) {
                obraRef.getSgExemplarCollection().add(sgExemplar);
                obraRef = em.merge(obraRef);
            }
            if (agenteRegisto != null) {
                agenteRegisto.getSgExemplarCollection().add(sgExemplar);
                agenteRegisto = em.merge(agenteRegisto);
            }
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : sgExemplar.getSgEmprestimoCollection()) {
                SgExemplar oldExemplarRefOfSgEmprestimoCollectionSgEmprestimo = sgEmprestimoCollectionSgEmprestimo.getExemplarRef();
                sgEmprestimoCollectionSgEmprestimo.setExemplarRef(sgExemplar);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
                if (oldExemplarRefOfSgEmprestimoCollectionSgEmprestimo != null) {
                    oldExemplarRefOfSgEmprestimoCollectionSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionSgEmprestimo);
                    oldExemplarRefOfSgEmprestimoCollectionSgEmprestimo = em.merge(oldExemplarRefOfSgEmprestimoCollectionSgEmprestimo);
                }
            }
            for (BReserva BReservaCollectionBReserva : sgExemplar.getBReservaCollection()) {
                SgExemplar oldLivroOfBReservaCollectionBReserva = BReservaCollectionBReserva.getLivro();
                BReservaCollectionBReserva.setLivro(sgExemplar);
                BReservaCollectionBReserva = em.merge(BReservaCollectionBReserva);
                if (oldLivroOfBReservaCollectionBReserva != null) {
                    oldLivroOfBReservaCollectionBReserva.getBReservaCollection().remove(BReservaCollectionBReserva);
                    oldLivroOfBReservaCollectionBReserva = em.merge(oldLivroOfBReservaCollectionBReserva);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgExemplar sgExemplar) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgExemplar persistentSgExemplar = em.find(SgExemplar.class, sgExemplar.getNrRegisto());
            SgObra obraRefOld = persistentSgExemplar.getObraRef();
            SgObra obraRefNew = sgExemplar.getObraRef();
            Users agenteRegistoOld = persistentSgExemplar.getAgenteRegisto();
            Users agenteRegistoNew = sgExemplar.getAgenteRegisto();
            Collection<SgEmprestimo> sgEmprestimoCollectionOld = persistentSgExemplar.getSgEmprestimoCollection();
            Collection<SgEmprestimo> sgEmprestimoCollectionNew = sgExemplar.getSgEmprestimoCollection();
            Collection<BReserva> BReservaCollectionOld = persistentSgExemplar.getBReservaCollection();
            Collection<BReserva> BReservaCollectionNew = sgExemplar.getBReservaCollection();
            if (obraRefNew != null) {
                obraRefNew = em.getReference(obraRefNew.getClass(), obraRefNew.getIdlivro());
                sgExemplar.setObraRef(obraRefNew);
            }
            if (agenteRegistoNew != null) {
                agenteRegistoNew = em.getReference(agenteRegistoNew.getClass(), agenteRegistoNew.getUtilizador());
                sgExemplar.setAgenteRegisto(agenteRegistoNew);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollectionNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimoToAttach : sgEmprestimoCollectionNew) {
                sgEmprestimoCollectionNewSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionNewSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollectionNew.add(sgEmprestimoCollectionNewSgEmprestimoToAttach);
            }
            sgEmprestimoCollectionNew = attachedSgEmprestimoCollectionNew;
            sgExemplar.setSgEmprestimoCollection(sgEmprestimoCollectionNew);
            Collection<BReserva> attachedBReservaCollectionNew = new ArrayList<BReserva>();
            for (BReserva BReservaCollectionNewBReservaToAttach : BReservaCollectionNew) {
                BReservaCollectionNewBReservaToAttach = em.getReference(BReservaCollectionNewBReservaToAttach.getClass(), BReservaCollectionNewBReservaToAttach.getIdagenda());
                attachedBReservaCollectionNew.add(BReservaCollectionNewBReservaToAttach);
            }
            BReservaCollectionNew = attachedBReservaCollectionNew;
            sgExemplar.setBReservaCollection(BReservaCollectionNew);
            sgExemplar = em.merge(sgExemplar);
            if (obraRefOld != null && !obraRefOld.equals(obraRefNew)) {
                obraRefOld.getSgExemplarCollection().remove(sgExemplar);
                obraRefOld = em.merge(obraRefOld);
            }
            if (obraRefNew != null && !obraRefNew.equals(obraRefOld)) {
                obraRefNew.getSgExemplarCollection().add(sgExemplar);
                obraRefNew = em.merge(obraRefNew);
            }
            if (agenteRegistoOld != null && !agenteRegistoOld.equals(agenteRegistoNew)) {
                agenteRegistoOld.getSgExemplarCollection().remove(sgExemplar);
                agenteRegistoOld = em.merge(agenteRegistoOld);
            }
            if (agenteRegistoNew != null && !agenteRegistoNew.equals(agenteRegistoOld)) {
                agenteRegistoNew.getSgExemplarCollection().add(sgExemplar);
                agenteRegistoNew = em.merge(agenteRegistoNew);
            }
            for (SgEmprestimo sgEmprestimoCollectionOldSgEmprestimo : sgEmprestimoCollectionOld) {
                if (!sgEmprestimoCollectionNew.contains(sgEmprestimoCollectionOldSgEmprestimo)) {
                    sgEmprestimoCollectionOldSgEmprestimo.setExemplarRef(null);
                    sgEmprestimoCollectionOldSgEmprestimo = em.merge(sgEmprestimoCollectionOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimo : sgEmprestimoCollectionNew) {
                if (!sgEmprestimoCollectionOld.contains(sgEmprestimoCollectionNewSgEmprestimo)) {
                    SgExemplar oldExemplarRefOfSgEmprestimoCollectionNewSgEmprestimo = sgEmprestimoCollectionNewSgEmprestimo.getExemplarRef();
                    sgEmprestimoCollectionNewSgEmprestimo.setExemplarRef(sgExemplar);
                    sgEmprestimoCollectionNewSgEmprestimo = em.merge(sgEmprestimoCollectionNewSgEmprestimo);
                    if (oldExemplarRefOfSgEmprestimoCollectionNewSgEmprestimo != null && !oldExemplarRefOfSgEmprestimoCollectionNewSgEmprestimo.equals(sgExemplar)) {
                        oldExemplarRefOfSgEmprestimoCollectionNewSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionNewSgEmprestimo);
                        oldExemplarRefOfSgEmprestimoCollectionNewSgEmprestimo = em.merge(oldExemplarRefOfSgEmprestimoCollectionNewSgEmprestimo);
                    }
                }
            }
            for (BReserva BReservaCollectionOldBReserva : BReservaCollectionOld) {
                if (!BReservaCollectionNew.contains(BReservaCollectionOldBReserva)) {
                    BReservaCollectionOldBReserva.setLivro(null);
                    BReservaCollectionOldBReserva = em.merge(BReservaCollectionOldBReserva);
                }
            }
            for (BReserva BReservaCollectionNewBReserva : BReservaCollectionNew) {
                if (!BReservaCollectionOld.contains(BReservaCollectionNewBReserva)) {
                    SgExemplar oldLivroOfBReservaCollectionNewBReserva = BReservaCollectionNewBReserva.getLivro();
                    BReservaCollectionNewBReserva.setLivro(sgExemplar);
                    BReservaCollectionNewBReserva = em.merge(BReservaCollectionNewBReserva);
                    if (oldLivroOfBReservaCollectionNewBReserva != null && !oldLivroOfBReservaCollectionNewBReserva.equals(sgExemplar)) {
                        oldLivroOfBReservaCollectionNewBReserva.getBReservaCollection().remove(BReservaCollectionNewBReserva);
                        oldLivroOfBReservaCollectionNewBReserva = em.merge(oldLivroOfBReservaCollectionNewBReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sgExemplar.getNrRegisto();
                if (findSgExemplar(id) == null) {
                    throw new NonexistentEntityException("The sgExemplar with id " + id + " no longer exists.");
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
            SgExemplar sgExemplar;
            try {
                sgExemplar = em.getReference(SgExemplar.class, id);
                sgExemplar.getNrRegisto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgExemplar with id " + id + " no longer exists.", enfe);
            }
            SgObra obraRef = sgExemplar.getObraRef();
            if (obraRef != null) {
                obraRef.getSgExemplarCollection().remove(sgExemplar);
                obraRef = em.merge(obraRef);
            }
            Users agenteRegisto = sgExemplar.getAgenteRegisto();
            if (agenteRegisto != null) {
                agenteRegisto.getSgExemplarCollection().remove(sgExemplar);
                agenteRegisto = em.merge(agenteRegisto);
            }
            Collection<SgEmprestimo> sgEmprestimoCollection = sgExemplar.getSgEmprestimoCollection();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : sgEmprestimoCollection) {
                sgEmprestimoCollectionSgEmprestimo.setExemplarRef(null);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
            }
            Collection<BReserva> BReservaCollection = sgExemplar.getBReservaCollection();
            for (BReserva BReservaCollectionBReserva : BReservaCollection) {
                BReservaCollectionBReserva.setLivro(null);
                BReservaCollectionBReserva = em.merge(BReservaCollectionBReserva);
            }
            em.remove(sgExemplar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgExemplar> findSgExemplarEntities() {
        return findSgExemplarEntities(true, -1, -1);
    }

    public List<SgExemplar> findSgExemplarEntities(int maxResults, int firstResult) {
        return findSgExemplarEntities(false, maxResults, firstResult);
    }

    private List<SgExemplar> findSgExemplarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgExemplar.class));
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

    public SgExemplar findSgExemplar(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgExemplar.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgExemplarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgExemplar> rt = cq.from(SgExemplar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
