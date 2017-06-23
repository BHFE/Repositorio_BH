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
import java.util.List;
import entidades.BLeitor;
import entidades.SgEmprestimoParametros;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
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
        if (sgEmprestimoParametros.getSgEmprestimoList() == null) {
            sgEmprestimoParametros.setSgEmprestimoList(new ArrayList<SgEmprestimo>());
        }
        if (sgEmprestimoParametros.getBLeitorList() == null) {
            sgEmprestimoParametros.setBLeitorList(new ArrayList<BLeitor>());
        }
        if (sgEmprestimoParametros.getBLeitorList1() == null) {
            sgEmprestimoParametros.setBLeitorList1(new ArrayList<BLeitor>());
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
            List<SgEmprestimo> attachedSgEmprestimoList = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListSgEmprestimoToAttach : sgEmprestimoParametros.getSgEmprestimoList()) {
                sgEmprestimoListSgEmprestimoToAttach = em.getReference(sgEmprestimoListSgEmprestimoToAttach.getClass(), sgEmprestimoListSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoList.add(sgEmprestimoListSgEmprestimoToAttach);
            }
            sgEmprestimoParametros.setSgEmprestimoList(attachedSgEmprestimoList);
            List<BLeitor> attachedBLeitorList = new ArrayList<BLeitor>();
            for (BLeitor BLeitorListBLeitorToAttach : sgEmprestimoParametros.getBLeitorList()) {
                BLeitorListBLeitorToAttach = em.getReference(BLeitorListBLeitorToAttach.getClass(), BLeitorListBLeitorToAttach.getNrCartao());
                attachedBLeitorList.add(BLeitorListBLeitorToAttach);
            }
            sgEmprestimoParametros.setBLeitorList(attachedBLeitorList);
            List<BLeitor> attachedBLeitorList1 = new ArrayList<BLeitor>();
            for (BLeitor BLeitorList1BLeitorToAttach : sgEmprestimoParametros.getBLeitorList1()) {
                BLeitorList1BLeitorToAttach = em.getReference(BLeitorList1BLeitorToAttach.getClass(), BLeitorList1BLeitorToAttach.getNrCartao());
                attachedBLeitorList1.add(BLeitorList1BLeitorToAttach);
            }
            sgEmprestimoParametros.setBLeitorList1(attachedBLeitorList1);
            em.persist(sgEmprestimoParametros);
            if (agenteBibliotecario != null) {
                agenteBibliotecario.getSgEmprestimoParametrosList().add(sgEmprestimoParametros);
                agenteBibliotecario = em.merge(agenteBibliotecario);
            }
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : sgEmprestimoParametros.getSgEmprestimoList()) {
                SgEmprestimoParametros oldParametrosRefOfSgEmprestimoListSgEmprestimo = sgEmprestimoListSgEmprestimo.getParametrosRef();
                sgEmprestimoListSgEmprestimo.setParametrosRef(sgEmprestimoParametros);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
                if (oldParametrosRefOfSgEmprestimoListSgEmprestimo != null) {
                    oldParametrosRefOfSgEmprestimoListSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListSgEmprestimo);
                    oldParametrosRefOfSgEmprestimoListSgEmprestimo = em.merge(oldParametrosRefOfSgEmprestimoListSgEmprestimo);
                }
            }
            for (BLeitor BLeitorListBLeitor : sgEmprestimoParametros.getBLeitorList()) {
                SgEmprestimoParametros oldIdParametroRegistoOfBLeitorListBLeitor = BLeitorListBLeitor.getIdParametroRegisto();
                BLeitorListBLeitor.setIdParametroRegisto(sgEmprestimoParametros);
                BLeitorListBLeitor = em.merge(BLeitorListBLeitor);
                if (oldIdParametroRegistoOfBLeitorListBLeitor != null) {
                    oldIdParametroRegistoOfBLeitorListBLeitor.getBLeitorList().remove(BLeitorListBLeitor);
                    oldIdParametroRegistoOfBLeitorListBLeitor = em.merge(oldIdParametroRegistoOfBLeitorListBLeitor);
                }
            }
            for (BLeitor BLeitorList1BLeitor : sgEmprestimoParametros.getBLeitorList1()) {
                SgEmprestimoParametros oldIdParametroActualizacaoOfBLeitorList1BLeitor = BLeitorList1BLeitor.getIdParametroActualizacao();
                BLeitorList1BLeitor.setIdParametroActualizacao(sgEmprestimoParametros);
                BLeitorList1BLeitor = em.merge(BLeitorList1BLeitor);
                if (oldIdParametroActualizacaoOfBLeitorList1BLeitor != null) {
                    oldIdParametroActualizacaoOfBLeitorList1BLeitor.getBLeitorList1().remove(BLeitorList1BLeitor);
                    oldIdParametroActualizacaoOfBLeitorList1BLeitor = em.merge(oldIdParametroActualizacaoOfBLeitorList1BLeitor);
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
            List<SgEmprestimo> sgEmprestimoListOld = persistentSgEmprestimoParametros.getSgEmprestimoList();
            List<SgEmprestimo> sgEmprestimoListNew = sgEmprestimoParametros.getSgEmprestimoList();
            List<BLeitor> BLeitorListOld = persistentSgEmprestimoParametros.getBLeitorList();
            List<BLeitor> BLeitorListNew = sgEmprestimoParametros.getBLeitorList();
            List<BLeitor> BLeitorList1Old = persistentSgEmprestimoParametros.getBLeitorList1();
            List<BLeitor> BLeitorList1New = sgEmprestimoParametros.getBLeitorList1();
            if (agenteBibliotecarioNew != null) {
                agenteBibliotecarioNew = em.getReference(agenteBibliotecarioNew.getClass(), agenteBibliotecarioNew.getUtilizador());
                sgEmprestimoParametros.setAgenteBibliotecario(agenteBibliotecarioNew);
            }
            List<SgEmprestimo> attachedSgEmprestimoListNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimoToAttach : sgEmprestimoListNew) {
                sgEmprestimoListNewSgEmprestimoToAttach = em.getReference(sgEmprestimoListNewSgEmprestimoToAttach.getClass(), sgEmprestimoListNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoListNew.add(sgEmprestimoListNewSgEmprestimoToAttach);
            }
            sgEmprestimoListNew = attachedSgEmprestimoListNew;
            sgEmprestimoParametros.setSgEmprestimoList(sgEmprestimoListNew);
            List<BLeitor> attachedBLeitorListNew = new ArrayList<BLeitor>();
            for (BLeitor BLeitorListNewBLeitorToAttach : BLeitorListNew) {
                BLeitorListNewBLeitorToAttach = em.getReference(BLeitorListNewBLeitorToAttach.getClass(), BLeitorListNewBLeitorToAttach.getNrCartao());
                attachedBLeitorListNew.add(BLeitorListNewBLeitorToAttach);
            }
            BLeitorListNew = attachedBLeitorListNew;
            sgEmprestimoParametros.setBLeitorList(BLeitorListNew);
            List<BLeitor> attachedBLeitorList1New = new ArrayList<BLeitor>();
            for (BLeitor BLeitorList1NewBLeitorToAttach : BLeitorList1New) {
                BLeitorList1NewBLeitorToAttach = em.getReference(BLeitorList1NewBLeitorToAttach.getClass(), BLeitorList1NewBLeitorToAttach.getNrCartao());
                attachedBLeitorList1New.add(BLeitorList1NewBLeitorToAttach);
            }
            BLeitorList1New = attachedBLeitorList1New;
            sgEmprestimoParametros.setBLeitorList1(BLeitorList1New);
            sgEmprestimoParametros = em.merge(sgEmprestimoParametros);
            if (agenteBibliotecarioOld != null && !agenteBibliotecarioOld.equals(agenteBibliotecarioNew)) {
                agenteBibliotecarioOld.getSgEmprestimoParametrosList().remove(sgEmprestimoParametros);
                agenteBibliotecarioOld = em.merge(agenteBibliotecarioOld);
            }
            if (agenteBibliotecarioNew != null && !agenteBibliotecarioNew.equals(agenteBibliotecarioOld)) {
                agenteBibliotecarioNew.getSgEmprestimoParametrosList().add(sgEmprestimoParametros);
                agenteBibliotecarioNew = em.merge(agenteBibliotecarioNew);
            }
            for (SgEmprestimo sgEmprestimoListOldSgEmprestimo : sgEmprestimoListOld) {
                if (!sgEmprestimoListNew.contains(sgEmprestimoListOldSgEmprestimo)) {
                    sgEmprestimoListOldSgEmprestimo.setParametrosRef(null);
                    sgEmprestimoListOldSgEmprestimo = em.merge(sgEmprestimoListOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimo : sgEmprestimoListNew) {
                if (!sgEmprestimoListOld.contains(sgEmprestimoListNewSgEmprestimo)) {
                    SgEmprestimoParametros oldParametrosRefOfSgEmprestimoListNewSgEmprestimo = sgEmprestimoListNewSgEmprestimo.getParametrosRef();
                    sgEmprestimoListNewSgEmprestimo.setParametrosRef(sgEmprestimoParametros);
                    sgEmprestimoListNewSgEmprestimo = em.merge(sgEmprestimoListNewSgEmprestimo);
                    if (oldParametrosRefOfSgEmprestimoListNewSgEmprestimo != null && !oldParametrosRefOfSgEmprestimoListNewSgEmprestimo.equals(sgEmprestimoParametros)) {
                        oldParametrosRefOfSgEmprestimoListNewSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListNewSgEmprestimo);
                        oldParametrosRefOfSgEmprestimoListNewSgEmprestimo = em.merge(oldParametrosRefOfSgEmprestimoListNewSgEmprestimo);
                    }
                }
            }
            for (BLeitor BLeitorListOldBLeitor : BLeitorListOld) {
                if (!BLeitorListNew.contains(BLeitorListOldBLeitor)) {
                    BLeitorListOldBLeitor.setIdParametroRegisto(null);
                    BLeitorListOldBLeitor = em.merge(BLeitorListOldBLeitor);
                }
            }
            for (BLeitor BLeitorListNewBLeitor : BLeitorListNew) {
                if (!BLeitorListOld.contains(BLeitorListNewBLeitor)) {
                    SgEmprestimoParametros oldIdParametroRegistoOfBLeitorListNewBLeitor = BLeitorListNewBLeitor.getIdParametroRegisto();
                    BLeitorListNewBLeitor.setIdParametroRegisto(sgEmprestimoParametros);
                    BLeitorListNewBLeitor = em.merge(BLeitorListNewBLeitor);
                    if (oldIdParametroRegistoOfBLeitorListNewBLeitor != null && !oldIdParametroRegistoOfBLeitorListNewBLeitor.equals(sgEmprestimoParametros)) {
                        oldIdParametroRegistoOfBLeitorListNewBLeitor.getBLeitorList().remove(BLeitorListNewBLeitor);
                        oldIdParametroRegistoOfBLeitorListNewBLeitor = em.merge(oldIdParametroRegistoOfBLeitorListNewBLeitor);
                    }
                }
            }
            for (BLeitor BLeitorList1OldBLeitor : BLeitorList1Old) {
                if (!BLeitorList1New.contains(BLeitorList1OldBLeitor)) {
                    BLeitorList1OldBLeitor.setIdParametroActualizacao(null);
                    BLeitorList1OldBLeitor = em.merge(BLeitorList1OldBLeitor);
                }
            }
            for (BLeitor BLeitorList1NewBLeitor : BLeitorList1New) {
                if (!BLeitorList1Old.contains(BLeitorList1NewBLeitor)) {
                    SgEmprestimoParametros oldIdParametroActualizacaoOfBLeitorList1NewBLeitor = BLeitorList1NewBLeitor.getIdParametroActualizacao();
                    BLeitorList1NewBLeitor.setIdParametroActualizacao(sgEmprestimoParametros);
                    BLeitorList1NewBLeitor = em.merge(BLeitorList1NewBLeitor);
                    if (oldIdParametroActualizacaoOfBLeitorList1NewBLeitor != null && !oldIdParametroActualizacaoOfBLeitorList1NewBLeitor.equals(sgEmprestimoParametros)) {
                        oldIdParametroActualizacaoOfBLeitorList1NewBLeitor.getBLeitorList1().remove(BLeitorList1NewBLeitor);
                        oldIdParametroActualizacaoOfBLeitorList1NewBLeitor = em.merge(oldIdParametroActualizacaoOfBLeitorList1NewBLeitor);
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
                agenteBibliotecario.getSgEmprestimoParametrosList().remove(sgEmprestimoParametros);
                agenteBibliotecario = em.merge(agenteBibliotecario);
            }
            List<SgEmprestimo> sgEmprestimoList = sgEmprestimoParametros.getSgEmprestimoList();
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : sgEmprestimoList) {
                sgEmprestimoListSgEmprestimo.setParametrosRef(null);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
            }
            List<BLeitor> BLeitorList = sgEmprestimoParametros.getBLeitorList();
            for (BLeitor BLeitorListBLeitor : BLeitorList) {
                BLeitorListBLeitor.setIdParametroRegisto(null);
                BLeitorListBLeitor = em.merge(BLeitorListBLeitor);
            }
            List<BLeitor> BLeitorList1 = sgEmprestimoParametros.getBLeitorList1();
            for (BLeitor BLeitorList1BLeitor : BLeitorList1) {
                BLeitorList1BLeitor.setIdParametroActualizacao(null);
                BLeitorList1BLeitor = em.merge(BLeitorList1BLeitor);
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
