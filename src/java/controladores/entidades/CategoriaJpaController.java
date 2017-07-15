/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.Categoria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Roles;
import java.util.ArrayList;
import java.util.List;
import entidades.Item;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class CategoriaJpaController implements Serializable {

    public CategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) throws PreexistingEntityException, Exception {
        if (categoria.getRolesList() == null) {
            categoria.setRolesList(new ArrayList<Roles>());
        }
        if (categoria.getItemList() == null) {
            categoria.setItemList(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Roles> attachedRolesList = new ArrayList<Roles>();
            for (Roles rolesListRolesToAttach : categoria.getRolesList()) {
                rolesListRolesToAttach = em.getReference(rolesListRolesToAttach.getClass(), rolesListRolesToAttach.getRolesPK());
                attachedRolesList.add(rolesListRolesToAttach);
            }
            categoria.setRolesList(attachedRolesList);
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : categoria.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getItem());
                attachedItemList.add(itemListItemToAttach);
            }
            categoria.setItemList(attachedItemList);
            em.persist(categoria);
            for (Roles rolesListRoles : categoria.getRolesList()) {
                Categoria oldCategoriaOfRolesListRoles = rolesListRoles.getCategoria();
                rolesListRoles.setCategoria(categoria);
                rolesListRoles = em.merge(rolesListRoles);
                if (oldCategoriaOfRolesListRoles != null) {
                    oldCategoriaOfRolesListRoles.getRolesList().remove(rolesListRoles);
                    oldCategoriaOfRolesListRoles = em.merge(oldCategoriaOfRolesListRoles);
                }
            }
            for (Item itemListItem : categoria.getItemList()) {
                Categoria oldIdCategoriaOfItemListItem = itemListItem.getIdCategoria();
                itemListItem.setIdCategoria(categoria);
                itemListItem = em.merge(itemListItem);
                if (oldIdCategoriaOfItemListItem != null) {
                    oldIdCategoriaOfItemListItem.getItemList().remove(itemListItem);
                    oldIdCategoriaOfItemListItem = em.merge(oldIdCategoriaOfItemListItem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategoria(categoria.getIdCategoria()) != null) {
                throw new PreexistingEntityException("Categoria " + categoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getIdCategoria());
            List<Roles> rolesListOld = persistentCategoria.getRolesList();
            List<Roles> rolesListNew = categoria.getRolesList();
            List<Item> itemListOld = persistentCategoria.getItemList();
            List<Item> itemListNew = categoria.getItemList();
            List<String> illegalOrphanMessages = null;
            for (Roles rolesListOldRoles : rolesListOld) {
                if (!rolesListNew.contains(rolesListOldRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Roles " + rolesListOldRoles + " since its categoria field is not nullable.");
                }
            }
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Item " + itemListOldItem + " since its idCategoria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Roles> attachedRolesListNew = new ArrayList<Roles>();
            for (Roles rolesListNewRolesToAttach : rolesListNew) {
                rolesListNewRolesToAttach = em.getReference(rolesListNewRolesToAttach.getClass(), rolesListNewRolesToAttach.getRolesPK());
                attachedRolesListNew.add(rolesListNewRolesToAttach);
            }
            rolesListNew = attachedRolesListNew;
            categoria.setRolesList(rolesListNew);
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getItem());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            categoria.setItemList(itemListNew);
            categoria = em.merge(categoria);
            for (Roles rolesListNewRoles : rolesListNew) {
                if (!rolesListOld.contains(rolesListNewRoles)) {
                    Categoria oldCategoriaOfRolesListNewRoles = rolesListNewRoles.getCategoria();
                    rolesListNewRoles.setCategoria(categoria);
                    rolesListNewRoles = em.merge(rolesListNewRoles);
                    if (oldCategoriaOfRolesListNewRoles != null && !oldCategoriaOfRolesListNewRoles.equals(categoria)) {
                        oldCategoriaOfRolesListNewRoles.getRolesList().remove(rolesListNewRoles);
                        oldCategoriaOfRolesListNewRoles = em.merge(oldCategoriaOfRolesListNewRoles);
                    }
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    Categoria oldIdCategoriaOfItemListNewItem = itemListNewItem.getIdCategoria();
                    itemListNewItem.setIdCategoria(categoria);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldIdCategoriaOfItemListNewItem != null && !oldIdCategoriaOfItemListNewItem.equals(categoria)) {
                        oldIdCategoriaOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldIdCategoriaOfItemListNewItem = em.merge(oldIdCategoriaOfItemListNewItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = categoria.getIdCategoria();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Roles> rolesListOrphanCheck = categoria.getRolesList();
            for (Roles rolesListOrphanCheckRoles : rolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Roles " + rolesListOrphanCheckRoles + " in its rolesList field has a non-nullable categoria field.");
            }
            List<Item> itemListOrphanCheck = categoria.getItemList();
            for (Item itemListOrphanCheckItem : itemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Item " + itemListOrphanCheckItem + " in its itemList field has a non-nullable idCategoria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
