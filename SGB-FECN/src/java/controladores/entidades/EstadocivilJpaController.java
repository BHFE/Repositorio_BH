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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
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
        if (estadocivil.getEstudanteList() == null) {
            estadocivil.setEstudanteList(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estudante> attachedEstudanteList = new ArrayList<Estudante>();
            for (Estudante estudanteListEstudanteToAttach : estadocivil.getEstudanteList()) {
                estudanteListEstudanteToAttach = em.getReference(estudanteListEstudanteToAttach.getClass(), estudanteListEstudanteToAttach.getIdEstudante());
                attachedEstudanteList.add(estudanteListEstudanteToAttach);
            }
            estadocivil.setEstudanteList(attachedEstudanteList);
            em.persist(estadocivil);
            for (Estudante estudanteListEstudante : estadocivil.getEstudanteList()) {
                Estadocivil oldEstadoCivilOfEstudanteListEstudante = estudanteListEstudante.getEstadoCivil();
                estudanteListEstudante.setEstadoCivil(estadocivil);
                estudanteListEstudante = em.merge(estudanteListEstudante);
                if (oldEstadoCivilOfEstudanteListEstudante != null) {
                    oldEstadoCivilOfEstudanteListEstudante.getEstudanteList().remove(estudanteListEstudante);
                    oldEstadoCivilOfEstudanteListEstudante = em.merge(oldEstadoCivilOfEstudanteListEstudante);
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
            List<Estudante> estudanteListOld = persistentEstadocivil.getEstudanteList();
            List<Estudante> estudanteListNew = estadocivil.getEstudanteList();
            List<Estudante> attachedEstudanteListNew = new ArrayList<Estudante>();
            for (Estudante estudanteListNewEstudanteToAttach : estudanteListNew) {
                estudanteListNewEstudanteToAttach = em.getReference(estudanteListNewEstudanteToAttach.getClass(), estudanteListNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteListNew.add(estudanteListNewEstudanteToAttach);
            }
            estudanteListNew = attachedEstudanteListNew;
            estadocivil.setEstudanteList(estudanteListNew);
            estadocivil = em.merge(estadocivil);
            for (Estudante estudanteListOldEstudante : estudanteListOld) {
                if (!estudanteListNew.contains(estudanteListOldEstudante)) {
                    estudanteListOldEstudante.setEstadoCivil(null);
                    estudanteListOldEstudante = em.merge(estudanteListOldEstudante);
                }
            }
            for (Estudante estudanteListNewEstudante : estudanteListNew) {
                if (!estudanteListOld.contains(estudanteListNewEstudante)) {
                    Estadocivil oldEstadoCivilOfEstudanteListNewEstudante = estudanteListNewEstudante.getEstadoCivil();
                    estudanteListNewEstudante.setEstadoCivil(estadocivil);
                    estudanteListNewEstudante = em.merge(estudanteListNewEstudante);
                    if (oldEstadoCivilOfEstudanteListNewEstudante != null && !oldEstadoCivilOfEstudanteListNewEstudante.equals(estadocivil)) {
                        oldEstadoCivilOfEstudanteListNewEstudante.getEstudanteList().remove(estudanteListNewEstudante);
                        oldEstadoCivilOfEstudanteListNewEstudante = em.merge(oldEstadoCivilOfEstudanteListNewEstudante);
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
            List<Estudante> estudanteList = estadocivil.getEstudanteList();
            for (Estudante estudanteListEstudante : estudanteList) {
                estudanteListEstudante.setEstadoCivil(null);
                estudanteListEstudante = em.merge(estudanteListEstudante);
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
