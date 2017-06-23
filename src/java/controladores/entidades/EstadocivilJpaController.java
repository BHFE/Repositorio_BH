/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import controladores.entidades.exceptions.PreexistingEntityException;
import entidades.Estadocivil;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Estudante;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class EstadocivilJpaController implements Serializable {

    public EstadocivilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estadocivil estadocivil) throws PreexistingEntityException, Exception {
        if (estadocivil.getEstudanteCollection() == null) {
            estadocivil.setEstudanteCollection(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Estudante> attachedEstudanteCollection = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionEstudanteToAttach : estadocivil.getEstudanteCollection()) {
                estudanteCollectionEstudanteToAttach = em.getReference(estudanteCollectionEstudanteToAttach.getClass(), estudanteCollectionEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection.add(estudanteCollectionEstudanteToAttach);
            }
            estadocivil.setEstudanteCollection(attachedEstudanteCollection);
            em.persist(estadocivil);
            for (Estudante estudanteCollectionEstudante : estadocivil.getEstudanteCollection()) {
                Estadocivil oldEstadoCivilOfEstudanteCollectionEstudante = estudanteCollectionEstudante.getEstadoCivil();
                estudanteCollectionEstudante.setEstadoCivil(estadocivil);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
                if (oldEstadoCivilOfEstudanteCollectionEstudante != null) {
                    oldEstadoCivilOfEstudanteCollectionEstudante.getEstudanteCollection().remove(estudanteCollectionEstudante);
                    oldEstadoCivilOfEstudanteCollectionEstudante = em.merge(oldEstadoCivilOfEstudanteCollectionEstudante);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadocivil(estadocivil.getIdEstado()) != null) {
                throw new PreexistingEntityException("Estadocivil " + estadocivil + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estadocivil estadocivil) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estadocivil persistentEstadocivil = em.find(Estadocivil.class, estadocivil.getIdEstado());
            Collection<Estudante> estudanteCollectionOld = persistentEstadocivil.getEstudanteCollection();
            Collection<Estudante> estudanteCollectionNew = estadocivil.getEstudanteCollection();
            Collection<Estudante> attachedEstudanteCollectionNew = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionNewEstudanteToAttach : estudanteCollectionNew) {
                estudanteCollectionNewEstudanteToAttach = em.getReference(estudanteCollectionNewEstudanteToAttach.getClass(), estudanteCollectionNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollectionNew.add(estudanteCollectionNewEstudanteToAttach);
            }
            estudanteCollectionNew = attachedEstudanteCollectionNew;
            estadocivil.setEstudanteCollection(estudanteCollectionNew);
            estadocivil = em.merge(estadocivil);
            for (Estudante estudanteCollectionOldEstudante : estudanteCollectionOld) {
                if (!estudanteCollectionNew.contains(estudanteCollectionOldEstudante)) {
                    estudanteCollectionOldEstudante.setEstadoCivil(null);
                    estudanteCollectionOldEstudante = em.merge(estudanteCollectionOldEstudante);
                }
            }
            for (Estudante estudanteCollectionNewEstudante : estudanteCollectionNew) {
                if (!estudanteCollectionOld.contains(estudanteCollectionNewEstudante)) {
                    Estadocivil oldEstadoCivilOfEstudanteCollectionNewEstudante = estudanteCollectionNewEstudante.getEstadoCivil();
                    estudanteCollectionNewEstudante.setEstadoCivil(estadocivil);
                    estudanteCollectionNewEstudante = em.merge(estudanteCollectionNewEstudante);
                    if (oldEstadoCivilOfEstudanteCollectionNewEstudante != null && !oldEstadoCivilOfEstudanteCollectionNewEstudante.equals(estadocivil)) {
                        oldEstadoCivilOfEstudanteCollectionNewEstudante.getEstudanteCollection().remove(estudanteCollectionNewEstudante);
                        oldEstadoCivilOfEstudanteCollectionNewEstudante = em.merge(oldEstadoCivilOfEstudanteCollectionNewEstudante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadocivil.getIdEstado();
                if (findEstadocivil(id) == null) {
                    throw new NonexistentEntityException("The estadocivil with id " + id + " no longer exists.");
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
            Estadocivil estadocivil;
            try {
                estadocivil = em.getReference(Estadocivil.class, id);
                estadocivil.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadocivil with id " + id + " no longer exists.", enfe);
            }
            Collection<Estudante> estudanteCollection = estadocivil.getEstudanteCollection();
            for (Estudante estudanteCollectionEstudante : estudanteCollection) {
                estudanteCollectionEstudante.setEstadoCivil(null);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
            }
            em.remove(estadocivil);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estadocivil> findEstadocivilEntities() {
        return findEstadocivilEntities(true, -1, -1);
    }

    public List<Estadocivil> findEstadocivilEntities(int maxResults, int firstResult) {
        return findEstadocivilEntities(false, maxResults, firstResult);
    }

    private List<Estadocivil> findEstadocivilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estadocivil.class));
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

    public Estadocivil findEstadocivil(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estadocivil.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadocivilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estadocivil> rt = cq.from(Estadocivil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
