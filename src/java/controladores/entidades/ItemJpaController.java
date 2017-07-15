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
import entidades.Categoria;
import entidades.Item;
import entidades.Roles;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) throws PreexistingEntityException, Exception {
        if (item.getRolesList() == null) {
            item.setRolesList(new ArrayList<Roles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = item.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                item.setIdCategoria(idCategoria);
            }
            List<Roles> attachedRolesList = new ArrayList<Roles>();
            for (Roles rolesListRolesToAttach : item.getRolesList()) {
                rolesListRolesToAttach = em.getReference(rolesListRolesToAttach.getClass(), rolesListRolesToAttach.getRolesPK());
                attachedRolesList.add(rolesListRolesToAttach);
            }
            item.setRolesList(attachedRolesList);
            em.persist(item);
            if (idCategoria != null) {
                idCategoria.getItemList().add(item);
                idCategoria = em.merge(idCategoria);
            }
            for (Roles rolesListRoles : item.getRolesList()) {
                Item oldItemOfRolesListRoles = rolesListRoles.getItem();
                rolesListRoles.setItem(item);
                rolesListRoles = em.merge(rolesListRoles);
                if (oldItemOfRolesListRoles != null) {
                    oldItemOfRolesListRoles.getRolesList().remove(rolesListRoles);
                    oldItemOfRolesListRoles = em.merge(oldItemOfRolesListRoles);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItem(item.getItem()) != null) {
                throw new PreexistingEntityException("Item " + item + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getItem());
            Categoria idCategoriaOld = persistentItem.getIdCategoria();
            Categoria idCategoriaNew = item.getIdCategoria();
            List<Roles> rolesListOld = persistentItem.getRolesList();
            List<Roles> rolesListNew = item.getRolesList();
            List<String> illegalOrphanMessages = null;
            for (Roles rolesListOldRoles : rolesListOld) {
                if (!rolesListNew.contains(rolesListOldRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Roles " + rolesListOldRoles + " since its item field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                item.setIdCategoria(idCategoriaNew);
            }
            List<Roles> attachedRolesListNew = new ArrayList<Roles>();
            for (Roles rolesListNewRolesToAttach : rolesListNew) {
                rolesListNewRolesToAttach = em.getReference(rolesListNewRolesToAttach.getClass(), rolesListNewRolesToAttach.getRolesPK());
                attachedRolesListNew.add(rolesListNewRolesToAttach);
            }
            rolesListNew = attachedRolesListNew;
            item.setRolesList(rolesListNew);
            item = em.merge(item);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getItemList().remove(item);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getItemList().add(item);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Roles rolesListNewRoles : rolesListNew) {
                if (!rolesListOld.contains(rolesListNewRoles)) {
                    Item oldItemOfRolesListNewRoles = rolesListNewRoles.getItem();
                    rolesListNewRoles.setItem(item);
                    rolesListNewRoles = em.merge(rolesListNewRoles);
                    if (oldItemOfRolesListNewRoles != null && !oldItemOfRolesListNewRoles.equals(item)) {
                        oldItemOfRolesListNewRoles.getRolesList().remove(rolesListNewRoles);
                        oldItemOfRolesListNewRoles = em.merge(oldItemOfRolesListNewRoles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = item.getItem();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getItem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Roles> rolesListOrphanCheck = item.getRolesList();
            for (Roles rolesListOrphanCheckRoles : rolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the Roles " + rolesListOrphanCheckRoles + " in its rolesList field has a non-nullable item field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = item.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getItemList().remove(item);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
