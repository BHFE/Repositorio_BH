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
import entidades.SgEmprestimo;
import entidades.SgEmprestimoParametros;
import entidades.SgExemplar;
import entidades.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class SgEmprestimoJpaController implements Serializable {

    public SgEmprestimoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgEmprestimo sgEmprestimo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitor idLeitor = sgEmprestimo.getIdLeitor();
            if (idLeitor != null) {
                idLeitor = em.getReference(idLeitor.getClass(), idLeitor.getNrCartao());
                sgEmprestimo.setIdLeitor(idLeitor);
            }
            BReserva reservaRef = sgEmprestimo.getReservaRef();
            if (reservaRef != null) {
                reservaRef = em.getReference(reservaRef.getClass(), reservaRef.getIdagenda());
                sgEmprestimo.setReservaRef(reservaRef);
            }
            SgEmprestimoParametros parametrosRef = sgEmprestimo.getParametrosRef();
            if (parametrosRef != null) {
                parametrosRef = em.getReference(parametrosRef.getClass(), parametrosRef.getIdparametro());
                sgEmprestimo.setParametrosRef(parametrosRef);
            }
            SgExemplar exemplarRef = sgEmprestimo.getExemplarRef();
            if (exemplarRef != null) {
                exemplarRef = em.getReference(exemplarRef.getClass(), exemplarRef.getNrRegisto());
                sgEmprestimo.setExemplarRef(exemplarRef);
            }
            Users agenteBibliot = sgEmprestimo.getAgenteBibliot();
            if (agenteBibliot != null) {
                agenteBibliot = em.getReference(agenteBibliot.getClass(), agenteBibliot.getUtilizador());
                sgEmprestimo.setAgenteBibliot(agenteBibliot);
            }
            em.persist(sgEmprestimo);
            if (idLeitor != null) {
                idLeitor.getSgEmprestimoCollection().add(sgEmprestimo);
                idLeitor = em.merge(idLeitor);
            }
            if (reservaRef != null) {
                reservaRef.getSgEmprestimoCollection().add(sgEmprestimo);
                reservaRef = em.merge(reservaRef);
            }
            if (parametrosRef != null) {
                parametrosRef.getSgEmprestimoCollection().add(sgEmprestimo);
                parametrosRef = em.merge(parametrosRef);
            }
            if (exemplarRef != null) {
                exemplarRef.getSgEmprestimoCollection().add(sgEmprestimo);
                exemplarRef = em.merge(exemplarRef);
            }
            if (agenteBibliot != null) {
                agenteBibliot.getSgEmprestimoCollection().add(sgEmprestimo);
                agenteBibliot = em.merge(agenteBibliot);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgEmprestimo sgEmprestimo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgEmprestimo persistentSgEmprestimo = em.find(SgEmprestimo.class, sgEmprestimo.getIdemprestimo());
            BLeitor idLeitorOld = persistentSgEmprestimo.getIdLeitor();
            BLeitor idLeitorNew = sgEmprestimo.getIdLeitor();
            BReserva reservaRefOld = persistentSgEmprestimo.getReservaRef();
            BReserva reservaRefNew = sgEmprestimo.getReservaRef();
            SgEmprestimoParametros parametrosRefOld = persistentSgEmprestimo.getParametrosRef();
            SgEmprestimoParametros parametrosRefNew = sgEmprestimo.getParametrosRef();
            SgExemplar exemplarRefOld = persistentSgEmprestimo.getExemplarRef();
            SgExemplar exemplarRefNew = sgEmprestimo.getExemplarRef();
            Users agenteBibliotOld = persistentSgEmprestimo.getAgenteBibliot();
            Users agenteBibliotNew = sgEmprestimo.getAgenteBibliot();
            if (idLeitorNew != null) {
                idLeitorNew = em.getReference(idLeitorNew.getClass(), idLeitorNew.getNrCartao());
                sgEmprestimo.setIdLeitor(idLeitorNew);
            }
            if (reservaRefNew != null) {
                reservaRefNew = em.getReference(reservaRefNew.getClass(), reservaRefNew.getIdagenda());
                sgEmprestimo.setReservaRef(reservaRefNew);
            }
            if (parametrosRefNew != null) {
                parametrosRefNew = em.getReference(parametrosRefNew.getClass(), parametrosRefNew.getIdparametro());
                sgEmprestimo.setParametrosRef(parametrosRefNew);
            }
            if (exemplarRefNew != null) {
                exemplarRefNew = em.getReference(exemplarRefNew.getClass(), exemplarRefNew.getNrRegisto());
                sgEmprestimo.setExemplarRef(exemplarRefNew);
            }
            if (agenteBibliotNew != null) {
                agenteBibliotNew = em.getReference(agenteBibliotNew.getClass(), agenteBibliotNew.getUtilizador());
                sgEmprestimo.setAgenteBibliot(agenteBibliotNew);
            }
            sgEmprestimo = em.merge(sgEmprestimo);
            if (idLeitorOld != null && !idLeitorOld.equals(idLeitorNew)) {
                idLeitorOld.getSgEmprestimoCollection().remove(sgEmprestimo);
                idLeitorOld = em.merge(idLeitorOld);
            }
            if (idLeitorNew != null && !idLeitorNew.equals(idLeitorOld)) {
                idLeitorNew.getSgEmprestimoCollection().add(sgEmprestimo);
                idLeitorNew = em.merge(idLeitorNew);
            }
            if (reservaRefOld != null && !reservaRefOld.equals(reservaRefNew)) {
                reservaRefOld.getSgEmprestimoCollection().remove(sgEmprestimo);
                reservaRefOld = em.merge(reservaRefOld);
            }
            if (reservaRefNew != null && !reservaRefNew.equals(reservaRefOld)) {
                reservaRefNew.getSgEmprestimoCollection().add(sgEmprestimo);
                reservaRefNew = em.merge(reservaRefNew);
            }
            if (parametrosRefOld != null && !parametrosRefOld.equals(parametrosRefNew)) {
                parametrosRefOld.getSgEmprestimoCollection().remove(sgEmprestimo);
                parametrosRefOld = em.merge(parametrosRefOld);
            }
            if (parametrosRefNew != null && !parametrosRefNew.equals(parametrosRefOld)) {
                parametrosRefNew.getSgEmprestimoCollection().add(sgEmprestimo);
                parametrosRefNew = em.merge(parametrosRefNew);
            }
            if (exemplarRefOld != null && !exemplarRefOld.equals(exemplarRefNew)) {
                exemplarRefOld.getSgEmprestimoCollection().remove(sgEmprestimo);
                exemplarRefOld = em.merge(exemplarRefOld);
            }
            if (exemplarRefNew != null && !exemplarRefNew.equals(exemplarRefOld)) {
                exemplarRefNew.getSgEmprestimoCollection().add(sgEmprestimo);
                exemplarRefNew = em.merge(exemplarRefNew);
            }
            if (agenteBibliotOld != null && !agenteBibliotOld.equals(agenteBibliotNew)) {
                agenteBibliotOld.getSgEmprestimoCollection().remove(sgEmprestimo);
                agenteBibliotOld = em.merge(agenteBibliotOld);
            }
            if (agenteBibliotNew != null && !agenteBibliotNew.equals(agenteBibliotOld)) {
                agenteBibliotNew.getSgEmprestimoCollection().add(sgEmprestimo);
                agenteBibliotNew = em.merge(agenteBibliotNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sgEmprestimo.getIdemprestimo();
                if (findSgEmprestimo(id) == null) {
                    throw new NonexistentEntityException("The sgEmprestimo with id " + id + " no longer exists.");
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
            SgEmprestimo sgEmprestimo;
            try {
                sgEmprestimo = em.getReference(SgEmprestimo.class, id);
                sgEmprestimo.getIdemprestimo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgEmprestimo with id " + id + " no longer exists.", enfe);
            }
            BLeitor idLeitor = sgEmprestimo.getIdLeitor();
            if (idLeitor != null) {
                idLeitor.getSgEmprestimoCollection().remove(sgEmprestimo);
                idLeitor = em.merge(idLeitor);
            }
            BReserva reservaRef = sgEmprestimo.getReservaRef();
            if (reservaRef != null) {
                reservaRef.getSgEmprestimoCollection().remove(sgEmprestimo);
                reservaRef = em.merge(reservaRef);
            }
            SgEmprestimoParametros parametrosRef = sgEmprestimo.getParametrosRef();
            if (parametrosRef != null) {
                parametrosRef.getSgEmprestimoCollection().remove(sgEmprestimo);
                parametrosRef = em.merge(parametrosRef);
            }
            SgExemplar exemplarRef = sgEmprestimo.getExemplarRef();
            if (exemplarRef != null) {
                exemplarRef.getSgEmprestimoCollection().remove(sgEmprestimo);
                exemplarRef = em.merge(exemplarRef);
            }
            Users agenteBibliot = sgEmprestimo.getAgenteBibliot();
            if (agenteBibliot != null) {
                agenteBibliot.getSgEmprestimoCollection().remove(sgEmprestimo);
                agenteBibliot = em.merge(agenteBibliot);
            }
            em.remove(sgEmprestimo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgEmprestimo> findSgEmprestimoEntities() {
        return findSgEmprestimoEntities(true, -1, -1);
    }

    public List<SgEmprestimo> findSgEmprestimoEntities(int maxResults, int firstResult) {
        return findSgEmprestimoEntities(false, maxResults, firstResult);
    }

    private List<SgEmprestimo> findSgEmprestimoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgEmprestimo.class));
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

    public SgEmprestimo findSgEmprestimo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgEmprestimo.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgEmprestimoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgEmprestimo> rt = cq.from(SgEmprestimo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
