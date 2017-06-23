/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.UsersUsergrupo;
import entidades.UsersUsergrupoPK;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Migueljr
 */
public class UsersUsergrupoJpaController implements Serializable {

    public UsersUsergrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsersUsergrupo usersUsergrupo) throws PreexistingEntityException, Exception {
        if (usersUsergrupo.getUsersUsergrupoPK() == null) {
            usersUsergrupo.setUsersUsergrupoPK(new UsersUsergrupoPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(usersUsergrupo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsersUsergrupo(usersUsergrupo.getUsersUsergrupoPK()) != null) {
                throw new PreexistingEntityException("UsersUsergrupo " + usersUsergrupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsersUsergrupo usersUsergrupo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usersUsergrupo = em.merge(usersUsergrupo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsersUsergrupoPK id = usersUsergrupo.getUsersUsergrupoPK();
                if (findUsersUsergrupo(id) == null) {
                    throw new NonexistentEntityException("The usersUsergrupo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsersUsergrupoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsersUsergrupo usersUsergrupo;
            try {
                usersUsergrupo = em.getReference(UsersUsergrupo.class, id);
                usersUsergrupo.getUsersUsergrupoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usersUsergrupo with id " + id + " no longer exists.", enfe);
            }
            em.remove(usersUsergrupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsersUsergrupo> findUsersUsergrupoEntities() {
        return findUsersUsergrupoEntities(true, -1, -1);
    }

    public List<UsersUsergrupo> findUsersUsergrupoEntities(int maxResults, int firstResult) {
        return findUsersUsergrupoEntities(false, maxResults, firstResult);
    }

    private List<UsersUsergrupo> findUsersUsergrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsersUsergrupo.class));
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

    public UsersUsergrupo findUsersUsergrupo(UsersUsergrupoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsersUsergrupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersUsergrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsersUsergrupo> rt = cq.from(UsersUsergrupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
