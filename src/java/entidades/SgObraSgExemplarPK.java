/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Migueljr
 */
@Embeddable
public class SgObraSgExemplarPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "getsgexemplarlist_nr_registo")
    private long getsgexemplarlistNrRegisto;
    @Basic(optional = false)
    @Column(name = "sgobra_idlivro")
    private long sgobraIdlivro;
    @Basic(optional = false)
    @Column(name = "sgexemplarlist_nr_registo")
    private long sgexemplarlistNrRegisto;

    public SgObraSgExemplarPK() {
    }

    public SgObraSgExemplarPK(long getsgexemplarlistNrRegisto, long sgobraIdlivro, long sgexemplarlistNrRegisto) {
        this.getsgexemplarlistNrRegisto = getsgexemplarlistNrRegisto;
        this.sgobraIdlivro = sgobraIdlivro;
        this.sgexemplarlistNrRegisto = sgexemplarlistNrRegisto;
    }

    public long getGetsgexemplarlistNrRegisto() {
        return getsgexemplarlistNrRegisto;
    }

    public void setGetsgexemplarlistNrRegisto(long getsgexemplarlistNrRegisto) {
        this.getsgexemplarlistNrRegisto = getsgexemplarlistNrRegisto;
    }

    public long getSgobraIdlivro() {
        return sgobraIdlivro;
    }

    public void setSgobraIdlivro(long sgobraIdlivro) {
        this.sgobraIdlivro = sgobraIdlivro;
    }

    public long getSgexemplarlistNrRegisto() {
        return sgexemplarlistNrRegisto;
    }

    public void setSgexemplarlistNrRegisto(long sgexemplarlistNrRegisto) {
        this.sgexemplarlistNrRegisto = sgexemplarlistNrRegisto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) getsgexemplarlistNrRegisto;
        hash += (int) sgobraIdlivro;
        hash += (int) sgexemplarlistNrRegisto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgObraSgExemplarPK)) {
            return false;
        }
        SgObraSgExemplarPK other = (SgObraSgExemplarPK) object;
        if (this.getsgexemplarlistNrRegisto != other.getsgexemplarlistNrRegisto) {
            return false;
        }
        if (this.sgobraIdlivro != other.sgobraIdlivro) {
            return false;
        }
        if (this.sgexemplarlistNrRegisto != other.sgexemplarlistNrRegisto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgObraSgExemplarPK[ getsgexemplarlistNrRegisto=" + getsgexemplarlistNrRegisto + ", sgobraIdlivro=" + sgobraIdlivro + ", sgexemplarlistNrRegisto=" + sgexemplarlistNrRegisto + " ]";
    }
    
}
