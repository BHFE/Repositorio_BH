/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Estudante;
import entidades.Pais;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) {
        if (pais.getEstudanteCollection() == null) {
            pais.setEstudanteCollection(new ArrayList<Estudante>());
        }
        if (pais.getEstudanteCollection1() == null) {
            pais.setEstudanteCollection1(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Estudante> attachedEstudanteCollection = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionEstudanteToAttach : pais.getEstudanteCollection()) {
                estudanteCollectionEstudanteToAttach = em.getReference(estudanteCollectionEstudanteToAttach.getClass(), estudanteCollectionEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection.add(estudanteCollectionEstudanteToAttach);
            }
            pais.setEstudanteCollection(attachedEstudanteCollection);
            Collection<Estudante> attachedEstudanteCollection1 = new ArrayList<Estudante>();
            for (Estudante estudanteCollection1EstudanteToAttach : pais.getEstudanteCollection1()) {
                estudanteCollection1EstudanteToAttach = em.getReference(estudanteCollection1EstudanteToAttach.getClass(), estudanteCollection1EstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection1.add(estudanteCollection1EstudanteToAttach);
            }
            pais.setEstudanteCollection1(attachedEstudanteCollection1);
            em.persist(pais);
            for (Estudante estudanteCollectionEstudante : pais.getEstudanteCollection()) {
                Pais oldEscolaPaisOfEstudanteCollectionEstudante = estudanteCollectionEstudante.getEscolaPais();
                estudanteCollectionEstudante.setEscolaPais(pais);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
                if (oldEscolaPaisOfEstudanteCollectionEstudante != null) {
                    oldEscolaPaisOfEstudanteCollectionEstudante.getEstudanteCollection().remove(estudanteCollectionEstudante);
                    oldEscolaPaisOfEstudanteCollectionEstudante = em.merge(oldEscolaPaisOfEstudanteCollectionEstudante);
                }
            }
            for (Estudante estudanteCollection1Estudante : pais.getEstudanteCollection1()) {
                Pais oldNacionalidadeOfEstudanteCollection1Estudante = estudanteCollection1Estudante.getNacionalidade();
                estudanteCollection1Estudante.setNacionalidade(pais);
                estudanteCollection1Estudante = em.merge(estudanteCollection1Estudante);
                if (oldNacionalidadeOfEstudanteCollection1Estudante != null) {
                    oldNacionalidadeOfEstudanteCollection1Estudante.getEstudanteCollection1().remove(estudanteCollection1Estudante);
                    oldNacionalidadeOfEstudanteCollection1Estudante = em.merge(oldNacionalidadeOfEstudanteCollection1Estudante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getIdPais());
            Collection<Estudante> estudanteCollectionOld = persistentPais.getEstudanteCollection();
            Collection<Estudante> estudanteCollectionNew = pais.getEstudanteCollection();
            Collection<Estudante> estudanteCollection1Old = persistentPais.getEstudanteCollection1();
            Collection<Estudante> estudanteCollection1New = pais.getEstudanteCollection1();
            List<String> illegalOrphanMessages = null;
            for (Estudante estudanteCollection1OldEstudante : estudanteCollection1Old) {
                if (!estudanteCollection1New.contains(estudanteCollection1OldEstudante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudante " + estudanteCollection1OldEstudante + " since its nacionalidade field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Estudante> attachedEstudanteCollectionNew = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionNewEstudanteToAttach : estudanteCollectionNew) {
                estudanteCollectionNewEstudanteToAttach = em.getReference(estudanteCollectionNewEstudanteToAttach.getClass(), estudanteCollectionNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollectionNew.add(estudanteCollectionNewEstudanteToAttach);
            }
            estudanteCollectionNew = attachedEstudanteCollectionNew;
            pais.setEstudanteCollection(estudanteCollectionNew);
            Collection<Estudante> attachedEstudanteCollection1New = new ArrayList<Estudante>();
            for (Estudante estudanteCollection1NewEstudanteToAttach : estudanteCollection1New) {
                estudanteCollection1NewEstudanteToAttach = em.getReference(estudanteCollection1NewEstudanteToAttach.getClass(), estudanteCollection1NewEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection1New.add(estudanteCollection1NewEstudanteToAttach);
            }
            estudanteCollection1New = attachedEstudanteCollection1New;
            pais.setEstudanteCollection1(estudanteCollection1New);
            pais = em.merge(pais);
            for (Estudante estudanteCollectionOldEstudante : estudanteCollectionOld) {
                if (!estudanteCollectionNew.contains(estudanteCollectionOldEstudante)) {
                    estudanteCollectionOldEstudante.setEscolaPais(null);
                    estudanteCollectionOldEstudante = em.merge(estudanteCollectionOldEstudante);
                }
            }
            for (Estudante estudanteCollectionNewEstudante : estudanteCollectionNew) {
                if (!estudanteCollectionOld.contains(estudanteCollectionNewEstudante)) {
                    Pais oldEscolaPaisOfEstudanteCollectionNewEstudante = estudanteCollectionNewEstudante.getEscolaPais();
                    estudanteCollectionNewEstudante.setEscolaPais(pais);
                    estudanteCollectionNewEstudante = em.merge(estudanteCollectionNewEstudante);
                    if (oldEscolaPaisOfEstudanteCollectionNewEstudante != null && !oldEscolaPaisOfEstudanteCollectionNewEstudante.equals(pais)) {
                        oldEscolaPaisOfEstudanteCollectionNewEstudante.getEstudanteCollection().remove(estudanteCollectionNewEstudante);
                        oldEscolaPaisOfEstudanteCollectionNewEstudante = em.merge(oldEscolaPaisOfEstudanteCollectionNewEstudante);
                    }
                }
            }
            for (Estudante estudanteCollection1NewEstudante : estudanteCollection1New) {
                if (!estudanteCollection1Old.contains(estudanteCollection1NewEstudante)) {
                    Pais oldNacionalidadeOfEstudanteCollection1NewEstudante = estudanteCollection1NewEstudante.getNacionalidade();
                    estudanteCollection1NewEstudante.setNacionalidade(pais);
                    estudanteCollection1NewEstudante = em.merge(estudanteCollection1NewEstudante);
                    if (oldNacionalidadeOfEstudanteCollection1NewEstudante != null && !oldNacionalidadeOfEstudanteCollection1NewEstudante.equals(pais)) {
                        oldNacionalidadeOfEstudanteCollection1NewEstudante.getEstudanteCollection1().remove(estudanteCollection1NewEstudante);
                        oldNacionalidadeOfEstudanteCollection1NewEstudante = em.merge(oldNacionalidadeOfEstudanteCollection1NewEstudante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pais.getIdPais();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getIdPais();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Estudante> estudanteCollection1OrphanCheck = pais.getEstudanteCollection1();
            for (Estudante estudanteCollection1OrphanCheckEstudante : estudanteCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Estudante " + estudanteCollection1OrphanCheckEstudante + " in its estudanteCollection1 field has a non-nullable nacionalidade field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Estudante> estudanteCollection = pais.getEstudanteCollection();
            for (Estudante estudanteCollectionEstudante : estudanteCollection) {
                estudanteCollectionEstudante.setEscolaPais(null);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
