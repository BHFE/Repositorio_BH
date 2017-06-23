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
import java.util.Collection;
import entidades.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
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
        if (faculdade.getFuncionarioCollection() == null) {
            faculdade.setFuncionarioCollection(new ArrayList<Funcionario>());
        }
        if (faculdade.getUsersCollection() == null) {
            faculdade.setUsersCollection(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Funcionario> attachedFuncionarioCollection = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionFuncionarioToAttach : faculdade.getFuncionarioCollection()) {
                funcionarioCollectionFuncionarioToAttach = em.getReference(funcionarioCollectionFuncionarioToAttach.getClass(), funcionarioCollectionFuncionarioToAttach.getIdFuncionario());
                attachedFuncionarioCollection.add(funcionarioCollectionFuncionarioToAttach);
            }
            faculdade.setFuncionarioCollection(attachedFuncionarioCollection);
            Collection<Users> attachedUsersCollection = new ArrayList<Users>();
            for (Users usersCollectionUsersToAttach : faculdade.getUsersCollection()) {
                usersCollectionUsersToAttach = em.getReference(usersCollectionUsersToAttach.getClass(), usersCollectionUsersToAttach.getUtilizador());
                attachedUsersCollection.add(usersCollectionUsersToAttach);
            }
            faculdade.setUsersCollection(attachedUsersCollection);
            em.persist(faculdade);
            for (Funcionario funcionarioCollectionFuncionario : faculdade.getFuncionarioCollection()) {
                Faculdade oldFaculdadeOfFuncionarioCollectionFuncionario = funcionarioCollectionFuncionario.getFaculdade();
                funcionarioCollectionFuncionario.setFaculdade(faculdade);
                funcionarioCollectionFuncionario = em.merge(funcionarioCollectionFuncionario);
                if (oldFaculdadeOfFuncionarioCollectionFuncionario != null) {
                    oldFaculdadeOfFuncionarioCollectionFuncionario.getFuncionarioCollection().remove(funcionarioCollectionFuncionario);
                    oldFaculdadeOfFuncionarioCollectionFuncionario = em.merge(oldFaculdadeOfFuncionarioCollectionFuncionario);
                }
            }
            for (Users usersCollectionUsers : faculdade.getUsersCollection()) {
                Faculdade oldFaculdadeOfUsersCollectionUsers = usersCollectionUsers.getFaculdade();
                usersCollectionUsers.setFaculdade(faculdade);
                usersCollectionUsers = em.merge(usersCollectionUsers);
                if (oldFaculdadeOfUsersCollectionUsers != null) {
                    oldFaculdadeOfUsersCollectionUsers.getUsersCollection().remove(usersCollectionUsers);
                    oldFaculdadeOfUsersCollectionUsers = em.merge(oldFaculdadeOfUsersCollectionUsers);
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
            Collection<Funcionario> funcionarioCollectionOld = persistentFaculdade.getFuncionarioCollection();
            Collection<Funcionario> funcionarioCollectionNew = faculdade.getFuncionarioCollection();
            Collection<Users> usersCollectionOld = persistentFaculdade.getUsersCollection();
            Collection<Users> usersCollectionNew = faculdade.getUsersCollection();
            Collection<Funcionario> attachedFuncionarioCollectionNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionNewFuncionarioToAttach : funcionarioCollectionNew) {
                funcionarioCollectionNewFuncionarioToAttach = em.getReference(funcionarioCollectionNewFuncionarioToAttach.getClass(), funcionarioCollectionNewFuncionarioToAttach.getIdFuncionario());
                attachedFuncionarioCollectionNew.add(funcionarioCollectionNewFuncionarioToAttach);
            }
            funcionarioCollectionNew = attachedFuncionarioCollectionNew;
            faculdade.setFuncionarioCollection(funcionarioCollectionNew);
            Collection<Users> attachedUsersCollectionNew = new ArrayList<Users>();
            for (Users usersCollectionNewUsersToAttach : usersCollectionNew) {
                usersCollectionNewUsersToAttach = em.getReference(usersCollectionNewUsersToAttach.getClass(), usersCollectionNewUsersToAttach.getUtilizador());
                attachedUsersCollectionNew.add(usersCollectionNewUsersToAttach);
            }
            usersCollectionNew = attachedUsersCollectionNew;
            faculdade.setUsersCollection(usersCollectionNew);
            faculdade = em.merge(faculdade);
            for (Funcionario funcionarioCollectionOldFuncionario : funcionarioCollectionOld) {
                if (!funcionarioCollectionNew.contains(funcionarioCollectionOldFuncionario)) {
                    funcionarioCollectionOldFuncionario.setFaculdade(null);
                    funcionarioCollectionOldFuncionario = em.merge(funcionarioCollectionOldFuncionario);
                }
            }
            for (Funcionario funcionarioCollectionNewFuncionario : funcionarioCollectionNew) {
                if (!funcionarioCollectionOld.contains(funcionarioCollectionNewFuncionario)) {
                    Faculdade oldFaculdadeOfFuncionarioCollectionNewFuncionario = funcionarioCollectionNewFuncionario.getFaculdade();
                    funcionarioCollectionNewFuncionario.setFaculdade(faculdade);
                    funcionarioCollectionNewFuncionario = em.merge(funcionarioCollectionNewFuncionario);
                    if (oldFaculdadeOfFuncionarioCollectionNewFuncionario != null && !oldFaculdadeOfFuncionarioCollectionNewFuncionario.equals(faculdade)) {
                        oldFaculdadeOfFuncionarioCollectionNewFuncionario.getFuncionarioCollection().remove(funcionarioCollectionNewFuncionario);
                        oldFaculdadeOfFuncionarioCollectionNewFuncionario = em.merge(oldFaculdadeOfFuncionarioCollectionNewFuncionario);
                    }
                }
            }
            for (Users usersCollectionOldUsers : usersCollectionOld) {
                if (!usersCollectionNew.contains(usersCollectionOldUsers)) {
                    usersCollectionOldUsers.setFaculdade(null);
                    usersCollectionOldUsers = em.merge(usersCollectionOldUsers);
                }
            }
            for (Users usersCollectionNewUsers : usersCollectionNew) {
                if (!usersCollectionOld.contains(usersCollectionNewUsers)) {
                    Faculdade oldFaculdadeOfUsersCollectionNewUsers = usersCollectionNewUsers.getFaculdade();
                    usersCollectionNewUsers.setFaculdade(faculdade);
                    usersCollectionNewUsers = em.merge(usersCollectionNewUsers);
                    if (oldFaculdadeOfUsersCollectionNewUsers != null && !oldFaculdadeOfUsersCollectionNewUsers.equals(faculdade)) {
                        oldFaculdadeOfUsersCollectionNewUsers.getUsersCollection().remove(usersCollectionNewUsers);
                        oldFaculdadeOfUsersCollectionNewUsers = em.merge(oldFaculdadeOfUsersCollectionNewUsers);
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
            Collection<Funcionario> funcionarioCollection = faculdade.getFuncionarioCollection();
            for (Funcionario funcionarioCollectionFuncionario : funcionarioCollection) {
                funcionarioCollectionFuncionario.setFaculdade(null);
                funcionarioCollectionFuncionario = em.merge(funcionarioCollectionFuncionario);
            }
            Collection<Users> usersCollection = faculdade.getUsersCollection();
            for (Users usersCollectionUsers : usersCollection) {
                usersCollectionUsers.setFaculdade(null);
                usersCollectionUsers = em.merge(usersCollectionUsers);
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
