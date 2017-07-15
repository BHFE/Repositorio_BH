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
import entidades.BLeitor;
import entidades.BvArtigo;
import entidades.BvArtigoCategoria;
import entidades.BvAvaliador;
import entidades.Curso;
import entidades.SgObraArea;
import entidades.BvLeitura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class BvArtigoJpaController implements Serializable {

    public BvArtigoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BvArtigo bvArtigo) {
        if (bvArtigo.getBvLeituraList() == null) {
            bvArtigo.setBvLeituraList(new ArrayList<BvLeitura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BLeitor publicador = bvArtigo.getPublicador();
            if (publicador != null) {
                publicador = em.getReference(publicador.getClass(), publicador.getNrCartao());
                bvArtigo.setPublicador(publicador);
            }
            BvArtigoCategoria tipodoc = bvArtigo.getTipodoc();
            if (tipodoc != null) {
                tipodoc = em.getReference(tipodoc.getClass(), tipodoc.getCategoria());
                bvArtigo.setTipodoc(tipodoc);
            }
            BvAvaliador avaliador = bvArtigo.getAvaliador();
            if (avaliador != null) {
                avaliador = em.getReference(avaliador.getClass(), avaliador.getIdLeitor());
                bvArtigo.setAvaliador(avaliador);
            }
            Curso cursoAlvo = bvArtigo.getCursoAlvo();
            if (cursoAlvo != null) {
                cursoAlvo = em.getReference(cursoAlvo.getClass(), cursoAlvo.getIdCurso());
                bvArtigo.setCursoAlvo(cursoAlvo);
            }
            SgObraArea area = bvArtigo.getArea();
            if (area != null) {
                area = em.getReference(area.getClass(), area.getIdarea());
                bvArtigo.setArea(area);
            }
            List<BvLeitura> attachedBvLeituraList = new ArrayList<BvLeitura>();
            for (BvLeitura bvLeituraListBvLeituraToAttach : bvArtigo.getBvLeituraList()) {
                bvLeituraListBvLeituraToAttach = em.getReference(bvLeituraListBvLeituraToAttach.getClass(), bvLeituraListBvLeituraToAttach.getBvLeituraPK());
                attachedBvLeituraList.add(bvLeituraListBvLeituraToAttach);
            }
            bvArtigo.setBvLeituraList(attachedBvLeituraList);
            em.persist(bvArtigo);
            if (publicador != null) {
                publicador.getBvArtigoList().add(bvArtigo);
                publicador = em.merge(publicador);
            }
            if (tipodoc != null) {
                tipodoc.getBvArtigoList().add(bvArtigo);
                tipodoc = em.merge(tipodoc);
            }
            if (avaliador != null) {
                avaliador.getBvArtigoList().add(bvArtigo);
                avaliador = em.merge(avaliador);
            }
            if (cursoAlvo != null) {
                cursoAlvo.getBvArtigoList().add(bvArtigo);
                cursoAlvo = em.merge(cursoAlvo);
            }
            if (area != null) {
                area.getBvArtigoList().add(bvArtigo);
                area = em.merge(area);
            }
            for (BvLeitura bvLeituraListBvLeitura : bvArtigo.getBvLeituraList()) {
                BvArtigo oldBvArtigoOfBvLeituraListBvLeitura = bvLeituraListBvLeitura.getBvArtigo();
                bvLeituraListBvLeitura.setBvArtigo(bvArtigo);
                bvLeituraListBvLeitura = em.merge(bvLeituraListBvLeitura);
                if (oldBvArtigoOfBvLeituraListBvLeitura != null) {
                    oldBvArtigoOfBvLeituraListBvLeitura.getBvLeituraList().remove(bvLeituraListBvLeitura);
                    oldBvArtigoOfBvLeituraListBvLeitura = em.merge(oldBvArtigoOfBvLeituraListBvLeitura);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BvArtigo bvArtigo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BvArtigo persistentBvArtigo = em.find(BvArtigo.class, bvArtigo.getIdartigo());
            BLeitor publicadorOld = persistentBvArtigo.getPublicador();
            BLeitor publicadorNew = bvArtigo.getPublicador();
            BvArtigoCategoria tipodocOld = persistentBvArtigo.getTipodoc();
            BvArtigoCategoria tipodocNew = bvArtigo.getTipodoc();
            BvAvaliador avaliadorOld = persistentBvArtigo.getAvaliador();
            BvAvaliador avaliadorNew = bvArtigo.getAvaliador();
            Curso cursoAlvoOld = persistentBvArtigo.getCursoAlvo();
            Curso cursoAlvoNew = bvArtigo.getCursoAlvo();
            SgObraArea areaOld = persistentBvArtigo.getArea();
            SgObraArea areaNew = bvArtigo.getArea();
            List<BvLeitura> bvLeituraListOld = persistentBvArtigo.getBvLeituraList();
            List<BvLeitura> bvLeituraListNew = bvArtigo.getBvLeituraList();
            List<String> illegalOrphanMessages = null;
            for (BvLeitura bvLeituraListOldBvLeitura : bvLeituraListOld) {
                if (!bvLeituraListNew.contains(bvLeituraListOldBvLeitura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BvLeitura " + bvLeituraListOldBvLeitura + " since its bvArtigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (publicadorNew != null) {
                publicadorNew = em.getReference(publicadorNew.getClass(), publicadorNew.getNrCartao());
                bvArtigo.setPublicador(publicadorNew);
            }
            if (tipodocNew != null) {
                tipodocNew = em.getReference(tipodocNew.getClass(), tipodocNew.getCategoria());
                bvArtigo.setTipodoc(tipodocNew);
            }
            if (avaliadorNew != null) {
                avaliadorNew = em.getReference(avaliadorNew.getClass(), avaliadorNew.getIdLeitor());
                bvArtigo.setAvaliador(avaliadorNew);
            }
            if (cursoAlvoNew != null) {
                cursoAlvoNew = em.getReference(cursoAlvoNew.getClass(), cursoAlvoNew.getIdCurso());
                bvArtigo.setCursoAlvo(cursoAlvoNew);
            }
            if (areaNew != null) {
                areaNew = em.getReference(areaNew.getClass(), areaNew.getIdarea());
                bvArtigo.setArea(areaNew);
            }
            List<BvLeitura> attachedBvLeituraListNew = new ArrayList<BvLeitura>();
            for (BvLeitura bvLeituraListNewBvLeituraToAttach : bvLeituraListNew) {
                bvLeituraListNewBvLeituraToAttach = em.getReference(bvLeituraListNewBvLeituraToAttach.getClass(), bvLeituraListNewBvLeituraToAttach.getBvLeituraPK());
                attachedBvLeituraListNew.add(bvLeituraListNewBvLeituraToAttach);
            }
            bvLeituraListNew = attachedBvLeituraListNew;
            bvArtigo.setBvLeituraList(bvLeituraListNew);
            bvArtigo = em.merge(bvArtigo);
            if (publicadorOld != null && !publicadorOld.equals(publicadorNew)) {
                publicadorOld.getBvArtigoList().remove(bvArtigo);
                publicadorOld = em.merge(publicadorOld);
            }
            if (publicadorNew != null && !publicadorNew.equals(publicadorOld)) {
                publicadorNew.getBvArtigoList().add(bvArtigo);
                publicadorNew = em.merge(publicadorNew);
            }
            if (tipodocOld != null && !tipodocOld.equals(tipodocNew)) {
                tipodocOld.getBvArtigoList().remove(bvArtigo);
                tipodocOld = em.merge(tipodocOld);
            }
            if (tipodocNew != null && !tipodocNew.equals(tipodocOld)) {
                tipodocNew.getBvArtigoList().add(bvArtigo);
                tipodocNew = em.merge(tipodocNew);
            }
            if (avaliadorOld != null && !avaliadorOld.equals(avaliadorNew)) {
                avaliadorOld.getBvArtigoList().remove(bvArtigo);
                avaliadorOld = em.merge(avaliadorOld);
            }
            if (avaliadorNew != null && !avaliadorNew.equals(avaliadorOld)) {
                avaliadorNew.getBvArtigoList().add(bvArtigo);
                avaliadorNew = em.merge(avaliadorNew);
            }
            if (cursoAlvoOld != null && !cursoAlvoOld.equals(cursoAlvoNew)) {
                cursoAlvoOld.getBvArtigoList().remove(bvArtigo);
                cursoAlvoOld = em.merge(cursoAlvoOld);
            }
            if (cursoAlvoNew != null && !cursoAlvoNew.equals(cursoAlvoOld)) {
                cursoAlvoNew.getBvArtigoList().add(bvArtigo);
                cursoAlvoNew = em.merge(cursoAlvoNew);
            }
            if (areaOld != null && !areaOld.equals(areaNew)) {
                areaOld.getBvArtigoList().remove(bvArtigo);
                areaOld = em.merge(areaOld);
            }
            if (areaNew != null && !areaNew.equals(areaOld)) {
                areaNew.getBvArtigoList().add(bvArtigo);
                areaNew = em.merge(areaNew);
            }
            for (BvLeitura bvLeituraListNewBvLeitura : bvLeituraListNew) {
                if (!bvLeituraListOld.contains(bvLeituraListNewBvLeitura)) {
                    BvArtigo oldBvArtigoOfBvLeituraListNewBvLeitura = bvLeituraListNewBvLeitura.getBvArtigo();
                    bvLeituraListNewBvLeitura.setBvArtigo(bvArtigo);
                    bvLeituraListNewBvLeitura = em.merge(bvLeituraListNewBvLeitura);
                    if (oldBvArtigoOfBvLeituraListNewBvLeitura != null && !oldBvArtigoOfBvLeituraListNewBvLeitura.equals(bvArtigo)) {
                        oldBvArtigoOfBvLeituraListNewBvLeitura.getBvLeituraList().remove(bvLeituraListNewBvLeitura);
                        oldBvArtigoOfBvLeituraListNewBvLeitura = em.merge(oldBvArtigoOfBvLeituraListNewBvLeitura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = bvArtigo.getIdartigo();
                if (findBvArtigo(id) == null) {
                    throw new NonexistentEntityException("The bvArtigo with id " + id + " no longer exists.");
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
            BvArtigo bvArtigo;
            try {
                bvArtigo = em.getReference(BvArtigo.class, id);
                bvArtigo.getIdartigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bvArtigo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BvLeitura> bvLeituraListOrphanCheck = bvArtigo.getBvLeituraList();
            for (BvLeitura bvLeituraListOrphanCheckBvLeitura : bvLeituraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BvArtigo (" + bvArtigo + ") cannot be destroyed since the BvLeitura " + bvLeituraListOrphanCheckBvLeitura + " in its bvLeituraList field has a non-nullable bvArtigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            BLeitor publicador = bvArtigo.getPublicador();
            if (publicador != null) {
                publicador.getBvArtigoList().remove(bvArtigo);
                publicador = em.merge(publicador);
            }
            BvArtigoCategoria tipodoc = bvArtigo.getTipodoc();
            if (tipodoc != null) {
                tipodoc.getBvArtigoList().remove(bvArtigo);
                tipodoc = em.merge(tipodoc);
            }
            BvAvaliador avaliador = bvArtigo.getAvaliador();
            if (avaliador != null) {
                avaliador.getBvArtigoList().remove(bvArtigo);
                avaliador = em.merge(avaliador);
            }
            Curso cursoAlvo = bvArtigo.getCursoAlvo();
            if (cursoAlvo != null) {
                cursoAlvo.getBvArtigoList().remove(bvArtigo);
                cursoAlvo = em.merge(cursoAlvo);
            }
            SgObraArea area = bvArtigo.getArea();
            if (area != null) {
                area.getBvArtigoList().remove(bvArtigo);
                area = em.merge(area);
            }
            em.remove(bvArtigo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BvArtigo> findBvArtigoEntities() {
        return findBvArtigoEntities(true, -1, -1);
    }

    public List<BvArtigo> findBvArtigoEntities(int maxResults, int firstResult) {
        return findBvArtigoEntities(false, maxResults, firstResult);
    }

    private List<BvArtigo> findBvArtigoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BvArtigo.class));
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

    public BvArtigo findBvArtigo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BvArtigo.class, id);
        } finally {
            em.close();
        }
    }

    public int getBvArtigoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BvArtigo> rt = cq.from(BvArtigo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
