/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Migueljr
 */
@Embeddable
public class SgObraAutorPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idautor")
    private long idautor;
    @Basic(optional = false)
    @Column(name = "idlivro")
    private long idlivro;
    @Basic(optional = false)
    @Column(name = "data_alocacao")
    @Temporal(TemporalType.DATE)
    private Date dataAlocacao;

    public SgObraAutorPK() {
    }

    public SgObraAutorPK(long idautor, long idlivro, Date dataAlocacao) {
        this.idautor = idautor;
        this.idlivro = idlivro;
        this.dataAlocacao = dataAlocacao;
    }

    public long getIdautor() {
        return idautor;
    }

    public void setIdautor(long idautor) {
        this.idautor = idautor;
    }

    public long getIdlivro() {
        return idlivro;
    }

    public void setIdlivro(long idlivro) {
        this.idlivro = idlivro;
    }

    public Date getDataAlocacao() {
        return dataAlocacao;
    }

    public void setDataAlocacao(Date dataAlocacao) {
        this.dataAlocacao = dataAlocacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idautor;
        hash += (int) idlivro;
        hash += (dataAlocacao != null ? dataAlocacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgObraAutorPK)) {
            return false;
        }
        SgObraAutorPK other = (SgObraAutorPK) object;
        if (this.idautor != other.idautor) {
            return false;
        }
        if (this.idlivro != other.idlivro) {
            return false;
        }
        if ((this.dataAlocacao == null && other.dataAlocacao != null) || (this.dataAlocacao != null && !this.dataAlocacao.equals(other.dataAlocacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgObraAutorPK[ idautor=" + idautor + ", idlivro=" + idlivro + ", dataAlocacao=" + dataAlocacao + " ]";
    }
    
}
