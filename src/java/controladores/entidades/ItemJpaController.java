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
import java.util.Collection;
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
        if (item.getRolesCollection() == null) {
            item.setRolesCollection(new ArrayList<Roles>());
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
            Collection<Roles> attachedRolesCollection = new ArrayList<Roles>();
            for (Roles rolesCollectionRolesToAttach : item.getRolesCollection()) {
                rolesCollectionRolesToAttach = em.getReference(rolesCollectionRolesToAttach.getClass(), rolesCollectionRolesToAttach.getRolesPK());
                attachedRolesCollection.add(rolesCollectionRolesToAttach);
            }
            item.setRolesCollection(attachedRolesCollection);
            em.persist(item);
            if (idCategoria != null) {
                idCategoria.getItemCollection().add(item);
                idCategoria = em.merge(idCategoria);
            }
            for (Roles rolesCollectionRoles : item.getRolesCollection()) {
                Item oldItemOfRolesCollectionRoles = rolesCollectionRoles.getItem();
                rolesCollectionRoles.setItem(item);
                rolesCollectionRoles = em.merge(rolesCollectionRoles);
                if (oldItemOfRolesCollectionRoles != null) {
                    oldItemOfRolesCollectionRoles.getRolesCollection().remove(rolesCollectionRoles);
                    oldItemOfRolesCollectionRoles = em.merge(oldItemOfRolesCollectionRoles);
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
            Collection<Roles> rolesCollectionOld = persistentItem.getRolesCollection();
            Collection<Roles> rolesCollectionNew = item.getRolesCollection();
            List<String> illegalOrphanMessages = null;
            for (Roles rolesCollectionOldRoles : rolesCollectionOld) {
                if (!rolesCollectionNew.contains(rolesCollectionOldRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Roles " + rolesCollectionOldRoles + " since its item field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                item.setIdCategoria(idCategoriaNew);
            }
            Collection<Roles> attachedRolesCollectionNew = new ArrayList<Roles>();
            for (Roles rolesCollectionNewRolesToAttach : rolesCollectionNew) {
                rolesCollectionNewRolesToAttach = em.getReference(rolesCollectionNewRolesToAttach.getClass(), rolesCollectionNewRolesToAttach.getRolesPK());
                attachedRolesCollectionNew.add(rolesCollectionNewRolesToAttach);
            }
            rolesCollectionNew = attachedRolesCollectionNew;
            item.setRolesCollection(rolesCollectionNew);
            item = em.merge(item);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getItemCollection().remove(item);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getItemCollection().add(item);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Roles rolesCollectionNewRoles : rolesCollectionNew) {
                if (!rolesCollectionOld.contains(rolesCollectionNewRoles)) {
                    Item oldItemOfRolesCollectionNewRoles = rolesCollectionNewRoles.getItem();
                    rolesCollectionNewRoles.setItem(item);
                    rolesCollectionNewRoles = em.merge(rolesCollectionNewRoles);
                    if (oldItemOfRolesCollectionNewRoles != null && !oldItemOfRolesCollectionNewRoles.equals(item)) {
                        oldItemOfRolesCollectionNewRoles.getRolesCollection().remove(rolesCollectionNewRoles);
                        oldItemOfRolesCollectionNewRoles = em.merge(oldItemOfRolesCollectionNewRoles);
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
            Collection<Roles> rolesCollectionOrphanCheck = item.getRolesCollection();
            for (Roles rolesCollectionOrphanCheckRoles : rolesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the Roles " + rolesCollectionOrphanCheckRoles + " in its rolesCollection field has a non-nullable item field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = item.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getItemCollection().remove(item);
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
