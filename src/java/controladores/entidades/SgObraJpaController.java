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
import entidades.Curso;
import entidades.SgObraArea;
import entidades.SgObraCategoria;
import entidades.Users;
import entidades.SgObraAutor;
import java.util.ArrayList;
import java.util.Collection;
import entidades.SgExemplar;
import entidades.SgObra;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class SgObraJpaController implements Serializable {

    public SgObraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SgObra sgObra) {
        if (sgObra.getSgObraAutorCollection() == null) {
            sgObra.setSgObraAutorCollection(new ArrayList<SgObraAutor>());
        }
        if (sgObra.getSgExemplarCollection() == null) {
            sgObra.setSgExemplarCollection(new ArrayList<SgExemplar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso curso = sgObra.getCurso();
            if (curso != null) {
                curso = em.getReference(curso.getClass(), curso.getIdCurso());
                sgObra.setCurso(curso);
            }
            SgObraArea area = sgObra.getArea();
            if (area != null) {
                area = em.getReference(area.getClass(), area.getIdarea());
                sgObra.setArea(area);
            }
            SgObraCategoria dominio = sgObra.getDominio();
            if (dominio != null) {
                dominio = em.getReference(dominio.getClass(), dominio.getCategoria());
                sgObra.setDominio(dominio);
            }
            Users bibliotecario = sgObra.getBibliotecario();
            if (bibliotecario != null) {
                bibliotecario = em.getReference(bibliotecario.getClass(), bibliotecario.getUtilizador());
                sgObra.setBibliotecario(bibliotecario);
            }
            Collection<SgObraAutor> attachedSgObraAutorCollection = new ArrayList<SgObraAutor>();
            for (SgObraAutor sgObraAutorCollectionSgObraAutorToAttach : sgObra.getSgObraAutorCollection()) {
                sgObraAutorCollectionSgObraAutorToAttach = em.getReference(sgObraAutorCollectionSgObraAutorToAttach.getClass(), sgObraAutorCollectionSgObraAutorToAttach.getSgObraAutorPK());
                attachedSgObraAutorCollection.add(sgObraAutorCollectionSgObraAutorToAttach);
            }
            sgObra.setSgObraAutorCollection(attachedSgObraAutorCollection);
            Collection<SgExemplar> attachedSgExemplarCollection = new ArrayList<SgExemplar>();
            for (SgExemplar sgExemplarCollectionSgExemplarToAttach : sgObra.getSgExemplarCollection()) {
                sgExemplarCollectionSgExemplarToAttach = em.getReference(sgExemplarCollectionSgExemplarToAttach.getClass(), sgExemplarCollectionSgExemplarToAttach.getNrRegisto());
                attachedSgExemplarCollection.add(sgExemplarCollectionSgExemplarToAttach);
            }
            sgObra.setSgExemplarCollection(attachedSgExemplarCollection);
            em.persist(sgObra);
            if (curso != null) {
                curso.getSgObraCollection().add(sgObra);
                curso = em.merge(curso);
            }
            if (area != null) {
                area.getSgObraCollection().add(sgObra);
                area = em.merge(area);
            }
            if (dominio != null) {
                dominio.getSgObraCollection().add(sgObra);
                dominio = em.merge(dominio);
            }
            if (bibliotecario != null) {
                bibliotecario.getSgObraCollection().add(sgObra);
                bibliotecario = em.merge(bibliotecario);
            }
            for (SgObraAutor sgObraAutorCollectionSgObraAutor : sgObra.getSgObraAutorCollection()) {
                SgObra oldSgObraOfSgObraAutorCollectionSgObraAutor = sgObraAutorCollectionSgObraAutor.getSgObra();
                sgObraAutorCollectionSgObraAutor.setSgObra(sgObra);
                sgObraAutorCollectionSgObraAutor = em.merge(sgObraAutorCollectionSgObraAutor);
                if (oldSgObraOfSgObraAutorCollectionSgObraAutor != null) {
                    oldSgObraOfSgObraAutorCollectionSgObraAutor.getSgObraAutorCollection().remove(sgObraAutorCollectionSgObraAutor);
                    oldSgObraOfSgObraAutorCollectionSgObraAutor = em.merge(oldSgObraOfSgObraAutorCollectionSgObraAutor);
                }
            }
            for (SgExemplar sgExemplarCollectionSgExemplar : sgObra.getSgExemplarCollection()) {
                SgObra oldObraRefOfSgExemplarCollectionSgExemplar = sgExemplarCollectionSgExemplar.getObraRef();
                sgExemplarCollectionSgExemplar.setObraRef(sgObra);
                sgExemplarCollectionSgExemplar = em.merge(sgExemplarCollectionSgExemplar);
                if (oldObraRefOfSgExemplarCollectionSgExemplar != null) {
                    oldObraRefOfSgExemplarCollectionSgExemplar.getSgExemplarCollection().remove(sgExemplarCollectionSgExemplar);
                    oldObraRefOfSgExemplarCollectionSgExemplar = em.merge(oldObraRefOfSgExemplarCollectionSgExemplar);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SgObra sgObra) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObra persistentSgObra = em.find(SgObra.class, sgObra.getIdlivro());
            Curso cursoOld = persistentSgObra.getCurso();
            Curso cursoNew = sgObra.getCurso();
            SgObraArea areaOld = persistentSgObra.getArea();
            SgObraArea areaNew = sgObra.getArea();
            SgObraCategoria dominioOld = persistentSgObra.getDominio();
            SgObraCategoria dominioNew = sgObra.getDominio();
            Users bibliotecarioOld = persistentSgObra.getBibliotecario();
            Users bibliotecarioNew = sgObra.getBibliotecario();
            Collection<SgObraAutor> sgObraAutorCollectionOld = persistentSgObra.getSgObraAutorCollection();
            Collection<SgObraAutor> sgObraAutorCollectionNew = sgObra.getSgObraAutorCollection();
            Collection<SgExemplar> sgExemplarCollectionOld = persistentSgObra.getSgExemplarCollection();
            Collection<SgExemplar> sgExemplarCollectionNew = sgObra.getSgExemplarCollection();
            List<String> illegalOrphanMessages = null;
            for (SgObraAutor sgObraAutorCollectionOldSgObraAutor : sgObraAutorCollectionOld) {
                if (!sgObraAutorCollectionNew.contains(sgObraAutorCollectionOldSgObraAutor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SgObraAutor " + sgObraAutorCollectionOldSgObraAutor + " since its sgObra field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cursoNew != null) {
                cursoNew = em.getReference(cursoNew.getClass(), cursoNew.getIdCurso());
                sgObra.setCurso(cursoNew);
            }
            if (areaNew != null) {
                areaNew = em.getReference(areaNew.getClass(), areaNew.getIdarea());
                sgObra.setArea(areaNew);
            }
            if (dominioNew != null) {
                dominioNew = em.getReference(dominioNew.getClass(), dominioNew.getCategoria());
                sgObra.setDominio(dominioNew);
            }
            if (bibliotecarioNew != null) {
                bibliotecarioNew = em.getReference(bibliotecarioNew.getClass(), bibliotecarioNew.getUtilizador());
                sgObra.setBibliotecario(bibliotecarioNew);
            }
            Collection<SgObraAutor> attachedSgObraAutorCollectionNew = new ArrayList<SgObraAutor>();
            for (SgObraAutor sgObraAutorCollectionNewSgObraAutorToAttach : sgObraAutorCollectionNew) {
                sgObraAutorCollectionNewSgObraAutorToAttach = em.getReference(sgObraAutorCollectionNewSgObraAutorToAttach.getClass(), sgObraAutorCollectionNewSgObraAutorToAttach.getSgObraAutorPK());
                attachedSgObraAutorCollectionNew.add(sgObraAutorCollectionNewSgObraAutorToAttach);
            }
            sgObraAutorCollectionNew = attachedSgObraAutorCollectionNew;
            sgObra.setSgObraAutorCollection(sgObraAutorCollectionNew);
            Collection<SgExemplar> attachedSgExemplarCollectionNew = new ArrayList<SgExemplar>();
            for (SgExemplar sgExemplarCollectionNewSgExemplarToAttach : sgExemplarCollectionNew) {
                sgExemplarCollectionNewSgExemplarToAttach = em.getReference(sgExemplarCollectionNewSgExemplarToAttach.getClass(), sgExemplarCollectionNewSgExemplarToAttach.getNrRegisto());
                attachedSgExemplarCollectionNew.add(sgExemplarCollectionNewSgExemplarToAttach);
            }
            sgExemplarCollectionNew = attachedSgExemplarCollectionNew;
            sgObra.setSgExemplarCollection(sgExemplarCollectionNew);
            sgObra = em.merge(sgObra);
            if (cursoOld != null && !cursoOld.equals(cursoNew)) {
                cursoOld.getSgObraCollection().remove(sgObra);
                cursoOld = em.merge(cursoOld);
            }
            if (cursoNew != null && !cursoNew.equals(cursoOld)) {
                cursoNew.getSgObraCollection().add(sgObra);
                cursoNew = em.merge(cursoNew);
            }
            if (areaOld != null && !areaOld.equals(areaNew)) {
                areaOld.getSgObraCollection().remove(sgObra);
                areaOld = em.merge(areaOld);
            }
            if (areaNew != null && !areaNew.equals(areaOld)) {
                areaNew.getSgObraCollection().add(sgObra);
                areaNew = em.merge(areaNew);
            }
            if (dominioOld != null && !dominioOld.equals(dominioNew)) {
                dominioOld.getSgObraCollection().remove(sgObra);
                dominioOld = em.merge(dominioOld);
            }
            if (dominioNew != null && !dominioNew.equals(dominioOld)) {
                dominioNew.getSgObraCollection().add(sgObra);
                dominioNew = em.merge(dominioNew);
            }
            if (bibliotecarioOld != null && !bibliotecarioOld.equals(bibliotecarioNew)) {
                bibliotecarioOld.getSgObraCollection().remove(sgObra);
                bibliotecarioOld = em.merge(bibliotecarioOld);
            }
            if (bibliotecarioNew != null && !bibliotecarioNew.equals(bibliotecarioOld)) {
                bibliotecarioNew.getSgObraCollection().add(sgObra);
                bibliotecarioNew = em.merge(bibliotecarioNew);
            }
            for (SgObraAutor sgObraAutorCollectionNewSgObraAutor : sgObraAutorCollectionNew) {
                if (!sgObraAutorCollectionOld.contains(sgObraAutorCollectionNewSgObraAutor)) {
                    SgObra oldSgObraOfSgObraAutorCollectionNewSgObraAutor = sgObraAutorCollectionNewSgObraAutor.getSgObra();
                    sgObraAutorCollectionNewSgObraAutor.setSgObra(sgObra);
                    sgObraAutorCollectionNewSgObraAutor = em.merge(sgObraAutorCollectionNewSgObraAutor);
                    if (oldSgObraOfSgObraAutorCollectionNewSgObraAutor != null && !oldSgObraOfSgObraAutorCollectionNewSgObraAutor.equals(sgObra)) {
                        oldSgObraOfSgObraAutorCollectionNewSgObraAutor.getSgObraAutorCollection().remove(sgObraAutorCollectionNewSgObraAutor);
                        oldSgObraOfSgObraAutorCollectionNewSgObraAutor = em.merge(oldSgObraOfSgObraAutorCollectionNewSgObraAutor);
                    }
                }
            }
            for (SgExemplar sgExemplarCollectionOldSgExemplar : sgExemplarCollectionOld) {
                if (!sgExemplarCollectionNew.contains(sgExemplarCollectionOldSgExemplar)) {
                    sgExemplarCollectionOldSgExemplar.setObraRef(null);
                    sgExemplarCollectionOldSgExemplar = em.merge(sgExemplarCollectionOldSgExemplar);
                }
            }
            for (SgExemplar sgExemplarCollectionNewSgExemplar : sgExemplarCollectionNew) {
                if (!sgExemplarCollectionOld.contains(sgExemplarCollectionNewSgExemplar)) {
                    SgObra oldObraRefOfSgExemplarCollectionNewSgExemplar = sgExemplarCollectionNewSgExemplar.getObraRef();
                    sgExemplarCollectionNewSgExemplar.setObraRef(sgObra);
                    sgExemplarCollectionNewSgExemplar = em.merge(sgExemplarCollectionNewSgExemplar);
                    if (oldObraRefOfSgExemplarCollectionNewSgExemplar != null && !oldObraRefOfSgExemplarCollectionNewSgExemplar.equals(sgObra)) {
                        oldObraRefOfSgExemplarCollectionNewSgExemplar.getSgExemplarCollection().remove(sgExemplarCollectionNewSgExemplar);
                        oldObraRefOfSgExemplarCollectionNewSgExemplar = em.merge(oldObraRefOfSgExemplarCollectionNewSgExemplar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sgObra.getIdlivro();
                if (findSgObra(id) == null) {
                    throw new NonexistentEntityException("The sgObra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SgObra sgObra;
            try {
                sgObra = em.getReference(SgObra.class, id);
                sgObra.getIdlivro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sgObra with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SgObraAutor> sgObraAutorCollectionOrphanCheck = sgObra.getSgObraAutorCollection();
            for (SgObraAutor sgObraAutorCollectionOrphanCheckSgObraAutor : sgObraAutorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SgObra (" + sgObra + ") cannot be destroyed since the SgObraAutor " + sgObraAutorCollectionOrphanCheckSgObraAutor + " in its sgObraAutorCollection field has a non-nullable sgObra field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso curso = sgObra.getCurso();
            if (curso != null) {
                curso.getSgObraCollection().remove(sgObra);
                curso = em.merge(curso);
            }
            SgObraArea area = sgObra.getArea();
            if (area != null) {
                area.getSgObraCollection().remove(sgObra);
                area = em.merge(area);
            }
            SgObraCategoria dominio = sgObra.getDominio();
            if (dominio != null) {
                dominio.getSgObraCollection().remove(sgObra);
                dominio = em.merge(dominio);
            }
            Users bibliotecario = sgObra.getBibliotecario();
            if (bibliotecario != null) {
                bibliotecario.getSgObraCollection().remove(sgObra);
                bibliotecario = em.merge(bibliotecario);
            }
            Collection<SgExemplar> sgExemplarCollection = sgObra.getSgExemplarCollection();
            for (SgExemplar sgExemplarCollectionSgExemplar : sgExemplarCollection) {
                sgExemplarCollectionSgExemplar.setObraRef(null);
                sgExemplarCollectionSgExemplar = em.merge(sgExemplarCollectionSgExemplar);
            }
            em.remove(sgObra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SgObra> findSgObraEntities() {
        return findSgObraEntities(true, -1, -1);
    }

    public List<SgObra> findSgObraEntities(int maxResults, int firstResult) {
        return findSgObraEntities(false, maxResults, firstResult);
    }

    private List<SgObra> findSgObraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SgObra.class));
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

    public SgObra findSgObra(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SgObra.class, id);
        } finally {
            em.close();
        }
    }

    public int getSgObraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SgObra> rt = cq.from(SgObra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
