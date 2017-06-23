/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Estudante;
import entidades.Viaingresso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class ViaingressoJpaController implements Serializable {

    public ViaingressoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Viaingresso viaingresso) throws PreexistingEntityException, Exception {
        if (viaingresso.getEstudanteCollection() == null) {
            viaingresso.setEstudanteCollection(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Estudante> attachedEstudanteCollection = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionEstudanteToAttach : viaingresso.getEstudanteCollection()) {
                estudanteCollectionEstudanteToAttach = em.getReference(estudanteCollectionEstudanteToAttach.getClass(), estudanteCollectionEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection.add(estudanteCollectionEstudanteToAttach);
            }
            viaingresso.setEstudanteCollection(attachedEstudanteCollection);
            em.persist(viaingresso);
            for (Estudante estudanteCollectionEstudante : viaingresso.getEstudanteCollection()) {
                Viaingresso oldViaIngressoOfEstudanteCollectionEstudante = estudanteCollectionEstudante.getViaIngresso();
                estudanteCollectionEstudante.setViaIngresso(viaingresso);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
                if (oldViaIngressoOfEstudanteCollectionEstudante != null) {
                    oldViaIngressoOfEstudanteCollectionEstudante.getEstudanteCollection().remove(estudanteCollectionEstudante);
                    oldViaIngressoOfEstudanteCollectionEstudante = em.merge(oldViaIngressoOfEstudanteCollectionEstudante);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findViaingresso(viaingresso.getIdViaIngresso()) != null) {
                throw new PreexistingEntityException("Viaingresso " + viaingresso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Viaingresso viaingresso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viaingresso persistentViaingresso = em.find(Viaingresso.class, viaingresso.getIdViaIngresso());
            Collection<Estudante> estudanteCollectionOld = persistentViaingresso.getEstudanteCollection();
            Collection<Estudante> estudanteCollectionNew = viaingresso.getEstudanteCollection();
            Collection<Estudante> attachedEstudanteCollectionNew = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionNewEstudanteToAttach : estudanteCollectionNew) {
                estudanteCollectionNewEstudanteToAttach = em.getReference(estudanteCollectionNewEstudanteToAttach.getClass(), estudanteCollectionNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollectionNew.add(estudanteCollectionNewEstudanteToAttach);
            }
            estudanteCollectionNew = attachedEstudanteCollectionNew;
            viaingresso.setEstudanteCollection(estudanteCollectionNew);
            viaingresso = em.merge(viaingresso);
            for (Estudante estudanteCollectionOldEstudante : estudanteCollectionOld) {
                if (!estudanteCollectionNew.contains(estudanteCollectionOldEstudante)) {
                    estudanteCollectionOldEstudante.setViaIngresso(null);
                    estudanteCollectionOldEstudante = em.merge(estudanteCollectionOldEstudante);
                }
            }
            for (Estudante estudanteCollectionNewEstudante : estudanteCollectionNew) {
                if (!estudanteCollectionOld.contains(estudanteCollectionNewEstudante)) {
                    Viaingresso oldViaIngressoOfEstudanteCollectionNewEstudante = estudanteCollectionNewEstudante.getViaIngresso();
                    estudanteCollectionNewEstudante.setViaIngresso(viaingresso);
                    estudanteCollectionNewEstudante = em.merge(estudanteCollectionNewEstudante);
                    if (oldViaIngressoOfEstudanteCollectionNewEstudante != null && !oldViaIngressoOfEstudanteCollectionNewEstudante.equals(viaingresso)) {
                        oldViaIngressoOfEstudanteCollectionNewEstudante.getEstudanteCollection().remove(estudanteCollectionNewEstudante);
                        oldViaIngressoOfEstudanteCollectionNewEstudante = em.merge(oldViaIngressoOfEstudanteCollectionNewEstudante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = viaingresso.getIdViaIngresso();
                if (findViaingresso(id) == null) {
                    throw new NonexistentEntityException("The viaingresso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viaingresso viaingresso;
            try {
                viaingresso = em.getReference(Viaingresso.class, id);
                viaingresso.getIdViaIngresso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viaingresso with id " + id + " no longer exists.", enfe);
            }
            Collection<Estudante> estudanteCollection = viaingresso.getEstudanteCollection();
            for (Estudante estudanteCollectionEstudante : estudanteCollection) {
                estudanteCollectionEstudante.setViaIngresso(null);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
            }
            em.remove(viaingresso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Viaingresso> findViaingressoEntities() {
        return findViaingressoEntities(true, -1, -1);
    }

    public List<Viaingresso> findViaingressoEntities(int maxResults, int firstResult) {
        return findViaingressoEntities(false, maxResults, firstResult);
    }

    private List<Viaingresso> findViaingressoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Viaingresso.class));
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

    public Viaingresso findViaingresso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Viaingresso.class, id);
        } finally {
            em.close();
        }
    }

    public int getViaingressoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Viaingresso> rt = cq.from(Viaingresso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
