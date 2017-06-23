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
 * @author Nobrega
 */
@Entity
@Table(name = "b_notificacao", catalog = "sgb", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BNotificacao.findAll", query = "SELECT b FROM BNotificacao b"),
    @NamedQuery(name = "BNotificacao.findByIdNotificacao", query = "SELECT b FROM BNotificacao b WHERE b.idNotificacao = :idNotificacao"),
    @NamedQuery(name = "BNotificacao.findByEmissor", query = "SELECT b FROM BNotificacao b WHERE b.emissor = :emissor"),
    @NamedQuery(name = "BNotificacao.findByMensagem", query = "SELECT b FROM BNotificacao b WHERE b.mensagem = :mensagem"),
    @NamedQuery(name = "BNotificacao.findByDataEnvio", query = "SELECT b FROM BNotificacao b WHERE b.dataEnvio = :dataEnvio")})
public class BNotificacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_notificacao", nullable = false)
    private Long idNotificacao;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String emissor;
    @Basic(optional = false)
    @Column(nullable = false, length = 8000)
    private String mensagem;
    @Column(name = "data_envio")
    @Temporal(TemporalType.DATE)
    private Date dataEnvio;
    @JoinColumn(name = "id_leitor", referencedColumnName = "nr_cartao")
    @ManyToOne(fetch = FetchType.LAZY)
    private BLeitor idLeitor;
    @JoinColumn(name = "id_reserva", referencedColumnName = "idagenda")
    @ManyToOne(fetch = FetchType.LAZY)
    private BReserva idReserva;
    @JoinColumn(name = "id_publicacao", referencedColumnName = "idartigo")
    @ManyToOne(fetch = FetchType.LAZY)
    private BvArtigo idPublicacao;

    public BNotificacao() {
    }

    public BNotificacao(Long idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public BNotificacao(Long idNotificacao, String emissor, String mensagem) {
        this.idNotificacao = idNotificacao;
        this.emissor = emissor;
        this.mensagem = mensagem;
    }

    public Long getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(Long idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public String getEmissor() {
        return emissor;
    }

    public void setEmissor(String emissor) {
        this.emissor = emissor;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public BLeitor getIdLeitor() {
        return idLeitor;
    }

    public void setIdLeitor(BLeitor idLeitor) {
        this.idLeitor = idLeitor;
    }

    public BReserva getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(BReserva idReserva) {
        this.idReserva = idReserva;
    }

    public BvArtigo getIdPublicacao() {
        return idPublicacao;
    }

    public void setIdPublicacao(BvArtigo idPublicacao) {
        this.idPublicacao = idPublicacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNotificacao != null ? idNotificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BNotificacao)) {
            return false;
        }
        BNotificacao other = (BNotificacao) object;
        if ((this.idNotificacao == null && other.idNotificacao != null) || (this.idNotificacao != null && !this.idNotificacao.equals(other.idNotificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BNotificacao[ idNotificacao=" + idNotificacao + " ]";
    }
    
}
