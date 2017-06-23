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
import entidades.Estudante;
import entidades.Pais;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nobrega
 */
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) {
        if (pais.getEstudanteList() == null) {
            pais.setEstudanteList(new ArrayList<Estudante>());
        }
        if (pais.getEstudanteList1() == null) {
            pais.setEstudanteList1(new ArrayList<Estudante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estudante> attachedEstudanteList = new ArrayList<Estudante>();
            for (Estudante estudanteListEstudanteToAttach : pais.getEstudanteList()) {
                estudanteListEstudanteToAttach = em.getReference(estudanteListEstudanteToAttach.getClass(), estudanteListEstudanteToAttach.getIdEstudante());
                attachedEstudanteList.add(estudanteListEstudanteToAttach);
            }
            pais.setEstudanteList(attachedEstudanteList);
            List<Estudante> attachedEstudanteList1 = new ArrayList<Estudante>();
            for (Estudante estudanteList1EstudanteToAttach : pais.getEstudanteList1()) {
                estudanteList1EstudanteToAttach = em.getReference(estudanteList1EstudanteToAttach.getClass(), estudanteList1EstudanteToAttach.getIdEstudante());
                attachedEstudanteList1.add(estudanteList1EstudanteToAttach);
            }
            pais.setEstudanteList1(attachedEstudanteList1);
            em.persist(pais);
            for (Estudante estudanteListEstudante : pais.getEstudanteList()) {
                Pais oldEscolaPaisOfEstudanteListEstudante = estudanteListEstudante.getEscolaPais();
                estudanteListEstudante.setEscolaPais(pais);
                estudanteListEstudante = em.merge(estudanteListEstudante);
                if (oldEscolaPaisOfEstudanteListEstudante != null) {
                    oldEscolaPaisOfEstudanteListEstudante.getEstudanteList().remove(estudanteListEstudante);
                    oldEscolaPaisOfEstudanteListEstudante = em.merge(oldEscolaPaisOfEstudanteListEstudante);
                }
            }
            for (Estudante estudanteList1Estudante : pais.getEstudanteList1()) {
                Pais oldNacionalidadeOfEstudanteList1Estudante = estudanteList1Estudante.getNacionalidade();
                estudanteList1Estudante.setNacionalidade(pais);
                estudanteList1Estudante = em.merge(estudanteList1Estudante);
                if (oldNacionalidadeOfEstudanteList1Estudante != null) {
                    oldNacionalidadeOfEstudanteList1Estudante.getEstudanteList1().remove(estudanteList1Estudante);
                    oldNacionalidadeOfEstudanteList1Estudante = em.merge(oldNacionalidadeOfEstudanteList1Estudante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getIdPais());
            List<Estudante> estudanteListOld = persistentPais.getEstudanteList();
            List<Estudante> estudanteListNew = pais.getEstudanteList();
            List<Estudante> estudanteList1Old = persistentPais.getEstudanteList1();
            List<Estudante> estudanteList1New = pais.getEstudanteList1();
            List<String> illegalOrphanMessages = null;
            for (Estudante estudanteList1OldEstudante : estudanteList1Old) {
                if (!estudanteList1New.contains(estudanteList1OldEstudante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudante " + estudanteList1OldEstudante + " since its nacionalidade field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Estudante> attachedEstudanteListNew = new ArrayList<Estudante>();
            for (Estudante estudanteListNewEstudanteToAttach : estudanteListNew) {
                estudanteListNewEstudanteToAttach = em.getReference(estudanteListNewEstudanteToAttach.getClass(), estudanteListNewEstudanteToAttach.getIdEstudante());
                attachedEstudanteListNew.add(estudanteListNewEstudanteToAttach);
            }
            estudanteListNew = attachedEstudanteListNew;
            pais.setEstudanteList(estudanteListNew);
            List<Estudante> attachedEstudanteList1New = new ArrayList<Estudante>();
            for (Estudante estudanteList1NewEstudanteToAttach : estudanteList1New) {
                estudanteList1NewEstudanteToAttach = em.getReference(estudanteList1NewEstudanteToAttach.getClass(), estudanteList1NewEstudanteToAttach.getIdEstudante());
                attachedEstudanteList1New.add(estudanteList1NewEstudanteToAttach);
            }
            estudanteList1New = attachedEstudanteList1New;
            pais.setEstudanteList1(estudanteList1New);
            pais = em.merge(pais);
            for (Estudante estudanteListOldEstudante : estudanteListOld) {
                if (!estudanteListNew.contains(estudanteListOldEstudante)) {
                    estudanteListOldEstudante.setEscolaPais(null);
                    estudanteListOldEstudante = em.merge(estudanteListOldEstudante);
                }
            }
            for (Estudante estudanteListNewEstudante : estudanteListNew) {
                if (!estudanteListOld.contains(estudanteListNewEstudante)) {
                    Pais oldEscolaPaisOfEstudanteListNewEstudante = estudanteListNewEstudante.getEscolaPais();
                    estudanteListNewEstudante.setEscolaPais(pais);
                    estudanteListNewEstudante = em.merge(estudanteListNewEstudante);
                    if (oldEscolaPaisOfEstudanteListNewEstudante != null && !oldEscolaPaisOfEstudanteListNewEstudante.equals(pais)) {
                        oldEscolaPaisOfEstudanteListNewEstudante.getEstudanteList().remove(estudanteListNewEstudante);
                        oldEscolaPaisOfEstudanteListNewEstudante = em.merge(oldEscolaPaisOfEstudanteListNewEstudante);
                    }
                }
            }
            for (Estudante estudanteList1NewEstudante : estudanteList1New) {
                if (!estudanteList1Old.contains(estudanteList1NewEstudante)) {
                    Pais oldNacionalidadeOfEstudanteList1NewEstudante = estudanteList1NewEstudante.getNacionalidade();
                    estudanteList1NewEstudante.setNacionalidade(pais);
                    estudanteList1NewEstudante = em.merge(estudanteList1NewEstudante);
                    if (oldNacionalidadeOfEstudanteList1NewEstudante != null && !oldNacionalidadeOfEstudanteList1NewEstudante.equals(pais)) {
                        oldNacionalidadeOfEstudanteList1NewEstudante.getEstudanteList1().remove(estudanteList1NewEstudante);
                        oldNacionalidadeOfEstudanteList1NewEstudante = em.merge(oldNacionalidadeOfEstudanteList1NewEstudante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pais.getIdPais();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getIdPais();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Estudante> estudanteList1OrphanCheck = pais.getEstudanteList1();
            for (Estudante estudanteList1OrphanCheckEstudante : estudanteList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Estudante " + estudanteList1OrphanCheckEstudante + " in its estudanteList1 field has a non-nullable nacionalidade field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Estudante> estudanteList = pais.getEstudanteList();
            for (Estudante estudanteListEstudante : estudanteList) {
                estudanteListEstudante.setEscolaPais(null);
                estudanteListEstudante = em.merge(estudanteListEstudante);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
