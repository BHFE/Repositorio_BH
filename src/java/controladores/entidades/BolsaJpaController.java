/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.Bolsa;
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
public class BolsaJpaController implements Serializable {

    public BolsaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bolsa bolsa) {
        if (bolsa.getEstudanteCollection() == null) {
            bolsa.setEstudanteCollection(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Estudante> attachedEstudanteCollection = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionEstudanteToAttach : bolsa.getEstudanteCollection()) {
                estudanteCollectionEstudanteToAttach = em.getReference(estudanteCollectionEstudanteToAttach.getClass(), estudanteCollectionEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection.add(estudanteCollectionEstudanteToAttach);
            }
            bolsa.setEstudanteCollection(attachedEstudanteCollection);
            em.persist(bolsa);
            for (Estudante estudanteCollectionEstudante : bolsa.getEstudanteCollection()) {
                Bolsa oldBolsaOfEstudanteCollectionEstudante = estudanteCollectionEstudante.getBolsa();
                estudanteCollectionEstudante.setBolsa(bolsa);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
                if (oldBolsaOfEstudanteCollectionEstudante != null) {
                    oldBolsaOfEstudanteCollectionEstudante.getEstudanteCollection().remove(estudanteCollectionEstudante);
                    oldBolsaOfEstudanteCollectionEstudante = em.merge(oldBolsaOfEstudanteCollectionEstudante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bolsa bolsa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bolsa persistentBolsa = em.find(Bolsa.class, bolsa.getIdBolsa());
            Collection<Estudante> estudanteCollectionOld = persistentBolsa.getEstudanteCollection();
            Collection<Estudante> estudanteCollectionNew = bolsa.getEstudanteCollection();
            Collection<Estudante> attachedEstudanteCollectionNew = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionNewEstudanteToAttach : estudanteCollectionNew) {
                estudanteCollectionNewEstudanteToAttach = em.getReference(estudanteCollectionNewEstudanteToAttach.getClass(), estudanteCollectionNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollectionNew.add(estudanteCollectionNewEstudanteToAttach);
            }
            estudanteCollectionNew = attachedEstudanteCollectionNew;
            bolsa.setEstudanteCollection(estudanteCollectionNew);
            bolsa = em.merge(bolsa);
            for (Estudante estudanteCollectionOldEstudante : estudanteCollectionOld) {
                if (!estudanteCollectionNew.contains(estudanteCollectionOldEstudante)) {
                    estudanteCollectionOldEstudante.setBolsa(null);
                    estudanteCollectionOldEstudante = em.merge(estudanteCollectionOldEstudante);
                }
            }
            for (Estudante estudanteCollectionNewEstudante : estudanteCollectionNew) {
                if (!estudanteCollectionOld.contains(estudanteCollectionNewEstudante)) {
                    Bolsa oldBolsaOfEstudanteCollectionNewEstudante = estudanteCollectionNewEstudante.getBolsa();
                    estudanteCollectionNewEstudante.setBolsa(bolsa);
                    estudanteCollectionNewEstudante = em.merge(estudanteCollectionNewEstudante);
                    if (oldBolsaOfEstudanteCollectionNewEstudante != null && !oldBolsaOfEstudanteCollectionNewEstudante.equals(bolsa)) {
                        oldBolsaOfEstudanteCollectionNewEstudante.getEstudanteCollection().remove(estudanteCollectionNewEstudante);
                        oldBolsaOfEstudanteCollectionNewEstudante = em.merge(oldBolsaOfEstudanteCollectionNewEstudante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = bolsa.getIdBolsa();
                if (findBolsa(id) == null) {
                    throw new NonexistentEntityException("The bolsa with id " + id + " no longer exists.");
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
            Bolsa bolsa;
            try {
                bolsa = em.getReference(Bolsa.class, id);
                bolsa.getIdBolsa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bolsa with id " + id + " no longer exists.", enfe);
            }
            Collection<Estudante> estudanteCollection = bolsa.getEstudanteCollection();
            for (Estudante estudanteCollectionEstudante : estudanteCollection) {
                estudanteCollectionEstudante.setBolsa(null);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
            }
            em.remove(bolsa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bolsa> findBolsaEntities() {
        return findBolsaEntities(true, -1, -1);
    }

    public List<Bolsa> findBolsaEntities(int maxResults, int firstResult) {
        return findBolsaEntities(false, maxResults, firstResult);
    }

    private List<Bolsa> findBolsaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bolsa.class));
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

    public Bolsa findBolsa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bolsa.class, id);
        } finally {
            em.close();
        }
    }

    public int getBolsaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bolsa> rt = cq.from(Bolsa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
