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
import entidades.Usergrupo;
import entidades.Roles;
import java.util.ArrayList;
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
        if (grupo.getRolesList() == null) {
            grupo.setRolesList(new ArrayList<Roles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usergrupo usergrupo = grupo.getUsergrupo();
            if (usergrupo != null) {
                usergrupo = em.getReference(usergrupo.getClass(), usergrupo.getIdGrupo());
                grupo.setUsergrupo(usergrupo);
            }
            List<Roles> attachedRolesList = new ArrayList<Roles>();
            for (Roles rolesListRolesToAttach : grupo.getRolesList()) {
                rolesListRolesToAttach = em.getReference(rolesListRolesToAttach.getClass(), rolesListRolesToAttach.getRolesPK());
                attachedRolesList.add(rolesListRolesToAttach);
            }
            grupo.setRolesList(attachedRolesList);
            em.persist(grupo);
            if (usergrupo != null) {
                Grupo oldGrupoOfUsergrupo = usergrupo.getGrupo();
                if (oldGrupoOfUsergrupo != null) {
                    oldGrupoOfUsergrupo.setUsergrupo(null);
                    oldGrupoOfUsergrupo = em.merge(oldGrupoOfUsergrupo);
                }
                usergrupo.setGrupo(grupo);
                usergrupo = em.merge(usergrupo);
            }
            for (Roles rolesListRoles : grupo.getRolesList()) {
                Grupo oldGrupoOfRolesListRoles = rolesListRoles.getGrupo();
                rolesListRoles.setGrupo(grupo);
                rolesListRoles = em.merge(rolesListRoles);
                if (oldGrupoOfRolesListRoles != null) {
                    oldGrupoOfRolesListRoles.getRolesList().remove(rolesListRoles);
                    oldGrupoOfRolesListRoles = em.merge(oldGrupoOfRolesListRoles);
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
            Usergrupo usergrupoOld = persistentGrupo.getUsergrupo();
            Usergrupo usergrupoNew = grupo.getUsergrupo();
            List<Roles> rolesListOld = persistentGrupo.getRolesList();
            List<Roles> rolesListNew = grupo.getRolesList();
            List<String> illegalOrphanMessages = null;
            if (usergrupoOld != null && !usergrupoOld.equals(usergrupoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Usergrupo " + usergrupoOld + " since its grupo field is not nullable.");
            }
            for (Roles rolesListOldRoles : rolesListOld) {
                if (!rolesListNew.contains(rolesListOldRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Roles " + rolesListOldRoles + " since its grupo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usergrupoNew != null) {
                usergrupoNew = em.getReference(usergrupoNew.getClass(), usergrupoNew.getIdGrupo());
                grupo.setUsergrupo(usergrupoNew);
            }
            List<Roles> attachedRolesListNew = new ArrayList<Roles>();
            for (Roles rolesListNewRolesToAttach : rolesListNew) {
                rolesListNewRolesToAttach = em.getReference(rolesListNewRolesToAttach.getClass(), rolesListNewRolesToAttach.getRolesPK());
                attachedRolesListNew.add(rolesListNewRolesToAttach);
            }
            rolesListNew = attachedRolesListNew;
            grupo.setRolesList(rolesListNew);
            grupo = em.merge(grupo);
            if (usergrupoNew != null && !usergrupoNew.equals(usergrupoOld)) {
                Grupo oldGrupoOfUsergrupo = usergrupoNew.getGrupo();
                if (oldGrupoOfUsergrupo != null) {
                    oldGrupoOfUsergrupo.setUsergrupo(null);
                    oldGrupoOfUsergrupo = em.merge(oldGrupoOfUsergrupo);
                }
                usergrupoNew.setGrupo(grupo);
                usergrupoNew = em.merge(usergrupoNew);
            }
            for (Roles rolesListNewRoles : rolesListNew) {
                if (!rolesListOld.contains(rolesListNewRoles)) {
                    Grupo oldGrupoOfRolesListNewRoles = rolesListNewRoles.getGrupo();
                    rolesListNewRoles.setGrupo(grupo);
                    rolesListNewRoles = em.merge(rolesListNewRoles);
                    if (oldGrupoOfRolesListNewRoles != null && !oldGrupoOfRolesListNewRoles.equals(grupo)) {
                        oldGrupoOfRolesListNewRoles.getRolesList().remove(rolesListNewRoles);
                        oldGrupoOfRolesListNewRoles = em.merge(oldGrupoOfRolesListNewRoles);
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
            Usergrupo usergrupoOrphanCheck = grupo.getUsergrupo();
            if (usergrupoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Usergrupo " + usergrupoOrphanCheck + " in its usergrupo field has a non-nullable grupo field.");
            }
            List<Roles> rolesListOrphanCheck = grupo.getRolesList();
            for (Roles rolesListOrphanCheckRoles : rolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupo (" + grupo + ") cannot be destroyed since the Roles " + rolesListOrphanCheckRoles + " in its rolesList field has a non-nullable grupo field.");
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
