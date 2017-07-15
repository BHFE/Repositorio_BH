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
import entidades.BReserva;
import entidades.SgExemplar;
import entidades.SgEmprestimo;
import java.util.ArrayList;
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
        if (BReserva.getSgEmprestimoList() == null) {
            BReserva.setSgEmprestimoList(new ArrayList<SgEmprestimo>());
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
            List<SgEmprestimo> attachedSgEmprestimoList = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListSgEmprestimoToAttach : BReserva.getSgEmprestimoList()) {
                sgEmprestimoListSgEmprestimoToAttach = em.getReference(sgEmprestimoListSgEmprestimoToAttach.getClass(), sgEmprestimoListSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoList.add(sgEmprestimoListSgEmprestimoToAttach);
            }
            BReserva.setSgEmprestimoList(attachedSgEmprestimoList);
            em.persist(BReserva);
            if (leitor != null) {
                leitor.getBReservaList().add(BReserva);
                leitor = em.merge(leitor);
            }
            if (livro != null) {
                livro.getBReservaList().add(BReserva);
                livro = em.merge(livro);
            }
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : BReserva.getSgEmprestimoList()) {
                BReserva oldReservaRefOfSgEmprestimoListSgEmprestimo = sgEmprestimoListSgEmprestimo.getReservaRef();
                sgEmprestimoListSgEmprestimo.setReservaRef(BReserva);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
                if (oldReservaRefOfSgEmprestimoListSgEmprestimo != null) {
                    oldReservaRefOfSgEmprestimoListSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListSgEmprestimo);
                    oldReservaRefOfSgEmprestimoListSgEmprestimo = em.merge(oldReservaRefOfSgEmprestimoListSgEmprestimo);
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
            List<SgEmprestimo> sgEmprestimoListOld = persistentBReserva.getSgEmprestimoList();
            List<SgEmprestimo> sgEmprestimoListNew = BReserva.getSgEmprestimoList();
            if (leitorNew != null) {
                leitorNew = em.getReference(leitorNew.getClass(), leitorNew.getNrCartao());
                BReserva.setLeitor(leitorNew);
            }
            if (livroNew != null) {
                livroNew = em.getReference(livroNew.getClass(), livroNew.getNrRegisto());
                BReserva.setLivro(livroNew);
            }
            List<SgEmprestimo> attachedSgEmprestimoListNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimoToAttach : sgEmprestimoListNew) {
                sgEmprestimoListNewSgEmprestimoToAttach = em.getReference(sgEmprestimoListNewSgEmprestimoToAttach.getClass(), sgEmprestimoListNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoListNew.add(sgEmprestimoListNewSgEmprestimoToAttach);
            }
            sgEmprestimoListNew = attachedSgEmprestimoListNew;
            BReserva.setSgEmprestimoList(sgEmprestimoListNew);
            BReserva = em.merge(BReserva);
            if (leitorOld != null && !leitorOld.equals(leitorNew)) {
                leitorOld.getBReservaList().remove(BReserva);
                leitorOld = em.merge(leitorOld);
            }
            if (leitorNew != null && !leitorNew.equals(leitorOld)) {
                leitorNew.getBReservaList().add(BReserva);
                leitorNew = em.merge(leitorNew);
            }
            if (livroOld != null && !livroOld.equals(livroNew)) {
                livroOld.getBReservaList().remove(BReserva);
                livroOld = em.merge(livroOld);
            }
            if (livroNew != null && !livroNew.equals(livroOld)) {
                livroNew.getBReservaList().add(BReserva);
                livroNew = em.merge(livroNew);
            }
            for (SgEmprestimo sgEmprestimoListOldSgEmprestimo : sgEmprestimoListOld) {
                if (!sgEmprestimoListNew.contains(sgEmprestimoListOldSgEmprestimo)) {
                    sgEmprestimoListOldSgEmprestimo.setReservaRef(null);
                    sgEmprestimoListOldSgEmprestimo = em.merge(sgEmprestimoListOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimo : sgEmprestimoListNew) {
                if (!sgEmprestimoListOld.contains(sgEmprestimoListNewSgEmprestimo)) {
                    BReserva oldReservaRefOfSgEmprestimoListNewSgEmprestimo = sgEmprestimoListNewSgEmprestimo.getReservaRef();
                    sgEmprestimoListNewSgEmprestimo.setReservaRef(BReserva);
                    sgEmprestimoListNewSgEmprestimo = em.merge(sgEmprestimoListNewSgEmprestimo);
                    if (oldReservaRefOfSgEmprestimoListNewSgEmprestimo != null && !oldReservaRefOfSgEmprestimoListNewSgEmprestimo.equals(BReserva)) {
                        oldReservaRefOfSgEmprestimoListNewSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListNewSgEmprestimo);
                        oldReservaRefOfSgEmprestimoListNewSgEmprestimo = em.merge(oldReservaRefOfSgEmprestimoListNewSgEmprestimo);
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
                leitor.getBReservaList().remove(BReserva);
                leitor = em.merge(leitor);
            }
            SgExemplar livro = BReserva.getLivro();
            if (livro != null) {
                livro.getBReservaList().remove(BReserva);
                livro = em.merge(livro);
            }
            List<SgEmprestimo> sgEmprestimoList = BReserva.getSgEmprestimoList();
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : sgEmprestimoList) {
                sgEmprestimoListSgEmprestimo.setReservaRef(null);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
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
