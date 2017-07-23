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
@Table(name = "sg_obra_autor", catalog = "bh", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgObraAutor.findAll", query = "SELECT s FROM SgObraAutor s")
    , @NamedQuery(name = "SgObraAutor.findByIdautor", query = "SELECT s FROM SgObraAutor s WHERE s.sgObraAutorPK.idautor = :idautor")
    , @NamedQuery(name = "SgObraAutor.findByIdlivro", query = "SELECT s FROM SgObraAutor s WHERE s.sgObraAutorPK.idlivro = :idlivro")
    , @NamedQuery(name = "SgObraAutor.findByDataAlocacao", query = "SELECT s FROM SgObraAutor s WHERE s.sgObraAutorPK.dataAlocacao = :dataAlocacao")})
public class SgObraAutor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SgObraAutorPK sgObraAutorPK;
    @JoinColumn(name = "idautor", referencedColumnName = "idautor", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SgAutor sgAutor;
    @JoinColumn(name = "idlivro", referencedColumnName = "idlivro", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SgObra sgObra;

    public SgObraAutor() {
    }

    public SgObraAutor(SgObraAutorPK sgObraAutorPK) {
        this.sgObraAutorPK = sgObraAutorPK;
    }

    public SgObraAutor(long idautor, long idlivro, Date dataAlocacao) {
        this.sgObraAutorPK = new SgObraAutorPK(idautor, idlivro, dataAlocacao);
    }

    public SgObraAutorPK getSgObraAutorPK() {
        return sgObraAutorPK;
    }

    public void setSgObraAutorPK(SgObraAutorPK sgObraAutorPK) {
        this.sgObraAutorPK = sgObraAutorPK;
    }

    public SgAutor getSgAutor() {
        return sgAutor;
    }

    public void setSgAutor(SgAutor sgAutor) {
        this.sgAutor = sgAutor;
    }

    public SgObra getSgObra() {
        return sgObra;
    }

    public void setSgObra(SgObra sgObra) {
        this.sgObra = sgObra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sgObraAutorPK != null ? sgObraAutorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgObraAutor)) {
            return false;
        }
        SgObraAutor other = (SgObraAutor) object;
        if ((this.sgObraAutorPK == null && other.sgObraAutorPK != null) || (this.sgObraAutorPK != null && !this.sgObraAutorPK.equals(other.sgObraAutorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgObraAutor[ sgObraAutorPK=" + sgObraAutorPK + " ]";
    }
    
}
