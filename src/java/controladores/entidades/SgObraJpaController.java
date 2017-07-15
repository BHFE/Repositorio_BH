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
import java.util.List;
import entidades.SgExemplar;
import entidades.SgObra;
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
        if (sgObra.getSgObraAutorList() == null) {
            sgObra.setSgObraAutorList(new ArrayList<SgObraAutor>());
        }
        if (sgObra.getSgExemplarList() == null) {
            sgObra.setSgExemplarList(new ArrayList<SgExemplar>());
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
            List<SgObraAutor> attachedSgObraAutorList = new ArrayList<SgObraAutor>();
            for (SgObraAutor sgObraAutorListSgObraAutorToAttach : sgObra.getSgObraAutorList()) {
                sgObraAutorListSgObraAutorToAttach = em.getReference(sgObraAutorListSgObraAutorToAttach.getClass(), sgObraAutorListSgObraAutorToAttach.getIdautor());
                attachedSgObraAutorList.add(sgObraAutorListSgObraAutorToAttach);
            }
            sgObra.setSgObraAutorList(attachedSgObraAutorList);
            List<SgExemplar> attachedSgExemplarList = new ArrayList<SgExemplar>();
            for (SgExemplar sgExemplarListSgExemplarToAttach : sgObra.getSgExemplarList()) {
                sgExemplarListSgExemplarToAttach = em.getReference(sgExemplarListSgExemplarToAttach.getClass(), sgExemplarListSgExemplarToAttach.getNrRegisto());
                attachedSgExemplarList.add(sgExemplarListSgExemplarToAttach);
            }
            sgObra.setSgExemplarList(attachedSgExemplarList);
            em.persist(sgObra);
            if (curso != null) {
                curso.getSgObraList().add(sgObra);
                curso = em.merge(curso);
            }
            if (area != null) {
                area.getSgObraList().add(sgObra);
                area = em.merge(area);
            }
            if (dominio != null) {
                dominio.getSgObraList().add(sgObra);
                dominio = em.merge(dominio);
            }
            if (bibliotecario != null) {
                bibliotecario.getSgObraList().add(sgObra);
                bibliotecario = em.merge(bibliotecario);
            }
            for (SgObraAutor sgObraAutorListSgObraAutor : sgObra.getSgObraAutorList()) {
                SgObra oldIdlivroOfSgObraAutorListSgObraAutor = sgObraAutorListSgObraAutor.getIdlivro();
                sgObraAutorListSgObraAutor.setIdlivro(sgObra);
                sgObraAutorListSgObraAutor = em.merge(sgObraAutorListSgObraAutor);
                if (oldIdlivroOfSgObraAutorListSgObraAutor != null) {
                    oldIdlivroOfSgObraAutorListSgObraAutor.getSgObraAutorList().remove(sgObraAutorListSgObraAutor);
                    oldIdlivroOfSgObraAutorListSgObraAutor = em.merge(oldIdlivroOfSgObraAutorListSgObraAutor);
                }
            }
            for (SgExemplar sgExemplarListSgExemplar : sgObra.getSgExemplarList()) {
                SgObra oldObraRefOfSgExemplarListSgExemplar = sgExemplarListSgExemplar.getObraRef();
                sgExemplarListSgExemplar.setObraRef(sgObra);
                sgExemplarListSgExemplar = em.merge(sgExemplarListSgExemplar);
                if (oldObraRefOfSgExemplarListSgExemplar != null) {
                    oldObraRefOfSgExemplarListSgExemplar.getSgExemplarList().remove(sgExemplarListSgExemplar);
                    oldObraRefOfSgExemplarListSgExemplar = em.merge(oldObraRefOfSgExemplarListSgExemplar);
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
            List<SgObraAutor> sgObraAutorListOld = persistentSgObra.getSgObraAutorList();
            List<SgObraAutor> sgObraAutorListNew = sgObra.getSgObraAutorList();
            List<SgExemplar> sgExemplarListOld = persistentSgObra.getSgExemplarList();
            List<SgExemplar> sgExemplarListNew = sgObra.getSgExemplarList();
            List<String> illegalOrphanMessages = null;
            for (SgObraAutor sgObraAutorListOldSgObraAutor : sgObraAutorListOld) {
                if (!sgObraAutorListNew.contains(sgObraAutorListOldSgObraAutor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SgObraAutor " + sgObraAutorListOldSgObraAutor + " since its idlivro field is not nullable.");
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
            List<SgObraAutor> attachedSgObraAutorListNew = new ArrayList<SgObraAutor>();
            for (SgObraAutor sgObraAutorListNewSgObraAutorToAttach : sgObraAutorListNew) {
                sgObraAutorListNewSgObraAutorToAttach = em.getReference(sgObraAutorListNewSgObraAutorToAttach.getClass(), sgObraAutorListNewSgObraAutorToAttach.getIdautor());
                attachedSgObraAutorListNew.add(sgObraAutorListNewSgObraAutorToAttach);
            }
            sgObraAutorListNew = attachedSgObraAutorListNew;
            sgObra.setSgObraAutorList(sgObraAutorListNew);
            List<SgExemplar> attachedSgExemplarListNew = new ArrayList<SgExemplar>();
            for (SgExemplar sgExemplarListNewSgExemplarToAttach : sgExemplarListNew) {
                sgExemplarListNewSgExemplarToAttach = em.getReference(sgExemplarListNewSgExemplarToAttach.getClass(), sgExemplarListNewSgExemplarToAttach.getNrRegisto());
                attachedSgExemplarListNew.add(sgExemplarListNewSgExemplarToAttach);
            }
            sgExemplarListNew = attachedSgExemplarListNew;
            sgObra.setSgExemplarList(sgExemplarListNew);
            sgObra = em.merge(sgObra);
            if (cursoOld != null && !cursoOld.equals(cursoNew)) {
                cursoOld.getSgObraList().remove(sgObra);
                cursoOld = em.merge(cursoOld);
            }
            if (cursoNew != null && !cursoNew.equals(cursoOld)) {
                cursoNew.getSgObraList().add(sgObra);
                cursoNew = em.merge(cursoNew);
            }
            if (areaOld != null && !areaOld.equals(areaNew)) {
                areaOld.getSgObraList().remove(sgObra);
                areaOld = em.merge(areaOld);
            }
            if (areaNew != null && !areaNew.equals(areaOld)) {
                areaNew.getSgObraList().add(sgObra);
                areaNew = em.merge(areaNew);
            }
            if (dominioOld != null && !dominioOld.equals(dominioNew)) {
                dominioOld.getSgObraList().remove(sgObra);
                dominioOld = em.merge(dominioOld);
            }
            if (dominioNew != null && !dominioNew.equals(dominioOld)) {
                dominioNew.getSgObraList().add(sgObra);
                dominioNew = em.merge(dominioNew);
            }
            if (bibliotecarioOld != null && !bibliotecarioOld.equals(bibliotecarioNew)) {
                bibliotecarioOld.getSgObraList().remove(sgObra);
                bibliotecarioOld = em.merge(bibliotecarioOld);
            }
            if (bibliotecarioNew != null && !bibliotecarioNew.equals(bibliotecarioOld)) {
                bibliotecarioNew.getSgObraList().add(sgObra);
                bibliotecarioNew = em.merge(bibliotecarioNew);
            }
            for (SgObraAutor sgObraAutorListNewSgObraAutor : sgObraAutorListNew) {
                if (!sgObraAutorListOld.contains(sgObraAutorListNewSgObraAutor)) {
                    SgObra oldIdlivroOfSgObraAutorListNewSgObraAutor = sgObraAutorListNewSgObraAutor.getIdlivro();
                    sgObraAutorListNewSgObraAutor.setIdlivro(sgObra);
                    sgObraAutorListNewSgObraAutor = em.merge(sgObraAutorListNewSgObraAutor);
                    if (oldIdlivroOfSgObraAutorListNewSgObraAutor != null && !oldIdlivroOfSgObraAutorListNewSgObraAutor.equals(sgObra)) {
                        oldIdlivroOfSgObraAutorListNewSgObraAutor.getSgObraAutorList().remove(sgObraAutorListNewSgObraAutor);
                        oldIdlivroOfSgObraAutorListNewSgObraAutor = em.merge(oldIdlivroOfSgObraAutorListNewSgObraAutor);
                    }
                }
            }
            for (SgExemplar sgExemplarListOldSgExemplar : sgExemplarListOld) {
                if (!sgExemplarListNew.contains(sgExemplarListOldSgExemplar)) {
                    sgExemplarListOldSgExemplar.setObraRef(null);
                    sgExemplarListOldSgExemplar = em.merge(sgExemplarListOldSgExemplar);
                }
            }
            for (SgExemplar sgExemplarListNewSgExemplar : sgExemplarListNew) {
                if (!sgExemplarListOld.contains(sgExemplarListNewSgExemplar)) {
                    SgObra oldObraRefOfSgExemplarListNewSgExemplar = sgExemplarListNewSgExemplar.getObraRef();
                    sgExemplarListNewSgExemplar.setObraRef(sgObra);
                    sgExemplarListNewSgExemplar = em.merge(sgExemplarListNewSgExemplar);
                    if (oldObraRefOfSgExemplarListNewSgExemplar != null && !oldObraRefOfSgExemplarListNewSgExemplar.equals(sgObra)) {
                        oldObraRefOfSgExemplarListNewSgExemplar.getSgExemplarList().remove(sgExemplarListNewSgExemplar);
                        oldObraRefOfSgExemplarListNewSgExemplar = em.merge(oldObraRefOfSgExemplarListNewSgExemplar);
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
            List<SgObraAutor> sgObraAutorListOrphanCheck = sgObra.getSgObraAutorList();
            for (SgObraAutor sgObraAutorListOrphanCheckSgObraAutor : sgObraAutorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SgObra (" + sgObra + ") cannot be destroyed since the SgObraAutor " + sgObraAutorListOrphanCheckSgObraAutor + " in its sgObraAutorList field has a non-nullable idlivro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso curso = sgObra.getCurso();
            if (curso != null) {
                curso.getSgObraList().remove(sgObra);
                curso = em.merge(curso);
            }
            SgObraArea area = sgObra.getArea();
            if (area != null) {
                area.getSgObraList().remove(sgObra);
                area = em.merge(area);
            }
            SgObraCategoria dominio = sgObra.getDominio();
            if (dominio != null) {
                dominio.getSgObraList().remove(sgObra);
                dominio = em.merge(dominio);
            }
            Users bibliotecario = sgObra.getBibliotecario();
            if (bibliotecario != null) {
                bibliotecario.getSgObraList().remove(sgObra);
                bibliotecario = em.merge(bibliotecario);
            }
            List<SgExemplar> sgExemplarList = sgObra.getSgExemplarList();
            for (SgExemplar sgExemplarListSgExemplar : sgExemplarList) {
                sgExemplarListSgExemplar.setObraRef(null);
                sgExemplarListSgExemplar = em.merge(sgExemplarListSgExemplar);
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
