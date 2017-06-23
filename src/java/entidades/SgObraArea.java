/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "sg_obra_area", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgObraArea.findAll", query = "SELECT s FROM SgObraArea s")
    , @NamedQuery(name = "SgObraArea.findByIdarea", query = "SELECT s FROM SgObraArea s WHERE s.idarea = :idarea")
    , @NamedQuery(name = "SgObraArea.findByDescricao", query = "SELECT s FROM SgObraArea s WHERE s.descricao = :descricao")})
public class SgObraArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idarea")
    private Long idarea;
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private Collection<SgObra> sgObraCollection;
    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private Collection<BvArtigo> bvArtigoCollection;

    public SgObraArea() {
    }

    public SgObraArea(Long idarea) {
        this.idarea = idarea;
    }

    public Long getIdarea() {
        return idarea;
    }

    public void setIdarea(Long idarea) {
        this.idarea = idarea;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<SgObra> getSgObraCollection() {
        return sgObraCollection;
    }

    public void setSgObraCollection(Collection<SgObra> sgObraCollection) {
        this.sgObraCollection = sgObraCollection;
    }

    @XmlTransient
    public Collection<BvArtigo> getBvArtigoCollection() {
        return bvArtigoCollection;
    }

    public void setBvArtigoCollection(Collection<BvArtigo> bvArtigoCollection) {
        this.bvArtigoCollection = bvArtigoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idarea != null ? idarea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgObraArea)) {
            return false;
        }
        SgObraArea other = (SgObraArea) object;
        if ((this.idarea == null && other.idarea != null) || (this.idarea != null && !this.idarea.equals(other.idarea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgObraArea[ idarea=" + idarea + " ]";
    }
    
}
