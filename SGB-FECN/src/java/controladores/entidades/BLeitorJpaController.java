/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.BLeitor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.BvAvaliador;
import entidades.SgEmprestimoParametros;
import entidades.Users;
import entidades.SgEmprestimo;
import java.util.ArrayList;
import java.util.List;
import entidades.BNotificacao;
import entidades.BvLeitura;
import entidades.BvArtigo;
import entidades.BReserva;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
 */
public class BLeitorJpaController implements Serializable {

    public BLeitorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BLeitor BLeitor) {
        if (BLeitor.getSgEmprestimoList() == null) {
            BLeitor.setSgEmprestimoList(new ArrayList<SgEmprestimo>());
        }
        if (BLeitor.getBNotificacaoList() == null) {
            BLeitor.setBNotificacaoList(new ArrayList<BNotificacao>());
        }
        if (BLeitor.getBvLeituraList() == null) {
            BLeitor.setBvLeituraList(new ArrayList<BvLeitura>());
        }
        if (BLeitor.getBvArtigoList() == null) {
            BLeitor.setBvArtigoList(new ArrayList<BvArtigo>());
        }
        if (BLeitor.getBReservaList() == null) {
            BLeitor.setBReservaList(new ArrayList<BReserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BvAvaliador bvAvaliador = BLeitor.getBvAvaliador();
            if (bvAvaliador != null) {
                bvAvaliador = em.getReference(bvAvaliador.getClass(), bvAvaliador.getIdLeitor());
                BLeitor.setBvAvaliador(bvAvaliador);
            }
            SgEmprestimoParametros idParametroRegisto = BLeitor.getIdParametroRegisto();
            if (idParametroRegisto != null) {
                idParametroRegisto = em.getReference(idParametroRegisto.getClass(), idParametroRegisto.getIdparametro());
                BLeitor.setIdParametroRegisto(idParametroRegisto);
            }
            SgEmprestimoParametros idParametroActualizacao = BLeitor.getIdParametroActualizacao();
            if (idParametroActualizacao != null) {
                idParametroActualizacao = em.getReference(idParametroActualizacao.getClass(), idParametroActualizacao.getIdparametro());
                BLeitor.setIdParametroActualizacao(idParametroActualizacao);
            }
            Users idutilizador = BLeitor.getIdutilizador();
            if (idutilizador != null) {
                idutilizador = em.getReference(idutilizador.getClass(), idutilizador.getUtilizador());
                BLeitor.setIdutilizador(idutilizador);
            }
            Users idagente = BLeitor.getIdagente();
            if (idagente != null) {
                idagente = em.getReference(idagente.getClass(), idagente.getUtilizador());
                BLeitor.setIdagente(idagente);
            }
            List<SgEmprestimo> attachedSgEmprestimoList = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListSgEmprestimoToAttach : BLeitor.getSgEmprestimoList()) {
                sgEmprestimoListSgEmprestimoToAttach = em.getReference(sgEmprestimoListSgEmprestimoToAttach.getClass(), sgEmprestimoListSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoList.add(sgEmprestimoListSgEmprestimoToAttach);
            }
            BLeitor.setSgEmprestimoList(attachedSgEmprestimoList);
            List<BNotificacao> attachedBNotificacaoList = new ArrayList<BNotificacao>();
            for (BNotificacao BNotificacaoListBNotificacaoToAttach : BLeitor.getBNotificacaoList()) {
                BNotificacaoListBNotificacaoToAttach = em.getReference(BNotificacaoListBNotificacaoToAttach.getClass(), BNotificacaoListBNotificacaoToAttach.getIdNotificacao());
                attachedBNotificacaoList.add(BNotificacaoListBNotificacaoToAttach);
            }
            BLeitor.setBNotificacaoList(attachedBNotificacaoList);
            List<BvLeitura> attachedBvLeituraList = new ArrayList<BvLeitura>();
            for (BvLeitura bvLeituraListBvLeituraToAttach : BLeitor.getBvLeituraList()) {
                bvLeituraListBvLeituraToAttach = em.getReference(bvLeituraListBvLeituraToAttach.getClass(), bvLeituraListBvLeituraToAttach.getBvLeituraPK());
                attachedBvLeituraList.add(bvLeituraListBvLeituraToAttach);
            }
            BLeitor.setBvLeituraList(attachedBvLeituraList);
            List<BvArtigo> attachedBvArtigoList = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListBvArtigoToAttach : BLeitor.getBvArtigoList()) {
                bvArtigoListBvArtigoToAttach = em.getReference(bvArtigoListBvArtigoToAttach.getClass(), bvArtigoListBvArtigoToAttach.getIdartigo());
                attachedBvArtigoList.add(bvArtigoListBvArtigoToAttach);
            }
            BLeitor.setBvArtigoList(attachedBvArtigoList);
            List<BReserva> attachedBReservaList = new ArrayList<BReserva>();
            for (BReserva BReservaListBReservaToAttach : BLeitor.getBReservaList()) {
                BReservaListBReservaToAttach = em.getReference(BReservaListBReservaToAttach.getClass(), BReservaListBReservaToAttach.getIdagenda());
                attachedBReservaList.add(BReservaListBReservaToAttach);
            }
            BLeitor.setBReservaList(attachedBReservaList);
            em.persist(BLeitor);
            if (bvAvaliador != null) {
                BLeitor oldBLeitorOfBvAvaliador = bvAvaliador.getBLeitor();
                if (oldBLeitorOfBvAvaliador != null) {
                    oldBLeitorOfBvAvaliador.setBvAvaliador(null);
                    oldBLeitorOfBvAvaliador = em.merge(oldBLeitorOfBvAvaliador);
                }
                bvAvaliador.setBLeitor(BLeitor);
                bvAvaliador = em.merge(bvAvaliador);
            }
            if (idParametroRegisto != null) {
                idParametroRegisto.getBLeitorList().add(BLeitor);
                idParametroRegisto = em.merge(idParametroRegisto);
            }
            if (idParametroActualizacao != null) {
                idParametroActualizacao.getBLeitorList().add(BLeitor);
                idParametroActualizacao = em.merge(idParametroActualizacao);
            }
            if (idutilizador != null) {
                idutilizador.getBLeitorList().add(BLeitor);
                idutilizador = em.merge(idutilizador);
            }
            if (idagente != null) {
                idagente.getBLeitorList().add(BLeitor);
                idagente = em.merge(idagente);
            }
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : BLeitor.getSgEmprestimoList()) {
                BLeitor oldIdLeitorOfSgEmprestimoListSgEmprestimo = sgEmprestimoListSgEmprestimo.getIdLeitor();
                sgEmprestimoListSgEmprestimo.setIdLeitor(BLeitor);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
                if (oldIdLeitorOfSgEmprestimoListSgEmprestimo != null) {
                    oldIdLeitorOfSgEmprestimoListSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListSgEmprestimo);
                    oldIdLeitorOfSgEmprestimoListSgEmprestimo = em.merge(oldIdLeitorOfSgEmprestimoListSgEmprestimo);
                }
            }
            for (BNotificacao BNotificacaoListBNotificacao : BLeitor.getBNotificacaoList()) {
                BLeitor oldIdLeitorOfBNotificacaoListBNotificacao = BNotificacaoListBNotificacao.getIdLeitor();
                BNotificacaoListBNotificacao.setIdLeitor(BLeitor);
                BNotificacaoListBNotificacao = em.merge(BNotificacaoListBNotificacao);
                if (oldIdLeitorOfBNotificacaoListBNotificacao != null) {
                    oldIdLeitorOfBNotificacaoListBNotificacao.getBNotificacaoList().remove(BNotificacaoListBNotificacao);
                    oldIdLeitorOfBNotificacaoListBNotificacao = em.merge(oldIdLeitorOfBNotificacaoListBNotificacao);
                }
            }
            for (BvLeitura bvLeituraListBvLeitura : BLeitor.getBvLeituraList()) {
                BLeitor oldBLeitorOfBvLeituraListBvLeitura = bvLeituraListBvLeitura.getBLeitor();
                bvLeituraListBvLeitura.setBLeitor(BLeitor);
                bvLeituraListBvLeitura = em.merge(bvLeituraListBvLeitura);
                if (oldBLeitorOfBvLeituraListBvLeitura != null) {
                    oldBLeitorOfBvLeituraListBvLeitura.getBvLeituraList().remove(bvLeituraListBvLeitura);
                    oldBLeitorOfBvLeituraListBvLeitura = em.merge(oldBLeitorOfBvLeituraListBvLeitura);
                }
            }
            for (BvArtigo bvArtigoListBvArtigo : BLeitor.getBvArtigoList()) {
                BLeitor oldPublicadorOfBvArtigoListBvArtigo = bvArtigoListBvArtigo.getPublicador();
                bvArtigoListBvArtigo.setPublicador(BLeitor);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
                if (oldPublicadorOfBvArtigoListBvArtigo != null) {
                    oldPublicadorOfBvArtigoListBvArtigo.getBvArtigoList().remove(bvArtigoListBvArtigo);
                    oldPublicadorOfBvArtigoListBvArtigo = em.merge(oldPublicadorOfBvArtigoListBvArtigo);
                }
            }
            for (BReserva BReservaListBReserva : BLeitor.getBReservaList()) {
                BLeitor oldLeitorOfBReservaListBReserva = BReservaListBReserva.getLeitor();
                BReservaListBReserva.setLeitor(BLeitor);
                BReservaListBReserva = em.merge(BReservaListBReserva);
                if (oldLeitorOfBReservaListBReserva != null) {
                    oldLeitorOfBReservaListBReserva.getBReservaList().remove(BReservaListBReserva);
                    oldLeitorOfBReservaListBReserva = em.merge(oldLeitorOfBReservaListBReserva);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BLeitor BLeitor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitor persistentBLeitor = em.find(BLeitor.class, BLeitor.getNrCartao());
            BvAvaliador bvAvaliadorOld = persistentBLeitor.getBvAvaliador();
            BvAvaliador bvAvaliadorNew = BLeitor.getBvAvaliador();
            SgEmprestimoParametros idParametroRegistoOld = persistentBLeitor.getIdParametroRegisto();
            SgEmprestimoParametros idParametroRegistoNew = BLeitor.getIdParametroRegisto();
            SgEmprestimoParametros idParametroActualizacaoOld = persistentBLeitor.getIdParametroActualizacao();
            SgEmprestimoParametros idParametroActualizacaoNew = BLeitor.getIdParametroActualizacao();
            Users idutilizadorOld = persistentBLeitor.getIdutilizador();
            Users idutilizadorNew = BLeitor.getIdutilizador();
            Users idagenteOld = persistentBLeitor.getIdagente();
            Users idagenteNew = BLeitor.getIdagente();
            List<SgEmprestimo> sgEmprestimoListOld = persistentBLeitor.getSgEmprestimoList();
            List<SgEmprestimo> sgEmprestimoListNew = BLeitor.getSgEmprestimoList();
            List<BNotificacao> BNotificacaoListOld = persistentBLeitor.getBNotificacaoList();
            List<BNotificacao> BNotificacaoListNew = BLeitor.getBNotificacaoList();
            List<BvLeitura> bvLeituraListOld = persistentBLeitor.getBvLeituraList();
            List<BvLeitura> bvLeituraListNew = BLeitor.getBvLeituraList();
            List<BvArtigo> bvArtigoListOld = persistentBLeitor.getBvArtigoList();
            List<BvArtigo> bvArtigoListNew = BLeitor.getBvArtigoList();
            List<BReserva> BReservaListOld = persistentBLeitor.getBReservaList();
            List<BReserva> BReservaListNew = BLeitor.getBReservaList();
            List<String> illegalOrphanMessages = null;
            if (bvAvaliadorOld != null && !bvAvaliadorOld.equals(bvAvaliadorNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain BvAvaliador " + bvAvaliadorOld + " since its BLeitor field is not nullable.");
            }
            for (BvLeitura bvLeituraListOldBvLeitura : bvLeituraListOld) {
                if (!bvLeituraListNew.contains(bvLeituraListOldBvLeitura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BvLeitura " + bvLeituraListOldBvLeitura + " since its BLeitor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (bvAvaliadorNew != null) {
                bvAvaliadorNew = em.getReference(bvAvaliadorNew.getClass(), bvAvaliadorNew.getIdLeitor());
                BLeitor.setBvAvaliador(bvAvaliadorNew);
            }
            if (idParametroRegistoNew != null) {
                idParametroRegistoNew = em.getReference(idParametroRegistoNew.getClass(), idParametroRegistoNew.getIdparametro());
                BLeitor.setIdParametroRegisto(idParametroRegistoNew);
            }
            if (idParametroActualizacaoNew != null) {
                idParametroActualizacaoNew = em.getReference(idParametroActualizacaoNew.getClass(), idParametroActualizacaoNew.getIdparametro());
                BLeitor.setIdParametroActualizacao(idParametroActualizacaoNew);
            }
            if (idutilizadorNew != null) {
                idutilizadorNew = em.getReference(idutilizadorNew.getClass(), idutilizadorNew.getUtilizador());
                BLeitor.setIdutilizador(idutilizadorNew);
            }
            if (idagenteNew != null) {
                idagenteNew = em.getReference(idagenteNew.getClass(), idagenteNew.getUtilizador());
                BLeitor.setIdagente(idagenteNew);
            }
            List<SgEmprestimo> attachedSgEmprestimoListNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimoToAttach : sgEmprestimoListNew) {
                sgEmprestimoListNewSgEmprestimoToAttach = em.getReference(sgEmprestimoListNewSgEmprestimoToAttach.getClass(), sgEmprestimoListNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoListNew.add(sgEmprestimoListNewSgEmprestimoToAttach);
            }
            sgEmprestimoListNew = attachedSgEmprestimoListNew;
            BLeitor.setSgEmprestimoList(sgEmprestimoListNew);
            List<BNotificacao> attachedBNotificacaoListNew = new ArrayList<BNotificacao>();
            for (BNotificacao BNotificacaoListNewBNotificacaoToAttach : BNotificacaoListNew) {
                BNotificacaoListNewBNotificacaoToAttach = em.getReference(BNotificacaoListNewBNotificacaoToAttach.getClass(), BNotificacaoListNewBNotificacaoToAttach.getIdNotificacao());
                attachedBNotificacaoListNew.add(BNotificacaoListNewBNotificacaoToAttach);
            }
            BNotificacaoListNew = attachedBNotificacaoListNew;
            BLeitor.setBNotificacaoList(BNotificacaoListNew);
            List<BvLeitura> attachedBvLeituraListNew = new ArrayList<BvLeitura>();
            for (BvLeitura bvLeituraListNewBvLeituraToAttach : bvLeituraListNew) {
                bvLeituraListNewBvLeituraToAttach = em.getReference(bvLeituraListNewBvLeituraToAttach.getClass(), bvLeituraListNewBvLeituraToAttach.getBvLeituraPK());
                attachedBvLeituraListNew.add(bvLeituraListNewBvLeituraToAttach);
            }
            bvLeituraListNew = attachedBvLeituraListNew;
            BLeitor.setBvLeituraList(bvLeituraListNew);
            List<BvArtigo> attachedBvArtigoListNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListNewBvArtigoToAttach : bvArtigoListNew) {
                bvArtigoListNewBvArtigoToAttach = em.getReference(bvArtigoListNewBvArtigoToAttach.getClass(), bvArtigoListNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoListNew.add(bvArtigoListNewBvArtigoToAttach);
            }
            bvArtigoListNew = attachedBvArtigoListNew;
            BLeitor.setBvArtigoList(bvArtigoListNew);
            List<BReserva> attachedBReservaListNew = new ArrayList<BReserva>();
            for (BReserva BReservaListNewBReservaToAttach : BReservaListNew) {
                BReservaListNewBReservaToAttach = em.getReference(BReservaListNewBReservaToAttach.getClass(), BReservaListNewBReservaToAttach.getIdagenda());
                attachedBReservaListNew.add(BReservaListNewBReservaToAttach);
            }
            BReservaListNew = attachedBReservaListNew;
            BLeitor.setBReservaList(BReservaListNew);
            BLeitor = em.merge(BLeitor);
            if (bvAvaliadorNew != null && !bvAvaliadorNew.equals(bvAvaliadorOld)) {
                BLeitor oldBLeitorOfBvAvaliador = bvAvaliadorNew.getBLeitor();
                if (oldBLeitorOfBvAvaliador != null) {
                    oldBLeitorOfBvAvaliador.setBvAvaliador(null);
                    oldBLeitorOfBvAvaliador = em.merge(oldBLeitorOfBvAvaliador);
                }
                bvAvaliadorNew.setBLeitor(BLeitor);
                bvAvaliadorNew = em.merge(bvAvaliadorNew);
            }
            if (idParametroRegistoOld != null && !idParametroRegistoOld.equals(idParametroRegistoNew)) {
                idParametroRegistoOld.getBLeitorList().remove(BLeitor);
                idParametroRegistoOld = em.merge(idParametroRegistoOld);
            }
            if (idParametroRegistoNew != null && !idParametroRegistoNew.equals(idParametroRegistoOld)) {
                idParametroRegistoNew.getBLeitorList().add(BLeitor);
                idParametroRegistoNew = em.merge(idParametroRegistoNew);
            }
            if (idParametroActualizacaoOld != null && !idParametroActualizacaoOld.equals(idParametroActualizacaoNew)) {
                idParametroActualizacaoOld.getBLeitorList().remove(BLeitor);
                idParametroActualizacaoOld = em.merge(idParametroActualizacaoOld);
            }
            if (idParametroActualizacaoNew != null && !idParametroActualizacaoNew.equals(idParametroActualizacaoOld)) {
                idParametroActualizacaoNew.getBLeitorList().add(BLeitor);
                idParametroActualizacaoNew = em.merge(idParametroActualizacaoNew);
            }
            if (idutilizadorOld != null && !idutilizadorOld.equals(idutilizadorNew)) {
                idutilizadorOld.getBLeitorList().remove(BLeitor);
                idutilizadorOld = em.merge(idutilizadorOld);
            }
            if (idutilizadorNew != null && !idutilizadorNew.equals(idutilizadorOld)) {
                idutilizadorNew.getBLeitorList().add(BLeitor);
                idutilizadorNew = em.merge(idutilizadorNew);
            }
            if (idagenteOld != null && !idagenteOld.equals(idagenteNew)) {
                idagenteOld.getBLeitorList().remove(BLeitor);
                idagenteOld = em.merge(idagenteOld);
            }
            if (idagenteNew != null && !idagenteNew.equals(idagenteOld)) {
                idagenteNew.getBLeitorList().add(BLeitor);
                idagenteNew = em.merge(idagenteNew);
            }
            for (SgEmprestimo sgEmprestimoListOldSgEmprestimo : sgEmprestimoListOld) {
                if (!sgEmprestimoListNew.contains(sgEmprestimoListOldSgEmprestimo)) {
                    sgEmprestimoListOldSgEmprestimo.setIdLeitor(null);
                    sgEmprestimoListOldSgEmprestimo = em.merge(sgEmprestimoListOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimo : sgEmprestimoListNew) {
                if (!sgEmprestimoListOld.contains(sgEmprestimoListNewSgEmprestimo)) {
                    BLeitor oldIdLeitorOfSgEmprestimoListNewSgEmprestimo = sgEmprestimoListNewSgEmprestimo.getIdLeitor();
                    sgEmprestimoListNewSgEmprestimo.setIdLeitor(BLeitor);
                    sgEmprestimoListNewSgEmprestimo = em.merge(sgEmprestimoListNewSgEmprestimo);
                    if (oldIdLeitorOfSgEmprestimoListNewSgEmprestimo != null && !oldIdLeitorOfSgEmprestimoListNewSgEmprestimo.equals(BLeitor)) {
                        oldIdLeitorOfSgEmprestimoListNewSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListNewSgEmprestimo);
                        oldIdLeitorOfSgEmprestimoListNewSgEmprestimo = em.merge(oldIdLeitorOfSgEmprestimoListNewSgEmprestimo);
                    }
                }
            }
            for (BNotificacao BNotificacaoListOldBNotificacao : BNotificacaoListOld) {
                if (!BNotificacaoListNew.contains(BNotificacaoListOldBNotificacao)) {
                    BNotificacaoListOldBNotificacao.setIdLeitor(null);
                    BNotificacaoListOldBNotificacao = em.merge(BNotificacaoListOldBNotificacao);
                }
            }
            for (BNotificacao BNotificacaoListNewBNotificacao : BNotificacaoListNew) {
                if (!BNotificacaoListOld.contains(BNotificacaoListNewBNotificacao)) {
                    BLeitor oldIdLeitorOfBNotificacaoListNewBNotificacao = BNotificacaoListNewBNotificacao.getIdLeitor();
                    BNotificacaoListNewBNotificacao.setIdLeitor(BLeitor);
                    BNotificacaoListNewBNotificacao = em.merge(BNotificacaoListNewBNotificacao);
                    if (oldIdLeitorOfBNotificacaoListNewBNotificacao != null && !oldIdLeitorOfBNotificacaoListNewBNotificacao.equals(BLeitor)) {
                        oldIdLeitorOfBNotificacaoListNewBNotificacao.getBNotificacaoList().remove(BNotificacaoListNewBNotificacao);
                        oldIdLeitorOfBNotificacaoListNewBNotificacao = em.merge(oldIdLeitorOfBNotificacaoListNewBNotificacao);
                    }
                }
            }
            for (BvLeitura bvLeituraListNewBvLeitura : bvLeituraListNew) {
                if (!bvLeituraListOld.contains(bvLeituraListNewBvLeitura)) {
                    BLeitor oldBLeitorOfBvLeituraListNewBvLeitura = bvLeituraListNewBvLeitura.getBLeitor();
                    bvLeituraListNewBvLeitura.setBLeitor(BLeitor);
                    bvLeituraListNewBvLeitura = em.merge(bvLeituraListNewBvLeitura);
                    if (oldBLeitorOfBvLeituraListNewBvLeitura != null && !oldBLeitorOfBvLeituraListNewBvLeitura.equals(BLeitor)) {
                        oldBLeitorOfBvLeituraListNewBvLeitura.getBvLeituraList().remove(bvLeituraListNewBvLeitura);
                        oldBLeitorOfBvLeituraListNewBvLeitura = em.merge(oldBLeitorOfBvLeituraListNewBvLeitura);
                    }
                }
            }
            for (BvArtigo bvArtigoListOldBvArtigo : bvArtigoListOld) {
                if (!bvArtigoListNew.contains(bvArtigoListOldBvArtigo)) {
                    bvArtigoListOldBvArtigo.setPublicador(null);
                    bvArtigoListOldBvArtigo = em.merge(bvArtigoListOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoListNewBvArtigo : bvArtigoListNew) {
                if (!bvArtigoListOld.contains(bvArtigoListNewBvArtigo)) {
                    BLeitor oldPublicadorOfBvArtigoListNewBvArtigo = bvArtigoListNewBvArtigo.getPublicador();
                    bvArtigoListNewBvArtigo.setPublicador(BLeitor);
                    bvArtigoListNewBvArtigo = em.merge(bvArtigoListNewBvArtigo);
                    if (oldPublicadorOfBvArtigoListNewBvArtigo != null && !oldPublicadorOfBvArtigoListNewBvArtigo.equals(BLeitor)) {
                        oldPublicadorOfBvArtigoListNewBvArtigo.getBvArtigoList().remove(bvArtigoListNewBvArtigo);
                        oldPublicadorOfBvArtigoListNewBvArtigo = em.merge(oldPublicadorOfBvArtigoListNewBvArtigo);
                    }
                }
            }
            for (BReserva BReservaListOldBReserva : BReservaListOld) {
                if (!BReservaListNew.contains(BReservaListOldBReserva)) {
                    BReservaListOldBReserva.setLeitor(null);
                    BReservaListOldBReserva = em.merge(BReservaListOldBReserva);
                }
            }
            for (BReserva BReservaListNewBReserva : BReservaListNew) {
                if (!BReservaListOld.contains(BReservaListNewBReserva)) {
                    BLeitor oldLeitorOfBReservaListNewBReserva = BReservaListNewBReserva.getLeitor();
                    BReservaListNewBReserva.setLeitor(BLeitor);
                    BReservaListNewBReserva = em.merge(BReservaListNewBReserva);
                    if (oldLeitorOfBReservaListNewBReserva != null && !oldLeitorOfBReservaListNewBReserva.equals(BLeitor)) {
                        oldLeitorOfBReservaListNewBReserva.getBReservaList().remove(BReservaListNewBReserva);
                        oldLeitorOfBReservaListNewBReserva = em.merge(oldLeitorOfBReservaListNewBReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = BLeitor.getNrCartao();
                if (findBLeitor(id) == null) {
                    throw new NonexistentEntityException("The bLeitor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitor BLeitor;
            try {
                BLeitor = em.getReference(BLeitor.class, id);
                BLeitor.getNrCartao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The BLeitor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            BvAvaliador bvAvaliadorOrphanCheck = BLeitor.getBvAvaliador();
            if (bvAvaliadorOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BLeitor (" + BLeitor + ") cannot be destroyed since the BvAvaliador " + bvAvaliadorOrphanCheck + " in its bvAvaliador field has a non-nullable BLeitor field.");
            }
            List<BvLeitura> bvLeituraListOrphanCheck = BLeitor.getBvLeituraList();
            for (BvLeitura bvLeituraListOrphanCheckBvLeitura : bvLeituraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BLeitor (" + BLeitor + ") cannot be destroyed since the BvLeitura " + bvLeituraListOrphanCheckBvLeitura + " in its bvLeituraList field has a non-nullable BLeitor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SgEmprestimoParametros idParametroRegisto = BLeitor.getIdParametroRegisto();
            if (idParametroRegisto != null) {
                idParametroRegisto.getBLeitorList().remove(BLeitor);
                idParametroRegisto = em.merge(idParametroRegisto);
            }
            SgEmprestimoParametros idParametroActualizacao = BLeitor.getIdParametroActualizacao();
            if (idParametroActualizacao != null) {
                idParametroActualizacao.getBLeitorList().remove(BLeitor);
                idParametroActualizacao = em.merge(idParametroActualizacao);
            }
            Users idutilizador = BLeitor.getIdutilizador();
            if (idutilizador != null) {
                idutilizador.getBLeitorList().remove(BLeitor);
                idutilizador = em.merge(idutilizador);
            }
            Users idagente = BLeitor.getIdagente();
            if (idagente != null) {
                idagente.getBLeitorList().remove(BLeitor);
                idagente = em.merge(idagente);
            }
            List<SgEmprestimo> sgEmprestimoList = BLeitor.getSgEmprestimoList();
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : sgEmprestimoList) {
                sgEmprestimoListSgEmprestimo.setIdLeitor(null);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
            }
            List<BNotificacao> BNotificacaoList = BLeitor.getBNotificacaoList();
            for (BNotificacao BNotificacaoListBNotificacao : BNotificacaoList) {
                BNotificacaoListBNotificacao.setIdLeitor(null);
                BNotificacaoListBNotificacao = em.merge(BNotificacaoListBNotificacao);
            }
            List<BvArtigo> bvArtigoList = BLeitor.getBvArtigoList();
            for (BvArtigo bvArtigoListBvArtigo : bvArtigoList) {
                bvArtigoListBvArtigo.setPublicador(null);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
            }
            List<BReserva> BReservaList = BLeitor.getBReservaList();
            for (BReserva BReservaListBReserva : BReservaList) {
                BReservaListBReserva.setLeitor(null);
                BReservaListBReserva = em.merge(BReservaListBReserva);
            }
            em.remove(BLeitor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BLeitor> findBLeitorEntities() {
        return findBLeitorEntities(true, -1, -1);
    }

    public List<BLeitor> findBLeitorEntities(int maxResults, int firstResult) {
        return findBLeitorEntities(false, maxResults, firstResult);
    }

    private List<BLeitor> findBLeitorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BLeitor.class));
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

    public BLeitor findBLeitor(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BLeitor.class, id);
        } finally {
            em.close();
        }
    }

    public int getBLeitorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BLeitor> rt = cq.from(BLeitor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
