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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
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
        if (viaingresso.getEstudanteList() == null) {
            viaingresso.setEstudanteList(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estudante> attachedEstudanteList = new ArrayList<Estudante>();
            for (Estudante estudanteListEstudanteToAttach : viaingresso.getEstudanteList()) {
                estudanteListEstudanteToAttach = em.getReference(estudanteListEstudanteToAttach.getClass(), estudanteListEstudanteToAttach.getIdEstudante());
                attachedEstudanteList.add(estudanteListEstudanteToAttach);
            }
            viaingresso.setEstudanteList(attachedEstudanteList);
            em.persist(viaingresso);
            for (Estudante estudanteListEstudante : viaingresso.getEstudanteList()) {
                Viaingresso oldViaIngressoOfEstudanteListEstudante = estudanteListEstudante.getViaIngresso();
                estudanteListEstudante.setViaIngresso(viaingresso);
                estudanteListEstudante = em.merge(estudanteListEstudante);
                if (oldViaIngressoOfEstudanteListEstudante != null) {
                    oldViaIngressoOfEstudanteListEstudante.getEstudanteList().remove(estudanteListEstudante);
                    oldViaIngressoOfEstudanteListEstudante = em.merge(oldViaIngressoOfEstudanteListEstudante);
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
            List<Estudante> estudanteListOld = persistentViaingresso.getEstudanteList();
            List<Estudante> estudanteListNew = viaingresso.getEstudanteList();
            List<Estudante> attachedEstudanteListNew = new ArrayList<Estudante>();
            for (Estudante estudanteListNewEstudanteToAttach : estudanteListNew) {
                estudanteListNewEstudanteToAttach = em.getReference(estudanteListNewEstudanteToAttach.getClass(), estudanteListNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteListNew.add(estudanteListNewEstudanteToAttach);
            }
            estudanteListNew = attachedEstudanteListNew;
            viaingresso.setEstudanteList(estudanteListNew);
            viaingresso = em.merge(viaingresso);
            for (Estudante estudanteListOldEstudante : estudanteListOld) {
                if (!estudanteListNew.contains(estudanteListOldEstudante)) {
                    estudanteListOldEstudante.setViaIngresso(null);
                    estudanteListOldEstudante = em.merge(estudanteListOldEstudante);
                }
            }
            for (Estudante estudanteListNewEstudante : estudanteListNew) {
                if (!estudanteListOld.contains(estudanteListNewEstudante)) {
                    Viaingresso oldViaIngressoOfEstudanteListNewEstudante = estudanteListNewEstudante.getViaIngresso();
                    estudanteListNewEstudante.setViaIngresso(viaingresso);
                    estudanteListNewEstudante = em.merge(estudanteListNewEstudante);
                    if (oldViaIngressoOfEstudanteListNewEstudante != null && !oldViaIngressoOfEstudanteListNewEstudante.equals(viaingresso)) {
                        oldViaIngressoOfEstudanteListNewEstudante.getEstudanteList().remove(estudanteListNewEstudante);
                        oldViaIngressoOfEstudanteListNewEstudante = em.merge(oldViaIngressoOfEstudanteListNewEstudante);
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
            List<Estudante> estudanteList = viaingresso.getEstudanteList();
            for (Estudante estudanteListEstudante : estudanteList) {
                estudanteListEstudante.setViaIngresso(null);
                estudanteListEstudante = em.merge(estudanteListEstudante);
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
