/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Nobrega
 */
@Entity
@Table(name = "sg_exemplar", catalog = "sgb", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgExemplar.findAll", query = "SELECT s FROM SgExemplar s"),
    @NamedQuery(name = "SgExemplar.findByNrRegisto", query = "SELECT s FROM SgExemplar s WHERE s.nrRegisto = :nrRegisto"),
    @NamedQuery(name = "SgExemplar.findByDataAquisicao", query = "SELECT s FROM SgExemplar s WHERE s.dataAquisicao = :dataAquisicao"),
    @NamedQuery(name = "SgExemplar.findByDataRegisto", query = "SELECT s FROM SgExemplar s WHERE s.dataRegisto = :dataRegisto"),
    @NamedQuery(name = "SgExemplar.findByEstado", query = "SELECT s FROM SgExemplar s WHERE s.estado = :estado"),
    @NamedQuery(name = "SgExemplar.findByForma", query = "SELECT s FROM SgExemplar s WHERE s.forma = :forma"),
    @NamedQuery(name = "SgExemplar.findByMotivoRemocao", query = "SELECT s FROM SgExemplar s WHERE s.motivoRemocao = :motivoRemocao")})
public class SgExemplar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nr_registo", nullable = false)
    private Long nrRegisto;
    @Column(name = "data_aquisicao")
    @Temporal(TemporalType.DATE)
    private Date dataAquisicao;
    @Column(name = "data_registo")
    @Temporal(TemporalType.DATE)
    private Date dataRegisto;
    @Column(length = 255)
    private String estado;
    @Column(length = 255)
    private String forma;
    @Column(name = "motivo_remocao", length = 255)
    private String motivoRemocao;
    @OneToMany(mappedBy = "exemplarRef", fetch = FetchType.LAZY)
    private List<SgEmprestimo> sgEmprestimoList;
    @JoinColumn(name = "obra_ref", referencedColumnName = "idlivro")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgObra obraRef;
    @JoinColumn(name = "agente_registo", referencedColumnName = "utilizador")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users agenteRegisto;
    @OneToMany(mappedBy = "livro", fetch = FetchType.LAZY)
    private List<BReserva> bReservaList;

    public SgExemplar() {
    }

    public SgExemplar(Long nrRegisto) {
        this.nrRegisto = nrRegisto;
    }

    public Long getNrRegisto() {
        return nrRegisto;
    }

    public void setNrRegisto(Long nrRegisto) {
        this.nrRegisto = nrRegisto;
    }

    public Date getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(Date dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public Date getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(Date dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getMotivoRemocao() {
        return motivoRemocao;
    }

    public void setMotivoRemocao(String motivoRemocao) {
        this.motivoRemocao = motivoRemocao;
    }

    @XmlTransient
    public List<SgEmprestimo> getSgEmprestimoList() {
        return sgEmprestimoList;
    }

    public void setSgEmprestimoList(List<SgEmprestimo> sgEmprestimoList) {
        this.sgEmprestimoList = sgEmprestimoList;
    }

    public SgObra getObraRef() {
        return obraRef;
    }

    public void setObraRef(SgObra obraRef) {
        this.obraRef = obraRef;
    }

    public Users getAgenteRegisto() {
        return agenteRegisto;
    }

    public void setAgenteRegisto(Users agenteRegisto) {
        this.agenteRegisto = agenteRegisto;
    }

    @XmlTransient
    public List<BReserva> getBReservaList() {
        return bReservaList;
    }

    public void setBReservaList(List<BReserva> bReservaList) {
        this.bReservaList = bReservaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nrRegisto != null ? nrRegisto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgExemplar)) {
            return false;
        }
        SgExemplar other = (SgExemplar) object;
        if ((this.nrRegisto == null && other.nrRegisto != null) || (this.nrRegisto != null && !this.nrRegisto.equals(other.nrRegisto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgExemplar[ nrRegisto=" + nrRegisto + " ]";
    }
    
}
