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
import entidades.SgObra;
import java.util.ArrayList;
import java.util.Collection;
import entidades.BvArtigo;
import entidades.Curso;
import entidades.Estudante;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class CursoJpaController implements Serializable {

    public CursoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Curso curso) {
        if (curso.getSgObraCollection() == null) {
            curso.setSgObraCollection(new ArrayList<SgObra>());
        }
        if (curso.getBvArtigoCollection() == null) {
            curso.setBvArtigoCollection(new ArrayList<BvArtigo>());
        }
        if (curso.getEstudanteCollection() == null) {
            curso.setEstudanteCollection(new ArrayList<Estudante>());
        }
        if (curso.getEstudanteCollection1() == null) {
            curso.setEstudanteCollection1(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SgObra> attachedSgObraCollection = new ArrayList<SgObra>();
            for (SgObra sgObraCollectionSgObraToAttach : curso.getSgObraCollection()) {
                sgObraCollectionSgObraToAttach = em.getReference(sgObraCollectionSgObraToAttach.getClass(), sgObraCollectionSgObraToAttach.getIdlivro());
                attachedSgObraCollection.add(sgObraCollectionSgObraToAttach);
            }
            curso.setSgObraCollection(attachedSgObraCollection);
            Collection<BvArtigo> attachedBvArtigoCollection = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionBvArtigoToAttach : curso.getBvArtigoCollection()) {
                bvArtigoCollectionBvArtigoToAttach = em.getReference(bvArtigoCollectionBvArtigoToAttach.getClass(), bvArtigoCollectionBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollection.add(bvArtigoCollectionBvArtigoToAttach);
            }
            curso.setBvArtigoCollection(attachedBvArtigoCollection);
            Collection<Estudante> attachedEstudanteCollection = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionEstudanteToAttach : curso.getEstudanteCollection()) {
                estudanteCollectionEstudanteToAttach = em.getReference(estudanteCollectionEstudanteToAttach.getClass(), estudanteCollectionEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection.add(estudanteCollectionEstudanteToAttach);
            }
            curso.setEstudanteCollection(attachedEstudanteCollection);
            Collection<Estudante> attachedEstudanteCollection1 = new ArrayList<Estudante>();
            for (Estudante estudanteCollection1EstudanteToAttach : curso.getEstudanteCollection1()) {
                estudanteCollection1EstudanteToAttach = em.getReference(estudanteCollection1EstudanteToAttach.getClass(), estudanteCollection1EstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection1.add(estudanteCollection1EstudanteToAttach);
            }
            curso.setEstudanteCollection1(attachedEstudanteCollection1);
            em.persist(curso);
            for (SgObra sgObraCollectionSgObra : curso.getSgObraCollection()) {
                Curso oldCursoOfSgObraCollectionSgObra = sgObraCollectionSgObra.getCurso();
                sgObraCollectionSgObra.setCurso(curso);
                sgObraCollectionSgObra = em.merge(sgObraCollectionSgObra);
                if (oldCursoOfSgObraCollectionSgObra != null) {
                    oldCursoOfSgObraCollectionSgObra.getSgObraCollection().remove(sgObraCollectionSgObra);
                    oldCursoOfSgObraCollectionSgObra = em.merge(oldCursoOfSgObraCollectionSgObra);
                }
            }
            for (BvArtigo bvArtigoCollectionBvArtigo : curso.getBvArtigoCollection()) {
                Curso oldCursoAlvoOfBvArtigoCollectionBvArtigo = bvArtigoCollectionBvArtigo.getCursoAlvo();
                bvArtigoCollectionBvArtigo.setCursoAlvo(curso);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
                if (oldCursoAlvoOfBvArtigoCollectionBvArtigo != null) {
                    oldCursoAlvoOfBvArtigoCollectionBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionBvArtigo);
                    oldCursoAlvoOfBvArtigoCollectionBvArtigo = em.merge(oldCursoAlvoOfBvArtigoCollectionBvArtigo);
                }
            }
            for (Estudante estudanteCollectionEstudante : curso.getEstudanteCollection()) {
                Curso oldCursocurrenteOfEstudanteCollectionEstudante = estudanteCollectionEstudante.getCursocurrente();
                estudanteCollectionEstudante.setCursocurrente(curso);
                estudanteCollectionEstudante = em.merge(estudanteCollectionEstudante);
                if (oldCursocurrenteOfEstudanteCollectionEstudante != null) {
                    oldCursocurrenteOfEstudanteCollectionEstudante.getEstudanteCollection().remove(estudanteCollectionEstudante);
                    oldCursocurrenteOfEstudanteCollectionEstudante = em.merge(oldCursocurrenteOfEstudanteCollectionEstudante);
                }
            }
            for (Estudante estudanteCollection1Estudante : curso.getEstudanteCollection1()) {
                Curso oldCursoingressoOfEstudanteCollection1Estudante = estudanteCollection1Estudante.getCursoingresso();
                estudanteCollection1Estudante.setCursoingresso(curso);
                estudanteCollection1Estudante = em.merge(estudanteCollection1Estudante);
                if (oldCursoingressoOfEstudanteCollection1Estudante != null) {
                    oldCursoingressoOfEstudanteCollection1Estudante.getEstudanteCollection1().remove(estudanteCollection1Estudante);
                    oldCursoingressoOfEstudanteCollection1Estudante = em.merge(oldCursoingressoOfEstudanteCollection1Estudante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getIdCurso());
            Collection<SgObra> sgObraCollectionOld = persistentCurso.getSgObraCollection();
            Collection<SgObra> sgObraCollectionNew = curso.getSgObraCollection();
            Collection<BvArtigo> bvArtigoCollectionOld = persistentCurso.getBvArtigoCollection();
            Collection<BvArtigo> bvArtigoCollectionNew = curso.getBvArtigoCollection();
            Collection<Estudante> estudanteCollectionOld = persistentCurso.getEstudanteCollection();
            Collection<Estudante> estudanteCollectionNew = curso.getEstudanteCollection();
            Collection<Estudante> estudanteCollection1Old = persistentCurso.getEstudanteCollection1();
            Collection<Estudante> estudanteCollection1New = curso.getEstudanteCollection1();
            List<String> illegalOrphanMessages = null;
            for (Estudante estudanteCollectionOldEstudante : estudanteCollectionOld) {
                if (!estudanteCollectionNew.contains(estudanteCollectionOldEstudante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudante " + estudanteCollectionOldEstudante + " since its cursocurrente field is not nullable.");
                }
            }
            for (Estudante estudanteCollection1OldEstudante : estudanteCollection1Old) {
                if (!estudanteCollection1New.contains(estudanteCollection1OldEstudante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudante " + estudanteCollection1OldEstudante + " since its cursoingresso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SgObra> attachedSgObraCollectionNew = new ArrayList<SgObra>();
            for (SgObra sgObraCollectionNewSgObraToAttach : sgObraCollectionNew) {
                sgObraCollectionNewSgObraToAttach = em.getReference(sgObraCollectionNewSgObraToAttach.getClass(), sgObraCollectionNewSgObraToAttach.getIdlivro());
                attachedSgObraCollectionNew.add(sgObraCollectionNewSgObraToAttach);
            }
            sgObraCollectionNew = attachedSgObraCollectionNew;
            curso.setSgObraCollection(sgObraCollectionNew);
            Collection<BvArtigo> attachedBvArtigoCollectionNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoCollectionNewBvArtigoToAttach : bvArtigoCollectionNew) {
                bvArtigoCollectionNewBvArtigoToAttach = em.getReference(bvArtigoCollectionNewBvArtigoToAttach.getClass(), bvArtigoCollectionNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoCollectionNew.add(bvArtigoCollectionNewBvArtigoToAttach);
            }
            bvArtigoCollectionNew = attachedBvArtigoCollectionNew;
            curso.setBvArtigoCollection(bvArtigoCollectionNew);
            Collection<Estudante> attachedEstudanteCollectionNew = new ArrayList<Estudante>();
            for (Estudante estudanteCollectionNewEstudanteToAttach : estudanteCollectionNew) {
                estudanteCollectionNewEstudanteToAttach = em.getReference(estudanteCollectionNewEstudanteToAttach.getClass(), estudanteCollectionNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollectionNew.add(estudanteCollectionNewEstudanteToAttach);
            }
            estudanteCollectionNew = attachedEstudanteCollectionNew;
            curso.setEstudanteCollection(estudanteCollectionNew);
            Collection<Estudante> attachedEstudanteCollection1New = new ArrayList<Estudante>();
            for (Estudante estudanteCollection1NewEstudanteToAttach : estudanteCollection1New) {
                estudanteCollection1NewEstudanteToAttach = em.getReference(estudanteCollection1NewEstudanteToAttach.getClass(), estudanteCollection1NewEstudanteToAttach.getIdEstudante());
                attachedEstudanteCollection1New.add(estudanteCollection1NewEstudanteToAttach);
            }
            estudanteCollection1New = attachedEstudanteCollection1New;
            curso.setEstudanteCollection1(estudanteCollection1New);
            curso = em.merge(curso);
            for (SgObra sgObraCollectionOldSgObra : sgObraCollectionOld) {
                if (!sgObraCollectionNew.contains(sgObraCollectionOldSgObra)) {
                    sgObraCollectionOldSgObra.setCurso(null);
                    sgObraCollectionOldSgObra = em.merge(sgObraCollectionOldSgObra);
                }
            }
            for (SgObra sgObraCollectionNewSgObra : sgObraCollectionNew) {
                if (!sgObraCollectionOld.contains(sgObraCollectionNewSgObra)) {
                    Curso oldCursoOfSgObraCollectionNewSgObra = sgObraCollectionNewSgObra.getCurso();
                    sgObraCollectionNewSgObra.setCurso(curso);
                    sgObraCollectionNewSgObra = em.merge(sgObraCollectionNewSgObra);
                    if (oldCursoOfSgObraCollectionNewSgObra != null && !oldCursoOfSgObraCollectionNewSgObra.equals(curso)) {
                        oldCursoOfSgObraCollectionNewSgObra.getSgObraCollection().remove(sgObraCollectionNewSgObra);
                        oldCursoOfSgObraCollectionNewSgObra = em.merge(oldCursoOfSgObraCollectionNewSgObra);
                    }
                }
            }
            for (BvArtigo bvArtigoCollectionOldBvArtigo : bvArtigoCollectionOld) {
                if (!bvArtigoCollectionNew.contains(bvArtigoCollectionOldBvArtigo)) {
                    bvArtigoCollectionOldBvArtigo.setCursoAlvo(null);
                    bvArtigoCollectionOldBvArtigo = em.merge(bvArtigoCollectionOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoCollectionNewBvArtigo : bvArtigoCollectionNew) {
                if (!bvArtigoCollectionOld.contains(bvArtigoCollectionNewBvArtigo)) {
                    Curso oldCursoAlvoOfBvArtigoCollectionNewBvArtigo = bvArtigoCollectionNewBvArtigo.getCursoAlvo();
                    bvArtigoCollectionNewBvArtigo.setCursoAlvo(curso);
                    bvArtigoCollectionNewBvArtigo = em.merge(bvArtigoCollectionNewBvArtigo);
                    if (oldCursoAlvoOfBvArtigoCollectionNewBvArtigo != null && !oldCursoAlvoOfBvArtigoCollectionNewBvArtigo.equals(curso)) {
                        oldCursoAlvoOfBvArtigoCollectionNewBvArtigo.getBvArtigoCollection().remove(bvArtigoCollectionNewBvArtigo);
                        oldCursoAlvoOfBvArtigoCollectionNewBvArtigo = em.merge(oldCursoAlvoOfBvArtigoCollectionNewBvArtigo);
                    }
                }
            }
            for (Estudante estudanteCollectionNewEstudante : estudanteCollectionNew) {
                if (!estudanteCollectionOld.contains(estudanteCollectionNewEstudante)) {
                    Curso oldCursocurrenteOfEstudanteCollectionNewEstudante = estudanteCollectionNewEstudante.getCursocurrente();
                    estudanteCollectionNewEstudante.setCursocurrente(curso);
                    estudanteCollectionNewEstudante = em.merge(estudanteCollectionNewEstudante);
                    if (oldCursocurrenteOfEstudanteCollectionNewEstudante != null && !oldCursocurrenteOfEstudanteCollectionNewEstudante.equals(curso)) {
                        oldCursocurrenteOfEstudanteCollectionNewEstudante.getEstudanteCollection().remove(estudanteCollectionNewEstudante);
                        oldCursocurrenteOfEstudanteCollectionNewEstudante = em.merge(oldCursocurrenteOfEstudanteCollectionNewEstudante);
                    }
                }
            }
            for (Estudante estudanteCollection1NewEstudante : estudanteCollection1New) {
                if (!estudanteCollection1Old.contains(estudanteCollection1NewEstudante)) {
                    Curso oldCursoingressoOfEstudanteCollection1NewEstudante = estudanteCollection1NewEstudante.getCursoingresso();
                    estudanteCollection1NewEstudante.setCursoingresso(curso);
                    estudanteCollection1NewEstudante = em.merge(estudanteCollection1NewEstudante);
                    if (oldCursoingressoOfEstudanteCollection1NewEstudante != null && !oldCursoingressoOfEstudanteCollection1NewEstudante.equals(curso)) {
                        oldCursoingressoOfEstudanteCollection1NewEstudante.getEstudanteCollection1().remove(estudanteCollection1NewEstudante);
                        oldCursoingressoOfEstudanteCollection1NewEstudante = em.merge(oldCursoingressoOfEstudanteCollection1NewEstudante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = curso.getIdCurso();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getIdCurso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Estudante> estudanteCollectionOrphanCheck = curso.getEstudanteCollection();
            for (Estudante estudanteCollectionOrphanCheckEstudante : estudanteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Estudante " + estudanteCollectionOrphanCheckEstudante + " in its estudanteCollection field has a non-nullable cursocurrente field.");
            }
            Collection<Estudante> estudanteCollection1OrphanCheck = curso.getEstudanteCollection1();
            for (Estudante estudanteCollection1OrphanCheckEstudante : estudanteCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Estudante " + estudanteCollection1OrphanCheckEstudante + " in its estudanteCollection1 field has a non-nullable cursoingresso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SgObra> sgObraCollection = curso.getSgObraCollection();
            for (SgObra sgObraCollectionSgObra : sgObraCollection) {
                sgObraCollectionSgObra.setCurso(null);
                sgObraCollectionSgObra = em.merge(sgObraCollectionSgObra);
            }
            Collection<BvArtigo> bvArtigoCollection = curso.getBvArtigoCollection();
            for (BvArtigo bvArtigoCollectionBvArtigo : bvArtigoCollection) {
                bvArtigoCollectionBvArtigo.setCursoAlvo(null);
                bvArtigoCollectionBvArtigo = em.merge(bvArtigoCollectionBvArtigo);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
