/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    @NamedQuery(name = "Funcionario.findAll", query = "SELECT f FROM Funcionario f"),
    @NamedQuery(name = "Funcionario.findByIdFuncionario", query = "SELECT f FROM Funcionario f WHERE f.idFuncionario = :idFuncionario"),
    @NamedQuery(name = "Funcionario.findByNome", query = "SELECT f FROM Funcionario f WHERE f.nome = :nome"),
    @NamedQuery(name = "Funcionario.findByApelido", query = "SELECT f FROM Funcionario f WHERE f.apelido = :apelido"),
    @NamedQuery(name = "Funcionario.findByContacto", query = "SELECT f FROM Funcionario f WHERE f.contacto = :contacto"),
    @NamedQuery(name = "Funcionario.findByEmail", query = "SELECT f FROM Funcionario f WHERE f.email = :email"),
    @NamedQuery(name = "Funcionario.findByNrfuncionario", query = "SELECT f FROM Funcionario f WHERE f.nrfuncionario = :nrfuncionario"),
    @NamedQuery(name = "Funcionario.findByMasculino", query = "SELECT f FROM Funcionario f WHERE f.masculino = :masculino")})
public class Funcionario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_funcionario", nullable = false)
    private Long idFuncionario;
    @Column(length = 45)
    private String nome;
    @Column(length = 45)
    private String apelido;
    @Column(length = 2147483647)
    private String contacto;
    @Column(length = 2147483647)
    private String email;
    @Column(length = 2147483647)
    private String nrfuncionario;
    private Boolean masculino;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "funcionario", fetch = FetchType.LAZY)
    private Tipochefia tipochefia;
    @JoinColumn(name = "faculdade", referencedColumnName = "id_faculdade")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faculdade faculdade;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "funcionario", fetch = FetchType.LAZY)
    private Docente docente;
    @OneToMany(mappedBy = "idFuncionario", fetch = FetchType.LAZY)
    private List<Users> usersList;

    public Funcionario() {
    }

    public Funcionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNrfuncionario() {
        return nrfuncionario;
    }

    public void setNrfuncionario(String nrfuncionario) {
        this.nrfuncionario = nrfuncionario;
    }

    public Boolean getMasculino() {
        return masculino;
    }

    public void setMasculino(Boolean masculino) {
        this.masculino = masculino;
    }

    public Tipochefia getTipochefia() {
        return tipochefia;
    }

    public void setTipochefia(Tipochefia tipochefia) {
        this.tipochefia = tipochefia;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    @XmlTransient
    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionario != null ? idFuncionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.idFuncionario == null && other.idFuncionario != null) || (this.idFuncionario != null && !this.idFuncionario.equals(other.idFuncionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Funcionario[ idFuncionario=" + idFuncionario + " ]";
    }
    
}
