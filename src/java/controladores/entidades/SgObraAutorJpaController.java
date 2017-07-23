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
import entidades.SgAutor;
import entidades.SgObra;
import entidades.SgObraAutor;
import entidades.SgObraAutorPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class SgObraAutorJpaController implements Serializable {

    public SgObraAutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgObraAutor sgObraAutor) throws PreexistingEntityException, Exception {
        if (sgObraAutor.getSgObraAutorPK() == null) {
            sgObraAutor.setSgObraAutorPK(new SgObraAutorPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgAutor sgAutor = sgObraAutor.getSgAutor();
            if (sgAutor != null) {
                sgAutor = em.getReference(sgAutor.getClass(), sgAutor.getIdautor());
                sgObraAutor.setSgAutor(sgAutor);
            }
            SgObra sgObra = sgObraAutor.getSgObra();
            if (sgObra != null) {
                sgObra = em.getReference(sgObra.getClass(), sgObra.getIdlivro());
                sgObraAutor.setSgObra(sgObra);
            }
            em.persist(sgObraAutor);
            if (sgAutor != null) {
                sgAutor.getSgObraAutorList().add(sgObraAutor);
                sgAutor = em.merge(sgAutor);
            }
            if (sgObra != null) {
                sgObra.getSgObraAutorList().add(sgObraAutor);
                sgObra = em.merge(sgObra);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSgObraAutor(sgObraAutor.getSgObraAutorPK()) != null) {
                throw new PreexistingEntityException("SgObraAutor " + sgObraAutor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgObraAutor sgObraAutor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraAutor persistentSgObraAutor = em.find(SgObraAutor.class, sgObraAutor.getSgObraAutorPK());
            SgAutor sgAutorOld = persistentSgObraAutor.getSgAutor();
            SgAutor sgAutorNew = sgObraAutor.getSgAutor();
            SgObra sgObraOld = persistentSgObraAutor.getSgObra();
            SgObra sgObraNew = sgObraAutor.getSgObra();
            if (sgAutorNew != null) {
                sgAutorNew = em.getReference(sgAutorNew.getClass(), sgAutorNew.getIdautor());
                sgObraAutor.setSgAutor(sgAutorNew);
            }
            if (sgObraNew != null) {
                sgObraNew = em.getReference(sgObraNew.getClass(), sgObraNew.getIdlivro());
                sgObraAutor.setSgObra(sgObraNew);
            }
            sgObraAutor = em.merge(sgObraAutor);
            if (sgAutorOld != null && !sgAutorOld.equals(sgAutorNew)) {
                sgAutorOld.getSgObraAutorList().remove(sgObraAutor);
                sgAutorOld = em.merge(sgAutorOld);
            }
            if (sgAutorNew != null && !sgAutorNew.equals(sgAutorOld)) {
                sgAutorNew.getSgObraAutorList().add(sgObraAutor);
                sgAutorNew = em.merge(sgAutorNew);
            }
            if (sgObraOld != null && !sgObraOld.equals(sgObraNew)) {
                sgObraOld.getSgObraAutorList().remove(sgObraAutor);
                sgObraOld = em.merge(sgObraOld);
            }
            if (sgObraNew != null && !sgObraNew.equals(sgObraOld)) {
                sgObraNew.getSgObraAutorList().add(sgObraAutor);
                sgObraNew = em.merge(sgObraNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SgObraAutorPK id = sgObraAutor.getSgObraAutorPK();
                if (findSgObraAutor(id) == null) {
                    throw new NonexistentEntityException("The sgObraAutor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SgObraAutorPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraAutor sgObraAutor;
            try {
                sgObraAutor = em.getReference(SgObraAutor.class, id);
                sgObraAutor.getSgObraAutorPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgObraAutor with id " + id + " no longer exists.", enfe);
            }
            SgAutor sgAutor = sgObraAutor.getSgAutor();
            if (sgAutor != null) {
                sgAutor.getSgObraAutorList().remove(sgObraAutor);
                sgAutor = em.merge(sgAutor);
            }
            SgObra sgObra = sgObraAutor.getSgObra();
            if (sgObra != null) {
                sgObra.getSgObraAutorList().remove(sgObraAutor);
                sgObra = em.merge(sgObra);
            }
            em.remove(sgObraAutor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgObraAutor> findSgObraAutorEntities() {
        return findSgObraAutorEntities(true, -1, -1);
    }

    public List<SgObraAutor> findSgObraAutorEntities(int maxResults, int firstResult) {
        return findSgObraAutorEntities(false, maxResults, firstResult);
    }

    private List<SgObraAutor> findSgObraAutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgObraAutor.class));
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

    public SgObraAutor findSgObraAutor(SgObraAutorPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgObraAutor.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgObraAutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgObraAutor> rt = cq.from(SgObraAutor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
