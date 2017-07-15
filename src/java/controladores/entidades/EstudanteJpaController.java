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
import entidades.Profissao;
import entidades.Bolsa;
import entidades.Curso;
import entidades.Estadocivil;
import entidades.Estudante;
import entidades.Pais;
import entidades.Viaingresso;
import entidades.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Migueljr
 */
public class EstudanteJpaController implements Serializable {

    public EstudanteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudante estudante) {
        if (estudante.getUsersList() == null) {
            estudante.setUsersList(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profissao profissao = estudante.getProfissao();
            if (profissao != null) {
                profissao = em.getReference(profissao.getClass(), profissao.getIdEstudante());
                estudante.setProfissao(profissao);
            }
            Bolsa bolsa = estudante.getBolsa();
            if (bolsa != null) {
                bolsa = em.getReference(bolsa.getClass(), bolsa.getIdBolsa());
                estudante.setBolsa(bolsa);
            }
            Curso cursocurrente = estudante.getCursocurrente();
            if (cursocurrente != null) {
                cursocurrente = em.getReference(cursocurrente.getClass(), cursocurrente.getIdCurso());
                estudante.setCursocurrente(cursocurrente);
            }
            Curso cursoingresso = estudante.getCursoingresso();
            if (cursoingresso != null) {
                cursoingresso = em.getReference(cursoingresso.getClass(), cursoingresso.getIdCurso());
                estudante.setCursoingresso(cursoingresso);
            }
            Estadocivil estadoCivil = estudante.getEstadoCivil();
            if (estadoCivil != null) {
                estadoCivil = em.getReference(estadoCivil.getClass(), estadoCivil.getIdEstado());
                estudante.setEstadoCivil(estadoCivil);
            }
            Pais escolaPais = estudante.getEscolaPais();
            if (escolaPais != null) {
                escolaPais = em.getReference(escolaPais.getClass(), escolaPais.getIdPais());
                estudante.setEscolaPais(escolaPais);
            }
            Pais nacionalidade = estudante.getNacionalidade();
            if (nacionalidade != null) {
                nacionalidade = em.getReference(nacionalidade.getClass(), nacionalidade.getIdPais());
                estudante.setNacionalidade(nacionalidade);
            }
            Viaingresso viaIngresso = estudante.getViaIngresso();
            if (viaIngresso != null) {
                viaIngresso = em.getReference(viaIngresso.getClass(), viaIngresso.getIdViaIngresso());
                estudante.setViaIngresso(viaIngresso);
            }
            List<Users> attachedUsersList = new ArrayList<Users>();
            for (Users usersListUsersToAttach : estudante.getUsersList()) {
                usersListUsersToAttach = em.getReference(usersListUsersToAttach.getClass(), usersListUsersToAttach.getUtilizador());
                attachedUsersList.add(usersListUsersToAttach);
            }
            estudante.setUsersList(attachedUsersList);
            em.persist(estudante);
            if (profissao != null) {
                Estudante oldEstudanteOfProfissao = profissao.getEstudante();
                if (oldEstudanteOfProfissao != null) {
                    oldEstudanteOfProfissao.setProfissao(null);
                    oldEstudanteOfProfissao = em.merge(oldEstudanteOfProfissao);
                }
                profissao.setEstudante(estudante);
                profissao = em.merge(profissao);
            }
            if (bolsa != null) {
                bolsa.getEstudanteList().add(estudante);
                bolsa = em.merge(bolsa);
            }
            if (cursocurrente != null) {
                cursocurrente.getEstudanteList().add(estudante);
                cursocurrente = em.merge(cursocurrente);
            }
            if (cursoingresso != null) {
                cursoingresso.getEstudanteList().add(estudante);
                cursoingresso = em.merge(cursoingresso);
            }
            if (estadoCivil != null) {
                estadoCivil.getEstudanteList().add(estudante);
                estadoCivil = em.merge(estadoCivil);
            }
            if (escolaPais != null) {
                escolaPais.getEstudanteList().add(estudante);
                escolaPais = em.merge(escolaPais);
            }
            if (nacionalidade != null) {
                nacionalidade.getEstudanteList().add(estudante);
                nacionalidade = em.merge(nacionalidade);
            }
            if (viaIngresso != null) {
                viaIngresso.getEstudanteList().add(estudante);
                viaIngresso = em.merge(viaIngresso);
            }
            for (Users usersListUsers : estudante.getUsersList()) {
                Estudante oldIdEstudanteOfUsersListUsers = usersListUsers.getIdEstudante();
                usersListUsers.setIdEstudante(estudante);
                usersListUsers = em.merge(usersListUsers);
                if (oldIdEstudanteOfUsersListUsers != null) {
                    oldIdEstudanteOfUsersListUsers.getUsersList().remove(usersListUsers);
                    oldIdEstudanteOfUsersListUsers = em.merge(oldIdEstudanteOfUsersListUsers);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estudante estudante) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudante persistentEstudante = em.find(Estudante.class, estudante.getIdEstudante());
            Profissao profissaoOld = persistentEstudante.getProfissao();
            Profissao profissaoNew = estudante.getProfissao();
            Bolsa bolsaOld = persistentEstudante.getBolsa();
            Bolsa bolsaNew = estudante.getBolsa();
            Curso cursocurrenteOld = persistentEstudante.getCursocurrente();
            Curso cursocurrenteNew = estudante.getCursocurrente();
            Curso cursoingressoOld = persistentEstudante.getCursoingresso();
            Curso cursoingressoNew = estudante.getCursoingresso();
            Estadocivil estadoCivilOld = persistentEstudante.getEstadoCivil();
            Estadocivil estadoCivilNew = estudante.getEstadoCivil();
            Pais escolaPaisOld = persistentEstudante.getEscolaPais();
            Pais escolaPaisNew = estudante.getEscolaPais();
            Pais nacionalidadeOld = persistentEstudante.getNacionalidade();
            Pais nacionalidadeNew = estudante.getNacionalidade();
            Viaingresso viaIngressoOld = persistentEstudante.getViaIngresso();
            Viaingresso viaIngressoNew = estudante.getViaIngresso();
            List<Users> usersListOld = persistentEstudante.getUsersList();
            List<Users> usersListNew = estudante.getUsersList();
            List<String> illegalOrphanMessages = null;
            if (profissaoOld != null && !profissaoOld.equals(profissaoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Profissao " + profissaoOld + " since its estudante field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (profissaoNew != null) {
                profissaoNew = em.getReference(profissaoNew.getClass(), profissaoNew.getIdEstudante());
                estudante.setProfissao(profissaoNew);
            }
            if (bolsaNew != null) {
                bolsaNew = em.getReference(bolsaNew.getClass(), bolsaNew.getIdBolsa());
                estudante.setBolsa(bolsaNew);
            }
            if (cursocurrenteNew != null) {
                cursocurrenteNew = em.getReference(cursocurrenteNew.getClass(), cursocurrenteNew.getIdCurso());
                estudante.setCursocurrente(cursocurrenteNew);
            }
            if (cursoingressoNew != null) {
                cursoingressoNew = em.getReference(cursoingressoNew.getClass(), cursoingressoNew.getIdCurso());
                estudante.setCursoingresso(cursoingressoNew);
            }
            if (estadoCivilNew != null) {
                estadoCivilNew = em.getReference(estadoCivilNew.getClass(), estadoCivilNew.getIdEstado());
                estudante.setEstadoCivil(estadoCivilNew);
            }
            if (escolaPaisNew != null) {
                escolaPaisNew = em.getReference(escolaPaisNew.getClass(), escolaPaisNew.getIdPais());
                estudante.setEscolaPais(escolaPaisNew);
            }
            if (nacionalidadeNew != null) {
                nacionalidadeNew = em.getReference(nacionalidadeNew.getClass(), nacionalidadeNew.getIdPais());
                estudante.setNacionalidade(nacionalidadeNew);
            }
            if (viaIngressoNew != null) {
                viaIngressoNew = em.getReference(viaIngressoNew.getClass(), viaIngressoNew.getIdViaIngresso());
                estudante.setViaIngresso(viaIngressoNew);
            }
            List<Users> attachedUsersListNew = new ArrayList<Users>();
            for (Users usersListNewUsersToAttach : usersListNew) {
                usersListNewUsersToAttach = em.getReference(usersListNewUsersToAttach.getClass(), usersListNewUsersToAttach.getUtilizador());
                attachedUsersListNew.add(usersListNewUsersToAttach);
            }
            usersListNew = attachedUsersListNew;
            estudante.setUsersList(usersListNew);
            estudante = em.merge(estudante);
            if (profissaoNew != null && !profissaoNew.equals(profissaoOld)) {
                Estudante oldEstudanteOfProfissao = profissaoNew.getEstudante();
                if (oldEstudanteOfProfissao != null) {
                    oldEstudanteOfProfissao.setProfissao(null);
                    oldEstudanteOfProfissao = em.merge(oldEstudanteOfProfissao);
                }
                profissaoNew.setEstudante(estudante);
                profissaoNew = em.merge(profissaoNew);
            }
            if (bolsaOld != null && !bolsaOld.equals(bolsaNew)) {
                bolsaOld.getEstudanteList().remove(estudante);
                bolsaOld = em.merge(bolsaOld);
            }
            if (bolsaNew != null && !bolsaNew.equals(bolsaOld)) {
                bolsaNew.getEstudanteList().add(estudante);
                bolsaNew = em.merge(bolsaNew);
            }
            if (cursocurrenteOld != null && !cursocurrenteOld.equals(cursocurrenteNew)) {
                cursocurrenteOld.getEstudanteList().remove(estudante);
                cursocurrenteOld = em.merge(cursocurrenteOld);
            }
            if (cursocurrenteNew != null && !cursocurrenteNew.equals(cursocurrenteOld)) {
                cursocurrenteNew.getEstudanteList().add(estudante);
                cursocurrenteNew = em.merge(cursocurrenteNew);
            }
            if (cursoingressoOld != null && !cursoingressoOld.equals(cursoingressoNew)) {
                cursoingressoOld.getEstudanteList().remove(estudante);
                cursoingressoOld = em.merge(cursoingressoOld);
            }
            if (cursoingressoNew != null && !cursoingressoNew.equals(cursoingressoOld)) {
                cursoingressoNew.getEstudanteList().add(estudante);
                cursoingressoNew = em.merge(cursoingressoNew);
            }
            if (estadoCivilOld != null && !estadoCivilOld.equals(estadoCivilNew)) {
                estadoCivilOld.getEstudanteList().remove(estudante);
                estadoCivilOld = em.merge(estadoCivilOld);
            }
            if (estadoCivilNew != null && !estadoCivilNew.equals(estadoCivilOld)) {
                estadoCivilNew.getEstudanteList().add(estudante);
                estadoCivilNew = em.merge(estadoCivilNew);
            }
            if (escolaPaisOld != null && !escolaPaisOld.equals(escolaPaisNew)) {
                escolaPaisOld.getEstudanteList().remove(estudante);
                escolaPaisOld = em.merge(escolaPaisOld);
            }
            if (escolaPaisNew != null && !escolaPaisNew.equals(escolaPaisOld)) {
                escolaPaisNew.getEstudanteList().add(estudante);
                escolaPaisNew = em.merge(escolaPaisNew);
            }
            if (nacionalidadeOld != null && !nacionalidadeOld.equals(nacionalidadeNew)) {
                nacionalidadeOld.getEstudanteList().remove(estudante);
                nacionalidadeOld = em.merge(nacionalidadeOld);
            }
            if (nacionalidadeNew != null && !nacionalidadeNew.equals(nacionalidadeOld)) {
                nacionalidadeNew.getEstudanteList().add(estudante);
                nacionalidadeNew = em.merge(nacionalidadeNew);
            }
            if (viaIngressoOld != null && !viaIngressoOld.equals(viaIngressoNew)) {
                viaIngressoOld.getEstudanteList().remove(estudante);
                viaIngressoOld = em.merge(viaIngressoOld);
            }
            if (viaIngressoNew != null && !viaIngressoNew.equals(viaIngressoOld)) {
                viaIngressoNew.getEstudanteList().add(estudante);
                viaIngressoNew = em.merge(viaIngressoNew);
            }
            for (Users usersListOldUsers : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers)) {
                    usersListOldUsers.setIdEstudante(null);
                    usersListOldUsers = em.merge(usersListOldUsers);
                }
            }
            for (Users usersListNewUsers : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers)) {
                    Estudante oldIdEstudanteOfUsersListNewUsers = usersListNewUsers.getIdEstudante();
                    usersListNewUsers.setIdEstudante(estudante);
                    usersListNewUsers = em.merge(usersListNewUsers);
                    if (oldIdEstudanteOfUsersListNewUsers != null && !oldIdEstudanteOfUsersListNewUsers.equals(estudante)) {
                        oldIdEstudanteOfUsersListNewUsers.getUsersList().remove(usersListNewUsers);
                        oldIdEstudanteOfUsersListNewUsers = em.merge(oldIdEstudanteOfUsersListNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = estudante.getIdEstudante();
                if (findEstudante(id) == null) {
                    throw new NonexistentEntityException("The estudante with id " + id + " no longer exists.");
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
            Estudante estudante;
            try {
                estudante = em.getReference(Estudante.class, id);
                estudante.getIdEstudante();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudante with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Profissao profissaoOrphanCheck = estudante.getProfissao();
            if (profissaoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estudante (" + estudante + ") cannot be destroyed since the Profissao " + profissaoOrphanCheck + " in its profissao field has a non-nullable estudante field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Bolsa bolsa = estudante.getBolsa();
            if (bolsa != null) {
                bolsa.getEstudanteList().remove(estudante);
                bolsa = em.merge(bolsa);
            }
            Curso cursocurrente = estudante.getCursocurrente();
            if (cursocurrente != null) {
                cursocurrente.getEstudanteList().remove(estudante);
                cursocurrente = em.merge(cursocurrente);
            }
            Curso cursoingresso = estudante.getCursoingresso();
            if (cursoingresso != null) {
                cursoingresso.getEstudanteList().remove(estudante);
                cursoingresso = em.merge(cursoingresso);
            }
            Estadocivil estadoCivil = estudante.getEstadoCivil();
            if (estadoCivil != null) {
                estadoCivil.getEstudanteList().remove(estudante);
                estadoCivil = em.merge(estadoCivil);
            }
            Pais escolaPais = estudante.getEscolaPais();
            if (escolaPais != null) {
                escolaPais.getEstudanteList().remove(estudante);
                escolaPais = em.merge(escolaPais);
            }
            Pais nacionalidade = estudante.getNacionalidade();
            if (nacionalidade != null) {
                nacionalidade.getEstudanteList().remove(estudante);
                nacionalidade = em.merge(nacionalidade);
            }
            Viaingresso viaIngresso = estudante.getViaIngresso();
            if (viaIngresso != null) {
                viaIngresso.getEstudanteList().remove(estudante);
                viaIngresso = em.merge(viaIngresso);
            }
            List<Users> usersList = estudante.getUsersList();
            for (Users usersListUsers : usersList) {
                usersListUsers.setIdEstudante(null);
                usersListUsers = em.merge(usersListUsers);
            }
            em.remove(estudante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estudante> findEstudanteEntities() {
        return findEstudanteEntities(true, -1, -1);
    }

    public List<Estudante> findEstudanteEntities(int maxResults, int firstResult) {
        return findEstudanteEntities(false, maxResults, firstResult);
    }

    private List<Estudante> findEstudanteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudante.class));
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

    public Estudante findEstudante(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudante.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudanteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudante> rt = cq.from(Estudante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
