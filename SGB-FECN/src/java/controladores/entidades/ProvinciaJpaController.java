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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
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
        if (provincia.getProfissaoList() == null) {
            provincia.setProfissaoList(new ArrayList<Profissao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Profissao> attachedProfissaoList = new ArrayList<Profissao>();
            for (Profissao profissaoListProfissaoToAttach : provincia.getProfissaoList()) {
                profissaoListProfissaoToAttach = em.getReference(profissaoListProfissaoToAttach.getClass(), profissaoListProfissaoToAttach.getIdEstudante());
                attachedProfissaoList.add(profissaoListProfissaoToAttach);
            }
            provincia.setProfissaoList(attachedProfissaoList);
            em.persist(provincia);
            for (Profissao profissaoListProfissao : provincia.getProfissaoList()) {
                Provincia oldProvinciaprOfProfissaoListProfissao = profissaoListProfissao.getProvinciapr();
                profissaoListProfissao.setProvinciapr(provincia);
                profissaoListProfissao = em.merge(profissaoListProfissao);
                if (oldProvinciaprOfProfissaoListProfissao != null) {
                    oldProvinciaprOfProfissaoListProfissao.getProfissaoList().remove(profissaoListProfissao);
                    oldProvinciaprOfProfissaoListProfissao = em.merge(oldProvinciaprOfProfissaoListProfissao);
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
            List<Profissao> profissaoListOld = persistentProvincia.getProfissaoList();
            List<Profissao> profissaoListNew = provincia.getProfissaoList();
            List<Profissao> attachedProfissaoListNew = new ArrayList<Profissao>();
            for (Profissao profissaoListNewProfissaoToAttach : profissaoListNew) {
                profissaoListNewProfissaoToAttach = em.getReference(profissaoListNewProfissaoToAttach.getClass(), profissaoListNewProfissaoToAttach.getIdEstudante());
                attachedProfissaoListNew.add(profissaoListNewProfissaoToAttach);
            }
            profissaoListNew = attachedProfissaoListNew;
            provincia.setProfissaoList(profissaoListNew);
            provincia = em.merge(provincia);
            for (Profissao profissaoListOldProfissao : profissaoListOld) {
                if (!profissaoListNew.contains(profissaoListOldProfissao)) {
                    profissaoListOldProfissao.setProvinciapr(null);
                    profissaoListOldProfissao = em.merge(profissaoListOldProfissao);
                }
            }
            for (Profissao profissaoListNewProfissao : profissaoListNew) {
                if (!profissaoListOld.contains(profissaoListNewProfissao)) {
                    Provincia oldProvinciaprOfProfissaoListNewProfissao = profissaoListNewProfissao.getProvinciapr();
                    profissaoListNewProfissao.setProvinciapr(provincia);
                    profissaoListNewProfissao = em.merge(profissaoListNewProfissao);
                    if (oldProvinciaprOfProfissaoListNewProfissao != null && !oldProvinciaprOfProfissaoListNewProfissao.equals(provincia)) {
                        oldProvinciaprOfProfissaoListNewProfissao.getProfissaoList().remove(profissaoListNewProfissao);
                        oldProvinciaprOfProfissaoListNewProfissao = em.merge(oldProvinciaprOfProfissaoListNewProfissao);
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
            List<Profissao> profissaoList = provincia.getProfissaoList();
            for (Profissao profissaoListProfissao : profissaoList) {
                profissaoListProfissao.setProvinciapr(null);
                profissaoListProfissao = em.merge(profissaoListProfissao);
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
