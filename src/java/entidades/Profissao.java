/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(catalog = "bh", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profissao.findAll", query = "SELECT p FROM Profissao p")
    , @NamedQuery(name = "Profissao.findByIdEstudante", query = "SELECT p FROM Profissao p WHERE p.idEstudante = :idEstudante")
    , @NamedQuery(name = "Profissao.findByDescricaopr", query = "SELECT p FROM Profissao p WHERE p.descricaopr = :descricaopr")
    , @NamedQuery(name = "Profissao.findByAvenidapr", query = "SELECT p FROM Profissao p WHERE p.avenidapr = :avenidapr")
    , @NamedQuery(name = "Profissao.findByContactopr", query = "SELECT p FROM Profissao p WHERE p.contactopr = :contactopr")
    , @NamedQuery(name = "Profissao.findByBairropr", query = "SELECT p FROM Profissao p WHERE p.bairropr = :bairropr")})
public class Profissao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_estudante", nullable = false)
    private Long idEstudante;
    @Column(length = 45)
    private String descricaopr;
    @Column(length = 45)
    private String avenidapr;
    @Column(length = 255)
    private String contactopr;
    @Column(length = 255)
    private String bairropr;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante estudante;
    @JoinColumn(name = "provinciapr", referencedColumnName = "id_provincia")
    @ManyToOne(fetch = FetchType.LAZY)
    private Provincia provinciapr;

    public Profissao() {
    }

    public Profissao(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Long getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public String getDescricaopr() {
        return descricaopr;
    }

    public void setDescricaopr(String descricaopr) {
        this.descricaopr = descricaopr;
    }

    public String getAvenidapr() {
        return avenidapr;
    }

    public void setAvenidapr(String avenidapr) {
        this.avenidapr = avenidapr;
    }

    public String getContactopr() {
        return contactopr;
    }

    public void setContactopr(String contactopr) {
        this.contactopr = contactopr;
    }

    public String getBairropr() {
        return bairropr;
    }

    public void setBairropr(String bairropr) {
        this.bairropr = bairropr;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public Provincia getProvinciapr() {
        return provinciapr;
    }

    public void setProvinciapr(Provincia provinciapr) {
        this.provinciapr = provinciapr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstudante != null ? idEstudante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profissao)) {
            return false;
        }
        Profissao other = (Profissao) object;
        if ((this.idEstudante == null && other.idEstudante != null) || (this.idEstudante != null && !this.idEstudante.equals(other.idEstudante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Profissao[ idEstudante=" + idEstudante + " ]";
    }
    
}
