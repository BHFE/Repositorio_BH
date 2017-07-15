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
import java.util.List;
import entidades.BvArtigo;
import entidades.Curso;
import entidades.Estudante;
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
        if (curso.getSgObraList() == null) {
            curso.setSgObraList(new ArrayList<SgObra>());
        }
        if (curso.getBvArtigoList() == null) {
            curso.setBvArtigoList(new ArrayList<BvArtigo>());
        }
        if (curso.getEstudanteList() == null) {
            curso.setEstudanteList(new ArrayList<Estudante>());
        }
        if (curso.getEstudanteList1() == null) {
            curso.setEstudanteList1(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SgObra> attachedSgObraList = new ArrayList<SgObra>();
            for (SgObra sgObraListSgObraToAttach : curso.getSgObraList()) {
                sgObraListSgObraToAttach = em.getReference(sgObraListSgObraToAttach.getClass(), sgObraListSgObraToAttach.getIdlivro());
                attachedSgObraList.add(sgObraListSgObraToAttach);
            }
            curso.setSgObraList(attachedSgObraList);
            List<BvArtigo> attachedBvArtigoList = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListBvArtigoToAttach : curso.getBvArtigoList()) {
                bvArtigoListBvArtigoToAttach = em.getReference(bvArtigoListBvArtigoToAttach.getClass(), bvArtigoListBvArtigoToAttach.getIdartigo());
                attachedBvArtigoList.add(bvArtigoListBvArtigoToAttach);
            }
            curso.setBvArtigoList(attachedBvArtigoList);
            List<Estudante> attachedEstudanteList = new ArrayList<Estudante>();
            for (Estudante estudanteListEstudanteToAttach : curso.getEstudanteList()) {
                estudanteListEstudanteToAttach = em.getReference(estudanteListEstudanteToAttach.getClass(), estudanteListEstudanteToAttach.getIdEstudante());
                attachedEstudanteList.add(estudanteListEstudanteToAttach);
            }
            curso.setEstudanteList(attachedEstudanteList);
            List<Estudante> attachedEstudanteList1 = new ArrayList<Estudante>();
            for (Estudante estudanteList1EstudanteToAttach : curso.getEstudanteList1()) {
                estudanteList1EstudanteToAttach = em.getReference(estudanteList1EstudanteToAttach.getClass(), estudanteList1EstudanteToAttach.getIdEstudante());
                attachedEstudanteList1.add(estudanteList1EstudanteToAttach);
            }
            curso.setEstudanteList1(attachedEstudanteList1);
            em.persist(curso);
            for (SgObra sgObraListSgObra : curso.getSgObraList()) {
                Curso oldCursoOfSgObraListSgObra = sgObraListSgObra.getCurso();
                sgObraListSgObra.setCurso(curso);
                sgObraListSgObra = em.merge(sgObraListSgObra);
                if (oldCursoOfSgObraListSgObra != null) {
                    oldCursoOfSgObraListSgObra.getSgObraList().remove(sgObraListSgObra);
                    oldCursoOfSgObraListSgObra = em.merge(oldCursoOfSgObraListSgObra);
                }
            }
            for (BvArtigo bvArtigoListBvArtigo : curso.getBvArtigoList()) {
                Curso oldCursoAlvoOfBvArtigoListBvArtigo = bvArtigoListBvArtigo.getCursoAlvo();
                bvArtigoListBvArtigo.setCursoAlvo(curso);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
                if (oldCursoAlvoOfBvArtigoListBvArtigo != null) {
                    oldCursoAlvoOfBvArtigoListBvArtigo.getBvArtigoList().remove(bvArtigoListBvArtigo);
                    oldCursoAlvoOfBvArtigoListBvArtigo = em.merge(oldCursoAlvoOfBvArtigoListBvArtigo);
                }
            }
            for (Estudante estudanteListEstudante : curso.getEstudanteList()) {
                Curso oldCursocurrenteOfEstudanteListEstudante = estudanteListEstudante.getCursocurrente();
                estudanteListEstudante.setCursocurrente(curso);
                estudanteListEstudante = em.merge(estudanteListEstudante);
                if (oldCursocurrenteOfEstudanteListEstudante != null) {
                    oldCursocurrenteOfEstudanteListEstudante.getEstudanteList().remove(estudanteListEstudante);
                    oldCursocurrenteOfEstudanteListEstudante = em.merge(oldCursocurrenteOfEstudanteListEstudante);
                }
            }
            for (Estudante estudanteList1Estudante : curso.getEstudanteList1()) {
                Curso oldCursoingressoOfEstudanteList1Estudante = estudanteList1Estudante.getCursoingresso();
                estudanteList1Estudante.setCursoingresso(curso);
                estudanteList1Estudante = em.merge(estudanteList1Estudante);
                if (oldCursoingressoOfEstudanteList1Estudante != null) {
                    oldCursoingressoOfEstudanteList1Estudante.getEstudanteList1().remove(estudanteList1Estudante);
                    oldCursoingressoOfEstudanteList1Estudante = em.merge(oldCursoingressoOfEstudanteList1Estudante);
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
            List<SgObra> sgObraListOld = persistentCurso.getSgObraList();
            List<SgObra> sgObraListNew = curso.getSgObraList();
            List<BvArtigo> bvArtigoListOld = persistentCurso.getBvArtigoList();
            List<BvArtigo> bvArtigoListNew = curso.getBvArtigoList();
            List<Estudante> estudanteListOld = persistentCurso.getEstudanteList();
            List<Estudante> estudanteListNew = curso.getEstudanteList();
            List<Estudante> estudanteList1Old = persistentCurso.getEstudanteList1();
            List<Estudante> estudanteList1New = curso.getEstudanteList1();
            List<String> illegalOrphanMessages = null;
            for (Estudante estudanteListOldEstudante : estudanteListOld) {
                if (!estudanteListNew.contains(estudanteListOldEstudante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudante " + estudanteListOldEstudante + " since its cursocurrente field is not nullable.");
                }
            }
            for (Estudante estudanteList1OldEstudante : estudanteList1Old) {
                if (!estudanteList1New.contains(estudanteList1OldEstudante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudante " + estudanteList1OldEstudante + " since its cursoingresso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SgObra> attachedSgObraListNew = new ArrayList<SgObra>();
            for (SgObra sgObraListNewSgObraToAttach : sgObraListNew) {
                sgObraListNewSgObraToAttach = em.getReference(sgObraListNewSgObraToAttach.getClass(), sgObraListNewSgObraToAttach.getIdlivro());
                attachedSgObraListNew.add(sgObraListNewSgObraToAttach);
            }
            sgObraListNew = attachedSgObraListNew;
            curso.setSgObraList(sgObraListNew);
            List<BvArtigo> attachedBvArtigoListNew = new ArrayList<BvArtigo>();
            for (BvArtigo bvArtigoListNewBvArtigoToAttach : bvArtigoListNew) {
                bvArtigoListNewBvArtigoToAttach = em.getReference(bvArtigoListNewBvArtigoToAttach.getClass(), bvArtigoListNewBvArtigoToAttach.getIdartigo());
                attachedBvArtigoListNew.add(bvArtigoListNewBvArtigoToAttach);
            }
            bvArtigoListNew = attachedBvArtigoListNew;
            curso.setBvArtigoList(bvArtigoListNew);
            List<Estudante> attachedEstudanteListNew = new ArrayList<Estudante>();
            for (Estudante estudanteListNewEstudanteToAttach : estudanteListNew) {
                estudanteListNewEstudanteToAttach = em.getReference(estudanteListNewEstudanteToAttach.getClass(), estudanteListNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteListNew.add(estudanteListNewEstudanteToAttach);
            }
            estudanteListNew = attachedEstudanteListNew;
            curso.setEstudanteList(estudanteListNew);
            List<Estudante> attachedEstudanteList1New = new ArrayList<Estudante>();
            for (Estudante estudanteList1NewEstudanteToAttach : estudanteList1New) {
                estudanteList1NewEstudanteToAttach = em.getReference(estudanteList1NewEstudanteToAttach.getClass(), estudanteList1NewEstudanteToAttach.getIdEstudante());
                attachedEstudanteList1New.add(estudanteList1NewEstudanteToAttach);
            }
            estudanteList1New = attachedEstudanteList1New;
            curso.setEstudanteList1(estudanteList1New);
            curso = em.merge(curso);
            for (SgObra sgObraListOldSgObra : sgObraListOld) {
                if (!sgObraListNew.contains(sgObraListOldSgObra)) {
                    sgObraListOldSgObra.setCurso(null);
                    sgObraListOldSgObra = em.merge(sgObraListOldSgObra);
                }
            }
            for (SgObra sgObraListNewSgObra : sgObraListNew) {
                if (!sgObraListOld.contains(sgObraListNewSgObra)) {
                    Curso oldCursoOfSgObraListNewSgObra = sgObraListNewSgObra.getCurso();
                    sgObraListNewSgObra.setCurso(curso);
                    sgObraListNewSgObra = em.merge(sgObraListNewSgObra);
                    if (oldCursoOfSgObraListNewSgObra != null && !oldCursoOfSgObraListNewSgObra.equals(curso)) {
                        oldCursoOfSgObraListNewSgObra.getSgObraList().remove(sgObraListNewSgObra);
                        oldCursoOfSgObraListNewSgObra = em.merge(oldCursoOfSgObraListNewSgObra);
                    }
                }
            }
            for (BvArtigo bvArtigoListOldBvArtigo : bvArtigoListOld) {
                if (!bvArtigoListNew.contains(bvArtigoListOldBvArtigo)) {
                    bvArtigoListOldBvArtigo.setCursoAlvo(null);
                    bvArtigoListOldBvArtigo = em.merge(bvArtigoListOldBvArtigo);
                }
            }
            for (BvArtigo bvArtigoListNewBvArtigo : bvArtigoListNew) {
                if (!bvArtigoListOld.contains(bvArtigoListNewBvArtigo)) {
                    Curso oldCursoAlvoOfBvArtigoListNewBvArtigo = bvArtigoListNewBvArtigo.getCursoAlvo();
                    bvArtigoListNewBvArtigo.setCursoAlvo(curso);
                    bvArtigoListNewBvArtigo = em.merge(bvArtigoListNewBvArtigo);
                    if (oldCursoAlvoOfBvArtigoListNewBvArtigo != null && !oldCursoAlvoOfBvArtigoListNewBvArtigo.equals(curso)) {
                        oldCursoAlvoOfBvArtigoListNewBvArtigo.getBvArtigoList().remove(bvArtigoListNewBvArtigo);
                        oldCursoAlvoOfBvArtigoListNewBvArtigo = em.merge(oldCursoAlvoOfBvArtigoListNewBvArtigo);
                    }
                }
            }
            for (Estudante estudanteListNewEstudante : estudanteListNew) {
                if (!estudanteListOld.contains(estudanteListNewEstudante)) {
                    Curso oldCursocurrenteOfEstudanteListNewEstudante = estudanteListNewEstudante.getCursocurrente();
                    estudanteListNewEstudante.setCursocurrente(curso);
                    estudanteListNewEstudante = em.merge(estudanteListNewEstudante);
                    if (oldCursocurrenteOfEstudanteListNewEstudante != null && !oldCursocurrenteOfEstudanteListNewEstudante.equals(curso)) {
                        oldCursocurrenteOfEstudanteListNewEstudante.getEstudanteList().remove(estudanteListNewEstudante);
                        oldCursocurrenteOfEstudanteListNewEstudante = em.merge(oldCursocurrenteOfEstudanteListNewEstudante);
                    }
                }
            }
            for (Estudante estudanteList1NewEstudante : estudanteList1New) {
                if (!estudanteList1Old.contains(estudanteList1NewEstudante)) {
                    Curso oldCursoingressoOfEstudanteList1NewEstudante = estudanteList1NewEstudante.getCursoingresso();
                    estudanteList1NewEstudante.setCursoingresso(curso);
                    estudanteList1NewEstudante = em.merge(estudanteList1NewEstudante);
                    if (oldCursoingressoOfEstudanteList1NewEstudante != null && !oldCursoingressoOfEstudanteList1NewEstudante.equals(curso)) {
                        oldCursoingressoOfEstudanteList1NewEstudante.getEstudanteList1().remove(estudanteList1NewEstudante);
                        oldCursoingressoOfEstudanteList1NewEstudante = em.merge(oldCursoingressoOfEstudanteList1NewEstudante);
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
            List<Estudante> estudanteListOrphanCheck = curso.getEstudanteList();
            for (Estudante estudanteListOrphanCheckEstudante : estudanteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Estudante " + estudanteListOrphanCheckEstudante + " in its estudanteList field has a non-nullable cursocurrente field.");
            }
            List<Estudante> estudanteList1OrphanCheck = curso.getEstudanteList1();
            for (Estudante estudanteList1OrphanCheckEstudante : estudanteList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Estudante " + estudanteList1OrphanCheckEstudante + " in its estudanteList1 field has a non-nullable cursoingresso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SgObra> sgObraList = curso.getSgObraList();
            for (SgObra sgObraListSgObra : sgObraList) {
                sgObraListSgObra.setCurso(null);
                sgObraListSgObra = em.merge(sgObraListSgObra);
            }
            List<BvArtigo> bvArtigoList = curso.getBvArtigoList();
            for (BvArtigo bvArtigoListBvArtigo : bvArtigoList) {
                bvArtigoListBvArtigo.setCursoAlvo(null);
                bvArtigoListBvArtigo = em.merge(bvArtigoListBvArtigo);
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
