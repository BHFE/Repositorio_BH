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
import entidades.BvArtigoCategoria;
import entidades.BvAvaliador;
import entidades.Curso;
import entidades.SgObraArea;
import entidades.BNotificacao;
import entidades.BvArtigo;
import java.util.ArrayList;
import java.util.Collection;
import entidades.BvLeitura;
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
        if (bvArtigo.getBNotificacaoCollection() == null) {
            bvArtigo.setBNotificacaoCollection(new ArrayList<BNotificacao>());
        }
        if (bvArtigo.getBvLeituraCollection() == null) {
            bvArtigo.setBvLeituraCollection(new ArrayList<BvLeitura>());
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
            Collection<BNotificacao> attachedBNotificacaoCollection = new ArrayList<BNotificacao>();
            for (BNotificacao BNotificacaoCollectionBNotificacaoToAttach : bvArtigo.getBNotificacaoCollection()) {
                BNotificacaoCollectionBNotificacaoToAttach = em.getReference(BNotificacaoCollectionBNotificacaoToAttach.getClass(), BNotificacaoCollectionBNotificacaoToAttach.getIdNotificacao());
                attachedBNotificacaoCollection.add(BNotificacaoCollectionBNotificacaoToAttach);
            }
            bvArtigo.setBNotificacaoCollection(attachedBNotificacaoCollection);
            Collection<BvLeitura> attachedBvLeituraCollection = new ArrayList<BvLeitura>();
            for (BvLeitura bvLeituraCollectionBvLeituraToAttach : bvArtigo.getBvLeituraCollection()) {
                bvLeituraCollectionBvLeituraToAttach = em.getReference(bvLeituraCollectionBvLeituraToAttach.getClass(), bvLeituraCollectionBvLeituraToAttach.getBvLeituraPK());
                attachedBvLeituraCollection.add(bvLeituraCollectionBvLeituraToAttach);
            }
            bvArtigo.setBvLeituraCollection(attachedBvLeituraCollection);
            em.persist(bvArtigo);
            if (publicador != null) {
                publicador.getBvArtigoCollection().add(bvArtigo);
                publicador = em.merge(publicador);
            }
            if (tipodoc != null) {
                tipodoc.getBvArtigoCollection().add(bvArtigo);
                tipodoc = em.merge(tipodoc);
            }
            if (avaliador != null) {
                avaliador.getBvArtigoCollection().add(bvArtigo);
                avaliador = em.merge(avaliador);
            }
            if (cursoAlvo != null) {
                cursoAlvo.getBvArtigoCollection().add(bvArtigo);
                cursoAlvo = em.merge(cursoAlvo);
            }
            if (area != null) {
                area.getBvArtigoCollection().add(bvArtigo);
                area = em.merge(area);
            }
            for (BNotificacao BNotificacaoCollectionBNotificacao : bvArtigo.getBNotificacaoCollection()) {
                BvArtigo oldIdPublicacaoOfBNotificacaoCollectionBNotificacao = BNotificacaoCollectionBNotificacao.getIdPublicacao();
                BNotificacaoCollectionBNotificacao.setIdPublicacao(bvArtigo);
                BNotificacaoCollectionBNotificacao = em.merge(BNotificacaoCollectionBNotificacao);
                if (oldIdPublicacaoOfBNotificacaoCollectionBNotificacao != null) {
                    oldIdPublicacaoOfBNotificacaoCollectionBNotificacao.getBNotificacaoCollection().remove(BNotificacaoCollectionBNotificacao);
                    oldIdPublicacaoOfBNotificacaoCollectionBNotificacao = em.merge(oldIdPublicacaoOfBNotificacaoCollectionBNotificacao);
                }
            }
            for (BvLeitura bvLeituraCollectionBvLeitura : bvArtigo.getBvLeituraCollection()) {
                BvArtigo oldBvArtigoOfBvLeituraCollectionBvLeitura = bvLeituraCollectionBvLeitura.getBvArtigo();
                bvLeituraCollectionBvLeitura.setBvArtigo(bvArtigo);
                bvLeituraCollectionBvLeitura = em.merge(bvLeituraCollectionBvLeitura);
                if (oldBvArtigoOfBvLeituraCollectionBvLeitura != null) {
                    oldBvArtigoOfBvLeituraCollectionBvLeitura.getBvLeituraCollection().remove(bvLeituraCollectionBvLeitura);
                    oldBvArtigoOfBvLeituraCollectionBvLeitura = em.merge(oldBvArtigoOfBvLeituraCollectionBvLeitura);
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
            Collection<BNotificacao> BNotificacaoCollectionOld = persistentBvArtigo.getBNotificacaoCollection();
            Collection<BNotificacao> BNotificacaoCollectionNew = bvArtigo.getBNotificacaoCollection();
            Collection<BvLeitura> bvLeituraCollectionOld = persistentBvArtigo.getBvLeituraCollection();
            Collection<BvLeitura> bvLeituraCollectionNew = bvArtigo.getBvLeituraCollection();
            List<String> illegalOrphanMessages = null;
            for (BvLeitura bvLeituraCollectionOldBvLeitura : bvLeituraCollectionOld) {
                if (!bvLeituraCollectionNew.contains(bvLeituraCollectionOldBvLeitura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BvLeitura " + bvLeituraCollectionOldBvLeitura + " since its bvArtigo field is not nullable.");
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
            Collection<BNotificacao> attachedBNotificacaoCollectionNew = new ArrayList<BNotificacao>();
            for (BNotificacao BNotificacaoCollectionNewBNotificacaoToAttach : BNotificacaoCollectionNew) {
                BNotificacaoCollectionNewBNotificacaoToAttach = em.getReference(BNotificacaoCollectionNewBNotificacaoToAttach.getClass(), BNotificacaoCollectionNewBNotificacaoToAttach.getIdNotificacao());
                attachedBNotificacaoCollectionNew.add(BNotificacaoCollectionNewBNotificacaoToAttach);
            }
            BNotificacaoCollectionNew = attachedBNotificacaoCollectionNew;
            bvArtigo.setBNotificacaoCollection(BNotificacaoCollectionNew);
            Collection<BvLeitura> attachedBvLeituraCollectionNew = new ArrayList<BvLeitura>();
            for (BvLeitura bvLeituraCollectionNewBvLeituraToAttach : bvLeituraCollectionNew) {
                bvLeituraCollectionNewBvLeituraToAttach = em.getReference(bvLeituraCollectionNewBvLeituraToAttach.getClass(), bvLeituraCollectionNewBvLeituraToAttach.getBvLeituraPK());
                attachedBvLeituraCollectionNew.add(bvLeituraCollectionNewBvLeituraToAttach);
            }
            bvLeituraCollectionNew = attachedBvLeituraCollectionNew;
            bvArtigo.setBvLeituraCollection(bvLeituraCollectionNew);
            bvArtigo = em.merge(bvArtigo);
            if (publicadorOld != null && !publicadorOld.equals(publicadorNew)) {
                publicadorOld.getBvArtigoCollection().remove(bvArtigo);
                publicadorOld = em.merge(publicadorOld);
            }
            if (publicadorNew != null && !publicadorNew.equals(publicadorOld)) {
                publicadorNew.getBvArtigoCollection().add(bvArtigo);
                publicadorNew = em.merge(publicadorNew);
            }
            if (tipodocOld != null && !tipodocOld.equals(tipodocNew)) {
                tipodocOld.getBvArtigoCollection().remove(bvArtigo);
                tipodocOld = em.merge(tipodocOld);
            }
            if (tipodocNew != null && !tipodocNew.equals(tipodocOld)) {
                tipodocNew.getBvArtigoCollection().add(bvArtigo);
                tipodocNew = em.merge(tipodocNew);
            }
            if (avaliadorOld != null && !avaliadorOld.equals(avaliadorNew)) {
                avaliadorOld.getBvArtigoCollection().remove(bvArtigo);
                avaliadorOld = em.merge(avaliadorOld);
            }
            if (avaliadorNew != null && !avaliadorNew.equals(avaliadorOld)) {
                avaliadorNew.getBvArtigoCollection().add(bvArtigo);
                avaliadorNew = em.merge(avaliadorNew);
            }
            if (cursoAlvoOld != null && !cursoAlvoOld.equals(cursoAlvoNew)) {
                cursoAlvoOld.getBvArtigoCollection().remove(bvArtigo);
                cursoAlvoOld = em.merge(cursoAlvoOld);
            }
            if (cursoAlvoNew != null && !cursoAlvoNew.equals(cursoAlvoOld)) {
                cursoAlvoNew.getBvArtigoCollection().add(bvArtigo);
                cursoAlvoNew = em.merge(cursoAlvoNew);
            }
            if (areaOld != null && !areaOld.equals(areaNew)) {
                areaOld.getBvArtigoCollection().remove(bvArtigo);
                areaOld = em.merge(areaOld);
            }
            if (areaNew != null && !areaNew.equals(areaOld)) {
                areaNew.getBvArtigoCollection().add(bvArtigo);
                areaNew = em.merge(areaNew);
            }
            for (BNotificacao BNotificacaoCollectionOldBNotificacao : BNotificacaoCollectionOld) {
                if (!BNotificacaoCollectionNew.contains(BNotificacaoCollectionOldBNotificacao)) {
                    BNotificacaoCollectionOldBNotificacao.setIdPublicacao(null);
                    BNotificacaoCollectionOldBNotificacao = em.merge(BNotificacaoCollectionOldBNotificacao);
                }
            }
            for (BNotificacao BNotificacaoCollectionNewBNotificacao : BNotificacaoCollectionNew) {
                if (!BNotificacaoCollectionOld.contains(BNotificacaoCollectionNewBNotificacao)) {
                    BvArtigo oldIdPublicacaoOfBNotificacaoCollectionNewBNotificacao = BNotificacaoCollectionNewBNotificacao.getIdPublicacao();
                    BNotificacaoCollectionNewBNotificacao.setIdPublicacao(bvArtigo);
                    BNotificacaoCollectionNewBNotificacao = em.merge(BNotificacaoCollectionNewBNotificacao);
                    if (oldIdPublicacaoOfBNotificacaoCollectionNewBNotificacao != null && !oldIdPublicacaoOfBNotificacaoCollectionNewBNotificacao.equals(bvArtigo)) {
                        oldIdPublicacaoOfBNotificacaoCollectionNewBNotificacao.getBNotificacaoCollection().remove(BNotificacaoCollectionNewBNotificacao);
                        oldIdPublicacaoOfBNotificacaoCollectionNewBNotificacao = em.merge(oldIdPublicacaoOfBNotificacaoCollectionNewBNotificacao);
                    }
                }
            }
            for (BvLeitura bvLeituraCollectionNewBvLeitura : bvLeituraCollectionNew) {
                if (!bvLeituraCollectionOld.contains(bvLeituraCollectionNewBvLeitura)) {
                    BvArtigo oldBvArtigoOfBvLeituraCollectionNewBvLeitura = bvLeituraCollectionNewBvLeitura.getBvArtigo();
                    bvLeituraCollectionNewBvLeitura.setBvArtigo(bvArtigo);
                    bvLeituraCollectionNewBvLeitura = em.merge(bvLeituraCollectionNewBvLeitura);
                    if (oldBvArtigoOfBvLeituraCollectionNewBvLeitura != null && !oldBvArtigoOfBvLeituraCollectionNewBvLeitura.equals(bvArtigo)) {
                        oldBvArtigoOfBvLeituraCollectionNewBvLeitura.getBvLeituraCollection().remove(bvLeituraCollectionNewBvLeitura);
                        oldBvArtigoOfBvLeituraCollectionNewBvLeitura = em.merge(oldBvArtigoOfBvLeituraCollectionNewBvLeitura);
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
            Collection<BvLeitura> bvLeituraCollectionOrphanCheck = bvArtigo.getBvLeituraCollection();
            for (BvLeitura bvLeituraCollectionOrphanCheckBvLeitura : bvLeituraCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BvArtigo (" + bvArtigo + ") cannot be destroyed since the BvLeitura " + bvLeituraCollectionOrphanCheckBvLeitura + " in its bvLeituraCollection field has a non-nullable bvArtigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            BLeitor publicador = bvArtigo.getPublicador();
            if (publicador != null) {
                publicador.getBvArtigoCollection().remove(bvArtigo);
                publicador = em.merge(publicador);
            }
            BvArtigoCategoria tipodoc = bvArtigo.getTipodoc();
            if (tipodoc != null) {
                tipodoc.getBvArtigoCollection().remove(bvArtigo);
                tipodoc = em.merge(tipodoc);
            }
            BvAvaliador avaliador = bvArtigo.getAvaliador();
            if (avaliador != null) {
                avaliador.getBvArtigoCollection().remove(bvArtigo);
                avaliador = em.merge(avaliador);
            }
            Curso cursoAlvo = bvArtigo.getCursoAlvo();
            if (cursoAlvo != null) {
                cursoAlvo.getBvArtigoCollection().remove(bvArtigo);
                cursoAlvo = em.merge(cursoAlvo);
            }
            SgObraArea area = bvArtigo.getArea();
            if (area != null) {
                area.getBvArtigoCollection().remove(bvArtigo);
                area = em.merge(area);
            }
            Collection<BNotificacao> BNotificacaoCollection = bvArtigo.getBNotificacaoCollection();
            for (BNotificacao BNotificacaoCollectionBNotificacao : BNotificacaoCollection) {
                BNotificacaoCollectionBNotificacao.setIdPublicacao(null);
                BNotificacaoCollectionBNotificacao = em.merge(BNotificacaoCollectionBNotificacao);
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
