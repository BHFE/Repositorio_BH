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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
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
        if (bolsa.getEstudanteList() == null) {
            bolsa.setEstudanteList(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estudante> attachedEstudanteList = new ArrayList<Estudante>();
            for (Estudante estudanteListEstudanteToAttach : bolsa.getEstudanteList()) {
                estudanteListEstudanteToAttach = em.getReference(estudanteListEstudanteToAttach.getClass(), estudanteListEstudanteToAttach.getIdEstudante());
                attachedEstudanteList.add(estudanteListEstudanteToAttach);
            }
            bolsa.setEstudanteList(attachedEstudanteList);
            em.persist(bolsa);
            for (Estudante estudanteListEstudante : bolsa.getEstudanteList()) {
                Bolsa oldBolsaOfEstudanteListEstudante = estudanteListEstudante.getBolsa();
                estudanteListEstudante.setBolsa(bolsa);
                estudanteListEstudante = em.merge(estudanteListEstudante);
                if (oldBolsaOfEstudanteListEstudante != null) {
                    oldBolsaOfEstudanteListEstudante.getEstudanteList().remove(estudanteListEstudante);
                    oldBolsaOfEstudanteListEstudante = em.merge(oldBolsaOfEstudanteListEstudante);
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
            List<Estudante> estudanteListOld = persistentBolsa.getEstudanteList();
            List<Estudante> estudanteListNew = bolsa.getEstudanteList();
            List<Estudante> attachedEstudanteListNew = new ArrayList<Estudante>();
            for (Estudante estudanteListNewEstudanteToAttach : estudanteListNew) {
                estudanteListNewEstudanteToAttach = em.getReference(estudanteListNewEstudanteToAttach.getClass(), estudanteListNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteListNew.add(estudanteListNewEstudanteToAttach);
            }
            estudanteListNew = attachedEstudanteListNew;
            bolsa.setEstudanteList(estudanteListNew);
            bolsa = em.merge(bolsa);
            for (Estudante estudanteListOldEstudante : estudanteListOld) {
                if (!estudanteListNew.contains(estudanteListOldEstudante)) {
                    estudanteListOldEstudante.setBolsa(null);
                    estudanteListOldEstudante = em.merge(estudanteListOldEstudante);
                }
            }
            for (Estudante estudanteListNewEstudante : estudanteListNew) {
                if (!estudanteListOld.contains(estudanteListNewEstudante)) {
                    Bolsa oldBolsaOfEstudanteListNewEstudante = estudanteListNewEstudante.getBolsa();
                    estudanteListNewEstudante.setBolsa(bolsa);
                    estudanteListNewEstudante = em.merge(estudanteListNewEstudante);
                    if (oldBolsaOfEstudanteListNewEstudante != null && !oldBolsaOfEstudanteListNewEstudante.equals(bolsa)) {
                        oldBolsaOfEstudanteListNewEstudante.getEstudanteList().remove(estudanteListNewEstudante);
                        oldBolsaOfEstudanteListNewEstudante = em.merge(oldBolsaOfEstudanteListNewEstudante);
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
            List<Estudante> estudanteList = bolsa.getEstudanteList();
            for (Estudante estudanteListEstudante : estudanteList) {
                estudanteListEstudante.setBolsa(null);
                estudanteListEstudante = em.merge(estudanteListEstudante);
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
