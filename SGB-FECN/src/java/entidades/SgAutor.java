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
@Table(name = "sg_autor", catalog = "sgb", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgAutor.findAll", query = "SELECT s FROM SgAutor s"),
    @NamedQuery(name = "SgAutor.findByIdautor", query = "SELECT s FROM SgAutor s WHERE s.idautor = :idautor"),
    @NamedQuery(name = "SgAutor.findByNome", query = "SELECT s FROM SgAutor s WHERE s.nome = :nome")})
public class SgAutor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long idautor;
    @Column(length = 255)
    private String nome;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sgAutor", fetch = FetchType.LAZY)
    private List<SgObraAutor> sgObraAutorList;

    public SgAutor() {
    }

    public SgAutor(Long idautor) {
        this.idautor = idautor;
    }

    public Long getIdautor() {
        return idautor;
    }

    public void setIdautor(Long idautor) {
        this.idautor = idautor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlTransient
    public List<SgObraAutor> getSgObraAutorList() {
        return sgObraAutorList;
    }

    public void setSgObraAutorList(List<SgObraAutor> sgObraAutorList) {
        this.sgObraAutorList = sgObraAutorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idautor != null ? idautor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAutor)) {
            return false;
        }
        SgAutor other = (SgAutor) object;
        if ((this.idautor == null && other.idautor != null) || (this.idautor != null && !this.idautor.equals(other.idautor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgAutor[ idautor=" + idautor + " ]";
    }
    
}
