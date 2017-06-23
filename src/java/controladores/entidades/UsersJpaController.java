/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Estudante;
import entidades.Faculdade;
import entidades.Funcionario;
import entidades.SgEmprestimo;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Usergrupo;
import entidades.SgObra;
import entidades.BLeitor;
import entidades.SgExemplar;
import entidades.SgEmprestimoParametros;
import entidades.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws PreexistingEntityException, Exception {
        if (users.getSgEmprestimoCollection() == null) {
            users.setSgEmprestimoCollection(new ArrayList<SgEmprestimo>());
        }
        if (users.getUsergrupoCollection() == null) {
            users.setUsergrupoCollection(new ArrayList<Usergrupo>());
        }
        if (users.getSgObraCollection() == null) {
            users.setSgObraCollection(new ArrayList<SgObra>());
        }
        if (users.getBLeitorCollection() == null) {
            users.setBLeitorCollection(new ArrayList<BLeitor>());
        }
        if (users.getBLeitorCollection1() == null) {
            users.setBLeitorCollection1(new ArrayList<BLeitor>());
        }
        if (users.getSgExemplarCollection() == null) {
            users.setSgExemplarCollection(new ArrayList<SgExemplar>());
        }
        if (users.getSgEmprestimoParametrosCollection() == null) {
            users.setSgEmprestimoParametrosCollection(new ArrayList<SgEmprestimoParametros>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudante idEstudante = users.getIdEstudante();
            if (idEstudante != null) {
                idEstudante = em.getReference(idEstudante.getClass(), idEstudante.getIdEstudante());
                users.setIdEstudante(idEstudante);
            }
            Faculdade faculdade = users.getFaculdade();
            if (faculdade != null) {
                faculdade = em.getReference(faculdade.getClass(), faculdade.getIdFaculdade());
                users.setFaculdade(faculdade);
            }
            Funcionario idFuncionario = users.getIdFuncionario();
            if (idFuncionario != null) {
                idFuncionario = em.getReference(idFuncionario.getClass(), idFuncionario.getIdFuncionario());
                users.setIdFuncionario(idFuncionario);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollection = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimoToAttach : users.getSgEmprestimoCollection()) {
                sgEmprestimoCollectionSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollection.add(sgEmprestimoCollectionSgEmprestimoToAttach);
            }
            users.setSgEmprestimoCollection(attachedSgEmprestimoCollection);
            Collection<Usergrupo> attachedUsergrupoCollection = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoCollectionUsergrupoToAttach : users.getUsergrupoCollection()) {
                usergrupoCollectionUsergrupoToAttach = em.getReference(usergrupoCollectionUsergrupoToAttach.getClass(), usergrupoCollectionUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoCollection.add(usergrupoCollectionUsergrupoToAttach);
            }
            users.setUsergrupoCollection(attachedUsergrupoCollection);
            Collection<SgObra> attachedSgObraCollection = new ArrayList<SgObra>();
            for (SgObra sgObraCollectionSgObraToAttach : users.getSgObraCollection()) {
                sgObraCollectionSgObraToAttach = em.getReference(sgObraCollectionSgObraToAttach.getClass(), sgObraCollectionSgObraToAttach.getIdlivro());
                attachedSgObraCollection.add(sgObraCollectionSgObraToAttach);
            }
            users.setSgObraCollection(attachedSgObraCollection);
            Collection<BLeitor> attachedBLeitorCollection = new ArrayList<BLeitor>();
            for (BLeitor BLeitorCollectionBLeitorToAttach : users.getBLeitorCollection()) {
                BLeitorCollectionBLeitorToAttach = em.getReference(BLeitorCollectionBLeitorToAttach.getClass(), BLeitorCollectionBLeitorToAttach.getNrCartao());
                attachedBLeitorCollection.add(BLeitorCollectionBLeitorToAttach);
            }
            users.setBLeitorCollection(attachedBLeitorCollection);
            Collection<BLeitor> attachedBLeitorCollection1 = new ArrayList<BLeitor>();
            for (BLeitor BLeitorCollection1BLeitorToAttach : users.getBLeitorCollection1()) {
                BLeitorCollection1BLeitorToAttach = em.getReference(BLeitorCollection1BLeitorToAttach.getClass(), BLeitorCollection1BLeitorToAttach.getNrCartao());
                attachedBLeitorCollection1.add(BLeitorCollection1BLeitorToAttach);
            }
            users.setBLeitorCollection1(attachedBLeitorCollection1);
            Collection<SgExemplar> attachedSgExemplarCollection = new ArrayList<SgExemplar>();
            for (SgExemplar sgExemplarCollectionSgExemplarToAttach : users.getSgExemplarCollection()) {
                sgExemplarCollectionSgExemplarToAttach = em.getReference(sgExemplarCollectionSgExemplarToAttach.getClass(), sgExemplarCollectionSgExemplarToAttach.getNrRegisto());
                attachedSgExemplarCollection.add(sgExemplarCollectionSgExemplarToAttach);
            }
            users.setSgExemplarCollection(attachedSgExemplarCollection);
            Collection<SgEmprestimoParametros> attachedSgEmprestimoParametrosCollection = new ArrayList<SgEmprestimoParametros>();
            for (SgEmprestimoParametros sgEmprestimoParametrosCollectionSgEmprestimoParametrosToAttach : users.getSgEmprestimoParametrosCollection()) {
                sgEmprestimoParametrosCollectionSgEmprestimoParametrosToAttach = em.getReference(sgEmprestimoParametrosCollectionSgEmprestimoParametrosToAttach.getClass(), sgEmprestimoParametrosCollectionSgEmprestimoParametrosToAttach.getIdparametro());
                attachedSgEmprestimoParametrosCollection.add(sgEmprestimoParametrosCollectionSgEmprestimoParametrosToAttach);
            }
            users.setSgEmprestimoParametrosCollection(attachedSgEmprestimoParametrosCollection);
            em.persist(users);
            if (idEstudante != null) {
                idEstudante.getUsersCollection().add(users);
                idEstudante = em.merge(idEstudante);
            }
            if (faculdade != null) {
                faculdade.getUsersCollection().add(users);
                faculdade = em.merge(faculdade);
            }
            if (idFuncionario != null) {
                idFuncionario.getUsersCollection().add(users);
                idFuncionario = em.merge(idFuncionario);
            }
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : users.getSgEmprestimoCollection()) {
                Users oldAgenteBibliotOfSgEmprestimoCollectionSgEmprestimo = sgEmprestimoCollectionSgEmprestimo.getAgenteBibliot();
                sgEmprestimoCollectionSgEmprestimo.setAgenteBibliot(users);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
                if (oldAgenteBibliotOfSgEmprestimoCollectionSgEmprestimo != null) {
                    oldAgenteBibliotOfSgEmprestimoCollectionSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionSgEmprestimo);
                    oldAgenteBibliotOfSgEmprestimoCollectionSgEmprestimo = em.merge(oldAgenteBibliotOfSgEmprestimoCollectionSgEmprestimo);
                }
            }
            for (Usergrupo usergrupoCollectionUsergrupo : users.getUsergrupoCollection()) {
                Users oldUsersOfUsergrupoCollectionUsergrupo = usergrupoCollectionUsergrupo.getUsers();
                usergrupoCollectionUsergrupo.setUsers(users);
                usergrupoCollectionUsergrupo = em.merge(usergrupoCollectionUsergrupo);
                if (oldUsersOfUsergrupoCollectionUsergrupo != null) {
                    oldUsersOfUsergrupoCollectionUsergrupo.getUsergrupoCollection().remove(usergrupoCollectionUsergrupo);
                    oldUsersOfUsergrupoCollectionUsergrupo = em.merge(oldUsersOfUsergrupoCollectionUsergrupo);
                }
            }
            for (SgObra sgObraCollectionSgObra : users.getSgObraCollection()) {
                Users oldBibliotecarioOfSgObraCollectionSgObra = sgObraCollectionSgObra.getBibliotecario();
                sgObraCollectionSgObra.setBibliotecario(users);
                sgObraCollectionSgObra = em.merge(sgObraCollectionSgObra);
                if (oldBibliotecarioOfSgObraCollectionSgObra != null) {
                    oldBibliotecarioOfSgObraCollectionSgObra.getSgObraCollection().remove(sgObraCollectionSgObra);
                    oldBibliotecarioOfSgObraCollectionSgObra = em.merge(oldBibliotecarioOfSgObraCollectionSgObra);
                }
            }
            for (BLeitor BLeitorCollectionBLeitor : users.getBLeitorCollection()) {
                Users oldIdagenteOfBLeitorCollectionBLeitor = BLeitorCollectionBLeitor.getIdagente();
                BLeitorCollectionBLeitor.setIdagente(users);
                BLeitorCollectionBLeitor = em.merge(BLeitorCollectionBLeitor);
                if (oldIdagenteOfBLeitorCollectionBLeitor != null) {
                    oldIdagenteOfBLeitorCollectionBLeitor.getBLeitorCollection().remove(BLeitorCollectionBLeitor);
                    oldIdagenteOfBLeitorCollectionBLeitor = em.merge(oldIdagenteOfBLeitorCollectionBLeitor);
                }
            }
            for (BLeitor BLeitorCollection1BLeitor : users.getBLeitorCollection1()) {
                Users oldIdutilizadorOfBLeitorCollection1BLeitor = BLeitorCollection1BLeitor.getIdutilizador();
                BLeitorCollection1BLeitor.setIdutilizador(users);
                BLeitorCollection1BLeitor = em.merge(BLeitorCollection1BLeitor);
                if (oldIdutilizadorOfBLeitorCollection1BLeitor != null) {
                    oldIdutilizadorOfBLeitorCollection1BLeitor.getBLeitorCollection1().remove(BLeitorCollection1BLeitor);
                    oldIdutilizadorOfBLeitorCollection1BLeitor = em.merge(oldIdutilizadorOfBLeitorCollection1BLeitor);
                }
            }
            for (SgExemplar sgExemplarCollectionSgExemplar : users.getSgExemplarCollection()) {
                Users oldAgenteRegistoOfSgExemplarCollectionSgExemplar = sgExemplarCollectionSgExemplar.getAgenteRegisto();
                sgExemplarCollectionSgExemplar.setAgenteRegisto(users);
                sgExemplarCollectionSgExemplar = em.merge(sgExemplarCollectionSgExemplar);
                if (oldAgenteRegistoOfSgExemplarCollectionSgExemplar != null) {
                    oldAgenteRegistoOfSgExemplarCollectionSgExemplar.getSgExemplarCollection().remove(sgExemplarCollectionSgExemplar);
                    oldAgenteRegistoOfSgExemplarCollectionSgExemplar = em.merge(oldAgenteRegistoOfSgExemplarCollectionSgExemplar);
                }
            }
            for (SgEmprestimoParametros sgEmprestimoParametrosCollectionSgEmprestimoParametros : users.getSgEmprestimoParametrosCollection()) {
                Users oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionSgEmprestimoParametros = sgEmprestimoParametrosCollectionSgEmprestimoParametros.getAgenteBibliotecario();
                sgEmprestimoParametrosCollectionSgEmprestimoParametros.setAgenteBibliotecario(users);
                sgEmprestimoParametrosCollectionSgEmprestimoParametros = em.merge(sgEmprestimoParametrosCollectionSgEmprestimoParametros);
                if (oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionSgEmprestimoParametros != null) {
                    oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionSgEmprestimoParametros.getSgEmprestimoParametrosCollection().remove(sgEmprestimoParametrosCollectionSgEmprestimoParametros);
                    oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionSgEmprestimoParametros = em.merge(oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionSgEmprestimoParametros);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsers(users.getUtilizador()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUtilizador());
            Estudante idEstudanteOld = persistentUsers.getIdEstudante();
            Estudante idEstudanteNew = users.getIdEstudante();
            Faculdade faculdadeOld = persistentUsers.getFaculdade();
            Faculdade faculdadeNew = users.getFaculdade();
            Funcionario idFuncionarioOld = persistentUsers.getIdFuncionario();
            Funcionario idFuncionarioNew = users.getIdFuncionario();
            Collection<SgEmprestimo> sgEmprestimoCollectionOld = persistentUsers.getSgEmprestimoCollection();
            Collection<SgEmprestimo> sgEmprestimoCollectionNew = users.getSgEmprestimoCollection();
            Collection<Usergrupo> usergrupoCollectionOld = persistentUsers.getUsergrupoCollection();
            Collection<Usergrupo> usergrupoCollectionNew = users.getUsergrupoCollection();
            Collection<SgObra> sgObraCollectionOld = persistentUsers.getSgObraCollection();
            Collection<SgObra> sgObraCollectionNew = users.getSgObraCollection();
            Collection<BLeitor> BLeitorCollectionOld = persistentUsers.getBLeitorCollection();
            Collection<BLeitor> BLeitorCollectionNew = users.getBLeitorCollection();
            Collection<BLeitor> BLeitorCollection1Old = persistentUsers.getBLeitorCollection1();
            Collection<BLeitor> BLeitorCollection1New = users.getBLeitorCollection1();
            Collection<SgExemplar> sgExemplarCollectionOld = persistentUsers.getSgExemplarCollection();
            Collection<SgExemplar> sgExemplarCollectionNew = users.getSgExemplarCollection();
            Collection<SgEmprestimoParametros> sgEmprestimoParametrosCollectionOld = persistentUsers.getSgEmprestimoParametrosCollection();
            Collection<SgEmprestimoParametros> sgEmprestimoParametrosCollectionNew = users.getSgEmprestimoParametrosCollection();
            List<String> illegalOrphanMessages = null;
            for (Usergrupo usergrupoCollectionOldUsergrupo : usergrupoCollectionOld) {
                if (!usergrupoCollectionNew.contains(usergrupoCollectionOldUsergrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usergrupo " + usergrupoCollectionOldUsergrupo + " since its users field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEstudanteNew != null) {
                idEstudanteNew = em.getReference(idEstudanteNew.getClass(), idEstudanteNew.getIdEstudante());
                users.setIdEstudante(idEstudanteNew);
            }
            if (faculdadeNew != null) {
                faculdadeNew = em.getReference(faculdadeNew.getClass(), faculdadeNew.getIdFaculdade());
                users.setFaculdade(faculdadeNew);
            }
            if (idFuncionarioNew != null) {
                idFuncionarioNew = em.getReference(idFuncionarioNew.getClass(), idFuncionarioNew.getIdFuncionario());
                users.setIdFuncionario(idFuncionarioNew);
            }
            Collection<SgEmprestimo> attachedSgEmprestimoCollectionNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimoToAttach : sgEmprestimoCollectionNew) {
                sgEmprestimoCollectionNewSgEmprestimoToAttach = em.getReference(sgEmprestimoCollectionNewSgEmprestimoToAttach.getClass(), sgEmprestimoCollectionNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoCollectionNew.add(sgEmprestimoCollectionNewSgEmprestimoToAttach);
            }
            sgEmprestimoCollectionNew = attachedSgEmprestimoCollectionNew;
            users.setSgEmprestimoCollection(sgEmprestimoCollectionNew);
            Collection<Usergrupo> attachedUsergrupoCollectionNew = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoCollectionNewUsergrupoToAttach : usergrupoCollectionNew) {
                usergrupoCollectionNewUsergrupoToAttach = em.getReference(usergrupoCollectionNewUsergrupoToAttach.getClass(), usergrupoCollectionNewUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoCollectionNew.add(usergrupoCollectionNewUsergrupoToAttach);
            }
            usergrupoCollectionNew = attachedUsergrupoCollectionNew;
            users.setUsergrupoCollection(usergrupoCollectionNew);
            Collection<SgObra> attachedSgObraCollectionNew = new ArrayList<SgObra>();
            for (SgObra sgObraCollectionNewSgObraToAttach : sgObraCollectionNew) {
                sgObraCollectionNewSgObraToAttach = em.getReference(sgObraCollectionNewSgObraToAttach.getClass(), sgObraCollectionNewSgObraToAttach.getIdlivro());
                attachedSgObraCollectionNew.add(sgObraCollectionNewSgObraToAttach);
            }
            sgObraCollectionNew = attachedSgObraCollectionNew;
            users.setSgObraCollection(sgObraCollectionNew);
            Collection<BLeitor> attachedBLeitorCollectionNew = new ArrayList<BLeitor>();
            for (BLeitor BLeitorCollectionNewBLeitorToAttach : BLeitorCollectionNew) {
                BLeitorCollectionNewBLeitorToAttach = em.getReference(BLeitorCollectionNewBLeitorToAttach.getClass(), BLeitorCollectionNewBLeitorToAttach.getNrCartao());
                attachedBLeitorCollectionNew.add(BLeitorCollectionNewBLeitorToAttach);
            }
            BLeitorCollectionNew = attachedBLeitorCollectionNew;
            users.setBLeitorCollection(BLeitorCollectionNew);
            Collection<BLeitor> attachedBLeitorCollection1New = new ArrayList<BLeitor>();
            for (BLeitor BLeitorCollection1NewBLeitorToAttach : BLeitorCollection1New) {
                BLeitorCollection1NewBLeitorToAttach = em.getReference(BLeitorCollection1NewBLeitorToAttach.getClass(), BLeitorCollection1NewBLeitorToAttach.getNrCartao());
                attachedBLeitorCollection1New.add(BLeitorCollection1NewBLeitorToAttach);
            }
            BLeitorCollection1New = attachedBLeitorCollection1New;
            users.setBLeitorCollection1(BLeitorCollection1New);
            Collection<SgExemplar> attachedSgExemplarCollectionNew = new ArrayList<SgExemplar>();
            for (SgExemplar sgExemplarCollectionNewSgExemplarToAttach : sgExemplarCollectionNew) {
                sgExemplarCollectionNewSgExemplarToAttach = em.getReference(sgExemplarCollectionNewSgExemplarToAttach.getClass(), sgExemplarCollectionNewSgExemplarToAttach.getNrRegisto());
                attachedSgExemplarCollectionNew.add(sgExemplarCollectionNewSgExemplarToAttach);
            }
            sgExemplarCollectionNew = attachedSgExemplarCollectionNew;
            users.setSgExemplarCollection(sgExemplarCollectionNew);
            Collection<SgEmprestimoParametros> attachedSgEmprestimoParametrosCollectionNew = new ArrayList<SgEmprestimoParametros>();
            for (SgEmprestimoParametros sgEmprestimoParametrosCollectionNewSgEmprestimoParametrosToAttach : sgEmprestimoParametrosCollectionNew) {
                sgEmprestimoParametrosCollectionNewSgEmprestimoParametrosToAttach = em.getReference(sgEmprestimoParametrosCollectionNewSgEmprestimoParametrosToAttach.getClass(), sgEmprestimoParametrosCollectionNewSgEmprestimoParametrosToAttach.getIdparametro());
                attachedSgEmprestimoParametrosCollectionNew.add(sgEmprestimoParametrosCollectionNewSgEmprestimoParametrosToAttach);
            }
            sgEmprestimoParametrosCollectionNew = attachedSgEmprestimoParametrosCollectionNew;
            users.setSgEmprestimoParametrosCollection(sgEmprestimoParametrosCollectionNew);
            users = em.merge(users);
            if (idEstudanteOld != null && !idEstudanteOld.equals(idEstudanteNew)) {
                idEstudanteOld.getUsersCollection().remove(users);
                idEstudanteOld = em.merge(idEstudanteOld);
            }
            if (idEstudanteNew != null && !idEstudanteNew.equals(idEstudanteOld)) {
                idEstudanteNew.getUsersCollection().add(users);
                idEstudanteNew = em.merge(idEstudanteNew);
            }
            if (faculdadeOld != null && !faculdadeOld.equals(faculdadeNew)) {
                faculdadeOld.getUsersCollection().remove(users);
                faculdadeOld = em.merge(faculdadeOld);
            }
            if (faculdadeNew != null && !faculdadeNew.equals(faculdadeOld)) {
                faculdadeNew.getUsersCollection().add(users);
                faculdadeNew = em.merge(faculdadeNew);
            }
            if (idFuncionarioOld != null && !idFuncionarioOld.equals(idFuncionarioNew)) {
                idFuncionarioOld.getUsersCollection().remove(users);
                idFuncionarioOld = em.merge(idFuncionarioOld);
            }
            if (idFuncionarioNew != null && !idFuncionarioNew.equals(idFuncionarioOld)) {
                idFuncionarioNew.getUsersCollection().add(users);
                idFuncionarioNew = em.merge(idFuncionarioNew);
            }
            for (SgEmprestimo sgEmprestimoCollectionOldSgEmprestimo : sgEmprestimoCollectionOld) {
                if (!sgEmprestimoCollectionNew.contains(sgEmprestimoCollectionOldSgEmprestimo)) {
                    sgEmprestimoCollectionOldSgEmprestimo.setAgenteBibliot(null);
                    sgEmprestimoCollectionOldSgEmprestimo = em.merge(sgEmprestimoCollectionOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoCollectionNewSgEmprestimo : sgEmprestimoCollectionNew) {
                if (!sgEmprestimoCollectionOld.contains(sgEmprestimoCollectionNewSgEmprestimo)) {
                    Users oldAgenteBibliotOfSgEmprestimoCollectionNewSgEmprestimo = sgEmprestimoCollectionNewSgEmprestimo.getAgenteBibliot();
                    sgEmprestimoCollectionNewSgEmprestimo.setAgenteBibliot(users);
                    sgEmprestimoCollectionNewSgEmprestimo = em.merge(sgEmprestimoCollectionNewSgEmprestimo);
                    if (oldAgenteBibliotOfSgEmprestimoCollectionNewSgEmprestimo != null && !oldAgenteBibliotOfSgEmprestimoCollectionNewSgEmprestimo.equals(users)) {
                        oldAgenteBibliotOfSgEmprestimoCollectionNewSgEmprestimo.getSgEmprestimoCollection().remove(sgEmprestimoCollectionNewSgEmprestimo);
                        oldAgenteBibliotOfSgEmprestimoCollectionNewSgEmprestimo = em.merge(oldAgenteBibliotOfSgEmprestimoCollectionNewSgEmprestimo);
                    }
                }
            }
            for (Usergrupo usergrupoCollectionNewUsergrupo : usergrupoCollectionNew) {
                if (!usergrupoCollectionOld.contains(usergrupoCollectionNewUsergrupo)) {
                    Users oldUsersOfUsergrupoCollectionNewUsergrupo = usergrupoCollectionNewUsergrupo.getUsers();
                    usergrupoCollectionNewUsergrupo.setUsers(users);
                    usergrupoCollectionNewUsergrupo = em.merge(usergrupoCollectionNewUsergrupo);
                    if (oldUsersOfUsergrupoCollectionNewUsergrupo != null && !oldUsersOfUsergrupoCollectionNewUsergrupo.equals(users)) {
                        oldUsersOfUsergrupoCollectionNewUsergrupo.getUsergrupoCollection().remove(usergrupoCollectionNewUsergrupo);
                        oldUsersOfUsergrupoCollectionNewUsergrupo = em.merge(oldUsersOfUsergrupoCollectionNewUsergrupo);
                    }
                }
            }
            for (SgObra sgObraCollectionOldSgObra : sgObraCollectionOld) {
                if (!sgObraCollectionNew.contains(sgObraCollectionOldSgObra)) {
                    sgObraCollectionOldSgObra.setBibliotecario(null);
                    sgObraCollectionOldSgObra = em.merge(sgObraCollectionOldSgObra);
                }
            }
            for (SgObra sgObraCollectionNewSgObra : sgObraCollectionNew) {
                if (!sgObraCollectionOld.contains(sgObraCollectionNewSgObra)) {
                    Users oldBibliotecarioOfSgObraCollectionNewSgObra = sgObraCollectionNewSgObra.getBibliotecario();
                    sgObraCollectionNewSgObra.setBibliotecario(users);
                    sgObraCollectionNewSgObra = em.merge(sgObraCollectionNewSgObra);
                    if (oldBibliotecarioOfSgObraCollectionNewSgObra != null && !oldBibliotecarioOfSgObraCollectionNewSgObra.equals(users)) {
                        oldBibliotecarioOfSgObraCollectionNewSgObra.getSgObraCollection().remove(sgObraCollectionNewSgObra);
                        oldBibliotecarioOfSgObraCollectionNewSgObra = em.merge(oldBibliotecarioOfSgObraCollectionNewSgObra);
                    }
                }
            }
            for (BLeitor BLeitorCollectionOldBLeitor : BLeitorCollectionOld) {
                if (!BLeitorCollectionNew.contains(BLeitorCollectionOldBLeitor)) {
                    BLeitorCollectionOldBLeitor.setIdagente(null);
                    BLeitorCollectionOldBLeitor = em.merge(BLeitorCollectionOldBLeitor);
                }
            }
            for (BLeitor BLeitorCollectionNewBLeitor : BLeitorCollectionNew) {
                if (!BLeitorCollectionOld.contains(BLeitorCollectionNewBLeitor)) {
                    Users oldIdagenteOfBLeitorCollectionNewBLeitor = BLeitorCollectionNewBLeitor.getIdagente();
                    BLeitorCollectionNewBLeitor.setIdagente(users);
                    BLeitorCollectionNewBLeitor = em.merge(BLeitorCollectionNewBLeitor);
                    if (oldIdagenteOfBLeitorCollectionNewBLeitor != null && !oldIdagenteOfBLeitorCollectionNewBLeitor.equals(users)) {
                        oldIdagenteOfBLeitorCollectionNewBLeitor.getBLeitorCollection().remove(BLeitorCollectionNewBLeitor);
                        oldIdagenteOfBLeitorCollectionNewBLeitor = em.merge(oldIdagenteOfBLeitorCollectionNewBLeitor);
                    }
                }
            }
            for (BLeitor BLeitorCollection1OldBLeitor : BLeitorCollection1Old) {
                if (!BLeitorCollection1New.contains(BLeitorCollection1OldBLeitor)) {
                    BLeitorCollection1OldBLeitor.setIdutilizador(null);
                    BLeitorCollection1OldBLeitor = em.merge(BLeitorCollection1OldBLeitor);
                }
            }
            for (BLeitor BLeitorCollection1NewBLeitor : BLeitorCollection1New) {
                if (!BLeitorCollection1Old.contains(BLeitorCollection1NewBLeitor)) {
                    Users oldIdutilizadorOfBLeitorCollection1NewBLeitor = BLeitorCollection1NewBLeitor.getIdutilizador();
                    BLeitorCollection1NewBLeitor.setIdutilizador(users);
                    BLeitorCollection1NewBLeitor = em.merge(BLeitorCollection1NewBLeitor);
                    if (oldIdutilizadorOfBLeitorCollection1NewBLeitor != null && !oldIdutilizadorOfBLeitorCollection1NewBLeitor.equals(users)) {
                        oldIdutilizadorOfBLeitorCollection1NewBLeitor.getBLeitorCollection1().remove(BLeitorCollection1NewBLeitor);
                        oldIdutilizadorOfBLeitorCollection1NewBLeitor = em.merge(oldIdutilizadorOfBLeitorCollection1NewBLeitor);
                    }
                }
            }
            for (SgExemplar sgExemplarCollectionOldSgExemplar : sgExemplarCollectionOld) {
                if (!sgExemplarCollectionNew.contains(sgExemplarCollectionOldSgExemplar)) {
                    sgExemplarCollectionOldSgExemplar.setAgenteRegisto(null);
                    sgExemplarCollectionOldSgExemplar = em.merge(sgExemplarCollectionOldSgExemplar);
                }
            }
            for (SgExemplar sgExemplarCollectionNewSgExemplar : sgExemplarCollectionNew) {
                if (!sgExemplarCollectionOld.contains(sgExemplarCollectionNewSgExemplar)) {
                    Users oldAgenteRegistoOfSgExemplarCollectionNewSgExemplar = sgExemplarCollectionNewSgExemplar.getAgenteRegisto();
                    sgExemplarCollectionNewSgExemplar.setAgenteRegisto(users);
                    sgExemplarCollectionNewSgExemplar = em.merge(sgExemplarCollectionNewSgExemplar);
                    if (oldAgenteRegistoOfSgExemplarCollectionNewSgExemplar != null && !oldAgenteRegistoOfSgExemplarCollectionNewSgExemplar.equals(users)) {
                        oldAgenteRegistoOfSgExemplarCollectionNewSgExemplar.getSgExemplarCollection().remove(sgExemplarCollectionNewSgExemplar);
                        oldAgenteRegistoOfSgExemplarCollectionNewSgExemplar = em.merge(oldAgenteRegistoOfSgExemplarCollectionNewSgExemplar);
                    }
                }
            }
            for (SgEmprestimoParametros sgEmprestimoParametrosCollectionOldSgEmprestimoParametros : sgEmprestimoParametrosCollectionOld) {
                if (!sgEmprestimoParametrosCollectionNew.contains(sgEmprestimoParametrosCollectionOldSgEmprestimoParametros)) {
                    sgEmprestimoParametrosCollectionOldSgEmprestimoParametros.setAgenteBibliotecario(null);
                    sgEmprestimoParametrosCollectionOldSgEmprestimoParametros = em.merge(sgEmprestimoParametrosCollectionOldSgEmprestimoParametros);
                }
            }
            for (SgEmprestimoParametros sgEmprestimoParametrosCollectionNewSgEmprestimoParametros : sgEmprestimoParametrosCollectionNew) {
                if (!sgEmprestimoParametrosCollectionOld.contains(sgEmprestimoParametrosCollectionNewSgEmprestimoParametros)) {
                    Users oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionNewSgEmprestimoParametros = sgEmprestimoParametrosCollectionNewSgEmprestimoParametros.getAgenteBibliotecario();
                    sgEmprestimoParametrosCollectionNewSgEmprestimoParametros.setAgenteBibliotecario(users);
                    sgEmprestimoParametrosCollectionNewSgEmprestimoParametros = em.merge(sgEmprestimoParametrosCollectionNewSgEmprestimoParametros);
                    if (oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionNewSgEmprestimoParametros != null && !oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionNewSgEmprestimoParametros.equals(users)) {
                        oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionNewSgEmprestimoParametros.getSgEmprestimoParametrosCollection().remove(sgEmprestimoParametrosCollectionNewSgEmprestimoParametros);
                        oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionNewSgEmprestimoParametros = em.merge(oldAgenteBibliotecarioOfSgEmprestimoParametrosCollectionNewSgEmprestimoParametros);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = users.getUtilizador();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUtilizador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Usergrupo> usergrupoCollectionOrphanCheck = users.getUsergrupoCollection();
            for (Usergrupo usergrupoCollectionOrphanCheckUsergrupo : usergrupoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Usergrupo " + usergrupoCollectionOrphanCheckUsergrupo + " in its usergrupoCollection field has a non-nullable users field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estudante idEstudante = users.getIdEstudante();
            if (idEstudante != null) {
                idEstudante.getUsersCollection().remove(users);
                idEstudante = em.merge(idEstudante);
            }
            Faculdade faculdade = users.getFaculdade();
            if (faculdade != null) {
                faculdade.getUsersCollection().remove(users);
                faculdade = em.merge(faculdade);
            }
            Funcionario idFuncionario = users.getIdFuncionario();
            if (idFuncionario != null) {
                idFuncionario.getUsersCollection().remove(users);
                idFuncionario = em.merge(idFuncionario);
            }
            Collection<SgEmprestimo> sgEmprestimoCollection = users.getSgEmprestimoCollection();
            for (SgEmprestimo sgEmprestimoCollectionSgEmprestimo : sgEmprestimoCollection) {
                sgEmprestimoCollectionSgEmprestimo.setAgenteBibliot(null);
                sgEmprestimoCollectionSgEmprestimo = em.merge(sgEmprestimoCollectionSgEmprestimo);
            }
            Collection<SgObra> sgObraCollection = users.getSgObraCollection();
            for (SgObra sgObraCollectionSgObra : sgObraCollection) {
                sgObraCollectionSgObra.setBibliotecario(null);
                sgObraCollectionSgObra = em.merge(sgObraCollectionSgObra);
            }
            Collection<BLeitor> BLeitorCollection = users.getBLeitorCollection();
            for (BLeitor BLeitorCollectionBLeitor : BLeitorCollection) {
                BLeitorCollectionBLeitor.setIdagente(null);
                BLeitorCollectionBLeitor = em.merge(BLeitorCollectionBLeitor);
            }
            Collection<BLeitor> BLeitorCollection1 = users.getBLeitorCollection1();
            for (BLeitor BLeitorCollection1BLeitor : BLeitorCollection1) {
                BLeitorCollection1BLeitor.setIdutilizador(null);
                BLeitorCollection1BLeitor = em.merge(BLeitorCollection1BLeitor);
            }
            Collection<SgExemplar> sgExemplarCollection = users.getSgExemplarCollection();
            for (SgExemplar sgExemplarCollectionSgExemplar : sgExemplarCollection) {
                sgExemplarCollectionSgExemplar.setAgenteRegisto(null);
                sgExemplarCollectionSgExemplar = em.merge(sgExemplarCollectionSgExemplar);
            }
            Collection<SgEmprestimoParametros> sgEmprestimoParametrosCollection = users.getSgEmprestimoParametrosCollection();
            for (SgEmprestimoParametros sgEmprestimoParametrosCollectionSgEmprestimoParametros : sgEmprestimoParametrosCollection) {
                sgEmprestimoParametrosCollectionSgEmprestimoParametros.setAgenteBibliotecario(null);
                sgEmprestimoParametrosCollectionSgEmprestimoParametros = em.merge(sgEmprestimoParametrosCollectionSgEmprestimoParametros);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
