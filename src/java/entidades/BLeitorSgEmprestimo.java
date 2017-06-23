/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
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
@Table(name = "b_leitor_sg_emprestimo", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BLeitorSgEmprestimo.findAll", query = "SELECT b FROM BLeitorSgEmprestimo b")
    , @NamedQuery(name = "BLeitorSgEmprestimo.findByBleitorNrCartao", query = "SELECT b FROM BLeitorSgEmprestimo b WHERE b.bLeitorSgEmprestimoPK.bleitorNrCartao = :bleitorNrCartao")
    , @NamedQuery(name = "BLeitorSgEmprestimo.findBySgemprestimolistIdemprestimo", query = "SELECT b FROM BLeitorSgEmprestimo b WHERE b.bLeitorSgEmprestimoPK.sgemprestimolistIdemprestimo = :sgemprestimolistIdemprestimo")})
public class BLeitorSgEmprestimo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BLeitorSgEmprestimoPK bLeitorSgEmprestimoPK;

    public BLeitorSgEmprestimo() {
    }

    public BLeitorSgEmprestimo(BLeitorSgEmprestimoPK bLeitorSgEmprestimoPK) {
        this.bLeitorSgEmprestimoPK = bLeitorSgEmprestimoPK;
    }

    public BLeitorSgEmprestimo(long bleitorNrCartao, long sgemprestimolistIdemprestimo) {
        this.bLeitorSgEmprestimoPK = new BLeitorSgEmprestimoPK(bleitorNrCartao, sgemprestimolistIdemprestimo);
    }

    public BLeitorSgEmprestimoPK getBLeitorSgEmprestimoPK() {
        return bLeitorSgEmprestimoPK;
    }

    public void setBLeitorSgEmprestimoPK(BLeitorSgEmprestimoPK bLeitorSgEmprestimoPK) {
        this.bLeitorSgEmprestimoPK = bLeitorSgEmprestimoPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bLeitorSgEmprestimoPK != null ? bLeitorSgEmprestimoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BLeitorSgEmprestimo)) {
            return false;
        }
        BLeitorSgEmprestimo other = (BLeitorSgEmprestimo) object;
        if ((this.bLeitorSgEmprestimoPK == null && other.bLeitorSgEmprestimoPK != null) || (this.bLeitorSgEmprestimoPK != null && !this.bLeitorSgEmprestimoPK.equals(other.bLeitorSgEmprestimoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BLeitorSgEmprestimo[ bLeitorSgEmprestimoPK=" + bLeitorSgEmprestimoPK + " ]";
    }
    
}
