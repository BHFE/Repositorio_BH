/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "bv_leitura", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BvLeitura.findAll", query = "SELECT b FROM BvLeitura b")
    , @NamedQuery(name = "BvLeitura.findByDataLeitura", query = "SELECT b FROM BvLeitura b WHERE b.bvLeituraPK.dataLeitura = :dataLeitura")
    , @NamedQuery(name = "BvLeitura.findByObra", query = "SELECT b FROM BvLeitura b WHERE b.bvLeituraPK.obra = :obra")
    , @NamedQuery(name = "BvLeitura.findByHorasLeitura", query = "SELECT b FROM BvLeitura b WHERE b.bvLeituraPK.horasLeitura = :horasLeitura")
    , @NamedQuery(name = "BvLeitura.findByLeitor", query = "SELECT b FROM BvLeitura b WHERE b.bvLeituraPK.leitor = :leitor")})
public class BvLeitura implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BvLeituraPK bvLeituraPK;
    @JoinColumn(name = "leitor", referencedColumnName = "nr_cartao", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BLeitor bLeitor;
    @JoinColumn(name = "obra", referencedColumnName = "idartigo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BvArtigo bvArtigo;

    public BvLeitura() {
    }

    public BvLeitura(BvLeituraPK bvLeituraPK) {
        this.bvLeituraPK = bvLeituraPK;
    }

    public BvLeitura(Date dataLeitura, long obra, Date horasLeitura, long leitor) {
        this.bvLeituraPK = new BvLeituraPK(dataLeitura, obra, horasLeitura, leitor);
    }

    public BvLeituraPK getBvLeituraPK() {
        return bvLeituraPK;
    }

    public void setBvLeituraPK(BvLeituraPK bvLeituraPK) {
        this.bvLeituraPK = bvLeituraPK;
    }

    public BLeitor getBLeitor() {
        return bLeitor;
    }

    public void setBLeitor(BLeitor bLeitor) {
        this.bLeitor = bLeitor;
    }

    public BvArtigo getBvArtigo() {
        return bvArtigo;
    }

    public void setBvArtigo(BvArtigo bvArtigo) {
        this.bvArtigo = bvArtigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bvLeituraPK != null ? bvLeituraPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BvLeitura)) {
            return false;
        }
        BvLeitura other = (BvLeitura) object;
        if ((this.bvLeituraPK == null && other.bvLeituraPK != null) || (this.bvLeituraPK != null && !this.bvLeituraPK.equals(other.bvLeituraPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BvLeitura[ bvLeituraPK=" + bvLeituraPK + " ]";
    }
    
}
