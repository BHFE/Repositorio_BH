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
import entidades.SgObra;
import entidades.SgObraCategoria;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
 */
public class SgObraCategoriaJpaController implements Serializable {

    public SgObraCategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgObraCategoria sgObraCategoria) throws PreexistingEntityException, Exception {
        if (sgObraCategoria.getSgObraList() == null) {
            sgObraCategoria.setSgObraList(new ArrayList<SgObra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SgObra> attachedSgObraList = new ArrayList<SgObra>();
            for (SgObra sgObraListSgObraToAttach : sgObraCategoria.getSgObraList()) {
                sgObraListSgObraToAttach = em.getReference(sgObraListSgObraToAttach.getClass(), sgObraListSgObraToAttach.getIdlivro());
                attachedSgObraList.add(sgObraListSgObraToAttach);
            }
            sgObraCategoria.setSgObraList(attachedSgObraList);
            em.persist(sgObraCategoria);
            for (SgObra sgObraListSgObra : sgObraCategoria.getSgObraList()) {
                SgObraCategoria oldDominioOfSgObraListSgObra = sgObraListSgObra.getDominio();
                sgObraListSgObra.setDominio(sgObraCategoria);
                sgObraListSgObra = em.merge(sgObraListSgObra);
                if (oldDominioOfSgObraListSgObra != null) {
                    oldDominioOfSgObraListSgObra.getSgObraList().remove(sgObraListSgObra);
                    oldDominioOfSgObraListSgObra = em.merge(oldDominioOfSgObraListSgObra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSgObraCategoria(sgObraCategoria.getCategoria()) != null) {
                throw new PreexistingEntityException("SgObraCategoria " + sgObraCategoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgObraCategoria sgObraCategoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraCategoria persistentSgObraCategoria = em.find(SgObraCategoria.class, sgObraCategoria.getCategoria());
            List<SgObra> sgObraListOld = persistentSgObraCategoria.getSgObraList();
            List<SgObra> sgObraListNew = sgObraCategoria.getSgObraList();
            List<SgObra> attachedSgObraListNew = new ArrayList<SgObra>();
            for (SgObra sgObraListNewSgObraToAttach : sgObraListNew) {
                sgObraListNewSgObraToAttach = em.getReference(sgObraListNewSgObraToAttach.getClass(), sgObraListNewSgObraToAttach.getIdlivro());
                attachedSgObraListNew.add(sgObraListNewSgObraToAttach);
            }
            sgObraListNew = attachedSgObraListNew;
            sgObraCategoria.setSgObraList(sgObraListNew);
            sgObraCategoria = em.merge(sgObraCategoria);
            for (SgObra sgObraListOldSgObra : sgObraListOld) {
                if (!sgObraListNew.contains(sgObraListOldSgObra)) {
                    sgObraListOldSgObra.setDominio(null);
                    sgObraListOldSgObra = em.merge(sgObraListOldSgObra);
                }
            }
            for (SgObra sgObraListNewSgObra : sgObraListNew) {
                if (!sgObraListOld.contains(sgObraListNewSgObra)) {
                    SgObraCategoria oldDominioOfSgObraListNewSgObra = sgObraListNewSgObra.getDominio();
                    sgObraListNewSgObra.setDominio(sgObraCategoria);
                    sgObraListNewSgObra = em.merge(sgObraListNewSgObra);
                    if (oldDominioOfSgObraListNewSgObra != null && !oldDominioOfSgObraListNewSgObra.equals(sgObraCategoria)) {
                        oldDominioOfSgObraListNewSgObra.getSgObraList().remove(sgObraListNewSgObra);
                        oldDominioOfSgObraListNewSgObra = em.merge(oldDominioOfSgObraListNewSgObra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = sgObraCategoria.getCategoria();
                if (findSgObraCategoria(id) == null) {
                    throw new NonexistentEntityException("The sgObraCategoria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObraCategoria sgObraCategoria;
            try {
                sgObraCategoria = em.getReference(SgObraCategoria.class, id);
                sgObraCategoria.getCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgObraCategoria with id " + id + " no longer exists.", enfe);
            }
            List<SgObra> sgObraList = sgObraCategoria.getSgObraList();
            for (SgObra sgObraListSgObra : sgObraList) {
                sgObraListSgObra.setDominio(null);
                sgObraListSgObra = em.merge(sgObraListSgObra);
            }
            em.remove(sgObraCategoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgObraCategoria> findSgObraCategoriaEntities() {
        return findSgObraCategoriaEntities(true, -1, -1);
    }

    public List<SgObraCategoria> findSgObraCategoriaEntities(int maxResults, int firstResult) {
        return findSgObraCategoriaEntities(false, maxResults, firstResult);
    }

    private List<SgObraCategoria> findSgObraCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgObraCategoria.class));
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

    public SgObraCategoria findSgObraCategoria(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgObraCategoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgObraCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgObraCategoria> rt = cq.from(SgObraCategoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
