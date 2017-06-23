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
@Table(name = "bv_avaliador", catalog = "sgb", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BvAvaliador.findAll", query = "SELECT b FROM BvAvaliador b"),
    @NamedQuery(name = "BvAvaliador.findByIdLeitor", query = "SELECT b FROM BvAvaliador b WHERE b.idLeitor = :idLeitor")})
public class BvAvaliador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_leitor", nullable = false)
    private Long idLeitor;
    @JoinColumn(name = "id_leitor", referencedColumnName = "nr_cartao", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private BLeitor bLeitor;
    @JoinColumn(name = "id_area", referencedColumnName = "categoria", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BvArtigoCategoria idArea;
    @OneToMany(mappedBy = "avaliador", fetch = FetchType.LAZY)
    private List<BvArtigo> bvArtigoList;

    public BvAvaliador() {
    }

    public BvAvaliador(Long idLeitor) {
        this.idLeitor = idLeitor;
    }

    public Long getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(Long idLeitor) {
        this.idLeitor = idLeitor;
    }

    public BLeitor getBLeitor() {
        return bLeitor;
    }

    public void setBLeitor(BLeitor bLeitor) {
        this.bLeitor = bLeitor;
    }

    public BvArtigoCategoria getIdArea() {
        return idArea;
    }

    public void setIdArea(BvArtigoCategoria idArea) {
        this.idArea = idArea;
    }

    @XmlTransient
    public List<BvArtigo> getBvArtigoList() {
        return bvArtigoList;
    }

    public void setBvArtigoList(List<BvArtigo> bvArtigoList) {
        this.bvArtigoList = bvArtigoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLeitor != null ? idLeitor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BvAvaliador)) {
            return false;
        }
        BvAvaliador other = (BvAvaliador) object;
        if ((this.idLeitor == null && other.idLeitor != null) || (this.idLeitor != null && !this.idLeitor.equals(other.idLeitor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BvAvaliador[ idLeitor=" + idLeitor + " ]";
    }
    
}
