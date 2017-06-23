/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.Grupo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Roles;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Usergrupo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class GrupoJpaController implements Serializable {

    public GrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupo grupo) throws PreexistingEntityException, Exception {
        if (grupo.getRolesCollection() == null) {
            grupo.setRolesCollection(new ArrayList<Roles>());
        }
        if (grupo.getUsergrupoCollection() == null) {
            grupo.setUsergrupoCollection(new ArrayList<Usergrupo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Roles> attachedRolesCollection = new ArrayList<Roles>();
            for (Roles rolesCollectionRolesToAttach : grupo.getRolesCollection()) {
                rolesCollectionRolesToAttach = em.getReference(rolesCollectionRolesToAttach.getClass(), rolesCollectionRolesToAttach.getRolesPK());
                attachedRolesCollection.add(rolesCollectionRolesToAttach);
            }
            grupo.setRolesCollection(attachedRolesCollection);
            Collection<Usergrupo> attachedUsergrupoCollection = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoCollectionUsergrupoToAttach : grupo.getUsergrupoCollection()) {
                usergrupoCollectionUsergrupoToAttach = em.getReference(usergrupoCollectionUsergrupoToAttach.getClass(), usergrupoCollectionUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoCollection.add(usergrupoCollectionUsergrupoToAttach);
            }
            grupo.setUsergrupoCollection(attachedUsergrupoCollection);
            em.persist(grupo);
            for (Roles rolesCollectionRoles : grupo.getRolesCollection()) {
                Grupo oldGrupoOfRolesCollectionRoles = rolesCollectionRoles.getGrupo();
                rolesCollectionRoles.setGrupo(grupo);
                rolesCollectionRoles = em.merge(rolesCollectionRoles);
                if (oldGrupoOfRolesCollectionRoles != null) {
                    oldGrupoOfRolesCollectionRoles.getRolesCollection().remove(rolesCollectionRoles);
                    oldGrupoOfRolesCollectionRoles = em.merge(oldGrupoOfRolesCollectionRoles);
                }
            }
            for (Usergrupo usergrupoCollectionUsergrupo : grupo.getUsergrupoCollection()) {
                Grupo oldGrupoOfUsergrupoCollectionUsergrupo = usergrupoCollectionUsergrupo.getGrupo();
                usergrupoCollectionUsergrupo.setGrupo(grupo);
                usergrupoCollectionUsergrupo = em.merge(usergrupoCollectionUsergrupo);
                if (oldGrupoOfUsergrupoCollectionUsergrupo != null) {
                    oldGrupoOfUsergrupoCollectionUsergrupo.getUsergrupoCollection().remove(usergrupoCollectionUsergrupo);
                    oldGrupoOfUsergrupoCollectionUsergrupo = em.merge(oldGrupoOfUsergrupoCollectionUsergrupo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupo(grupo.getIdGrupo()) != null) {
                throw new PreexistingEntityException("Grupo " + grupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getIdGrupo());
            Collection<Roles> rolesCollectionOld = persistentGrupo.getRolesCollection();
            Collection<Roles> rolesCollectionNew = grupo.getRolesCollection();
            Collection<Usergrupo> usergrupoCollectionOld = persistentGrupo.getUsergrupoCollection();
            Collection<Usergrupo> usergrupoCollectionNew = grupo.getUsergrupoCollection();
            List<String> illegalOrphanMessages = null;
            for (Roles rolesCollectionOldRoles : rolesCollectionOld) {
                if (!rolesCollectionNew.contains(rolesCollectionOldRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Roles " + rolesCollectionOldRoles + " since its grupo field is not nullable.");
                }
            }
            for (Usergrupo usergrupoCollectionOldUsergrupo : usergrupoCollectionOld) {
                if (!usergrupoCollectionNew.contains(usergrupoCollectionOldUsergrupo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usergrupo " + usergrupoCollectionOldUsergrupo + " since its grupo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Roles> attachedRolesCollectionNew = new ArrayList<Roles>();
            for (Roles rolesCollectionNewRolesToAttach : rolesCollectionNew) {
                rolesCollectionNewRolesToAttach = em.getReference(rolesCollectionNewRolesToAttach.getClass(), rolesCollectionNewRolesToAttach.getRolesPK());
                attachedRolesCollectionNew.add(rolesCollectionNewRolesToAttach);
            }
            rolesCollectionNew = attachedRolesCollectionNew;
            grupo.setRolesCollection(rolesCollectionNew);
            Collection<Usergrupo> attachedUsergrupoCollectionNew = new ArrayList<Usergrupo>();
            for (Usergrupo usergrupoCollectionNewUsergrupoToAttach : usergrupoCollectionNew) {
                usergrupoCollectionNewUsergrupoToAttach = em.getReference(usergrupoCollectionNewUsergrupoToAttach.getClass(), usergrupoCollectionNewUsergrupoToAttach.getUsergrupoPK());
                attachedUsergrupoCollectionNew.add(usergrupoCollectionNewUsergrupoToAttach);
            }
            usergrupoCollectionNew = attachedUsergrupoCollectionNew;
            grupo.setUsergrupoCollection(usergrupoCollectionNew);
            grupo = em.merge(grupo);
            for (Roles rolesCollectionNewRoles : rolesCollectionNew) {
                if (!rolesCollectionOld.contains(rolesCollectionNewRoles)) {
                    Grupo oldGrupoOfRolesCollectionNewRoles = rolesCollectionNewRoles.getGrupo();
                    rolesCollectionNewRoles.setGrupo(grupo);
                    rolesCollectionNewRoles = em.merge(rolesCollectionNewRoles);
                    if (oldGrupoOfRolesCollectionNewRoles != null && !oldGrupoOfRolesCollectionNewRoles.equals(grupo)) {
                        oldGrupoOfRolesCollectionNewRoles.getRolesCollection().remove(rolesCollectionNewRoles);
                        oldGrupoOfRolesCollectionNewRoles = em.merge(oldGrupoOfRolesCollectionNewRoles);
                    }
                }
            }
            for (Usergrupo usergrupoCollectionNewUsergrupo : usergrupoCollectionNew) {
                if (!usergrupoCollectionOld.contains(usergrupoCollectionNewUsergrupo)) {
                    Grupo oldGrupoOfUsergrupoCollectionNewUsergrupo = usergrupoCollectionNewUsergrupo.getGrupo();
                    usergrupoCollectionNewUsergrupo.setGrupo(grupo);
                    usergrupoCollectionNewUsergrupo = em.merge(usergrupoCollectionNewUsergrupo);
                    if (oldGrupoOfUsergrupoCollectionNewUsergrupo != null && !oldGrupoOfUsergrupoCollectionNewUsergrupo.equals(grupo)) {
                        oldGrupoOfUsergrupoCollectionNewUsergrupo.getUsergrupoCollection().remove(usergrupoCollectionNewUsergrupo);
                        oldGrupoOfUsergrupoCollectionNewUsergrupo = em.merge(oldGrupoOfUsergrupoCollectionNewUsergrupo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = grupo.getIdGrupo();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
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
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getIdGrupo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Roles> rolesCollectionOrphanCheck = grupo.getRolesCollection();
            for (Roles rolesCollectionOrphanCheckRoles : rolesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Roles " + rolesCollectionOrphanCheckRoles + " in its rolesCollection field has a non-nullable grupo field.");
            }
            Collection<Usergrupo> usergrupoCollectionOrphanCheck = grupo.getUsergrupoCollection();
            for (Usergrupo usergrupoCollectionOrphanCheckUsergrupo : usergrupoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Usergrupo " + usergrupoCollectionOrphanCheckUsergrupo + " in its usergrupoCollection field has a non-nullable grupo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
