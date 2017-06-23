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
public class BLeitorSgEmprestimoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "bleitor_nr_cartao")
    private long bleitorNrCartao;
    @Basic(optional = false)
    @Column(name = "sgemprestimolist_idemprestimo")
    private long sgemprestimolistIdemprestimo;

    public BLeitorSgEmprestimoPK() {
    }

    public BLeitorSgEmprestimoPK(long bleitorNrCartao, long sgemprestimolistIdemprestimo) {
        this.bleitorNrCartao = bleitorNrCartao;
        this.sgemprestimolistIdemprestimo = sgemprestimolistIdemprestimo;
    }

    public long getBleitorNrCartao() {
        return bleitorNrCartao;
    }

    public void setBleitorNrCartao(long bleitorNrCartao) {
        this.bleitorNrCartao = bleitorNrCartao;
    }

    public long getSgemprestimolistIdemprestimo() {
        return sgemprestimolistIdemprestimo;
    }

    public void setSgemprestimolistIdemprestimo(long sgemprestimolistIdemprestimo) {
        this.sgemprestimolistIdemprestimo = sgemprestimolistIdemprestimo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) bleitorNrCartao;
        hash += (int) sgemprestimolistIdemprestimo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BLeitorSgEmprestimoPK)) {
            return false;
        }
        BLeitorSgEmprestimoPK other = (BLeitorSgEmprestimoPK) object;
        if (this.bleitorNrCartao != other.bleitorNrCartao) {
            return false;
        }
        if (this.sgemprestimolistIdemprestimo != other.sgemprestimolistIdemprestimo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BLeitorSgEmprestimoPK[ bleitorNrCartao=" + bleitorNrCartao + ", sgemprestimolistIdemprestimo=" + sgemprestimolistIdemprestimo + " ]";
    }
    
}
