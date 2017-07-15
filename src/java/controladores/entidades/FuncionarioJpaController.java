/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Tipochefia;
import entidades.Faculdade;
import entidades.Docente;
import entidades.Funcionario;
import entidades.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class FuncionarioJpaController implements Serializable {

    public FuncionarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Funcionario funcionario) {
        if (funcionario.getUsersList() == null) {
            funcionario.setUsersList(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipochefia tipochefia = funcionario.getTipochefia();
            if (tipochefia != null) {
                tipochefia = em.getReference(tipochefia.getClass(), tipochefia.getIdfuncionario());
                funcionario.setTipochefia(tipochefia);
            }
            Faculdade faculdade = funcionario.getFaculdade();
            if (faculdade != null) {
                faculdade = em.getReference(faculdade.getClass(), faculdade.getIdFaculdade());
                funcionario.setFaculdade(faculdade);
            }
            Docente docente = funcionario.getDocente();
            if (docente != null) {
                docente = em.getReference(docente.getClass(), docente.getIddocente());
                funcionario.setDocente(docente);
            }
            List<Users> attachedUsersList = new ArrayList<Users>();
            for (Users usersListUsersToAttach : funcionario.getUsersList()) {
                usersListUsersToAttach = em.getReference(usersListUsersToAttach.getClass(), usersListUsersToAttach.getUtilizador());
                attachedUsersList.add(usersListUsersToAttach);
            }
            funcionario.setUsersList(attachedUsersList);
            em.persist(funcionario);
            if (tipochefia != null) {
                Funcionario oldFuncionarioOfTipochefia = tipochefia.getFuncionario();
                if (oldFuncionarioOfTipochefia != null) {
                    oldFuncionarioOfTipochefia.setTipochefia(null);
                    oldFuncionarioOfTipochefia = em.merge(oldFuncionarioOfTipochefia);
                }
                tipochefia.setFuncionario(funcionario);
                tipochefia = em.merge(tipochefia);
            }
            if (faculdade != null) {
                faculdade.getFuncionarioList().add(funcionario);
                faculdade = em.merge(faculdade);
            }
            if (docente != null) {
                Funcionario oldFuncionarioOfDocente = docente.getFuncionario();
                if (oldFuncionarioOfDocente != null) {
                    oldFuncionarioOfDocente.setDocente(null);
                    oldFuncionarioOfDocente = em.merge(oldFuncionarioOfDocente);
                }
                docente.setFuncionario(funcionario);
                docente = em.merge(docente);
            }
            for (Users usersListUsers : funcionario.getUsersList()) {
                Funcionario oldIdFuncionarioOfUsersListUsers = usersListUsers.getIdFuncionario();
                usersListUsers.setIdFuncionario(funcionario);
                usersListUsers = em.merge(usersListUsers);
                if (oldIdFuncionarioOfUsersListUsers != null) {
                    oldIdFuncionarioOfUsersListUsers.getUsersList().remove(usersListUsers);
                    oldIdFuncionarioOfUsersListUsers = em.merge(oldIdFuncionarioOfUsersListUsers);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funcionario funcionario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario persistentFuncionario = em.find(Funcionario.class, funcionario.getIdFuncionario());
            Tipochefia tipochefiaOld = persistentFuncionario.getTipochefia();
            Tipochefia tipochefiaNew = funcionario.getTipochefia();
            Faculdade faculdadeOld = persistentFuncionario.getFaculdade();
            Faculdade faculdadeNew = funcionario.getFaculdade();
            Docente docenteOld = persistentFuncionario.getDocente();
            Docente docenteNew = funcionario.getDocente();
            List<Users> usersListOld = persistentFuncionario.getUsersList();
            List<Users> usersListNew = funcionario.getUsersList();
            List<String> illegalOrphanMessages = null;
            if (tipochefiaOld != null && !tipochefiaOld.equals(tipochefiaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Tipochefia " + tipochefiaOld + " since its funcionario field is not nullable.");
            }
            if (docenteOld != null && !docenteOld.equals(docenteNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Docente " + docenteOld + " since its funcionario field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipochefiaNew != null) {
                tipochefiaNew = em.getReference(tipochefiaNew.getClass(), tipochefiaNew.getIdfuncionario());
                funcionario.setTipochefia(tipochefiaNew);
            }
            if (faculdadeNew != null) {
                faculdadeNew = em.getReference(faculdadeNew.getClass(), faculdadeNew.getIdFaculdade());
                funcionario.setFaculdade(faculdadeNew);
            }
            if (docenteNew != null) {
                docenteNew = em.getReference(docenteNew.getClass(), docenteNew.getIddocente());
                funcionario.setDocente(docenteNew);
            }
            List<Users> attachedUsersListNew = new ArrayList<Users>();
            for (Users usersListNewUsersToAttach : usersListNew) {
                usersListNewUsersToAttach = em.getReference(usersListNewUsersToAttach.getClass(), usersListNewUsersToAttach.getUtilizador());
                attachedUsersListNew.add(usersListNewUsersToAttach);
            }
            usersListNew = attachedUsersListNew;
            funcionario.setUsersList(usersListNew);
            funcionario = em.merge(funcionario);
            if (tipochefiaNew != null && !tipochefiaNew.equals(tipochefiaOld)) {
                Funcionario oldFuncionarioOfTipochefia = tipochefiaNew.getFuncionario();
                if (oldFuncionarioOfTipochefia != null) {
                    oldFuncionarioOfTipochefia.setTipochefia(null);
                    oldFuncionarioOfTipochefia = em.merge(oldFuncionarioOfTipochefia);
                }
                tipochefiaNew.setFuncionario(funcionario);
                tipochefiaNew = em.merge(tipochefiaNew);
            }
            if (faculdadeOld != null && !faculdadeOld.equals(faculdadeNew)) {
                faculdadeOld.getFuncionarioList().remove(funcionario);
                faculdadeOld = em.merge(faculdadeOld);
            }
            if (faculdadeNew != null && !faculdadeNew.equals(faculdadeOld)) {
                faculdadeNew.getFuncionarioList().add(funcionario);
                faculdadeNew = em.merge(faculdadeNew);
            }
            if (docenteNew != null && !docenteNew.equals(docenteOld)) {
                Funcionario oldFuncionarioOfDocente = docenteNew.getFuncionario();
                if (oldFuncionarioOfDocente != null) {
                    oldFuncionarioOfDocente.setDocente(null);
                    oldFuncionarioOfDocente = em.merge(oldFuncionarioOfDocente);
                }
                docenteNew.setFuncionario(funcionario);
                docenteNew = em.merge(docenteNew);
            }
            for (Users usersListOldUsers : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers)) {
                    usersListOldUsers.setIdFuncionario(null);
                    usersListOldUsers = em.merge(usersListOldUsers);
                }
            }
            for (Users usersListNewUsers : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers)) {
                    Funcionario oldIdFuncionarioOfUsersListNewUsers = usersListNewUsers.getIdFuncionario();
                    usersListNewUsers.setIdFuncionario(funcionario);
                    usersListNewUsers = em.merge(usersListNewUsers);
                    if (oldIdFuncionarioOfUsersListNewUsers != null && !oldIdFuncionarioOfUsersListNewUsers.equals(funcionario)) {
                        oldIdFuncionarioOfUsersListNewUsers.getUsersList().remove(usersListNewUsers);
                        oldIdFuncionarioOfUsersListNewUsers = em.merge(oldIdFuncionarioOfUsersListNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = funcionario.getIdFuncionario();
                if (findFuncionario(id) == null) {
                    throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.");
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
            Funcionario funcionario;
            try {
                funcionario = em.getReference(Funcionario.class, id);
                funcionario.getIdFuncionario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Tipochefia tipochefiaOrphanCheck = funcionario.getTipochefia();
            if (tipochefiaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Funcionario (" + funcionario + ") cannot be destroyed since the Tipochefia " + tipochefiaOrphanCheck + " in its tipochefia field has a non-nullable funcionario field.");
            }
            Docente docenteOrphanCheck = funcionario.getDocente();
            if (docenteOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Funcionario (" + funcionario + ") cannot be destroyed since the Docente " + docenteOrphanCheck + " in its docente field has a non-nullable funcionario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Faculdade faculdade = funcionario.getFaculdade();
            if (faculdade != null) {
                faculdade.getFuncionarioList().remove(funcionario);
                faculdade = em.merge(faculdade);
            }
            List<Users> usersList = funcionario.getUsersList();
            for (Users usersListUsers : usersList) {
                usersListUsers.setIdFuncionario(null);
                usersListUsers = em.merge(usersListUsers);
            }
            em.remove(funcionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Funcionario> findFuncionarioEntities() {
        return findFuncionarioEntities(true, -1, -1);
    }

    public List<Funcionario> findFuncionarioEntities(int maxResults, int firstResult) {
        return findFuncionarioEntities(false, maxResults, firstResult);
    }

    private List<Funcionario> findFuncionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcionario.class));
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

    public Funcionario findFuncionario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcionario> rt = cq.from(Funcionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
