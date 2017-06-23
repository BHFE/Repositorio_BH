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
import java.util.Collection;
import entidades.BNotificacao;
import entidades.BvLeitura;
import entidades.BvArtigo;
import entidades.BReserva;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
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
        if (BLeitor.getSgEmprestimoCollection() == null) {
            BLeitor.setSgEmprestimoCollection(new ArrayList<SgEmprestimo>());
        }
        if (BLeitor.getBNotificacaoCollection() == null) {
            BLeitor.setBNotificacaoCollection(new ArrayList<BNotificacao>());
        }
        if (BLeitor.getBvLeituraCollection() == null) {
            BLeitor.setBvLeituraCollection(new ArrayList<BvLeitura>());
        }
        if (BLeitor.getBvArtigoCollection() == null) {
            BLeitor.setBvArtigoCollection(new ArrayList<BvArtigo>());
        }
        if (BLeitor.getBReservaCollection() == null) {
            BLeitor.setBReservaCollection(new ArrayList<BReserva>());
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
            SgEmprestimoParametros idParametroActualizacao = BLeitor.getIdParametroActualizacao();
            if (idParametroActualizacao != null) {
                idParametroActualizacao = em.getReference(idParametroActualizacao.getClass(), idParametroActualizacao.getIdparametro());
                BLeitor.setIdParametroActualizacao(idParametroActualizacao);
            }
            SgEmprestimoParametros idParametroRegisto = BLeitor.getIdParametroRegisto();
            if (idParametroRegisto != null) {
                idParametroRegisto = em.getReference(idParametroRegisto.getClass(), idParametroRegisto.getIdparametro());
                BLeitor.setIdParametroRegisto(idParametroRegisto);
            }
            Users idagente = BLeitor.getIdagente();
            if (idagente != null) {
                idagente = em.getReference(idagente.getClass(), idagente.getUtilizador());
                BLeitor.setIdagente(idagente);
            }
            Users idutilizador = BLeitor.getIdutilizador();
            if (idutilizador != null) {
                idutilizador = em.getReference(idutilizador.getClass(), idutilizador.getUtilizador());
                BLeitor.setIdutilizador(idutilizador);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollection = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimoToAttach : BLeitor.getSgEmprestimoCollection()) {
                sgEmprestimoCollectionSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollection.add(sgEmprestimoCollectionSgEmprestimoToAttach);
            }
            BLeitor.setSgEmprestimoCollection(attachedSgEmprestimoCollection);
            Collection<BNotificacao> attachedBNotificacaoCollection = new ArrayList<BNotificacao>();
            for (BNotificacao BNotificacaoCollectionBNotificacaoToAttach : BLeitor.getBNotificacaoCollection()) {
                BNotificacaoCollectionBNotificacaoToAttach = em.getReference(BNotificacaoCollectionBNotificacaoToAttach.getClass(), BNotificacaoCollectionBNotificacaoToAttach.getIdNotificacao());
                attachedBNotificacaoCollection.add(BNotificacaoCollectionBNotificacaoToAttach);
            }
            BLeitor.setBNotificacaoCollection(attachedBNotificacaoCollection);
            Collection<BvLeitura> attachedBvLeituraCollection = new ArrayList<BvLeitura>();
            for (BvLeitura bvLeituraCollectionBvLeituraToAttach : BLeitor.getBvLeituraCollection()) {
                bvLeituraCollectionBvLeituraToAttach = em.getReference(bvLeituraCollectionBvLeituraToAttach.getClass(), bvLeituraCollectionBvLeituraToAttach.getBvLeituraPK());
                attachedBvLeituraCollection.add(bvLeituraCollectionBvLeituraToAttach);
            }
            BLeitor.setBvLeituraCollection(attachedBvLeituraCollection);
            Collection<BvArtigo> attachedBvArtigoCollection = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionBvArtigoToAttach : BLeitor.getBvArtigoCollection()) {
                bvArtigoCollectionBvArtigoToAttach = em.getReference(bvArtigoCollectionBvArtigoToAttach.getClass(), bvArtigoCollectionBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollection.add(bvArtigoCollectionBvArtigoToAttach);
            }
            BLeitor.setBvArtigoCollection(attachedBvArtigoCollection);
            Collection<BReserva> attachedBReservaCollection = new ArrayList<BReserva>();
            for (BReserva BReservaCollectionBReservaToAttach : BLeitor.getBReservaCollection()) {
                BReservaCollectionBReservaToAttach = em.getReference(BReservaCollectionBReservaToAttach.getClass(), BReservaCollectionBReservaToAttach.getIdagenda());
                attachedBReservaCollection.add(BReservaCollectionBReservaToAttach);
            }
            BLeitor.setBReservaCollection(attachedBReservaCollection);
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
            if (idParametroActualizacao != null) {
                idParametroActualizacao.getBLeitorCollection().add(BLeitor);
                idParametroActualizacao = em.merge(idParametroActualizacao);
            }
            if (idParametroRegisto != null) {
                idParametroRegisto.getBLeitorCollection().add(BLeitor);
                idParametroRegisto = em.merge(idParametroRegisto);
            }
            if (idagente != null) {
                idagente.getBLeitorCollection().add(BLeitor);
                idagente = em.merge(idagente);
            }
            if (idutilizador != null) {
                idutilizador.getBLeitorCollection().add(BLeitor);
                idutilizador = em.merge(idutilizador);
            }
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : BLeitor.getSgEmprestimoCollection()) {
                BLeitor oldIdLeitorOfSgEmprestimoCollectionSgEmprestimo = sgEmprestimoCollectionSgEmprestimo.getIdLeitor();
                sgEmprestimoCollectionSgEmprestimo.setIdLeitor(BLeitor);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
                if (oldIdLeitorOfSgEmprestimoCollectionSgEmprestimo != null) {
                    oldIdLeitorOfSgEmprestimoCollectionSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionSgEmprestimo);
                    oldIdLeitorOfSgEmprestimoCollectionSgEmprestimo = em.merge(oldIdLeitorOfSgEmprestimoCollectionSgEmprestimo);
                }
            }
            for (BNotificacao BNotificacaoCollectionBNotificacao : BLeitor.getBNotificacaoCollection()) {
                BLeitor oldIdLeitorOfBNotificacaoCollectionBNotificacao = BNotificacaoCollectionBNotificacao.getIdLeitor();
                BNotificacaoCollectionBNotificacao.setIdLeitor(BLeitor);
                BNotificacaoCollectionBNotificacao = em.merge(BNotificacaoCollectionBNotificacao);
                if (oldIdLeitorOfBNotificacaoCollectionBNotificacao != null) {
                    oldIdLeitorOfBNotificacaoCollectionBNotificacao.getBNotificacaoCollection().remove(BNotificacaoCollectionBNotificacao);
                    oldIdLeitorOfBNotificacaoCollectionBNotificacao = em.merge(oldIdLeitorOfBNotificacaoCollectionBNotificacao);
                }
            }
            for (BvLeitura bvLeituraCollectionBvLeitura : BLeitor.getBvLeituraCollection()) {
                BLeitor oldBLeitorOfBvLeituraCollectionBvLeitura = bvLeituraCollectionBvLeitura.getBLeitor();
                bvLeituraCollectionBvLeitura.setBLeitor(BLeitor);
                bvLeituraCollectionBvLeitura = em.merge(bvLeituraCollectionBvLeitura);
                if (oldBLeitorOfBvLeituraCollectionBvLeitura != null) {
                    oldBLeitorOfBvLeituraCollectionBvLeitura.getBvLeituraCollection().remove(bvLeituraCollectionBvLeitura);
                    oldBLeitorOfBvLeituraCollectionBvLeitura = em.merge(oldBLeitorOfBvLeituraCollectionBvLeitura);
                }
            }
            for (BvArtigo bvArtigoCollectionBvArtigo : BLeitor.getBvArtigoCollection()) {
                BLeitor oldPublicadorOfBvArtigoCollectionBvArtigo = bvArtigoCollectionBvArtigo.getPublicador();
                bvArtigoCollectionBvArtigo.setPublicador(BLeitor);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
                if (oldPublicadorOfBvArtigoCollectionBvArtigo != null) {
                    oldPublicadorOfBvArtigoCollectionBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionBvArtigo);
                    oldPublicadorOfBvArtigoCollectionBvArtigo = em.merge(oldPublicadorOfBvArtigoCollectionBvArtigo);
                }
            }
            for (BReserva BReservaCollectionBReserva : BLeitor.getBReservaCollection()) {
                BLeitor oldLeitorOfBReservaCollectionBReserva = BReservaCollectionBReserva.getLeitor();
                BReservaCollectionBReserva.setLeitor(BLeitor);
                BReservaCollectionBReserva = em.merge(BReservaCollectionBReserva);
                if (oldLeitorOfBReservaCollectionBReserva != null) {
                    oldLeitorOfBReservaCollectionBReserva.getBReservaCollection().remove(BReservaCollectionBReserva);
                    oldLeitorOfBReservaCollectionBReserva = em.merge(oldLeitorOfBReservaCollectionBReserva);
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
            SgEmprestimoParametros idParametroActualizacaoOld = persistentBLeitor.getIdParametroActualizacao();
            SgEmprestimoParametros idParametroActualizacaoNew = BLeitor.getIdParametroActualizacao();
            SgEmprestimoParametros idParametroRegistoOld = persistentBLeitor.getIdParametroRegisto();
            SgEmprestimoParametros idParametroRegistoNew = BLeitor.getIdParametroRegisto();
            Users idagenteOld = persistentBLeitor.getIdagente();
            Users idagenteNew = BLeitor.getIdagente();
            Users idutilizadorOld = persistentBLeitor.getIdutilizador();
            Users idutilizadorNew = BLeitor.getIdutilizador();
            Collection<SgEmprestimo> sgEmprestimoCollectionOld = persistentBLeitor.getSgEmprestimoCollection();
            Collection<SgEmprestimo> sgEmprestimoCollectionNew = BLeitor.getSgEmprestimoCollection();
            Collection<BNotificacao> BNotificacaoCollectionOld = persistentBLeitor.getBNotificacaoCollection();
            Collection<BNotificacao> BNotificacaoCollectionNew = BLeitor.getBNotificacaoCollection();
            Collection<BvLeitura> bvLeituraCollectionOld = persistentBLeitor.getBvLeituraCollection();
            Collection<BvLeitura> bvLeituraCollectionNew = BLeitor.getBvLeituraCollection();
            Collection<BvArtigo> bvArtigoCollectionOld = persistentBLeitor.getBvArtigoCollection();
            Collection<BvArtigo> bvArtigoCollectionNew = BLeitor.getBvArtigoCollection();
            Collection<BReserva> BReservaCollectionOld = persistentBLeitor.getBReservaCollection();
            Collection<BReserva> BReservaCollectionNew = BLeitor.getBReservaCollection();
            List<String> illegalOrphanMessages = null;
            if (bvAvaliadorOld != null && !bvAvaliadorOld.equals(bvAvaliadorNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain BvAvaliador " + bvAvaliadorOld + " since its BLeitor field is not nullable.");
            }
            for (BvLeitura bvLeituraCollectionOldBvLeitura : bvLeituraCollectionOld) {
                if (!bvLeituraCollectionNew.contains(bvLeituraCollectionOldBvLeitura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BvLeitura " + bvLeituraCollectionOldBvLeitura + " since its BLeitor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (bvAvaliadorNew != null) {
                bvAvaliadorNew = em.getReference(bvAvaliadorNew.getClass(), bvAvaliadorNew.getIdLeitor());
                BLeitor.setBvAvaliador(bvAvaliadorNew);
            }
            if (idParametroActualizacaoNew != null) {
                idParametroActualizacaoNew = em.getReference(idParametroActualizacaoNew.getClass(), idParametroActualizacaoNew.getIdparametro());
                BLeitor.setIdParametroActualizacao(idParametroActualizacaoNew);
            }
            if (idParametroRegistoNew != null) {
                idParametroRegistoNew = em.getReference(idParametroRegistoNew.getClass(), idParametroRegistoNew.getIdparametro());
                BLeitor.setIdParametroRegisto(idParametroRegistoNew);
            }
            if (idagenteNew != null) {
                idagenteNew = em.getReference(idagenteNew.getClass(), idagenteNew.getUtilizador());
                BLeitor.setIdagente(idagenteNew);
            }
            if (idutilizadorNew != null) {
                idutilizadorNew = em.getReference(idutilizadorNew.getClass(), idutilizadorNew.getUtilizador());
                BLeitor.setIdutilizador(idutilizadorNew);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollectionNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimoToAttach : sgEmprestimoCollectionNew) {
                sgEmprestimoCollectionNewSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionNewSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollectionNew.add(sgEmprestimoCollectionNewSgEmprestimoToAttach);
            }
            sgEmprestimoCollectionNew = attachedSgEmprestimoCollectionNew;
            BLeitor.setSgEmprestimoCollection(sgEmprestimoCollectionNew);
            Collection<BNotificacao> attachedBNotificacaoCollectionNew = new ArrayList<BNotificacao>();
            for (BNotificacao BNotificacaoCollectionNewBNotificacaoToAttach : BNotificacaoCollectionNew) {
                BNotificacaoCollectionNewBNotificacaoToAttach = em.getReference(BNotificacaoCollectionNewBNotificacaoToAttach.getClass(), BNotificacaoCollectionNewBNotificacaoToAttach.getIdNotificacao());
                attachedBNotificacaoCollectionNew.add(BNotificacaoCollectionNewBNotificacaoToAttach);
            }
            BNotificacaoCollectionNew = attachedBNotificacaoCollectionNew;
            BLeitor.setBNotificacaoCollection(BNotificacaoCollectionNew);
            Collection<BvLeitura> attachedBvLeituraCollectionNew = new ArrayList<BvLeitura>();
            for (BvLeitura bvLeituraCollectionNewBvLeituraToAttach : bvLeituraCollectionNew) {
                bvLeituraCollectionNewBvLeituraToAttach = em.getReference(bvLeituraCollectionNewBvLeituraToAttach.getClass(), bvLeituraCollectionNewBvLeituraToAttach.getBvLeituraPK());
                attachedBvLeituraCollectionNew.add(bvLeituraCollectionNewBvLeituraToAttach);
            }
            bvLeituraCollectionNew = attachedBvLeituraCollectionNew;
            BLeitor.setBvLeituraCollection(bvLeituraCollectionNew);
            Collection<BvArtigo> attachedBvArtigoCollectionNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionNewBvArtigoToAttach : bvArtigoCollectionNew) {
                bvArtigoCollectionNewBvArtigoToAttach = em.getReference(bvArtigoCollectionNewBvArtigoToAttach.getClass(), bvArtigoCollectionNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollectionNew.add(bvArtigoCollectionNewBvArtigoToAttach);
            }
            bvArtigoCollectionNew = attachedBvArtigoCollectionNew;
            BLeitor.setBvArtigoCollection(bvArtigoCollectionNew);
            Collection<BReserva> attachedBReservaCollectionNew = new ArrayList<BReserva>();
            for (BReserva BReservaCollectionNewBReservaToAttach : BReservaCollectionNew) {
                BReservaCollectionNewBReservaToAttach = em.getReference(BReservaCollectionNewBReservaToAttach.getClass(), BReservaCollectionNewBReservaToAttach.getIdagenda());
                attachedBReservaCollectionNew.add(BReservaCollectionNewBReservaToAttach);
            }
            BReservaCollectionNew = attachedBReservaCollectionNew;
            BLeitor.setBReservaCollection(BReservaCollectionNew);
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
            if (idParametroActualizacaoOld != null && !idParametroActualizacaoOld.equals(idParametroActualizacaoNew)) {
                idParametroActualizacaoOld.getBLeitorCollection().remove(BLeitor);
                idParametroActualizacaoOld = em.merge(idParametroActualizacaoOld);
            }
            if (idParametroActualizacaoNew != null && !idParametroActualizacaoNew.equals(idParametroActualizacaoOld)) {
                idParametroActualizacaoNew.getBLeitorCollection().add(BLeitor);
                idParametroActualizacaoNew = em.merge(idParametroActualizacaoNew);
            }
            if (idParametroRegistoOld != null && !idParametroRegistoOld.equals(idParametroRegistoNew)) {
                idParametroRegistoOld.getBLeitorCollection().remove(BLeitor);
                idParametroRegistoOld = em.merge(idParametroRegistoOld);
            }
            if (idParametroRegistoNew != null && !idParametroRegistoNew.equals(idParametroRegistoOld)) {
                idParametroRegistoNew.getBLeitorCollection().add(BLeitor);
                idParametroRegistoNew = em.merge(idParametroRegistoNew);
            }
            if (idagenteOld != null && !idagenteOld.equals(idagenteNew)) {
                idagenteOld.getBLeitorCollection().remove(BLeitor);
                idagenteOld = em.merge(idagenteOld);
            }
            if (idagenteNew != null && !idagenteNew.equals(idagenteOld)) {
                idagenteNew.getBLeitorCollection().add(BLeitor);
                idagenteNew = em.merge(idagenteNew);
            }
            if (idutilizadorOld != null && !idutilizadorOld.equals(idutilizadorNew)) {
                idutilizadorOld.getBLeitorCollection().remove(BLeitor);
                idutilizadorOld = em.merge(idutilizadorOld);
            }
            if (idutilizadorNew != null && !idutilizadorNew.equals(idutilizadorOld)) {
                idutilizadorNew.getBLeitorCollection().add(BLeitor);
                idutilizadorNew = em.merge(idutilizadorNew);
            }
            for (SgEmprestimo sgEmprestimoCollectionOldSgEmprestimo : sgEmprestimoCollectionOld) {
                if (!sgEmprestimoCollectionNew.contains(sgEmprestimoCollectionOldSgEmprestimo)) {
                    sgEmprestimoCollectionOldSgEmprestimo.setIdLeitor(null);
                    sgEmprestimoCollectionOldSgEmprestimo = em.merge(sgEmprestimoCollectionOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimo : sgEmprestimoCollectionNew) {
                if (!sgEmprestimoCollectionOld.contains(sgEmprestimoCollectionNewSgEmprestimo)) {
                    BLeitor oldIdLeitorOfSgEmprestimoCollectionNewSgEmprestimo = sgEmprestimoCollectionNewSgEmprestimo.getIdLeitor();
                    sgEmprestimoCollectionNewSgEmprestimo.setIdLeitor(BLeitor);
                    sgEmprestimoCollectionNewSgEmprestimo = em.merge(sgEmprestimoCollectionNewSgEmprestimo);
                    if (oldIdLeitorOfSgEmprestimoCollectionNewSgEmprestimo != null && !oldIdLeitorOfSgEmprestimoCollectionNewSgEmprestimo.equals(BLeitor)) {
                        oldIdLeitorOfSgEmprestimoCollectionNewSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionNewSgEmprestimo);
                        oldIdLeitorOfSgEmprestimoCollectionNewSgEmprestimo = em.merge(oldIdLeitorOfSgEmprestimoCollectionNewSgEmprestimo);
                    }
                }
            }
            for (BNotificacao BNotificacaoCollectionOldBNotificacao : BNotificacaoCollectionOld) {
                if (!BNotificacaoCollectionNew.contains(BNotificacaoCollectionOldBNotificacao)) {
                    BNotificacaoCollectionOldBNotificacao.setIdLeitor(null);
                    BNotificacaoCollectionOldBNotificacao = em.merge(BNotificacaoCollectionOldBNotificacao);
                }
            }
            for (BNotificacao BNotificacaoCollectionNewBNotificacao : BNotificacaoCollectionNew) {
                if (!BNotificacaoCollectionOld.contains(BNotificacaoCollectionNewBNotificacao)) {
                    BLeitor oldIdLeitorOfBNotificacaoCollectionNewBNotificacao = BNotificacaoCollectionNewBNotificacao.getIdLeitor();
                    BNotificacaoCollectionNewBNotificacao.setIdLeitor(BLeitor);
                    BNotificacaoCollectionNewBNotificacao = em.merge(BNotificacaoCollectionNewBNotificacao);
                    if (oldIdLeitorOfBNotificacaoCollectionNewBNotificacao != null && !oldIdLeitorOfBNotificacaoCollectionNewBNotificacao.equals(BLeitor)) {
                        oldIdLeitorOfBNotificacaoCollectionNewBNotificacao.getBNotificacaoCollection().remove(BNotificacaoCollectionNewBNotificacao);
                        oldIdLeitorOfBNotificacaoCollectionNewBNotificacao = em.merge(oldIdLeitorOfBNotificacaoCollectionNewBNotificacao);
                    }
                }
            }
            for (BvLeitura bvLeituraCollectionNewBvLeitura : bvLeituraCollectionNew) {
                if (!bvLeituraCollectionOld.contains(bvLeituraCollectionNewBvLeitura)) {
                    BLeitor oldBLeitorOfBvLeituraCollectionNewBvLeitura = bvLeituraCollectionNewBvLeitura.getBLeitor();
                    bvLeituraCollectionNewBvLeitura.setBLeitor(BLeitor);
                    bvLeituraCollectionNewBvLeitura = em.merge(bvLeituraCollectionNewBvLeitura);
                    if (oldBLeitorOfBvLeituraCollectionNewBvLeitura != null && !oldBLeitorOfBvLeituraCollectionNewBvLeitura.equals(BLeitor)) {
                        oldBLeitorOfBvLeituraCollectionNewBvLeitura.getBvLeituraCollection().remove(bvLeituraCollectionNewBvLeitura);
                        oldBLeitorOfBvLeituraCollectionNewBvLeitura = em.merge(oldBLeitorOfBvLeituraCollectionNewBvLeitura);
                    }
                }
            }
            for (BvArtigo bvArtigoCollectionOldBvArtigo : bvArtigoCollectionOld) {
                if (!bvArtigoCollectionNew.contains(bvArtigoCollectionOldBvArtigo)) {
                    bvArtigoCollectionOldBvArtigo.setPublicador(null);
                    bvArtigoCollectionOldBvArtigo = em.merge(bvArtigoCollectionOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoCollectionNewBvArtigo : bvArtigoCollectionNew) {
                if (!bvArtigoCollectionOld.contains(bvArtigoCollectionNewBvArtigo)) {
                    BLeitor oldPublicadorOfBvArtigoCollectionNewBvArtigo = bvArtigoCollectionNewBvArtigo.getPublicador();
                    bvArtigoCollectionNewBvArtigo.setPublicador(BLeitor);
                    bvArtigoCollectionNewBvArtigo = em.merge(bvArtigoCollectionNewBvArtigo);
                    if (oldPublicadorOfBvArtigoCollectionNewBvArtigo != null && !oldPublicadorOfBvArtigoCollectionNewBvArtigo.equals(BLeitor)) {
                        oldPublicadorOfBvArtigoCollectionNewBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionNewBvArtigo);
                        oldPublicadorOfBvArtigoCollectionNewBvArtigo = em.merge(oldPublicadorOfBvArtigoCollectionNewBvArtigo);
                    }
                }
            }
            for (BReserva BReservaCollectionOldBReserva : BReservaCollectionOld) {
                if (!BReservaCollectionNew.contains(BReservaCollectionOldBReserva)) {
                    BReservaCollectionOldBReserva.setLeitor(null);
                    BReservaCollectionOldBReserva = em.merge(BReservaCollectionOldBReserva);
                }
            }
            for (BReserva BReservaCollectionNewBReserva : BReservaCollectionNew) {
                if (!BReservaCollectionOld.contains(BReservaCollectionNewBReserva)) {
                    BLeitor oldLeitorOfBReservaCollectionNewBReserva = BReservaCollectionNewBReserva.getLeitor();
                    BReservaCollectionNewBReserva.setLeitor(BLeitor);
                    BReservaCollectionNewBReserva = em.merge(BReservaCollectionNewBReserva);
                    if (oldLeitorOfBReservaCollectionNewBReserva != null && !oldLeitorOfBReservaCollectionNewBReserva.equals(BLeitor)) {
                        oldLeitorOfBReservaCollectionNewBReserva.getBReservaCollection().remove(BReservaCollectionNewBReserva);
                        oldLeitorOfBReservaCollectionNewBReserva = em.merge(oldLeitorOfBReservaCollectionNewBReserva);
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
            Collection<BvLeitura> bvLeituraCollectionOrphanCheck = BLeitor.getBvLeituraCollection();
            for (BvLeitura bvLeituraCollectionOrphanCheckBvLeitura : bvLeituraCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BLeitor (" + BLeitor + ") cannot be destroyed since the BvLeitura " + bvLeituraCollectionOrphanCheckBvLeitura + " in its bvLeituraCollection field has a non-nullable BLeitor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SgEmprestimoParametros idParametroActualizacao = BLeitor.getIdParametroActualizacao();
            if (idParametroActualizacao != null) {
                idParametroActualizacao.getBLeitorCollection().remove(BLeitor);
                idParametroActualizacao = em.merge(idParametroActualizacao);
            }
            SgEmprestimoParametros idParametroRegisto = BLeitor.getIdParametroRegisto();
            if (idParametroRegisto != null) {
                idParametroRegisto.getBLeitorCollection().remove(BLeitor);
                idParametroRegisto = em.merge(idParametroRegisto);
            }
            Users idagente = BLeitor.getIdagente();
            if (idagente != null) {
                idagente.getBLeitorCollection().remove(BLeitor);
                idagente = em.merge(idagente);
            }
            Users idutilizador = BLeitor.getIdutilizador();
            if (idutilizador != null) {
                idutilizador.getBLeitorCollection().remove(BLeitor);
                idutilizador = em.merge(idutilizador);
            }
            Collection<SgEmprestimo> sgEmprestimoCollection = BLeitor.getSgEmprestimoCollection();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : sgEmprestimoCollection) {
                sgEmprestimoCollectionSgEmprestimo.setIdLeitor(null);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
            }
            Collection<BNotificacao> BNotificacaoCollection = BLeitor.getBNotificacaoCollection();
            for (BNotificacao BNotificacaoCollectionBNotificacao : BNotificacaoCollection) {
                BNotificacaoCollectionBNotificacao.setIdLeitor(null);
                BNotificacaoCollectionBNotificacao = em.merge(BNotificacaoCollectionBNotificacao);
            }
            Collection<BvArtigo> bvArtigoCollection = BLeitor.getBvArtigoCollection();
            for (BvArtigo bvArtigoCollectionBvArtigo : bvArtigoCollection) {
                bvArtigoCollectionBvArtigo.setPublicador(null);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
            }
            Collection<BReserva> BReservaCollection = BLeitor.getBReservaCollection();
            for (BReserva BReservaCollectionBReserva : BReservaCollection) {
                BReservaCollectionBReserva.setLeitor(null);
                BReservaCollectionBReserva = em.merge(BReservaCollectionBReserva);
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
