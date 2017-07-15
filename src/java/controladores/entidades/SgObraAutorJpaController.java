/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.entidades;

import controladores.entidades.exceptions.IllegalOrphanException;
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
import java.util.ArrayList;
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

    public void create(SgObraAutor sgObraAutor) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        SgAutor sgAutorOrphanCheck = sgObraAutor.getSgAutor();
        if (sgAutorOrphanCheck != null) {
            SgObraAutor oldSgObraAutorOfSgAutor = sgAutorOrphanCheck.getSgObraAutor();
            if (oldSgObraAutorOfSgAutor != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The SgAutor " + sgAutorOrphanCheck + " already has an item of type SgObraAutor whose sgAutor column cannot be null. Please make another selection for the sgAutor field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
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
            SgObra idlivro = sgObraAutor.getIdlivro();
            if (idlivro != null) {
                idlivro = em.getReference(idlivro.getClass(), idlivro.getIdlivro());
                sgObraAutor.setIdlivro(idlivro);
            }
            em.persist(sgObraAutor);
            if (sgAutor != null) {
                sgAutor.setSgObraAutor(sgObraAutor);
                sgAutor = em.merge(sgAutor);
            }
            if (idlivro != null) {
                idlivro.getSgObraAutorList().add(sgObraAutor);
                idlivro = em.merge(idlivro);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSgObraAutor(sgObraAutor.getIdautor()) != null) {
                throw new PreexistingEntityException("SgObraAutor " + sgObraAutor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgObraAutor sgObraAutor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraAutor persistentSgObraAutor = em.find(SgObraAutor.class, sgObraAutor.getIdautor());
            SgAutor sgAutorOld = persistentSgObraAutor.getSgAutor();
            SgAutor sgAutorNew = sgObraAutor.getSgAutor();
            SgObra idlivroOld = persistentSgObraAutor.getIdlivro();
            SgObra idlivroNew = sgObraAutor.getIdlivro();
            List<String> illegalOrphanMessages = null;
            if (sgAutorNew != null && !sgAutorNew.equals(sgAutorOld)) {
                SgObraAutor oldSgObraAutorOfSgAutor = sgAutorNew.getSgObraAutor();
                if (oldSgObraAutorOfSgAutor != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The SgAutor " + sgAutorNew + " already has an item of type SgObraAutor whose sgAutor column cannot be null. Please make another selection for the sgAutor field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (sgAutorNew != null) {
                sgAutorNew = em.getReference(sgAutorNew.getClass(), sgAutorNew.getIdautor());
                sgObraAutor.setSgAutor(sgAutorNew);
            }
            if (idlivroNew != null) {
                idlivroNew = em.getReference(idlivroNew.getClass(), idlivroNew.getIdlivro());
                sgObraAutor.setIdlivro(idlivroNew);
            }
            sgObraAutor = em.merge(sgObraAutor);
            if (sgAutorOld != null && !sgAutorOld.equals(sgAutorNew)) {
                sgAutorOld.setSgObraAutor(null);
                sgAutorOld = em.merge(sgAutorOld);
            }
            if (sgAutorNew != null && !sgAutorNew.equals(sgAutorOld)) {
                sgAutorNew.setSgObraAutor(sgObraAutor);
                sgAutorNew = em.merge(sgAutorNew);
            }
            if (idlivroOld != null && !idlivroOld.equals(idlivroNew)) {
                idlivroOld.getSgObraAutorList().remove(sgObraAutor);
                idlivroOld = em.merge(idlivroOld);
            }
            if (idlivroNew != null && !idlivroNew.equals(idlivroOld)) {
                idlivroNew.getSgObraAutorList().add(sgObraAutor);
                idlivroNew = em.merge(idlivroNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sgObraAutor.getIdautor();
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

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraAutor sgObraAutor;
            try {
                sgObraAutor = em.getReference(SgObraAutor.class, id);
                sgObraAutor.getIdautor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgObraAutor with id " + id + " no longer exists.", enfe);
            }
            SgAutor sgAutor = sgObraAutor.getSgAutor();
            if (sgAutor != null) {
                sgAutor.setSgObraAutor(null);
                sgAutor = em.merge(sgAutor);
            }
            SgObra idlivro = sgObraAutor.getIdlivro();
            if (idlivro != null) {
                idlivro.getSgObraAutorList().remove(sgObraAutor);
                idlivro = em.merge(idlivro);
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

    public SgObraAutor findSgObraAutor(Long id) {
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
