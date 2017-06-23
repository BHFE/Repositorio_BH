/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "users", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findByUtilizador", query = "SELECT u FROM Users u WHERE u.utilizador = :utilizador")
    , @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")
    , @NamedQuery(name = "Users.findByPasword", query = "SELECT u FROM Users u WHERE u.pasword = :pasword")
    , @NamedQuery(name = "Users.findByLastAccess", query = "SELECT u FROM Users u WHERE u.lastAccess = :lastAccess")
    , @NamedQuery(name = "Users.findByNome", query = "SELECT u FROM Users u WHERE u.nome = :nome")
    , @NamedQuery(name = "Users.findByUestudante", query = "SELECT u FROM Users u WHERE u.uestudante = :uestudante")
    , @NamedQuery(name = "Users.findByEstado", query = "SELECT u FROM Users u WHERE u.estado = :estado")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "utilizador")
    private String utilizador;
    @Column(name = "email")
    private String email;
    @Column(name = "pasword")
    private String pasword;
    @Column(name = "last_access")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;
    @Column(name = "nome")
    private String nome;
    @Column(name = "uestudante")
    private Boolean uestudante;
    @Column(name = "estado")
    private String estado;
    @OneToMany(mappedBy = "agenteBibliot", fetch = FetchType.LAZY)
    private Collection<SgEmprestimo> sgEmprestimoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users", fetch = FetchType.LAZY)
    private Collection<Usergrupo> usergrupoCollection;
    @OneToMany(mappedBy = "bibliotecario", fetch = FetchType.LAZY)
    private Collection<SgObra> sgObraCollection;
    @OneToMany(mappedBy = "idagente", fetch = FetchType.LAZY)
    private Collection<BLeitor> bLeitorCollection;
    @OneToMany(mappedBy = "idutilizador", fetch = FetchType.LAZY)
    private Collection<BLeitor> bLeitorCollection1;
    @OneToMany(mappedBy = "agenteRegisto", fetch = FetchType.LAZY)
    private Collection<SgExemplar> sgExemplarCollection;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante")
    @ManyToOne(fetch = FetchType.LAZY)
    private Estudante idEstudante;
    @JoinColumn(name = "faculdade", referencedColumnName = "id_faculdade")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faculdade faculdade;
    @JoinColumn(name = "id_funcionario", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario idFuncionario;
    @OneToMany(mappedBy = "agenteBibliotecario", fetch = FetchType.LAZY)
    private Collection<SgEmprestimoParametros> sgEmprestimoParametrosCollection;
    private List<Usergrupo> usersgrupolist;

    public Users() {
    }

    public Users(String utilizador) {
        this.utilizador = utilizador;
    }

    public String getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(String utilizador) {
        this.utilizador = utilizador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getUestudante() {
        return uestudante;
    }

    public void setUestudante(Boolean uestudante) {
        this.uestudante = uestudante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<SgEmprestimo> getSgEmprestimoCollection() {
        return sgEmprestimoCollection;
    }

    public void setSgEmprestimoCollection(Collection<SgEmprestimo> sgEmprestimoCollection) {
        this.sgEmprestimoCollection = sgEmprestimoCollection;
    }

    @XmlTransient
    public Collection<Usergrupo> getUsergrupoCollection() {
        return usergrupoCollection;
    }

    public void setUsergrupoCollection(Collection<Usergrupo> usergrupoCollection) {
        this.usergrupoCollection = usergrupoCollection;
    }

    @XmlTransient
    public Collection<SgObra> getSgObraCollection() {
        return sgObraCollection;
    }

    public void setSgObraCollection(Collection<SgObra> sgObraCollection) {
        this.sgObraCollection = sgObraCollection;
    }

    @XmlTransient
    public Collection<BLeitor> getBLeitorCollection() {
        return bLeitorCollection;
    }

    public void setBLeitorCollection(Collection<BLeitor> bLeitorCollection) {
        this.bLeitorCollection = bLeitorCollection;
    }

    @XmlTransient
    public Collection<BLeitor> getBLeitorCollection1() {
        return bLeitorCollection1;
    }

    public void setBLeitorCollection1(Collection<BLeitor> bLeitorCollection1) {
        this.bLeitorCollection1 = bLeitorCollection1;
    }

    @XmlTransient
    public Collection<SgExemplar> getSgExemplarCollection() {
        return sgExemplarCollection;
    }

    public void setSgExemplarCollection(Collection<SgExemplar> sgExemplarCollection) {
        this.sgExemplarCollection = sgExemplarCollection;
    }

    public Estudante getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Estudante idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    public Funcionario getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Funcionario idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    @XmlTransient
    public Collection<SgEmprestimoParametros> getSgEmprestimoParametrosCollection() {
        return sgEmprestimoParametrosCollection;
    }

    public void setSgEmprestimoParametrosCollection(Collection<SgEmprestimoParametros> sgEmprestimoParametrosCollection) {
        this.sgEmprestimoParametrosCollection = sgEmprestimoParametrosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (utilizador != null ? utilizador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.utilizador == null && other.utilizador != null) || (this.utilizador != null && !this.utilizador.equals(other.utilizador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Users[ utilizador=" + utilizador + " ]";
    }

    public List<Usergrupo> getUsergrupoList() {
        return usersgrupolist;
    }
    
}
