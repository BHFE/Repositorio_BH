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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "sg_emprestimo", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgEmprestimo.findAll", query = "SELECT s FROM SgEmprestimo s")
    , @NamedQuery(name = "SgEmprestimo.findByIdemprestimo", query = "SELECT s FROM SgEmprestimo s WHERE s.idemprestimo = :idemprestimo")
    , @NamedQuery(name = "SgEmprestimo.findByDataDevolucao", query = "SELECT s FROM SgEmprestimo s WHERE s.dataDevolucao = :dataDevolucao")
    , @NamedQuery(name = "SgEmprestimo.findByDataEmprestimo", query = "SELECT s FROM SgEmprestimo s WHERE s.dataEmprestimo = :dataEmprestimo")
    , @NamedQuery(name = "SgEmprestimo.findByMultaEstado", query = "SELECT s FROM SgEmprestimo s WHERE s.multaEstado = :multaEstado")
    , @NamedQuery(name = "SgEmprestimo.findByMultaMotivo", query = "SELECT s FROM SgEmprestimo s WHERE s.multaMotivo = :multaMotivo")
    , @NamedQuery(name = "SgEmprestimo.findByMultaPagamento", query = "SELECT s FROM SgEmprestimo s WHERE s.multaPagamento = :multaPagamento")
    , @NamedQuery(name = "SgEmprestimo.findByMultaValor", query = "SELECT s FROM SgEmprestimo s WHERE s.multaValor = :multaValor")
    , @NamedQuery(name = "SgEmprestimo.findByTipoEmprestimo", query = "SELECT s FROM SgEmprestimo s WHERE s.tipoEmprestimo = :tipoEmprestimo")
    , @NamedQuery(name = "SgEmprestimo.findByMultaCriacaodata", query = "SELECT s FROM SgEmprestimo s WHERE s.multaCriacaodata = :multaCriacaodata")
    , @NamedQuery(name = "SgEmprestimo.findByEstado", query = "SELECT s FROM SgEmprestimo s WHERE s.estado = :estado")
    , @NamedQuery(name = "SgEmprestimo.findByEstadoRenovacao", query = "SELECT s FROM SgEmprestimo s WHERE s.estadoRenovacao = :estadoRenovacao")})
public class SgEmprestimo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idemprestimo")
    private Long idemprestimo;
    @Column(name = "data_devolucao")
    @Temporal(TemporalType.DATE)
    private Date dataDevolucao;
    @Column(name = "data_emprestimo")
    @Temporal(TemporalType.DATE)
    private Date dataEmprestimo;
    @Column(name = "multa_estado")
    private String multaEstado;
    @Column(name = "multa_motivo")
    private String multaMotivo;
    @Column(name = "multa_pagamento")
    private String multaPagamento;
    @Column(name = "multa_valor")
    private String multaValor;
    @Column(name = "tipo_emprestimo")
    private String tipoEmprestimo;
    @Column(name = "multa_criacaodata")
    @Temporal(TemporalType.DATE)
    private Date multaCriacaodata;
    @Column(name = "estado")
    private String estado;
    @Column(name = "estado_renovacao")
    private String estadoRenovacao;
    @JoinColumn(name = "id_leitor", referencedColumnName = "nr_cartao")
    @ManyToOne(fetch = FetchType.LAZY)
    private BLeitor idLeitor;
    @JoinColumn(name = "reserva_ref", referencedColumnName = "idagenda")
    @ManyToOne(fetch = FetchType.LAZY)
    private BReserva reservaRef;
    @JoinColumn(name = "parametros_ref", referencedColumnName = "idparametro")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgEmprestimoParametros parametrosRef;
    @JoinColumn(name = "exemplar_ref", referencedColumnName = "nr_registo")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgExemplar exemplarRef;
    @JoinColumn(name = "agente_bibliot", referencedColumnName = "utilizador")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users agenteBibliot;

    public SgEmprestimo() {
    }

    public SgEmprestimo(Long idemprestimo) {
        this.idemprestimo = idemprestimo;
    }

    public Long getIdemprestimo() {
        return idemprestimo;
    }

    public void setIdemprestimo(Long idemprestimo) {
        this.idemprestimo = idemprestimo;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public String getMultaEstado() {
        return multaEstado;
    }

    public void setMultaEstado(String multaEstado) {
        this.multaEstado = multaEstado;
    }

    public String getMultaMotivo() {
        return multaMotivo;
    }

    public void setMultaMotivo(String multaMotivo) {
        this.multaMotivo = multaMotivo;
    }

    public String getMultaPagamento() {
        return multaPagamento;
    }

    public void setMultaPagamento(String multaPagamento) {
        this.multaPagamento = multaPagamento;
    }

    public String getMultaValor() {
        return multaValor;
    }

    public void setMultaValor(String multaValor) {
        this.multaValor = multaValor;
    }

    public String getTipoEmprestimo() {
        return tipoEmprestimo;
    }

    public void setTipoEmprestimo(String tipoEmprestimo) {
        this.tipoEmprestimo = tipoEmprestimo;
    }

    public Date getMultaCriacaodata() {
        return multaCriacaodata;
    }

    public void setMultaCriacaodata(Date multaCriacaodata) {
        this.multaCriacaodata = multaCriacaodata;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoRenovacao() {
        return estadoRenovacao;
    }

    public void setEstadoRenovacao(String estadoRenovacao) {
        this.estadoRenovacao = estadoRenovacao;
    }

    public BLeitor getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(BLeitor idLeitor) {
        this.idLeitor = idLeitor;
    }

    public BReserva getReservaRef() {
        return reservaRef;
    }

    public void setReservaRef(BReserva reservaRef) {
        this.reservaRef = reservaRef;
    }

    public SgEmprestimoParametros getParametrosRef() {
        return parametrosRef;
    }

    public void setParametrosRef(SgEmprestimoParametros parametrosRef) {
        this.parametrosRef = parametrosRef;
    }

    public SgExemplar getExemplarRef() {
        return exemplarRef;
    }

    public void setExemplarRef(SgExemplar exemplarRef) {
        this.exemplarRef = exemplarRef;
    }

    public Users getAgenteBibliot() {
        return agenteBibliot;
    }

    public void setAgenteBibliot(Users agenteBibliot) {
        this.agenteBibliot = agenteBibliot;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idemprestimo != null ? idemprestimo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEmprestimo)) {
            return false;
        }
        SgEmprestimo other = (SgEmprestimo) object;
        if ((this.idemprestimo == null && other.idemprestimo != null) || (this.idemprestimo != null && !this.idemprestimo.equals(other.idemprestimo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgEmprestimo[ idemprestimo=" + idemprestimo + " ]";
    }
    
}
