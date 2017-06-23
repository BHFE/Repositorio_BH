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
import java.util.Collection;
import entidades.Item;
import java.util.List;
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
        if (categoria.getRolesCollection() == null) {
            categoria.setRolesCollection(new ArrayList<Roles>());
        }
        if (categoria.getItemCollection() == null) {
            categoria.setItemCollection(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Roles> attachedRolesCollection = new ArrayList<Roles>();
            for (Roles rolesCollectionRolesToAttach : categoria.getRolesCollection()) {
                rolesCollectionRolesToAttach = em.getReference(rolesCollectionRolesToAttach.getClass(), rolesCollectionRolesToAttach.getRolesPK());
                attachedRolesCollection.add(rolesCollectionRolesToAttach);
            }
            categoria.setRolesCollection(attachedRolesCollection);
            Collection<Item> attachedItemCollection = new ArrayList<Item>();
            for (Item itemCollectionItemToAttach : categoria.getItemCollection()) {
                itemCollectionItemToAttach = em.getReference(itemCollectionItemToAttach.getClass(), itemCollectionItemToAttach.getItem());
                attachedItemCollection.add(itemCollectionItemToAttach);
            }
            categoria.setItemCollection(attachedItemCollection);
            em.persist(categoria);
            for (Roles rolesCollectionRoles : categoria.getRolesCollection()) {
                Categoria oldCategoriaOfRolesCollectionRoles = rolesCollectionRoles.getCategoria();
                rolesCollectionRoles.setCategoria(categoria);
                rolesCollectionRoles = em.merge(rolesCollectionRoles);
                if (oldCategoriaOfRolesCollectionRoles != null) {
                    oldCategoriaOfRolesCollectionRoles.getRolesCollection().remove(rolesCollectionRoles);
                    oldCategoriaOfRolesCollectionRoles = em.merge(oldCategoriaOfRolesCollectionRoles);
                }
            }
            for (Item itemCollectionItem : categoria.getItemCollection()) {
                Categoria oldIdCategoriaOfItemCollectionItem = itemCollectionItem.getIdCategoria();
                itemCollectionItem.setIdCategoria(categoria);
                itemCollectionItem = em.merge(itemCollectionItem);
                if (oldIdCategoriaOfItemCollectionItem != null) {
                    oldIdCategoriaOfItemCollectionItem.getItemCollection().remove(itemCollectionItem);
                    oldIdCategoriaOfItemCollectionItem = em.merge(oldIdCategoriaOfItemCollectionItem);
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
            Collection<Roles> rolesCollectionOld = persistentCategoria.getRolesCollection();
            Collection<Roles> rolesCollectionNew = categoria.getRolesCollection();
            Collection<Item> itemCollectionOld = persistentCategoria.getItemCollection();
            Collection<Item> itemCollectionNew = categoria.getItemCollection();
            List<String> illegalOrphanMessages = null;
            for (Roles rolesCollectionOldRoles : rolesCollectionOld) {
                if (!rolesCollectionNew.contains(rolesCollectionOldRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Roles " + rolesCollectionOldRoles + " since its categoria field is not nullable.");
                }
            }
            for (Item itemCollectionOldItem : itemCollectionOld) {
                if (!itemCollectionNew.contains(itemCollectionOldItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Item " + itemCollectionOldItem + " since its idCategoria field is not nullable.");
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
            categoria.setRolesCollection(rolesCollectionNew);
            Collection<Item> attachedItemCollectionNew = new ArrayList<Item>();
            for (Item itemCollectionNewItemToAttach : itemCollectionNew) {
                itemCollectionNewItemToAttach = em.getReference(itemCollectionNewItemToAttach.getClass(), itemCollectionNewItemToAttach.getItem());
                attachedItemCollectionNew.add(itemCollectionNewItemToAttach);
            }
            itemCollectionNew = attachedItemCollectionNew;
            categoria.setItemCollection(itemCollectionNew);
            categoria = em.merge(categoria);
            for (Roles rolesCollectionNewRoles : rolesCollectionNew) {
                if (!rolesCollectionOld.contains(rolesCollectionNewRoles)) {
                    Categoria oldCategoriaOfRolesCollectionNewRoles = rolesCollectionNewRoles.getCategoria();
                    rolesCollectionNewRoles.setCategoria(categoria);
                    rolesCollectionNewRoles = em.merge(rolesCollectionNewRoles);
                    if (oldCategoriaOfRolesCollectionNewRoles != null && !oldCategoriaOfRolesCollectionNewRoles.equals(categoria)) {
                        oldCategoriaOfRolesCollectionNewRoles.getRolesCollection().remove(rolesCollectionNewRoles);
                        oldCategoriaOfRolesCollectionNewRoles = em.merge(oldCategoriaOfRolesCollectionNewRoles);
                    }
                }
            }
            for (Item itemCollectionNewItem : itemCollectionNew) {
                if (!itemCollectionOld.contains(itemCollectionNewItem)) {
                    Categoria oldIdCategoriaOfItemCollectionNewItem = itemCollectionNewItem.getIdCategoria();
                    itemCollectionNewItem.setIdCategoria(categoria);
                    itemCollectionNewItem = em.merge(itemCollectionNewItem);
                    if (oldIdCategoriaOfItemCollectionNewItem != null && !oldIdCategoriaOfItemCollectionNewItem.equals(categoria)) {
                        oldIdCategoriaOfItemCollectionNewItem.getItemCollection().remove(itemCollectionNewItem);
                        oldIdCategoriaOfItemCollectionNewItem = em.merge(oldIdCategoriaOfItemCollectionNewItem);
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
            Collection<Roles> rolesCollectionOrphanCheck = categoria.getRolesCollection();
            for (Roles rolesCollectionOrphanCheckRoles : rolesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Roles " + rolesCollectionOrphanCheckRoles + " in its rolesCollection field has a non-nullable categoria field.");
            }
            Collection<Item> itemCollectionOrphanCheck = categoria.getItemCollection();
            for (Item itemCollectionOrphanCheckItem : itemCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Item " + itemCollectionOrphanCheckItem + " in its itemCollection field has a non-nullable idCategoria field.");
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
