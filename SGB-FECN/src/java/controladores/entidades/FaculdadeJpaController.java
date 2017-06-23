/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.Faculdade;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Funcionario;
import java.util.ArrayList;
import java.util.List;
import entidades.Users;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
 */
public class FaculdadeJpaController implements Serializable {

    public FaculdadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Faculdade faculdade) {
        if (faculdade.getFuncionarioList() == null) {
            faculdade.setFuncionarioList(new ArrayList<Funcionario>());
        }
        if (faculdade.getUsersList() == null) {
            faculdade.setUsersList(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Funcionario> attachedFuncionarioList = new ArrayList<Funcionario>();
            for (Funcionario funcionarioListFuncionarioToAttach : faculdade.getFuncionarioList()) {
                funcionarioListFuncionarioToAttach = em.getReference(funcionarioListFuncionarioToAttach.getClass(), funcionarioListFuncionarioToAttach.getIdFuncionario());
                attachedFuncionarioList.add(funcionarioListFuncionarioToAttach);
            }
            faculdade.setFuncionarioList(attachedFuncionarioList);
            List<Users> attachedUsersList = new ArrayList<Users>();
            for (Users usersListUsersToAttach : faculdade.getUsersList()) {
                usersListUsersToAttach = em.getReference(usersListUsersToAttach.getClass(), usersListUsersToAttach.getUtilizador());
                attachedUsersList.add(usersListUsersToAttach);
            }
            faculdade.setUsersList(attachedUsersList);
            em.persist(faculdade);
            for (Funcionario funcionarioListFuncionario : faculdade.getFuncionarioList()) {
                Faculdade oldFaculdadeOfFuncionarioListFuncionario = funcionarioListFuncionario.getFaculdade();
                funcionarioListFuncionario.setFaculdade(faculdade);
                funcionarioListFuncionario = em.merge(funcionarioListFuncionario);
                if (oldFaculdadeOfFuncionarioListFuncionario != null) {
                    oldFaculdadeOfFuncionarioListFuncionario.getFuncionarioList().remove(funcionarioListFuncionario);
                    oldFaculdadeOfFuncionarioListFuncionario = em.merge(oldFaculdadeOfFuncionarioListFuncionario);
                }
            }
            for (Users usersListUsers : faculdade.getUsersList()) {
                Faculdade oldFaculdadeOfUsersListUsers = usersListUsers.getFaculdade();
                usersListUsers.setFaculdade(faculdade);
                usersListUsers = em.merge(usersListUsers);
                if (oldFaculdadeOfUsersListUsers != null) {
                    oldFaculdadeOfUsersListUsers.getUsersList().remove(usersListUsers);
                    oldFaculdadeOfUsersListUsers = em.merge(oldFaculdadeOfUsersListUsers);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Faculdade faculdade) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Faculdade persistentFaculdade = em.find(Faculdade.class, faculdade.getIdFaculdade());
            List<Funcionario> funcionarioListOld = persistentFaculdade.getFuncionarioList();
            List<Funcionario> funcionarioListNew = faculdade.getFuncionarioList();
            List<Users> usersListOld = persistentFaculdade.getUsersList();
            List<Users> usersListNew = faculdade.getUsersList();
            List<Funcionario> attachedFuncionarioListNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioListNewFuncionarioToAttach : funcionarioListNew) {
                funcionarioListNewFuncionarioToAttach = em.getReference(funcionarioListNewFuncionarioToAttach.getClass(), funcionarioListNewFuncionarioToAttach.getIdFuncionario());
                attachedFuncionarioListNew.add(funcionarioListNewFuncionarioToAttach);
            }
            funcionarioListNew = attachedFuncionarioListNew;
            faculdade.setFuncionarioList(funcionarioListNew);
            List<Users> attachedUsersListNew = new ArrayList<Users>();
            for (Users usersListNewUsersToAttach : usersListNew) {
                usersListNewUsersToAttach = em.getReference(usersListNewUsersToAttach.getClass(), usersListNewUsersToAttach.getUtilizador());
                attachedUsersListNew.add(usersListNewUsersToAttach);
            }
            usersListNew = attachedUsersListNew;
            faculdade.setUsersList(usersListNew);
            faculdade = em.merge(faculdade);
            for (Funcionario funcionarioListOldFuncionario : funcionarioListOld) {
                if (!funcionarioListNew.contains(funcionarioListOldFuncionario)) {
                    funcionarioListOldFuncionario.setFaculdade(null);
                    funcionarioListOldFuncionario = em.merge(funcionarioListOldFuncionario);
                }
            }
            for (Funcionario funcionarioListNewFuncionario : funcionarioListNew) {
                if (!funcionarioListOld.contains(funcionarioListNewFuncionario)) {
                    Faculdade oldFaculdadeOfFuncionarioListNewFuncionario = funcionarioListNewFuncionario.getFaculdade();
                    funcionarioListNewFuncionario.setFaculdade(faculdade);
                    funcionarioListNewFuncionario = em.merge(funcionarioListNewFuncionario);
                    if (oldFaculdadeOfFuncionarioListNewFuncionario != null && !oldFaculdadeOfFuncionarioListNewFuncionario.equals(faculdade)) {
                        oldFaculdadeOfFuncionarioListNewFuncionario.getFuncionarioList().remove(funcionarioListNewFuncionario);
                        oldFaculdadeOfFuncionarioListNewFuncionario = em.merge(oldFaculdadeOfFuncionarioListNewFuncionario);
                    }
                }
            }
            for (Users usersListOldUsers : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers)) {
                    usersListOldUsers.setFaculdade(null);
                    usersListOldUsers = em.merge(usersListOldUsers);
                }
            }
            for (Users usersListNewUsers : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers)) {
                    Faculdade oldFaculdadeOfUsersListNewUsers = usersListNewUsers.getFaculdade();
                    usersListNewUsers.setFaculdade(faculdade);
                    usersListNewUsers = em.merge(usersListNewUsers);
                    if (oldFaculdadeOfUsersListNewUsers != null && !oldFaculdadeOfUsersListNewUsers.equals(faculdade)) {
                        oldFaculdadeOfUsersListNewUsers.getUsersList().remove(usersListNewUsers);
                        oldFaculdadeOfUsersListNewUsers = em.merge(oldFaculdadeOfUsersListNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = faculdade.getIdFaculdade();
                if (findFaculdade(id) == null) {
                    throw new NonexistentEntityException("The faculdade with id " + id + " no longer exists.");
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
            Faculdade faculdade;
            try {
                faculdade = em.getReference(Faculdade.class, id);
                faculdade.getIdFaculdade();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faculdade with id " + id + " no longer exists.", enfe);
            }
            List<Funcionario> funcionarioList = faculdade.getFuncionarioList();
            for (Funcionario funcionarioListFuncionario : funcionarioList) {
                funcionarioListFuncionario.setFaculdade(null);
                funcionarioListFuncionario = em.merge(funcionarioListFuncionario);
            }
            List<Users> usersList = faculdade.getUsersList();
            for (Users usersListUsers : usersList) {
                usersListUsers.setFaculdade(null);
                usersListUsers = em.merge(usersListUsers);
            }
            em.remove(faculdade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Faculdade> findFaculdadeEntities() {
        return findFaculdadeEntities(true, -1, -1);
    }

    public List<Faculdade> findFaculdadeEntities(int maxResults, int firstResult) {
        return findFaculdadeEntities(false, maxResults, firstResult);
    }

    private List<Faculdade> findFaculdadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Faculdade.class));
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

    public Faculdade findFaculdade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Faculdade.class, id);
        } finally {
            em.close();
        }
    }

    public int getFaculdadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Faculdade> rt = cq.from(Faculdade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
