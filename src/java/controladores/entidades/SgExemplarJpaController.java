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
import java.util.List;
import entidades.BReserva;
import entidades.SgExemplar;
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
        if (sgExemplar.getSgEmprestimoList() == null) {
            sgExemplar.setSgEmprestimoList(new ArrayList<SgEmprestimo>());
        }
        if (sgExemplar.getBReservaList() == null) {
            sgExemplar.setBReservaList(new ArrayList<BReserva>());
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
            List<SgEmprestimo> attachedSgEmprestimoList = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListSgEmprestimoToAttach : sgExemplar.getSgEmprestimoList()) {
                sgEmprestimoListSgEmprestimoToAttach = em.getReference(sgEmprestimoListSgEmprestimoToAttach.getClass(), sgEmprestimoListSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoList.add(sgEmprestimoListSgEmprestimoToAttach);
            }
            sgExemplar.setSgEmprestimoList(attachedSgEmprestimoList);
            List<BReserva> attachedBReservaList = new ArrayList<BReserva>();
            for (BReserva BReservaListBReservaToAttach : sgExemplar.getBReservaList()) {
                BReservaListBReservaToAttach = em.getReference(BReservaListBReservaToAttach.getClass(), BReservaListBReservaToAttach.getIdagenda());
                attachedBReservaList.add(BReservaListBReservaToAttach);
            }
            sgExemplar.setBReservaList(attachedBReservaList);
            em.persist(sgExemplar);
            if (obraRef != null) {
                obraRef.getSgExemplarList().add(sgExemplar);
                obraRef = em.merge(obraRef);
            }
            if (agenteRegisto != null) {
                agenteRegisto.getSgExemplarList().add(sgExemplar);
                agenteRegisto = em.merge(agenteRegisto);
            }
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : sgExemplar.getSgEmprestimoList()) {
                SgExemplar oldExemplarRefOfSgEmprestimoListSgEmprestimo = sgEmprestimoListSgEmprestimo.getExemplarRef();
                sgEmprestimoListSgEmprestimo.setExemplarRef(sgExemplar);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
                if (oldExemplarRefOfSgEmprestimoListSgEmprestimo != null) {
                    oldExemplarRefOfSgEmprestimoListSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListSgEmprestimo);
                    oldExemplarRefOfSgEmprestimoListSgEmprestimo = em.merge(oldExemplarRefOfSgEmprestimoListSgEmprestimo);
                }
            }
            for (BReserva BReservaListBReserva : sgExemplar.getBReservaList()) {
                SgExemplar oldLivroOfBReservaListBReserva = BReservaListBReserva.getLivro();
                BReservaListBReserva.setLivro(sgExemplar);
                BReservaListBReserva = em.merge(BReservaListBReserva);
                if (oldLivroOfBReservaListBReserva != null) {
                    oldLivroOfBReservaListBReserva.getBReservaList().remove(BReservaListBReserva);
                    oldLivroOfBReservaListBReserva = em.merge(oldLivroOfBReservaListBReserva);
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
            List<SgEmprestimo> sgEmprestimoListOld = persistentSgExemplar.getSgEmprestimoList();
            List<SgEmprestimo> sgEmprestimoListNew = sgExemplar.getSgEmprestimoList();
            List<BReserva> BReservaListOld = persistentSgExemplar.getBReservaList();
            List<BReserva> BReservaListNew = sgExemplar.getBReservaList();
            if (obraRefNew != null) {
                obraRefNew = em.getReference(obraRefNew.getClass(), obraRefNew.getIdlivro());
                sgExemplar.setObraRef(obraRefNew);
            }
            if (agenteRegistoNew != null) {
                agenteRegistoNew = em.getReference(agenteRegistoNew.getClass(), agenteRegistoNew.getUtilizador());
                sgExemplar.setAgenteRegisto(agenteRegistoNew);
            }
            List<SgEmprestimo> attachedSgEmprestimoListNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimoToAttach : sgEmprestimoListNew) {
                sgEmprestimoListNewSgEmprestimoToAttach = em.getReference(sgEmprestimoListNewSgEmprestimoToAttach.getClass(), sgEmprestimoListNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoListNew.add(sgEmprestimoListNewSgEmprestimoToAttach);
            }
            sgEmprestimoListNew = attachedSgEmprestimoListNew;
            sgExemplar.setSgEmprestimoList(sgEmprestimoListNew);
            List<BReserva> attachedBReservaListNew = new ArrayList<BReserva>();
            for (BReserva BReservaListNewBReservaToAttach : BReservaListNew) {
                BReservaListNewBReservaToAttach = em.getReference(BReservaListNewBReservaToAttach.getClass(), BReservaListNewBReservaToAttach.getIdagenda());
                attachedBReservaListNew.add(BReservaListNewBReservaToAttach);
            }
            BReservaListNew = attachedBReservaListNew;
            sgExemplar.setBReservaList(BReservaListNew);
            sgExemplar = em.merge(sgExemplar);
            if (obraRefOld != null && !obraRefOld.equals(obraRefNew)) {
                obraRefOld.getSgExemplarList().remove(sgExemplar);
                obraRefOld = em.merge(obraRefOld);
            }
            if (obraRefNew != null && !obraRefNew.equals(obraRefOld)) {
                obraRefNew.getSgExemplarList().add(sgExemplar);
                obraRefNew = em.merge(obraRefNew);
            }
            if (agenteRegistoOld != null && !agenteRegistoOld.equals(agenteRegistoNew)) {
                agenteRegistoOld.getSgExemplarList().remove(sgExemplar);
                agenteRegistoOld = em.merge(agenteRegistoOld);
            }
            if (agenteRegistoNew != null && !agenteRegistoNew.equals(agenteRegistoOld)) {
                agenteRegistoNew.getSgExemplarList().add(sgExemplar);
                agenteRegistoNew = em.merge(agenteRegistoNew);
            }
            for (SgEmprestimo sgEmprestimoListOldSgEmprestimo : sgEmprestimoListOld) {
                if (!sgEmprestimoListNew.contains(sgEmprestimoListOldSgEmprestimo)) {
                    sgEmprestimoListOldSgEmprestimo.setExemplarRef(null);
                    sgEmprestimoListOldSgEmprestimo = em.merge(sgEmprestimoListOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimo : sgEmprestimoListNew) {
                if (!sgEmprestimoListOld.contains(sgEmprestimoListNewSgEmprestimo)) {
                    SgExemplar oldExemplarRefOfSgEmprestimoListNewSgEmprestimo = sgEmprestimoListNewSgEmprestimo.getExemplarRef();
                    sgEmprestimoListNewSgEmprestimo.setExemplarRef(sgExemplar);
                    sgEmprestimoListNewSgEmprestimo = em.merge(sgEmprestimoListNewSgEmprestimo);
                    if (oldExemplarRefOfSgEmprestimoListNewSgEmprestimo != null && !oldExemplarRefOfSgEmprestimoListNewSgEmprestimo.equals(sgExemplar)) {
                        oldExemplarRefOfSgEmprestimoListNewSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListNewSgEmprestimo);
                        oldExemplarRefOfSgEmprestimoListNewSgEmprestimo = em.merge(oldExemplarRefOfSgEmprestimoListNewSgEmprestimo);
                    }
                }
            }
            for (BReserva BReservaListOldBReserva : BReservaListOld) {
                if (!BReservaListNew.contains(BReservaListOldBReserva)) {
                    BReservaListOldBReserva.setLivro(null);
                    BReservaListOldBReserva = em.merge(BReservaListOldBReserva);
                }
            }
            for (BReserva BReservaListNewBReserva : BReservaListNew) {
                if (!BReservaListOld.contains(BReservaListNewBReserva)) {
                    SgExemplar oldLivroOfBReservaListNewBReserva = BReservaListNewBReserva.getLivro();
                    BReservaListNewBReserva.setLivro(sgExemplar);
                    BReservaListNewBReserva = em.merge(BReservaListNewBReserva);
                    if (oldLivroOfBReservaListNewBReserva != null && !oldLivroOfBReservaListNewBReserva.equals(sgExemplar)) {
                        oldLivroOfBReservaListNewBReserva.getBReservaList().remove(BReservaListNewBReserva);
                        oldLivroOfBReservaListNewBReserva = em.merge(oldLivroOfBReservaListNewBReserva);
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
                obraRef.getSgExemplarList().remove(sgExemplar);
                obraRef = em.merge(obraRef);
            }
            Users agenteRegisto = sgExemplar.getAgenteRegisto();
            if (agenteRegisto != null) {
                agenteRegisto.getSgExemplarList().remove(sgExemplar);
                agenteRegisto = em.merge(agenteRegisto);
            }
            List<SgEmprestimo> sgEmprestimoList = sgExemplar.getSgEmprestimoList();
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : sgEmprestimoList) {
                sgEmprestimoListSgEmprestimo.setExemplarRef(null);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
            }
            List<BReserva> BReservaList = sgExemplar.getBReservaList();
            for (BReserva BReservaListBReserva : BReservaList) {
                BReservaListBReserva.setLivro(null);
                BReservaListBReserva = em.merge(BReservaListBReserva);
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
