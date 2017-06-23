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
import java.util.List;
import entidades.Usergrupo;
import entidades.SgObra;
import entidades.BLeitor;
import entidades.SgExemplar;
import entidades.SgEmprestimoParametros;
import entidades.Users;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
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
        if (users.getSgEmprestimoList() == null) {
            users.setSgEmprestimoList(new ArrayList<SgEmprestimo>());
        }
        if (users.getUsergrupoList() == null) {
            users.setUsergrupoList(new ArrayList<Usergrupo>());
        }
        if (users.getSgObraList() == null) {
            users.setSgObraList(new ArrayList<SgObra>());
        }
        if (users.getBLeitorList() == null) {
            users.setBLeitorList(new ArrayList<BLeitor>());
        }
        if (users.getBLeitorList1() == null) {
            users.setBLeitorList1(new ArrayList<BLeitor>());
        }
        if (users.getSgExemplarList() == null) {
            users.setSgExemplarList(new ArrayList<SgExemplar>());
        }
        if (users.getSgEmprestimoParametrosList() == null) {
            users.setSgEmprestimoParametrosList(new ArrayList<SgEmprestimoParametros>());
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
            List<SgEmprestimo> attachedSgEmprestimoList = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListSgEmprestimoToAttach : users.getSgEmprestimoList()) {
                sgEmprestimoListSgEmprestimoToAttach = em.getReference(sgEmprestimoListSgEmprestimoToAttach.getClass(), sgEmprestimoListSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoList.add(sgEmprestimoListSgEmprestimoToAttach);
            }
            users.setSgEmprestimoList(attachedSgEmprestimoList);
            List<Usergrupo> attachedUsergrupoList = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoListUsergrupoToAttach : users.getUsergrupoList()) {
                usergrupoListUsergrupoToAttach = em.getReference(usergrupoListUsergrupoToAttach.getClass(), usergrupoListUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoList.add(usergrupoListUsergrupoToAttach);
            }
            users.setUsergrupoList(attachedUsergrupoList);
            List<SgObra> attachedSgObraList = new ArrayList<SgObra>();
            for (SgObra sgObraListSgObraToAttach : users.getSgObraList()) {
                sgObraListSgObraToAttach = em.getReference(sgObraListSgObraToAttach.getClass(), sgObraListSgObraToAttach.getIdlivro());
                attachedSgObraList.add(sgObraListSgObraToAttach);
            }
            users.setSgObraList(attachedSgObraList);
            List<BLeitor> attachedBLeitorList = new ArrayList<BLeitor>();
            for (BLeitor BLeitorListBLeitorToAttach : users.getBLeitorList()) {
                BLeitorListBLeitorToAttach = em.getReference(BLeitorListBLeitorToAttach.getClass(), BLeitorListBLeitorToAttach.getNrCartao());
                attachedBLeitorList.add(BLeitorListBLeitorToAttach);
            }
            users.setBLeitorList(attachedBLeitorList);
            List<BLeitor> attachedBLeitorList1 = new ArrayList<BLeitor>();
            for (BLeitor BLeitorList1BLeitorToAttach : users.getBLeitorList1()) {
                BLeitorList1BLeitorToAttach = em.getReference(BLeitorList1BLeitorToAttach.getClass(), BLeitorList1BLeitorToAttach.getNrCartao());
                attachedBLeitorList1.add(BLeitorList1BLeitorToAttach);
            }
            users.setBLeitorList1(attachedBLeitorList1);
            List<SgExemplar> attachedSgExemplarList = new ArrayList<SgExemplar>();
            for (SgExemplar sgExemplarListSgExemplarToAttach : users.getSgExemplarList()) {
                sgExemplarListSgExemplarToAttach = em.getReference(sgExemplarListSgExemplarToAttach.getClass(), sgExemplarListSgExemplarToAttach.getNrRegisto());
                attachedSgExemplarList.add(sgExemplarListSgExemplarToAttach);
            }
            users.setSgExemplarList(attachedSgExemplarList);
            List<SgEmprestimoParametros> attachedSgEmprestimoParametrosList = new ArrayList<SgEmprestimoParametros>();
            for (SgEmprestimoParametros sgEmprestimoParametrosListSgEmprestimoParametrosToAttach : users.getSgEmprestimoParametrosList()) {
                sgEmprestimoParametrosListSgEmprestimoParametrosToAttach = em.getReference(sgEmprestimoParametrosListSgEmprestimoParametrosToAttach.getClass(), sgEmprestimoParametrosListSgEmprestimoParametrosToAttach.getIdparametro());
                attachedSgEmprestimoParametrosList.add(sgEmprestimoParametrosListSgEmprestimoParametrosToAttach);
            }
            users.setSgEmprestimoParametrosList(attachedSgEmprestimoParametrosList);
            em.persist(users);
            if (idEstudante != null) {
                idEstudante.getUsersList().add(users);
                idEstudante = em.merge(idEstudante);
            }
            if (faculdade != null) {
                faculdade.getUsersList().add(users);
                faculdade = em.merge(faculdade);
            }
            if (idFuncionario != null) {
                idFuncionario.getUsersList().add(users);
                idFuncionario = em.merge(idFuncionario);
            }
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : users.getSgEmprestimoList()) {
                Users oldAgenteBibliotOfSgEmprestimoListSgEmprestimo = sgEmprestimoListSgEmprestimo.getAgenteBibliot();
                sgEmprestimoListSgEmprestimo.setAgenteBibliot(users);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
                if (oldAgenteBibliotOfSgEmprestimoListSgEmprestimo != null) {
                    oldAgenteBibliotOfSgEmprestimoListSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListSgEmprestimo);
                    oldAgenteBibliotOfSgEmprestimoListSgEmprestimo = em.merge(oldAgenteBibliotOfSgEmprestimoListSgEmprestimo);
                }
            }
            for (Usergrupo usergrupoListUsergrupo : users.getUsergrupoList()) {
                Users oldUsersOfUsergrupoListUsergrupo = usergrupoListUsergrupo.getUsers();
                usergrupoListUsergrupo.setUsers(users);
                usergrupoListUsergrupo = em.merge(usergrupoListUsergrupo);
                if (oldUsersOfUsergrupoListUsergrupo != null) {
                    oldUsersOfUsergrupoListUsergrupo.getUsergrupoList().remove(usergrupoListUsergrupo);
                    oldUsersOfUsergrupoListUsergrupo = em.merge(oldUsersOfUsergrupoListUsergrupo);
                }
            }
            for (SgObra sgObraListSgObra : users.getSgObraList()) {
                Users oldBibliotecarioOfSgObraListSgObra = sgObraListSgObra.getBibliotecario();
                sgObraListSgObra.setBibliotecario(users);
                sgObraListSgObra = em.merge(sgObraListSgObra);
                if (oldBibliotecarioOfSgObraListSgObra != null) {
                    oldBibliotecarioOfSgObraListSgObra.getSgObraList().remove(sgObraListSgObra);
                    oldBibliotecarioOfSgObraListSgObra = em.merge(oldBibliotecarioOfSgObraListSgObra);
                }
            }
            for (BLeitor BLeitorListBLeitor : users.getBLeitorList()) {
                Users oldIdutilizadorOfBLeitorListBLeitor = BLeitorListBLeitor.getIdutilizador();
                BLeitorListBLeitor.setIdutilizador(users);
                BLeitorListBLeitor = em.merge(BLeitorListBLeitor);
                if (oldIdutilizadorOfBLeitorListBLeitor != null) {
                    oldIdutilizadorOfBLeitorListBLeitor.getBLeitorList().remove(BLeitorListBLeitor);
                    oldIdutilizadorOfBLeitorListBLeitor = em.merge(oldIdutilizadorOfBLeitorListBLeitor);
                }
            }
            for (BLeitor BLeitorList1BLeitor : users.getBLeitorList1()) {
                Users oldIdagenteOfBLeitorList1BLeitor = BLeitorList1BLeitor.getIdagente();
                BLeitorList1BLeitor.setIdagente(users);
                BLeitorList1BLeitor = em.merge(BLeitorList1BLeitor);
                if (oldIdagenteOfBLeitorList1BLeitor != null) {
                    oldIdagenteOfBLeitorList1BLeitor.getBLeitorList1().remove(BLeitorList1BLeitor);
                    oldIdagenteOfBLeitorList1BLeitor = em.merge(oldIdagenteOfBLeitorList1BLeitor);
                }
            }
            for (SgExemplar sgExemplarListSgExemplar : users.getSgExemplarList()) {
                Users oldAgenteRegistoOfSgExemplarListSgExemplar = sgExemplarListSgExemplar.getAgenteRegisto();
                sgExemplarListSgExemplar.setAgenteRegisto(users);
                sgExemplarListSgExemplar = em.merge(sgExemplarListSgExemplar);
                if (oldAgenteRegistoOfSgExemplarListSgExemplar != null) {
                    oldAgenteRegistoOfSgExemplarListSgExemplar.getSgExemplarList().remove(sgExemplarListSgExemplar);
                    oldAgenteRegistoOfSgExemplarListSgExemplar = em.merge(oldAgenteRegistoOfSgExemplarListSgExemplar);
                }
            }
            for (SgEmprestimoParametros sgEmprestimoParametrosListSgEmprestimoParametros : users.getSgEmprestimoParametrosList()) {
                Users oldAgenteBibliotecarioOfSgEmprestimoParametrosListSgEmprestimoParametros = sgEmprestimoParametrosListSgEmprestimoParametros.getAgenteBibliotecario();
                sgEmprestimoParametrosListSgEmprestimoParametros.setAgenteBibliotecario(users);
                sgEmprestimoParametrosListSgEmprestimoParametros = em.merge(sgEmprestimoParametrosListSgEmprestimoParametros);
                if (oldAgenteBibliotecarioOfSgEmprestimoParametrosListSgEmprestimoParametros != null) {
                    oldAgenteBibliotecarioOfSgEmprestimoParametrosListSgEmprestimoParametros.getSgEmprestimoParametrosList().remove(sgEmprestimoParametrosListSgEmprestimoParametros);
                    oldAgenteBibliotecarioOfSgEmprestimoParametrosListSgEmprestimoParametros = em.merge(oldAgenteBibliotecarioOfSgEmprestimoParametrosListSgEmprestimoParametros);
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
            List<SgEmprestimo> sgEmprestimoListOld = persistentUsers.getSgEmprestimoList();
            List<SgEmprestimo> sgEmprestimoListNew = users.getSgEmprestimoList();
            List<Usergrupo> usergrupoListOld = persistentUsers.getUsergrupoList();
            List<Usergrupo> usergrupoListNew = users.getUsergrupoList();
            List<SgObra> sgObraListOld = persistentUsers.getSgObraList();
            List<SgObra> sgObraListNew = users.getSgObraList();
            List<BLeitor> BLeitorListOld = persistentUsers.getBLeitorList();
            List<BLeitor> BLeitorListNew = users.getBLeitorList();
            List<BLeitor> BLeitorList1Old = persistentUsers.getBLeitorList1();
            List<BLeitor> BLeitorList1New = users.getBLeitorList1();
            List<SgExemplar> sgExemplarListOld = persistentUsers.getSgExemplarList();
            List<SgExemplar> sgExemplarListNew = users.getSgExemplarList();
            List<SgEmprestimoParametros> sgEmprestimoParametrosListOld = persistentUsers.getSgEmprestimoParametrosList();
            List<SgEmprestimoParametros> sgEmprestimoParametrosListNew = users.getSgEmprestimoParametrosList();
            List<String> illegalOrphanMessages = null;
            for (Usergrupo usergrupoListOldUsergrupo : usergrupoListOld) {
                if (!usergrupoListNew.contains(usergrupoListOldUsergrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usergrupo " + usergrupoListOldUsergrupo + " since its users field is not nullable.");
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
            List<SgEmprestimo> attachedSgEmprestimoListNew = new ArrayList<SgEmprestimo>();
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimoToAttach : sgEmprestimoListNew) {
                sgEmprestimoListNewSgEmprestimoToAttach = em.getReference(sgEmprestimoListNewSgEmprestimoToAttach.getClass(), sgEmprestimoListNewSgEmprestimoToAttach.getIdemprestimo());
                attachedSgEmprestimoListNew.add(sgEmprestimoListNewSgEmprestimoToAttach);
            }
            sgEmprestimoListNew = attachedSgEmprestimoListNew;
            users.setSgEmprestimoList(sgEmprestimoListNew);
            List<Usergrupo> attachedUsergrupoListNew = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoListNewUsergrupoToAttach : usergrupoListNew) {
                usergrupoListNewUsergrupoToAttach = em.getReference(usergrupoListNewUsergrupoToAttach.getClass(), usergrupoListNewUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoListNew.add(usergrupoListNewUsergrupoToAttach);
            }
            usergrupoListNew = attachedUsergrupoListNew;
            users.setUsergrupoList(usergrupoListNew);
            List<SgObra> attachedSgObraListNew = new ArrayList<SgObra>();
            for (SgObra sgObraListNewSgObraToAttach : sgObraListNew) {
                sgObraListNewSgObraToAttach = em.getReference(sgObraListNewSgObraToAttach.getClass(), sgObraListNewSgObraToAttach.getIdlivro());
                attachedSgObraListNew.add(sgObraListNewSgObraToAttach);
            }
            sgObraListNew = attachedSgObraListNew;
            users.setSgObraList(sgObraListNew);
            List<BLeitor> attachedBLeitorListNew = new ArrayList<BLeitor>();
            for (BLeitor BLeitorListNewBLeitorToAttach : BLeitorListNew) {
                BLeitorListNewBLeitorToAttach = em.getReference(BLeitorListNewBLeitorToAttach.getClass(), BLeitorListNewBLeitorToAttach.getNrCartao());
                attachedBLeitorListNew.add(BLeitorListNewBLeitorToAttach);
            }
            BLeitorListNew = attachedBLeitorListNew;
            users.setBLeitorList(BLeitorListNew);
            List<BLeitor> attachedBLeitorList1New = new ArrayList<BLeitor>();
            for (BLeitor BLeitorList1NewBLeitorToAttach : BLeitorList1New) {
                BLeitorList1NewBLeitorToAttach = em.getReference(BLeitorList1NewBLeitorToAttach.getClass(), BLeitorList1NewBLeitorToAttach.getNrCartao());
                attachedBLeitorList1New.add(BLeitorList1NewBLeitorToAttach);
            }
            BLeitorList1New = attachedBLeitorList1New;
            users.setBLeitorList1(BLeitorList1New);
            List<SgExemplar> attachedSgExemplarListNew = new ArrayList<SgExemplar>();
            for (SgExemplar sgExemplarListNewSgExemplarToAttach : sgExemplarListNew) {
                sgExemplarListNewSgExemplarToAttach = em.getReference(sgExemplarListNewSgExemplarToAttach.getClass(), sgExemplarListNewSgExemplarToAttach.getNrRegisto());
                attachedSgExemplarListNew.add(sgExemplarListNewSgExemplarToAttach);
            }
            sgExemplarListNew = attachedSgExemplarListNew;
            users.setSgExemplarList(sgExemplarListNew);
            List<SgEmprestimoParametros> attachedSgEmprestimoParametrosListNew = new ArrayList<SgEmprestimoParametros>();
            for (SgEmprestimoParametros sgEmprestimoParametrosListNewSgEmprestimoParametrosToAttach : sgEmprestimoParametrosListNew) {
                sgEmprestimoParametrosListNewSgEmprestimoParametrosToAttach = em.getReference(sgEmprestimoParametrosListNewSgEmprestimoParametrosToAttach.getClass(), sgEmprestimoParametrosListNewSgEmprestimoParametrosToAttach.getIdparametro());
                attachedSgEmprestimoParametrosListNew.add(sgEmprestimoParametrosListNewSgEmprestimoParametrosToAttach);
            }
            sgEmprestimoParametrosListNew = attachedSgEmprestimoParametrosListNew;
            users.setSgEmprestimoParametrosList(sgEmprestimoParametrosListNew);
            users = em.merge(users);
            if (idEstudanteOld != null && !idEstudanteOld.equals(idEstudanteNew)) {
                idEstudanteOld.getUsersList().remove(users);
                idEstudanteOld = em.merge(idEstudanteOld);
            }
            if (idEstudanteNew != null && !idEstudanteNew.equals(idEstudanteOld)) {
                idEstudanteNew.getUsersList().add(users);
                idEstudanteNew = em.merge(idEstudanteNew);
            }
            if (faculdadeOld != null && !faculdadeOld.equals(faculdadeNew)) {
                faculdadeOld.getUsersList().remove(users);
                faculdadeOld = em.merge(faculdadeOld);
            }
            if (faculdadeNew != null && !faculdadeNew.equals(faculdadeOld)) {
                faculdadeNew.getUsersList().add(users);
                faculdadeNew = em.merge(faculdadeNew);
            }
            if (idFuncionarioOld != null && !idFuncionarioOld.equals(idFuncionarioNew)) {
                idFuncionarioOld.getUsersList().remove(users);
                idFuncionarioOld = em.merge(idFuncionarioOld);
            }
            if (idFuncionarioNew != null && !idFuncionarioNew.equals(idFuncionarioOld)) {
                idFuncionarioNew.getUsersList().add(users);
                idFuncionarioNew = em.merge(idFuncionarioNew);
            }
            for (SgEmprestimo sgEmprestimoListOldSgEmprestimo : sgEmprestimoListOld) {
                if (!sgEmprestimoListNew.contains(sgEmprestimoListOldSgEmprestimo)) {
                    sgEmprestimoListOldSgEmprestimo.setAgenteBibliot(null);
                    sgEmprestimoListOldSgEmprestimo = em.merge(sgEmprestimoListOldSgEmprestimo);
                }
            }
            for (SgEmprestimo sgEmprestimoListNewSgEmprestimo : sgEmprestimoListNew) {
                if (!sgEmprestimoListOld.contains(sgEmprestimoListNewSgEmprestimo)) {
                    Users oldAgenteBibliotOfSgEmprestimoListNewSgEmprestimo = sgEmprestimoListNewSgEmprestimo.getAgenteBibliot();
                    sgEmprestimoListNewSgEmprestimo.setAgenteBibliot(users);
                    sgEmprestimoListNewSgEmprestimo = em.merge(sgEmprestimoListNewSgEmprestimo);
                    if (oldAgenteBibliotOfSgEmprestimoListNewSgEmprestimo != null && !oldAgenteBibliotOfSgEmprestimoListNewSgEmprestimo.equals(users)) {
                        oldAgenteBibliotOfSgEmprestimoListNewSgEmprestimo.getSgEmprestimoList().remove(sgEmprestimoListNewSgEmprestimo);
                        oldAgenteBibliotOfSgEmprestimoListNewSgEmprestimo = em.merge(oldAgenteBibliotOfSgEmprestimoListNewSgEmprestimo);
                    }
                }
            }
            for (Usergrupo usergrupoListNewUsergrupo : usergrupoListNew) {
                if (!usergrupoListOld.contains(usergrupoListNewUsergrupo)) {
                    Users oldUsersOfUsergrupoListNewUsergrupo = usergrupoListNewUsergrupo.getUsers();
                    usergrupoListNewUsergrupo.setUsers(users);
                    usergrupoListNewUsergrupo = em.merge(usergrupoListNewUsergrupo);
                    if (oldUsersOfUsergrupoListNewUsergrupo != null && !oldUsersOfUsergrupoListNewUsergrupo.equals(users)) {
                        oldUsersOfUsergrupoListNewUsergrupo.getUsergrupoList().remove(usergrupoListNewUsergrupo);
                        oldUsersOfUsergrupoListNewUsergrupo = em.merge(oldUsersOfUsergrupoListNewUsergrupo);
                    }
                }
            }
            for (SgObra sgObraListOldSgObra : sgObraListOld) {
                if (!sgObraListNew.contains(sgObraListOldSgObra)) {
                    sgObraListOldSgObra.setBibliotecario(null);
                    sgObraListOldSgObra = em.merge(sgObraListOldSgObra);
                }
            }
            for (SgObra sgObraListNewSgObra : sgObraListNew) {
                if (!sgObraListOld.contains(sgObraListNewSgObra)) {
                    Users oldBibliotecarioOfSgObraListNewSgObra = sgObraListNewSgObra.getBibliotecario();
                    sgObraListNewSgObra.setBibliotecario(users);
                    sgObraListNewSgObra = em.merge(sgObraListNewSgObra);
                    if (oldBibliotecarioOfSgObraListNewSgObra != null && !oldBibliotecarioOfSgObraListNewSgObra.equals(users)) {
                        oldBibliotecarioOfSgObraListNewSgObra.getSgObraList().remove(sgObraListNewSgObra);
                        oldBibliotecarioOfSgObraListNewSgObra = em.merge(oldBibliotecarioOfSgObraListNewSgObra);
                    }
                }
            }
            for (BLeitor BLeitorListOldBLeitor : BLeitorListOld) {
                if (!BLeitorListNew.contains(BLeitorListOldBLeitor)) {
                    BLeitorListOldBLeitor.setIdutilizador(null);
                    BLeitorListOldBLeitor = em.merge(BLeitorListOldBLeitor);
                }
            }
            for (BLeitor BLeitorListNewBLeitor : BLeitorListNew) {
                if (!BLeitorListOld.contains(BLeitorListNewBLeitor)) {
                    Users oldIdutilizadorOfBLeitorListNewBLeitor = BLeitorListNewBLeitor.getIdutilizador();
                    BLeitorListNewBLeitor.setIdutilizador(users);
                    BLeitorListNewBLeitor = em.merge(BLeitorListNewBLeitor);
                    if (oldIdutilizadorOfBLeitorListNewBLeitor != null && !oldIdutilizadorOfBLeitorListNewBLeitor.equals(users)) {
                        oldIdutilizadorOfBLeitorListNewBLeitor.getBLeitorList().remove(BLeitorListNewBLeitor);
                        oldIdutilizadorOfBLeitorListNewBLeitor = em.merge(oldIdutilizadorOfBLeitorListNewBLeitor);
                    }
                }
            }
            for (BLeitor BLeitorList1OldBLeitor : BLeitorList1Old) {
                if (!BLeitorList1New.contains(BLeitorList1OldBLeitor)) {
                    BLeitorList1OldBLeitor.setIdagente(null);
                    BLeitorList1OldBLeitor = em.merge(BLeitorList1OldBLeitor);
                }
            }
            for (BLeitor BLeitorList1NewBLeitor : BLeitorList1New) {
                if (!BLeitorList1Old.contains(BLeitorList1NewBLeitor)) {
                    Users oldIdagenteOfBLeitorList1NewBLeitor = BLeitorList1NewBLeitor.getIdagente();
                    BLeitorList1NewBLeitor.setIdagente(users);
                    BLeitorList1NewBLeitor = em.merge(BLeitorList1NewBLeitor);
                    if (oldIdagenteOfBLeitorList1NewBLeitor != null && !oldIdagenteOfBLeitorList1NewBLeitor.equals(users)) {
                        oldIdagenteOfBLeitorList1NewBLeitor.getBLeitorList1().remove(BLeitorList1NewBLeitor);
                        oldIdagenteOfBLeitorList1NewBLeitor = em.merge(oldIdagenteOfBLeitorList1NewBLeitor);
                    }
                }
            }
            for (SgExemplar sgExemplarListOldSgExemplar : sgExemplarListOld) {
                if (!sgExemplarListNew.contains(sgExemplarListOldSgExemplar)) {
                    sgExemplarListOldSgExemplar.setAgenteRegisto(null);
                    sgExemplarListOldSgExemplar = em.merge(sgExemplarListOldSgExemplar);
                }
            }
            for (SgExemplar sgExemplarListNewSgExemplar : sgExemplarListNew) {
                if (!sgExemplarListOld.contains(sgExemplarListNewSgExemplar)) {
                    Users oldAgenteRegistoOfSgExemplarListNewSgExemplar = sgExemplarListNewSgExemplar.getAgenteRegisto();
                    sgExemplarListNewSgExemplar.setAgenteRegisto(users);
                    sgExemplarListNewSgExemplar = em.merge(sgExemplarListNewSgExemplar);
                    if (oldAgenteRegistoOfSgExemplarListNewSgExemplar != null && !oldAgenteRegistoOfSgExemplarListNewSgExemplar.equals(users)) {
                        oldAgenteRegistoOfSgExemplarListNewSgExemplar.getSgExemplarList().remove(sgExemplarListNewSgExemplar);
                        oldAgenteRegistoOfSgExemplarListNewSgExemplar = em.merge(oldAgenteRegistoOfSgExemplarListNewSgExemplar);
                    }
                }
            }
            for (SgEmprestimoParametros sgEmprestimoParametrosListOldSgEmprestimoParametros : sgEmprestimoParametrosListOld) {
                if (!sgEmprestimoParametrosListNew.contains(sgEmprestimoParametrosListOldSgEmprestimoParametros)) {
                    sgEmprestimoParametrosListOldSgEmprestimoParametros.setAgenteBibliotecario(null);
                    sgEmprestimoParametrosListOldSgEmprestimoParametros = em.merge(sgEmprestimoParametrosListOldSgEmprestimoParametros);
                }
            }
            for (SgEmprestimoParametros sgEmprestimoParametrosListNewSgEmprestimoParametros : sgEmprestimoParametrosListNew) {
                if (!sgEmprestimoParametrosListOld.contains(sgEmprestimoParametrosListNewSgEmprestimoParametros)) {
                    Users oldAgenteBibliotecarioOfSgEmprestimoParametrosListNewSgEmprestimoParametros = sgEmprestimoParametrosListNewSgEmprestimoParametros.getAgenteBibliotecario();
                    sgEmprestimoParametrosListNewSgEmprestimoParametros.setAgenteBibliotecario(users);
                    sgEmprestimoParametrosListNewSgEmprestimoParametros = em.merge(sgEmprestimoParametrosListNewSgEmprestimoParametros);
                    if (oldAgenteBibliotecarioOfSgEmprestimoParametrosListNewSgEmprestimoParametros != null && !oldAgenteBibliotecarioOfSgEmprestimoParametrosListNewSgEmprestimoParametros.equals(users)) {
                        oldAgenteBibliotecarioOfSgEmprestimoParametrosListNewSgEmprestimoParametros.getSgEmprestimoParametrosList().remove(sgEmprestimoParametrosListNewSgEmprestimoParametros);
                        oldAgenteBibliotecarioOfSgEmprestimoParametrosListNewSgEmprestimoParametros = em.merge(oldAgenteBibliotecarioOfSgEmprestimoParametrosListNewSgEmprestimoParametros);
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
            List<Usergrupo> usergrupoListOrphanCheck = users.getUsergrupoList();
            for (Usergrupo usergrupoListOrphanCheckUsergrupo : usergrupoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Usergrupo " + usergrupoListOrphanCheckUsergrupo + " in its usergrupoList field has a non-nullable users field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estudante idEstudante = users.getIdEstudante();
            if (idEstudante != null) {
                idEstudante.getUsersList().remove(users);
                idEstudante = em.merge(idEstudante);
            }
            Faculdade faculdade = users.getFaculdade();
            if (faculdade != null) {
                faculdade.getUsersList().remove(users);
                faculdade = em.merge(faculdade);
            }
            Funcionario idFuncionario = users.getIdFuncionario();
            if (idFuncionario != null) {
                idFuncionario.getUsersList().remove(users);
                idFuncionario = em.merge(idFuncionario);
            }
            List<SgEmprestimo> sgEmprestimoList = users.getSgEmprestimoList();
            for (SgEmprestimo sgEmprestimoListSgEmprestimo : sgEmprestimoList) {
                sgEmprestimoListSgEmprestimo.setAgenteBibliot(null);
                sgEmprestimoListSgEmprestimo = em.merge(sgEmprestimoListSgEmprestimo);
            }
            List<SgObra> sgObraList = users.getSgObraList();
            for (SgObra sgObraListSgObra : sgObraList) {
                sgObraListSgObra.setBibliotecario(null);
                sgObraListSgObra = em.merge(sgObraListSgObra);
            }
            List<BLeitor> BLeitorList = users.getBLeitorList();
            for (BLeitor BLeitorListBLeitor : BLeitorList) {
                BLeitorListBLeitor.setIdutilizador(null);
                BLeitorListBLeitor = em.merge(BLeitorListBLeitor);
            }
            List<BLeitor> BLeitorList1 = users.getBLeitorList1();
            for (BLeitor BLeitorList1BLeitor : BLeitorList1) {
                BLeitorList1BLeitor.setIdagente(null);
                BLeitorList1BLeitor = em.merge(BLeitorList1BLeitor);
            }
            List<SgExemplar> sgExemplarList = users.getSgExemplarList();
            for (SgExemplar sgExemplarListSgExemplar : sgExemplarList) {
                sgExemplarListSgExemplar.setAgenteRegisto(null);
                sgExemplarListSgExemplar = em.merge(sgExemplarListSgExemplar);
            }
            List<SgEmprestimoParametros> sgEmprestimoParametrosList = users.getSgEmprestimoParametrosList();
            for (SgEmprestimoParametros sgEmprestimoParametrosListSgEmprestimoParametros : sgEmprestimoParametrosList) {
                sgEmprestimoParametrosListSgEmprestimoParametros.setAgenteBibliotecario(null);
                sgEmprestimoParametrosListSgEmprestimoParametros = em.merge(sgEmprestimoParametrosListSgEmprestimoParametros);
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
