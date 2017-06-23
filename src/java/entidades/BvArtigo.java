/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
@Table(name = "bv_artigo", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BvArtigo.findAll", query = "SELECT b FROM BvArtigo b")
    , @NamedQuery(name = "BvArtigo.findByIdartigo", query = "SELECT b FROM BvArtigo b WHERE b.idartigo = :idartigo")
    , @NamedQuery(name = "BvArtigo.findByAutor", query = "SELECT b FROM BvArtigo b WHERE b.autor = :autor")
    , @NamedQuery(name = "BvArtigo.findByDataPublicacao", query = "SELECT b FROM BvArtigo b WHERE b.dataPublicacao = :dataPublicacao")
    , @NamedQuery(name = "BvArtigo.findByDescricao", query = "SELECT b FROM BvArtigo b WHERE b.descricao = :descricao")
    , @NamedQuery(name = "BvArtigo.findByDirectorioCapa", query = "SELECT b FROM BvArtigo b WHERE b.directorioCapa = :directorioCapa")
    , @NamedQuery(name = "BvArtigo.findByDirectorioPdf", query = "SELECT b FROM BvArtigo b WHERE b.directorioPdf = :directorioPdf")
    , @NamedQuery(name = "BvArtigo.findByDireitos", query = "SELECT b FROM BvArtigo b WHERE b.direitos = :direitos")
    , @NamedQuery(name = "BvArtigo.findByEstado", query = "SELECT b FROM BvArtigo b WHERE b.estado = :estado")
    , @NamedQuery(name = "BvArtigo.findByFormato", query = "SELECT b FROM BvArtigo b WHERE b.formato = :formato")
    , @NamedQuery(name = "BvArtigo.findByIdioma", query = "SELECT b FROM BvArtigo b WHERE b.idioma = :idioma")
    , @NamedQuery(name = "BvArtigo.findByTitulo", query = "SELECT b FROM BvArtigo b WHERE b.titulo = :titulo")
    , @NamedQuery(name = "BvArtigo.findByAvaliacaoObs", query = "SELECT b FROM BvArtigo b WHERE b.avaliacaoObs = :avaliacaoObs")
    , @NamedQuery(name = "BvArtigo.findByAvaliacaoNota", query = "SELECT b FROM BvArtigo b WHERE b.avaliacaoNota = :avaliacaoNota")
    , @NamedQuery(name = "BvArtigo.findByDataSubmissao", query = "SELECT b FROM BvArtigo b WHERE b.dataSubmissao = :dataSubmissao")})
public class BvArtigo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idartigo")
    private Long idartigo;
    @Column(name = "autor")
    private String autor;
    @Column(name = "data_publicacao")
    @Temporal(TemporalType.DATE)
    private Date dataPublicacao;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "directorio_capa")
    private String directorioCapa;
    @Column(name = "directorio_pdf")
    private String directorioPdf;
    @Column(name = "direitos")
    private String direitos;
    @Column(name = "estado")
    private String estado;
    @Column(name = "formato")
    private String formato;
    @Column(name = "idioma")
    private String idioma;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "avaliacao_obs")
    private String avaliacaoObs;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avaliacao_nota")
    private Double avaliacaoNota;
    @Column(name = "data_submissao")
    @Temporal(TemporalType.DATE)
    private Date dataSubmissao;
    @OneToMany(mappedBy = "idPublicacao", fetch = FetchType.LAZY)
    private Collection<BNotificacao> bNotificacaoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bvArtigo", fetch = FetchType.LAZY)
    private Collection<BvLeitura> bvLeituraCollection;
    @JoinColumn(name = "publicador", referencedColumnName = "nr_cartao")
    @ManyToOne(fetch = FetchType.LAZY)
    private BLeitor publicador;
    @JoinColumn(name = "tipodoc", referencedColumnName = "categoria")
    @ManyToOne(fetch = FetchType.LAZY)
    private BvArtigoCategoria tipodoc;
    @JoinColumn(name = "avaliador", referencedColumnName = "id_leitor")
    @ManyToOne(fetch = FetchType.LAZY)
    private BvAvaliador avaliador;
    @JoinColumn(name = "curso_alvo", referencedColumnName = "id_curso")
    @ManyToOne(fetch = FetchType.LAZY)
    private Curso cursoAlvo;
    @JoinColumn(name = "area", referencedColumnName = "idarea")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgObraArea area;

    public BvArtigo() {
    }

    public BvArtigo(Long idartigo) {
        this.idartigo = idartigo;
    }

    public Long getIdartigo() {
        return idartigo;
    }

    public void setIdartigo(Long idartigo) {
        this.idartigo = idartigo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(Date dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDirectorioCapa() {
        return directorioCapa;
    }

    public void setDirectorioCapa(String directorioCapa) {
        this.directorioCapa = directorioCapa;
    }

    public String getDirectorioPdf() {
        return directorioPdf;
    }

    public void setDirectorioPdf(String directorioPdf) {
        this.directorioPdf = directorioPdf;
    }

    public String getDireitos() {
        return direitos;
    }

    public void setDireitos(String direitos) {
        this.direitos = direitos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAvaliacaoObs() {
        return avaliacaoObs;
    }

    public void setAvaliacaoObs(String avaliacaoObs) {
        this.avaliacaoObs = avaliacaoObs;
    }

    public Double getAvaliacaoNota() {
        return avaliacaoNota;
    }

    public void setAvaliacaoNota(Double avaliacaoNota) {
        this.avaliacaoNota = avaliacaoNota;
    }

    public Date getDataSubmissao() {
        return dataSubmissao;
    }

    public void setDataSubmissao(Date dataSubmissao) {
        this.dataSubmissao = dataSubmissao;
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

    public BLeitor getPublicador() {
        return publicador;
    }

    public void setPublicador(BLeitor publicador) {
        this.publicador = publicador;
    }

    public BvArtigoCategoria getTipodoc() {
        return tipodoc;
    }

    public void setTipodoc(BvArtigoCategoria tipodoc) {
        this.tipodoc = tipodoc;
    }

    public BvAvaliador getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(BvAvaliador avaliador) {
        this.avaliador = avaliador;
    }

    public Curso getCursoAlvo() {
        return cursoAlvo;
    }

    public void setCursoAlvo(Curso cursoAlvo) {
        this.cursoAlvo = cursoAlvo;
    }

    public SgObraArea getArea() {
        return area;
    }

    public void setArea(SgObraArea area) {
        this.area = area;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idartigo != null ? idartigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BvArtigo)) {
            return false;
        }
        BvArtigo other = (BvArtigo) object;
        if ((this.idartigo == null && other.idartigo != null) || (this.idartigo != null && !this.idartigo.equals(other.idartigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BvArtigo[ idartigo=" + idartigo + " ]";
    }
    
}
