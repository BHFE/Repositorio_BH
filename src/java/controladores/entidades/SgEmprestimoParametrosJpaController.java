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
import entidades.Users;
import entidades.SgEmprestimo;
import java.util.ArrayList;
import java.util.Collection;
import entidades.BLeitor;
import entidades.SgEmprestimoParametros;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class SgEmprestimoParametrosJpaController implements Serializable {

    public SgEmprestimoParametrosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgEmprestimoParametros sgEmprestimoParametros) {
        if (sgEmprestimoParametros.getSgEmprestimoCollection() == null) {
            sgEmprestimoParametros.setSgEmprestimoCollection(new ArrayList<SgEmprestimo>());
        }
        if (sgEmprestimoParametros.getBLeitorCollection() == null) {
            sgEmprestimoParametros.setBLeitorCollection(new ArrayList<BLeitor>());
        }
        if (sgEmprestimoParametros.getBLeitorCollection1() == null) {
            sgEmprestimoParametros.setBLeitorCollection1(new ArrayList<BLeitor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users agenteBibliotecario = sgEmprestimoParametros.getAgenteBibliotecario();
            if (agenteBibliotecario != null) {
                agenteBibliotecario = em.getReference(agenteBibliotecario.getClass(), agenteBibliotecario.getUtilizador());
                sgEmprestimoParametros.setAgenteBibliotecario(agenteBibliotecario);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollection = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimoToAttach : sgEmprestimoParametros.getSgEmprestimoCollection()) {
                sgEmprestimoCollectionSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollection.add(sgEmprestimoCollectionSgEmprestimoToAttach);
            }
            sgEmprestimoParametros.setSgEmprestimoCollection(attachedSgEmprestimoCollection);
            Collection<BLeitor> attachedBLeitorCollection = new ArrayList<BLeitor>();
            for (BLeitor BLeitorCollectionBLeitorToAttach : sgEmprestimoParametros.getBLeitorCollection()) {
                BLeitorCollectionBLeitorToAttach = em.getReference(BLeitorCollectionBLeitorToAttach.getClass(), BLeitorCollectionBLeitorToAttach.getNrCartao());
                attachedBLeitorCollection.add(BLeitorCollectionBLeitorToAttach);
            }
            sgEmprestimoParametros.setBLeitorCollection(attachedBLeitorCollection);
            Collection<BLeitor> attachedBLeitorCollection1 = new ArrayList<BLeitor>();
            for (BLeitor BLeitorCollection1BLeitorToAttach : sgEmprestimoParametros.getBLeitorCollection1()) {
                BLeitorCollection1BLeitorToAttach = em.getReference(BLeitorCollection1BLeitorToAttach.getClass(), BLeitorCollection1BLeitorToAttach.getNrCartao());
                attachedBLeitorCollection1.add(BLeitorCollection1BLeitorToAttach);
            }
            sgEmprestimoParametros.setBLeitorCollection1(attachedBLeitorCollection1);
            em.persist(sgEmprestimoParametros);
            if (agenteBibliotecario != null) {
                agenteBibliotecario.getSgEmprestimoParametrosCollection().add(sgEmprestimoParametros);
                agenteBibliotecario = em.merge(agenteBibliotecario);
            }
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : sgEmprestimoParametros.getSgEmprestimoCollection()) {
                SgEmprestimoParametros oldParametrosRefOfSgEmprestimoCollectionSgEmprestimo = sgEmprestimoCollectionSgEmprestimo.getParametrosRef();
                sgEmprestimoCollectionSgEmprestimo.setParametrosRef(sgEmprestimoParametros);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
                if (oldParametrosRefOfSgEmprestimoCollectionSgEmprestimo != null) {
                    oldParametrosRefOfSgEmprestimoCollectionSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionSgEmprestimo);
                    oldParametrosRefOfSgEmprestimoCollectionSgEmprestimo = em.merge(oldParametrosRefOfSgEmprestimoCollectionSgEmprestimo);
                }
            }
            for (BLeitor BLeitorCollectionBLeitor : sgEmprestimoParametros.getBLeitorCollection()) {
                SgEmprestimoParametros oldIdParametroActualizacaoOfBLeitorCollectionBLeitor = BLeitorCollectionBLeitor.getIdParametroActualizacao();
                BLeitorCollectionBLeitor.setIdParametroActualizacao(sgEmprestimoParametros);
                BLeitorCollectionBLeitor = em.merge(BLeitorCollectionBLeitor);
                if (oldIdParametroActualizacaoOfBLeitorCollectionBLeitor != null) {
                    oldIdParametroActualizacaoOfBLeitorCollectionBLeitor.getBLeitorCollection().remove(BLeitorCollectionBLeitor);
                    oldIdParametroActualizacaoOfBLeitorCollectionBLeitor = em.merge(oldIdParametroActualizacaoOfBLeitorCollectionBLeitor);
                }
            }
            for (BLeitor BLeitorCollection1BLeitor : sgEmprestimoParametros.getBLeitorCollection1()) {
                SgEmprestimoParametros oldIdParametroRegistoOfBLeitorCollection1BLeitor = BLeitorCollection1BLeitor.getIdParametroRegisto();
                BLeitorCollection1BLeitor.setIdParametroRegisto(sgEmprestimoParametros);
                BLeitorCollection1BLeitor = em.merge(BLeitorCollection1BLeitor);
                if (oldIdParametroRegistoOfBLeitorCollection1BLeitor != null) {
                    oldIdParametroRegistoOfBLeitorCollection1BLeitor.getBLeitorCollection1().remove(BLeitorCollection1BLeitor);
                    oldIdParametroRegistoOfBLeitorCollection1BLeitor = em.merge(oldIdParametroRegistoOfBLeitorCollection1BLeitor);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgEmprestimoParametros sgEmprestimoParametros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgEmprestimoParametros persistentSgEmprestimoParametros = em.find(SgEmprestimoParametros.class, sgEmprestimoParametros.getIdparametro());
            Users agenteBibliotecarioOld = persistentSgEmprestimoParametros.getAgenteBibliotecario();
            Users agenteBibliotecarioNew = sgEmprestimoParametros.getAgenteBibliotecario();
            Collection<SgEmprestimo> sgEmprestimoCollectionOld = persistentSgEmprestimoParametros.getSgEmprestimoCollection();
            Collection<SgEmprestimo> sgEmprestimoCollectionNew = sgEmprestimoParametros.getSgEmprestimoCollection();
            Collection<BLeitor> BLeitorCollectionOld = persistentSgEmprestimoParametros.getBLeitorCollection();
            Collection<BLeitor> BLeitorCollectionNew = sgEmprestimoParametros.getBLeitorCollection();
            Collection<BLeitor> BLeitorCollection1Old = persistentSgEmprestimoParametros.getBLeitorCollection1();
            Collection<BLeitor> BLeitorCollection1New = sgEmprestimoParametros.getBLeitorCollection1();
            if (agenteBibliotecarioNew != null) {
                agenteBibliotecarioNew = em.getReference(agenteBibliotecarioNew.getClass(), agenteBibliotecarioNew.getUtilizador());
                sgEmprestimoParametros.setAgenteBibliotecario(agenteBibliotecarioNew);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollectionNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimoToAttach : sgEmprestimoCollectionNew) {
                sgEmprestimoCollectionNewSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionNewSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollectionNew.add(sgEmprestimoCollectionNewSgEmprestimoToAttach);
            }
            sgEmprestimoCollectionNew = attachedSgEmprestimoCollectionNew;
            sgEmprestimoParametros.setSgEmprestimoCollection(sgEmprestimoCollectionNew);
            Collection<BLeitor> attachedBLeitorCollectionNew = new ArrayList<BLeitor>();
            for (BLeitor BLeitorCollectionNewBLeitorToAttach : BLeitorCollectionNew) {
                BLeitorCollectionNewBLeitorToAttach = em.getReference(BLeitorCollectionNewBLeitorToAttach.getClass(), BLeitorCollectionNewBLeitorToAttach.getNrCartao());
                attachedBLeitorCollectionNew.add(BLeitorCollectionNewBLeitorToAttach);
            }
            BLeitorCollectionNew = attachedBLeitorCollectionNew;
            sgEmprestimoParametros.setBLeitorCollection(BLeitorCollectionNew);
            Collection<BLeitor> attachedBLeitorCollection1New = new ArrayList<BLeitor>();
            for (BLeitor BLeitorCollection1NewBLeitorToAttach : BLeitorCollection1New) {
                BLeitorCollection1NewBLeitorToAttach = em.getReference(BLeitorCollection1NewBLeitorToAttach.getClass(), BLeitorCollection1NewBLeitorToAttach.getNrCartao());
                attachedBLeitorCollection1New.add(BLeitorCollection1NewBLeitorToAttach);
            }
            BLeitorCollection1New = attachedBLeitorCollection1New;
            sgEmprestimoParametros.setBLeitorCollection1(BLeitorCollection1New);
            sgEmprestimoParametros = em.merge(sgEmprestimoParametros);
            if (agenteBibliotecarioOld != null && !agenteBibliotecarioOld.equals(agenteBibliotecarioNew)) {
                agenteBibliotecarioOld.getSgEmprestimoParametrosCollection().remove(sgEmprestimoParametros);
                agenteBibliotecarioOld = em.merge(agenteBibliotecarioOld);
            }
            if (agenteBibliotecarioNew != null && !agenteBibliotecarioNew.equals(agenteBibliotecarioOld)) {
                agenteBibliotecarioNew.getSgEmprestimoParametrosCollection().add(sgEmprestimoParametros);
                agenteBibliotecarioNew = em.merge(agenteBibliotecarioNew);
            }
            for (SgEmprestimo sgEmprestimoCollectionOldSgEmprestimo : sgEmprestimoCollectionOld) {
                if (!sgEmprestimoCollectionNew.contains(sgEmprestimoCollectionOldSgEmprestimo)) {
                    sgEmprestimoCollectionOldSgEmprestimo.setParametrosRef(null);
                    sgEmprestimoCollectionOldSgEmprestimo = em.merge(sgEmprestimoCollectionOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimo : sgEmprestimoCollectionNew) {
                if (!sgEmprestimoCollectionOld.contains(sgEmprestimoCollectionNewSgEmprestimo)) {
                    SgEmprestimoParametros oldParametrosRefOfSgEmprestimoCollectionNewSgEmprestimo = sgEmprestimoCollectionNewSgEmprestimo.getParametrosRef();
                    sgEmprestimoCollectionNewSgEmprestimo.setParametrosRef(sgEmprestimoParametros);
                    sgEmprestimoCollectionNewSgEmprestimo = em.merge(sgEmprestimoCollectionNewSgEmprestimo);
                    if (oldParametrosRefOfSgEmprestimoCollectionNewSgEmprestimo != null && !oldParametrosRefOfSgEmprestimoCollectionNewSgEmprestimo.equals(sgEmprestimoParametros)) {
                        oldParametrosRefOfSgEmprestimoCollectionNewSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionNewSgEmprestimo);
                        oldParametrosRefOfSgEmprestimoCollectionNewSgEmprestimo = em.merge(oldParametrosRefOfSgEmprestimoCollectionNewSgEmprestimo);
                    }
                }
            }
            for (BLeitor BLeitorCollectionOldBLeitor : BLeitorCollectionOld) {
                if (!BLeitorCollectionNew.contains(BLeitorCollectionOldBLeitor)) {
                    BLeitorCollectionOldBLeitor.setIdParametroActualizacao(null);
                    BLeitorCollectionOldBLeitor = em.merge(BLeitorCollectionOldBLeitor);
                }
            }
            for (BLeitor BLeitorCollectionNewBLeitor : BLeitorCollectionNew) {
                if (!BLeitorCollectionOld.contains(BLeitorCollectionNewBLeitor)) {
                    SgEmprestimoParametros oldIdParametroActualizacaoOfBLeitorCollectionNewBLeitor = BLeitorCollectionNewBLeitor.getIdParametroActualizacao();
                    BLeitorCollectionNewBLeitor.setIdParametroActualizacao(sgEmprestimoParametros);
                    BLeitorCollectionNewBLeitor = em.merge(BLeitorCollectionNewBLeitor);
                    if (oldIdParametroActualizacaoOfBLeitorCollectionNewBLeitor != null && !oldIdParametroActualizacaoOfBLeitorCollectionNewBLeitor.equals(sgEmprestimoParametros)) {
                        oldIdParametroActualizacaoOfBLeitorCollectionNewBLeitor.getBLeitorCollection().remove(BLeitorCollectionNewBLeitor);
                        oldIdParametroActualizacaoOfBLeitorCollectionNewBLeitor = em.merge(oldIdParametroActualizacaoOfBLeitorCollectionNewBLeitor);
                    }
                }
            }
            for (BLeitor BLeitorCollection1OldBLeitor : BLeitorCollection1Old) {
                if (!BLeitorCollection1New.contains(BLeitorCollection1OldBLeitor)) {
                    BLeitorCollection1OldBLeitor.setIdParametroRegisto(null);
                    BLeitorCollection1OldBLeitor = em.merge(BLeitorCollection1OldBLeitor);
                }
            }
            for (BLeitor BLeitorCollection1NewBLeitor : BLeitorCollection1New) {
                if (!BLeitorCollection1Old.contains(BLeitorCollection1NewBLeitor)) {
                    SgEmprestimoParametros oldIdParametroRegistoOfBLeitorCollection1NewBLeitor = BLeitorCollection1NewBLeitor.getIdParametroRegisto();
                    BLeitorCollection1NewBLeitor.setIdParametroRegisto(sgEmprestimoParametros);
                    BLeitorCollection1NewBLeitor = em.merge(BLeitorCollection1NewBLeitor);
                    if (oldIdParametroRegistoOfBLeitorCollection1NewBLeitor != null && !oldIdParametroRegistoOfBLeitorCollection1NewBLeitor.equals(sgEmprestimoParametros)) {
                        oldIdParametroRegistoOfBLeitorCollection1NewBLeitor.getBLeitorCollection1().remove(BLeitorCollection1NewBLeitor);
                        oldIdParametroRegistoOfBLeitorCollection1NewBLeitor = em.merge(oldIdParametroRegistoOfBLeitorCollection1NewBLeitor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sgEmprestimoParametros.getIdparametro();
                if (findSgEmprestimoParametros(id) == null) {
                    throw new NonexistentEntityException("The sgEmprestimoParametros with id " + id + " no longer exists.");
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
            SgEmprestimoParametros sgEmprestimoParametros;
            try {
                sgEmprestimoParametros = em.getReference(SgEmprestimoParametros.class, id);
                sgEmprestimoParametros.getIdparametro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgEmprestimoParametros with id " + id + " no longer exists.", enfe);
            }
            Users agenteBibliotecario = sgEmprestimoParametros.getAgenteBibliotecario();
            if (agenteBibliotecario != null) {
                agenteBibliotecario.getSgEmprestimoParametrosCollection().remove(sgEmprestimoParametros);
                agenteBibliotecario = em.merge(agenteBibliotecario);
            }
            Collection<SgEmprestimo> sgEmprestimoCollection = sgEmprestimoParametros.getSgEmprestimoCollection();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : sgEmprestimoCollection) {
                sgEmprestimoCollectionSgEmprestimo.setParametrosRef(null);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
            }
            Collection<BLeitor> BLeitorCollection = sgEmprestimoParametros.getBLeitorCollection();
            for (BLeitor BLeitorCollectionBLeitor : BLeitorCollection) {
                BLeitorCollectionBLeitor.setIdParametroActualizacao(null);
                BLeitorCollectionBLeitor = em.merge(BLeitorCollectionBLeitor);
            }
            Collection<BLeitor> BLeitorCollection1 = sgEmprestimoParametros.getBLeitorCollection1();
            for (BLeitor BLeitorCollection1BLeitor : BLeitorCollection1) {
                BLeitorCollection1BLeitor.setIdParametroRegisto(null);
                BLeitorCollection1BLeitor = em.merge(BLeitorCollection1BLeitor);
            }
            em.remove(sgEmprestimoParametros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgEmprestimoParametros> findSgEmprestimoParametrosEntities() {
        return findSgEmprestimoParametrosEntities(true, -1, -1);
    }

    public List<SgEmprestimoParametros> findSgEmprestimoParametrosEntities(int maxResults, int firstResult) {
        return findSgEmprestimoParametrosEntities(false, maxResults, firstResult);
    }

    private List<SgEmprestimoParametros> findSgEmprestimoParametrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgEmprestimoParametros.class));
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

    public SgEmprestimoParametros findSgEmprestimoParametros(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgEmprestimoParametros.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgEmprestimoParametrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgEmprestimoParametros> rt = cq.from(SgEmprestimoParametros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
