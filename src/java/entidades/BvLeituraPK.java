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
public class BvLeituraPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "data_leitura")
    @Temporal(TemporalType.DATE)
    private Date dataLeitura;
    @Basic(optional = false)
    @Column(name = "obra")
    private long obra;
    @Basic(optional = false)
    @Column(name = "horas_leitura")
    @Temporal(TemporalType.TIME)
    private Date horasLeitura;
    @Basic(optional = false)
    @Column(name = "leitor")
    private long leitor;

    public BvLeituraPK() {
    }

    public BvLeituraPK(Date dataLeitura, long obra, Date horasLeitura, long leitor) {
        this.dataLeitura = dataLeitura;
        this.obra = obra;
        this.horasLeitura = horasLeitura;
        this.leitor = leitor;
    }

    public Date getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public long getObra() {
        return obra;
    }

    public void setObra(long obra) {
        this.obra = obra;
    }

    public Date getHorasLeitura() {
        return horasLeitura;
    }

    public void setHorasLeitura(Date horasLeitura) {
        this.horasLeitura = horasLeitura;
    }

    public long getLeitor() {
        return leitor;
    }

    public void setLeitor(long leitor) {
        this.leitor = leitor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataLeitura != null ? dataLeitura.hashCode() : 0);
        hash += (int) obra;
        hash += (horasLeitura != null ? horasLeitura.hashCode() : 0);
        hash += (int) leitor;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BvLeituraPK)) {
            return false;
        }
        BvLeituraPK other = (BvLeituraPK) object;
        if ((this.dataLeitura == null && other.dataLeitura != null) || (this.dataLeitura != null && !this.dataLeitura.equals(other.dataLeitura))) {
            return false;
        }
        if (this.obra != other.obra) {
            return false;
        }
        if ((this.horasLeitura == null && other.horasLeitura != null) || (this.horasLeitura != null && !this.horasLeitura.equals(other.horasLeitura))) {
            return false;
        }
        if (this.leitor != other.leitor) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BvLeituraPK[ dataLeitura=" + dataLeitura + ", obra=" + obra + ", horasLeitura=" + horasLeitura + ", leitor=" + leitor + " ]";
    }
    
}
