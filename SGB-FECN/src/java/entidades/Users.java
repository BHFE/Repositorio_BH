/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
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
 * @author Nobrega
 */
@Entity
@Table(catalog = "sgb", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUtilizador", query = "SELECT u FROM Users u WHERE u.utilizador = :utilizador"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByPasword", query = "SELECT u FROM Users u WHERE u.pasword = :pasword"),
    @NamedQuery(name = "Users.findByLastAccess", query = "SELECT u FROM Users u WHERE u.lastAccess = :lastAccess"),
    @NamedQuery(name = "Users.findByNome", query = "SELECT u FROM Users u WHERE u.nome = :nome"),
    @NamedQuery(name = "Users.findByUestudante", query = "SELECT u FROM Users u WHERE u.uestudante = :uestudante"),
    @NamedQuery(name = "Users.findByEstado", query = "SELECT u FROM Users u WHERE u.estado = :estado")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String utilizador;
    @Column(length = 45)
    private String email;
    @Column(length = 45)
    private String pasword;
    @Column(name = "last_access")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;
    @Column(length = 45)
    private String nome;
    private Boolean uestudante;
    @Column(length = 255)
    private String estado;
    @OneToMany(mappedBy = "agenteBibliot", fetch = FetchType.LAZY)
    private List<SgEmprestimo> sgEmprestimoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users", fetch = FetchType.LAZY)
    private List<Usergrupo> usergrupoList;
    @OneToMany(mappedBy = "bibliotecario", fetch = FetchType.LAZY)
    private List<SgObra> sgObraList;
    @OneToMany(mappedBy = "idutilizador", fetch = FetchType.LAZY)
    private List<BLeitor> bLeitorList;
    @OneToMany(mappedBy = "idagente", fetch = FetchType.LAZY)
    private List<BLeitor> bLeitorList1;
    @OneToMany(mappedBy = "agenteRegisto", fetch = FetchType.LAZY)
    private List<SgExemplar> sgExemplarList;
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
    private List<SgEmprestimoParametros> sgEmprestimoParametrosList;

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
    public List<SgEmprestimo> getSgEmprestimoList() {
        return sgEmprestimoList;
    }

    public void setSgEmprestimoList(List<SgEmprestimo> sgEmprestimoList) {
        this.sgEmprestimoList = sgEmprestimoList;
    }

    @XmlTransient
    public List<Usergrupo> getUsergrupoList() {
        return usergrupoList;
    }

    public void setUsergrupoList(List<Usergrupo> usergrupoList) {
        this.usergrupoList = usergrupoList;
    }

    @XmlTransient
    public List<SgObra> getSgObraList() {
        return sgObraList;
    }

    public void setSgObraList(List<SgObra> sgObraList) {
        this.sgObraList = sgObraList;
    }

    @XmlTransient
    public List<BLeitor> getBLeitorList() {
        return bLeitorList;
    }

    public void setBLeitorList(List<BLeitor> bLeitorList) {
        this.bLeitorList = bLeitorList;
    }

    @XmlTransient
    public List<BLeitor> getBLeitorList1() {
        return bLeitorList1;
    }

    public void setBLeitorList1(List<BLeitor> bLeitorList1) {
        this.bLeitorList1 = bLeitorList1;
    }

    @XmlTransient
    public List<SgExemplar> getSgExemplarList() {
        return sgExemplarList;
    }

    public void setSgExemplarList(List<SgExemplar> sgExemplarList) {
        this.sgExemplarList = sgExemplarList;
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
    public List<SgEmprestimoParametros> getSgEmprestimoParametrosList() {
        return sgEmprestimoParametrosList;
    }

    public void setSgEmprestimoParametrosList(List<SgEmprestimoParametros> sgEmprestimoParametrosList) {
        this.sgEmprestimoParametrosList = sgEmprestimoParametrosList;
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
    
}
