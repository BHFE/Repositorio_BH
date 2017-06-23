/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "Faculdade.findAll", query = "SELECT f FROM Faculdade f"),
    @NamedQuery(name = "Faculdade.findByIdFaculdade", query = "SELECT f FROM Faculdade f WHERE f.idFaculdade = :idFaculdade"),
    @NamedQuery(name = "Faculdade.findByDesricao", query = "SELECT f FROM Faculdade f WHERE f.desricao = :desricao"),
    @NamedQuery(name = "Faculdade.findByAbreviatura", query = "SELECT f FROM Faculdade f WHERE f.abreviatura = :abreviatura"),
    @NamedQuery(name = "Faculdade.findByDirector", query = "SELECT f FROM Faculdade f WHERE f.director = :director"),
    @NamedQuery(name = "Faculdade.findByEndereco", query = "SELECT f FROM Faculdade f WHERE f.endereco = :endereco")})
public class Faculdade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_faculdade", nullable = false)
    private Integer idFaculdade;
    @Column(length = 45)
    private String desricao;
    @Column(length = 11)
    private String abreviatura;
    @Basic(optional = false)
    @Column(nullable = false)
    private long director;
    @Column(length = 2147483647)
    private String endereco;
    @OneToMany(mappedBy = "faculdade", fetch = FetchType.LAZY)
    private List<Funcionario> funcionarioList;
    @OneToMany(mappedBy = "faculdade", fetch = FetchType.LAZY)
    private List<Users> usersList;

    public Faculdade() {
    }

    public Faculdade(Integer idFaculdade) {
        this.idFaculdade = idFaculdade;
    }

    public Faculdade(Integer idFaculdade, long director) {
        this.idFaculdade = idFaculdade;
        this.director = director;
    }

    public Integer getIdFaculdade() {
        return idFaculdade;
    }

    public void setIdFaculdade(Integer idFaculdade) {
        this.idFaculdade = idFaculdade;
    }

    public String getDesricao() {
        return desricao;
    }

    public void setDesricao(String desricao) {
        this.desricao = desricao;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public long getDirector() {
        return director;
    }

    public void setDirector(long director) {
        this.director = director;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @XmlTransient
    public List<Funcionario> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<Funcionario> funcionarioList) {
        this.funcionarioList = funcionarioList;
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
        hash += (idFaculdade != null ? idFaculdade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Faculdade)) {
            return false;
        }
        Faculdade other = (Faculdade) object;
        if ((this.idFaculdade == null && other.idFaculdade != null) || (this.idFaculdade != null && !this.idFaculdade.equals(other.idFaculdade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Faculdade[ idFaculdade=" + idFaculdade + " ]";
    }
    
}
