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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "sg_obra_sg_obra_autor", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgObraSgObraAutor.findAll", query = "SELECT s FROM SgObraSgObraAutor s")
    , @NamedQuery(name = "SgObraSgObraAutor.findBySgobraIdlivro", query = "SELECT s FROM SgObraSgObraAutor s WHERE s.sgObraSgObraAutorPK.sgobraIdlivro = :sgobraIdlivro")
    , @NamedQuery(name = "SgObraSgObraAutor.findByDataAlocacao", query = "SELECT s FROM SgObraSgObraAutor s WHERE s.sgObraSgObraAutorPK.dataAlocacao = :dataAlocacao")
    , @NamedQuery(name = "SgObraSgObraAutor.findByIdautor", query = "SELECT s FROM SgObraSgObraAutor s WHERE s.sgObraSgObraAutorPK.idautor = :idautor")
    , @NamedQuery(name = "SgObraSgObraAutor.findByIdlivro", query = "SELECT s FROM SgObraSgObraAutor s WHERE s.sgObraSgObraAutorPK.idlivro = :idlivro")})
public class SgObraSgObraAutor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SgObraSgObraAutorPK sgObraSgObraAutorPK;

    public SgObraSgObraAutor() {
    }

    public SgObraSgObraAutor(SgObraSgObraAutorPK sgObraSgObraAutorPK) {
        this.sgObraSgObraAutorPK = sgObraSgObraAutorPK;
    }

    public SgObraSgObraAutor(long sgobraIdlivro, Date dataAlocacao, long idautor, long idlivro) {
        this.sgObraSgObraAutorPK = new SgObraSgObraAutorPK(sgobraIdlivro, dataAlocacao, idautor, idlivro);
    }

    public SgObraSgObraAutorPK getSgObraSgObraAutorPK() {
        return sgObraSgObraAutorPK;
    }

    public void setSgObraSgObraAutorPK(SgObraSgObraAutorPK sgObraSgObraAutorPK) {
        this.sgObraSgObraAutorPK = sgObraSgObraAutorPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sgObraSgObraAutorPK != null ? sgObraSgObraAutorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgObraSgObraAutor)) {
            return false;
        }
        SgObraSgObraAutor other = (SgObraSgObraAutor) object;
        if ((this.sgObraSgObraAutorPK == null && other.sgObraSgObraAutorPK != null) || (this.sgObraSgObraAutorPK != null && !this.sgObraSgObraAutorPK.equals(other.sgObraSgObraAutorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgObraSgObraAutor[ sgObraSgObraAutorPK=" + sgObraSgObraAutorPK + " ]";
    }
    
}
