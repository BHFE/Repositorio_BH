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
import java.util.Collection;
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
        if (funcionario.getUsersCollection() == null) {
            funcionario.setUsersCollection(new ArrayList<Users>());
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
            Collection<Users> attachedUsersCollection = new ArrayList<Users>();
            for (Users usersCollectionUsersToAttach : funcionario.getUsersCollection()) {
                usersCollectionUsersToAttach = em.getReference(usersCollectionUsersToAttach.getClass(), usersCollectionUsersToAttach.getUtilizador());
                attachedUsersCollection.add(usersCollectionUsersToAttach);
            }
            funcionario.setUsersCollection(attachedUsersCollection);
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
                faculdade.getFuncionarioCollection().add(funcionario);
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
            for (Users usersCollectionUsers : funcionario.getUsersCollection()) {
                Funcionario oldIdFuncionarioOfUsersCollectionUsers = usersCollectionUsers.getIdFuncionario();
                usersCollectionUsers.setIdFuncionario(funcionario);
                usersCollectionUsers = em.merge(usersCollectionUsers);
                if (oldIdFuncionarioOfUsersCollectionUsers != null) {
                    oldIdFuncionarioOfUsersCollectionUsers.getUsersCollection().remove(usersCollectionUsers);
                    oldIdFuncionarioOfUsersCollectionUsers = em.merge(oldIdFuncionarioOfUsersCollectionUsers);
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
            Collection<Users> usersCollectionOld = persistentFuncionario.getUsersCollection();
            Collection<Users> usersCollectionNew = funcionario.getUsersCollection();
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
            Collection<Users> attachedUsersCollectionNew = new ArrayList<Users>();
            for (Users usersCollectionNewUsersToAttach : usersCollectionNew) {
                usersCollectionNewUsersToAttach = em.getReference(usersCollectionNewUsersToAttach.getClass(), usersCollectionNewUsersToAttach.getUtilizador());
                attachedUsersCollectionNew.add(usersCollectionNewUsersToAttach);
            }
            usersCollectionNew = attachedUsersCollectionNew;
            funcionario.setUsersCollection(usersCollectionNew);
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
                faculdadeOld.getFuncionarioCollection().remove(funcionario);
                faculdadeOld = em.merge(faculdadeOld);
            }
            if (faculdadeNew != null && !faculdadeNew.equals(faculdadeOld)) {
                faculdadeNew.getFuncionarioCollection().add(funcionario);
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
            for (Users usersCollectionOldUsers : usersCollectionOld) {
                if (!usersCollectionNew.contains(usersCollectionOldUsers)) {
                    usersCollectionOldUsers.setIdFuncionario(null);
                    usersCollectionOldUsers = em.merge(usersCollectionOldUsers);
                }
            }
            for (Users usersCollectionNewUsers : usersCollectionNew) {
                if (!usersCollectionOld.contains(usersCollectionNewUsers)) {
                    Funcionario oldIdFuncionarioOfUsersCollectionNewUsers = usersCollectionNewUsers.getIdFuncionario();
                    usersCollectionNewUsers.setIdFuncionario(funcionario);
                    usersCollectionNewUsers = em.merge(usersCollectionNewUsers);
                    if (oldIdFuncionarioOfUsersCollectionNewUsers != null && !oldIdFuncionarioOfUsersCollectionNewUsers.equals(funcionario)) {
                        oldIdFuncionarioOfUsersCollectionNewUsers.getUsersCollection().remove(usersCollectionNewUsers);
                        oldIdFuncionarioOfUsersCollectionNewUsers = em.merge(oldIdFuncionarioOfUsersCollectionNewUsers);
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
                faculdade.getFuncionarioCollection().remove(funcionario);
                faculdade = em.merge(faculdade);
            }
            Collection<Users> usersCollection = funcionario.getUsersCollection();
            for (Users usersCollectionUsers : usersCollection) {
                usersCollectionUsers.setIdFuncionario(null);
                usersCollectionUsers = em.merge(usersCollectionUsers);
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
