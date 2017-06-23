/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Profissao;
import entidades.Provincia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class ProvinciaJpaController implements Serializable {

    public ProvinciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Provincia provincia) {
        if (provincia.getProfissaoCollection() == null) {
            provincia.setProfissaoCollection(new ArrayList<Profissao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Profissao> attachedProfissaoCollection = new ArrayList<Profissao>();
            for (Profissao profissaoCollectionProfissaoToAttach : provincia.getProfissaoCollection()) {
                profissaoCollectionProfissaoToAttach = em.getReference(profissaoCollectionProfissaoToAttach.getClass(), profissaoCollectionProfissaoToAttach.getIdEstudante());
                attachedProfissaoCollection.add(profissaoCollectionProfissaoToAttach);
            }
            provincia.setProfissaoCollection(attachedProfissaoCollection);
            em.persist(provincia);
            for (Profissao profissaoCollectionProfissao : provincia.getProfissaoCollection()) {
                Provincia oldProvinciaprOfProfissaoCollectionProfissao = profissaoCollectionProfissao.getProvinciapr();
                profissaoCollectionProfissao.setProvinciapr(provincia);
                profissaoCollectionProfissao = em.merge(profissaoCollectionProfissao);
                if (oldProvinciaprOfProfissaoCollectionProfissao != null) {
                    oldProvinciaprOfProfissaoCollectionProfissao.getProfissaoCollection().remove(profissaoCollectionProfissao);
                    oldProvinciaprOfProfissaoCollectionProfissao = em.merge(oldProvinciaprOfProfissaoCollectionProfissao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Provincia provincia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provincia persistentProvincia = em.find(Provincia.class, provincia.getIdProvincia());
            Collection<Profissao> profissaoCollectionOld = persistentProvincia.getProfissaoCollection();
            Collection<Profissao> profissaoCollectionNew = provincia.getProfissaoCollection();
            Collection<Profissao> attachedProfissaoCollectionNew = new ArrayList<Profissao>();
            for (Profissao profissaoCollectionNewProfissaoToAttach : profissaoCollectionNew) {
                profissaoCollectionNewProfissaoToAttach = em.getReference(profissaoCollectionNewProfissaoToAttach.getClass(), profissaoCollectionNewProfissaoToAttach.getIdEstudante());
                attachedProfissaoCollectionNew.add(profissaoCollectionNewProfissaoToAttach);
            }
            profissaoCollectionNew = attachedProfissaoCollectionNew;
            provincia.setProfissaoCollection(profissaoCollectionNew);
            provincia = em.merge(provincia);
            for (Profissao profissaoCollectionOldProfissao : profissaoCollectionOld) {
                if (!profissaoCollectionNew.contains(profissaoCollectionOldProfissao)) {
                    profissaoCollectionOldProfissao.setProvinciapr(null);
                    profissaoCollectionOldProfissao = em.merge(profissaoCollectionOldProfissao);
                }
            }
            for (Profissao profissaoCollectionNewProfissao : profissaoCollectionNew) {
                if (!profissaoCollectionOld.contains(profissaoCollectionNewProfissao)) {
                    Provincia oldProvinciaprOfProfissaoCollectionNewProfissao = profissaoCollectionNewProfissao.getProvinciapr();
                    profissaoCollectionNewProfissao.setProvinciapr(provincia);
                    profissaoCollectionNewProfissao = em.merge(profissaoCollectionNewProfissao);
                    if (oldProvinciaprOfProfissaoCollectionNewProfissao != null && !oldProvinciaprOfProfissaoCollectionNewProfissao.equals(provincia)) {
                        oldProvinciaprOfProfissaoCollectionNewProfissao.getProfissaoCollection().remove(profissaoCollectionNewProfissao);
                        oldProvinciaprOfProfissaoCollectionNewProfissao = em.merge(oldProvinciaprOfProfissaoCollectionNewProfissao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = provincia.getIdProvincia();
                if (findProvincia(id) == null) {
                    throw new NonexistentEntityException("The provincia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provincia provincia;
            try {
                provincia = em.getReference(Provincia.class, id);
                provincia.getIdProvincia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The provincia with id " + id + " no longer exists.", enfe);
            }
            Collection<Profissao> profissaoCollection = provincia.getProfissaoCollection();
            for (Profissao profissaoCollectionProfissao : profissaoCollection) {
                profissaoCollectionProfissao.setProvinciapr(null);
                profissaoCollectionProfissao = em.merge(profissaoCollectionProfissao);
            }
            em.remove(provincia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Provincia> findProvinciaEntities() {
        return findProvinciaEntities(true, -1, -1);
    }

    public List<Provincia> findProvinciaEntities(int maxResults, int firstResult) {
        return findProvinciaEntities(false, maxResults, firstResult);
    }

    private List<Provincia> findProvinciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Provincia.class));
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

    public Provincia findProvincia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Provincia.class, id);
        } finally {
            em.close();
        }
    }

    public int getProvinciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Provincia> rt = cq.from(Provincia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
