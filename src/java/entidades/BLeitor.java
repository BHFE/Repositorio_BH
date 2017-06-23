/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "b_leitor", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BLeitor.findAll", query = "SELECT b FROM BLeitor b")
    , @NamedQuery(name = "BLeitor.findByNrCartao", query = "SELECT b FROM BLeitor b WHERE b.nrCartao = :nrCartao")
    , @NamedQuery(name = "BLeitor.findByTipoLeitor", query = "SELECT b FROM BLeitor b WHERE b.tipoLeitor = :tipoLeitor")
    , @NamedQuery(name = "BLeitor.findByFotoUrl", query = "SELECT b FROM BLeitor b WHERE b.fotoUrl = :fotoUrl")
    , @NamedQuery(name = "BLeitor.findByDataRegisto", query = "SELECT b FROM BLeitor b WHERE b.dataRegisto = :dataRegisto")
    , @NamedQuery(name = "BLeitor.findByDataActualizacao", query = "SELECT b FROM BLeitor b WHERE b.dataActualizacao = :dataActualizacao")
    , @NamedQuery(name = "BLeitor.findByBi", query = "SELECT b FROM BLeitor b WHERE b.bi = :bi")
    , @NamedQuery(name = "BLeitor.findByEmail", query = "SELECT b FROM BLeitor b WHERE b.email = :email")
    , @NamedQuery(name = "BLeitor.findByMoradia", query = "SELECT b FROM BLeitor b WHERE b.moradia = :moradia")
    , @NamedQuery(name = "BLeitor.findByNome", query = "SELECT b FROM BLeitor b WHERE b.nome = :nome")
    , @NamedQuery(name = "BLeitor.findByTelefone", query = "SELECT b FROM BLeitor b WHERE b.telefone = :telefone")
    , @NamedQuery(name = "BLeitor.findByEstado", query = "SELECT b FROM BLeitor b WHERE b.estado = :estado")
    , @NamedQuery(name = "BLeitor.findByTipoConta", query = "SELECT b FROM BLeitor b WHERE b.tipoConta = :tipoConta")})
public class BLeitor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nr_cartao")
    private Long nrCartao;
    @Column(name = "tipo_leitor")
    private String tipoLeitor;
    @Column(name = "foto_url")
    private String fotoUrl;
    @Column(name = "data_registo")
    @Temporal(TemporalType.DATE)
    private Date dataRegisto;
    @Column(name = "data_actualizacao")
    @Temporal(TemporalType.DATE)
    private Date dataActualizacao;
    @Column(name = "bi")
    private String bi;
    @Column(name = "email")
    private String email;
    @Column(name = "moradia")
    private String moradia;
    @Column(name = "nome")
    private String nome;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "estado")
    private String estado;
    @Column(name = "tipo_conta")
    private String tipoConta;
    @OneToMany(mappedBy = "idLeitor", fetch = FetchType.LAZY)
    private Collection<SgEmprestimo> sgEmprestimoCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "bLeitor", fetch = FetchType.LAZY)
    private BvAvaliador bvAvaliador;
    @OneToMany(mappedBy = "idLeitor", fetch = FetchType.LAZY)
    private Collection<BNotificacao> bNotificacaoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bLeitor", fetch = FetchType.LAZY)
    private Collection<BvLeitura> bvLeituraCollection;
    @OneToMany(mappedBy = "publicador", fetch = FetchType.LAZY)
    private Collection<BvArtigo> bvArtigoCollection;
    @JoinColumn(name = "id_parametro_actualizacao", referencedColumnName = "idparametro")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgEmprestimoParametros idParametroActualizacao;
    @JoinColumn(name = "id_parametro_registo", referencedColumnName = "idparametro")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgEmprestimoParametros idParametroRegisto;
    @JoinColumn(name = "idagente", referencedColumnName = "utilizador")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users idagente;
    @JoinColumn(name = "idutilizador", referencedColumnName = "utilizador")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users idutilizador;
    @OneToMany(mappedBy = "leitor", fetch = FetchType.LAZY)
    private Collection<BReserva> bReservaCollection;
    private List<SgEmprestimo> SgEmprestimolist;

    public BLeitor() {
    }

    public BLeitor(Long nrCartao) {
        this.nrCartao = nrCartao;
    }

    public Long getNrCartao() {
        return nrCartao;
    }

    public void setNrCartao(Long nrCartao) {
        this.nrCartao = nrCartao;
    }

    public String getTipoLeitor() {
        return tipoLeitor;
    }

    public void setTipoLeitor(String tipoLeitor) {
        this.tipoLeitor = tipoLeitor;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Date getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(Date dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public Date getDataActualizacao() {
        return dataActualizacao;
    }

    public void setDataActualizacao(Date dataActualizacao) {
        this.dataActualizacao = dataActualizacao;
    }

    public String getBi() {
        return bi;
    }

    public void setBi(String bi) {
        this.bi = bi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoradia() {
        return moradia;
    }

    public void setMoradia(String moradia) {
        this.moradia = moradia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    @XmlTransient
    public Collection<SgEmprestimo> getSgEmprestimoCollection() {
        return sgEmprestimoCollection;
    }

    public void setSgEmprestimoCollection(Collection<SgEmprestimo> sgEmprestimoCollection) {
        this.sgEmprestimoCollection = sgEmprestimoCollection;
    }

    public BvAvaliador getBvAvaliador() {
        return bvAvaliador;
    }

    public void setBvAvaliador(BvAvaliador bvAvaliador) {
        this.bvAvaliador = bvAvaliador;
    }

    @XmlTransient
    public Collection<BNotificacao> getBNotificacaoCollection() {
        return bNotificacaoCollection;
    }

    public void setBNotificacaoCollection(Collection<BNotificacao> bNotificacaoCollection) {
        this.bNotificacaoCollection = bNotificacaoCollection;
    }

    @XmlTransient
    public Collection<BvLeitura> getBvLeituraCollection() {
        return bvLeituraCollection;
    }

    public void setBvLeituraCollection(Collection<BvLeitura> bvLeituraCollection) {
        this.bvLeituraCollection = bvLeituraCollection;
    }

    @XmlTransient
    public Collection<BvArtigo> getBvArtigoCollection() {
        return bvArtigoCollection;
    }

    public void setBvArtigoCollection(Collection<BvArtigo> bvArtigoCollection) {
        this.bvArtigoCollection = bvArtigoCollection;
    }

    public SgEmprestimoParametros getIdParametroActualizacao() {
        return idParametroActualizacao;
    }

    public void setIdParametroActualizacao(SgEmprestimoParametros idParametroActualizacao) {
        this.idParametroActualizacao = idParametroActualizacao;
    }

    public SgEmprestimoParametros getIdParametroRegisto() {
        return idParametroRegisto;
    }

    public void setIdParametroRegisto(SgEmprestimoParametros idParametroRegisto) {
        this.idParametroRegisto = idParametroRegisto;
    }

    public Users getIdagente() {
        return idagente;
    }

    public void setIdagente(Users idagente) {
        this.idagente = idagente;
    }

    public Users getIdutilizador() {
        return idutilizador;
    }

    public void setIdutilizador(Users idutilizador) {
        this.idutilizador = idutilizador;
    }

    @XmlTransient
    public Collection<BReserva> getBReservaCollection() {
        return bReservaCollection;
    }

    public void setBReservaCollection(Collection<BReserva> bReservaCollection) {
        this.bReservaCollection = bReservaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nrCartao != null ? nrCartao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BLeitor)) {
            return false;
        }
        BLeitor other = (BLeitor) object;
        if ((this.nrCartao == null && other.nrCartao != null) || (this.nrCartao != null && !this.nrCartao.equals(other.nrCartao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BLeitor[ nrCartao=" + nrCartao + " ]";
    }

    public List<SgEmprestimo> getSgEmprestimoList() {
      return SgEmprestimolist;
    }
    
}
