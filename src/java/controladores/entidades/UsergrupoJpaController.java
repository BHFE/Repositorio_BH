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
import entidades.Grupo;
import entidades.Usergrupo;
import entidades.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class UsergrupoJpaController implements Serializable {

    public UsergrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usergrupo usergrupo) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Grupo grupoOrphanCheck = usergrupo.getGrupo();
        if (grupoOrphanCheck != null) {
            Usergrupo oldUsergrupoOfGrupo = grupoOrphanCheck.getUsergrupo();
            if (oldUsergrupoOfGrupo != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Grupo " + grupoOrphanCheck + " already has an item of type Usergrupo whose grupo column cannot be null. Please make another selection for the grupo field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupo = usergrupo.getGrupo();
            if (grupo != null) {
                grupo = em.getReference(grupo.getClass(), grupo.getIdGrupo());
                usergrupo.setGrupo(grupo);
            }
            Users utilizador = usergrupo.getUtilizador();
            if (utilizador != null) {
                utilizador = em.getReference(utilizador.getClass(), utilizador.getUtilizador());
                usergrupo.setUtilizador(utilizador);
            }
            em.persist(usergrupo);
            if (grupo != null) {
                grupo.setUsergrupo(usergrupo);
                grupo = em.merge(grupo);
            }
            if (utilizador != null) {
                utilizador.getUsergrupoList().add(usergrupo);
                utilizador = em.merge(utilizador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsergrupo(usergrupo.getIdGrupo()) != null) {
                throw new PreexistingEntityException("Usergrupo " + usergrupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usergrupo usergrupo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usergrupo persistentUsergrupo = em.find(Usergrupo.class, usergrupo.getIdGrupo());
            Grupo grupoOld = persistentUsergrupo.getGrupo();
            Grupo grupoNew = usergrupo.getGrupo();
            Users utilizadorOld = persistentUsergrupo.getUtilizador();
            Users utilizadorNew = usergrupo.getUtilizador();
            List<String> illegalOrphanMessages = null;
            if (grupoNew != null && !grupoNew.equals(grupoOld)) {
                Usergrupo oldUsergrupoOfGrupo = grupoNew.getUsergrupo();
                if (oldUsergrupoOfGrupo != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Grupo " + grupoNew + " already has an item of type Usergrupo whose grupo column cannot be null. Please make another selection for the grupo field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (grupoNew != null) {
                grupoNew = em.getReference(grupoNew.getClass(), grupoNew.getIdGrupo());
                usergrupo.setGrupo(grupoNew);
            }
            if (utilizadorNew != null) {
                utilizadorNew = em.getReference(utilizadorNew.getClass(), utilizadorNew.getUtilizador());
                usergrupo.setUtilizador(utilizadorNew);
            }
            usergrupo = em.merge(usergrupo);
            if (grupoOld != null && !grupoOld.equals(grupoNew)) {
                grupoOld.setUsergrupo(null);
                grupoOld = em.merge(grupoOld);
            }
            if (grupoNew != null && !grupoNew.equals(grupoOld)) {
                grupoNew.setUsergrupo(usergrupo);
                grupoNew = em.merge(grupoNew);
            }
            if (utilizadorOld != null && !utilizadorOld.equals(utilizadorNew)) {
                utilizadorOld.getUsergrupoList().remove(usergrupo);
                utilizadorOld = em.merge(utilizadorOld);
            }
            if (utilizadorNew != null && !utilizadorNew.equals(utilizadorOld)) {
                utilizadorNew.getUsergrupoList().add(usergrupo);
                utilizadorNew = em.merge(utilizadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usergrupo.getIdGrupo();
                if (findUsergrupo(id) == null) {
                    throw new NonexistentEntityException("The usergrupo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usergrupo usergrupo;
            try {
                usergrupo = em.getReference(Usergrupo.class, id);
                usergrupo.getIdGrupo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usergrupo with id " + id + " no longer exists.", enfe);
            }
            Grupo grupo = usergrupo.getGrupo();
            if (grupo != null) {
                grupo.setUsergrupo(null);
                grupo = em.merge(grupo);
            }
            Users utilizador = usergrupo.getUtilizador();
            if (utilizador != null) {
                utilizador.getUsergrupoList().remove(usergrupo);
                utilizador = em.merge(utilizador);
            }
            em.remove(usergrupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usergrupo> findUsergrupoEntities() {
        return findUsergrupoEntities(true, -1, -1);
    }

    public List<Usergrupo> findUsergrupoEntities(int maxResults, int firstResult) {
        return findUsergrupoEntities(false, maxResults, firstResult);
    }

    private List<Usergrupo> findUsergrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usergrupo.class));
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

    public Usergrupo findUsergrupo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usergrupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsergrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usergrupo> rt = cq.from(Usergrupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
