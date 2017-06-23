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
 * @author Nobrega
 */
@Entity
@Table(name = "bv_artigo", catalog = "sgb", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BvArtigo.findAll", query = "SELECT b FROM BvArtigo b"),
    @NamedQuery(name = "BvArtigo.findByIdartigo", query = "SELECT b FROM BvArtigo b WHERE b.idartigo = :idartigo"),
    @NamedQuery(name = "BvArtigo.findByAutor", query = "SELECT b FROM BvArtigo b WHERE b.autor = :autor"),
    @NamedQuery(name = "BvArtigo.findByDataPublicacao", query = "SELECT b FROM BvArtigo b WHERE b.dataPublicacao = :dataPublicacao"),
    @NamedQuery(name = "BvArtigo.findByDescricao", query = "SELECT b FROM BvArtigo b WHERE b.descricao = :descricao"),
    @NamedQuery(name = "BvArtigo.findByDirectorioCapa", query = "SELECT b FROM BvArtigo b WHERE b.directorioCapa = :directorioCapa"),
    @NamedQuery(name = "BvArtigo.findByDirectorioPdf", query = "SELECT b FROM BvArtigo b WHERE b.directorioPdf = :directorioPdf"),
    @NamedQuery(name = "BvArtigo.findByDireitos", query = "SELECT b FROM BvArtigo b WHERE b.direitos = :direitos"),
    @NamedQuery(name = "BvArtigo.findByEstado", query = "SELECT b FROM BvArtigo b WHERE b.estado = :estado"),
    @NamedQuery(name = "BvArtigo.findByFormato", query = "SELECT b FROM BvArtigo b WHERE b.formato = :formato"),
    @NamedQuery(name = "BvArtigo.findByIdioma", query = "SELECT b FROM BvArtigo b WHERE b.idioma = :idioma"),
    @NamedQuery(name = "BvArtigo.findByTitulo", query = "SELECT b FROM BvArtigo b WHERE b.titulo = :titulo"),
    @NamedQuery(name = "BvArtigo.findByAvaliacaoObs", query = "SELECT b FROM BvArtigo b WHERE b.avaliacaoObs = :avaliacaoObs"),
    @NamedQuery(name = "BvArtigo.findByAvaliacaoNota", query = "SELECT b FROM BvArtigo b WHERE b.avaliacaoNota = :avaliacaoNota"),
    @NamedQuery(name = "BvArtigo.findByDataSubmissao", query = "SELECT b FROM BvArtigo b WHERE b.dataSubmissao = :dataSubmissao")})
public class BvArtigo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long idartigo;
    @Column(length = 255)
    private String autor;
    @Column(name = "data_publicacao")
    @Temporal(TemporalType.DATE)
    private Date dataPublicacao;
    @Column(length = 8000)
    private String descricao;
    @Column(name = "directorio_capa", length = 255)
    private String directorioCapa;
    @Column(name = "directorio_pdf", length = 255)
    private String directorioPdf;
    @Column(length = 8000)
    private String direitos;
    @Column(length = 255)
    private String estado;
    @Column(length = 255)
    private String formato;
    @Column(length = 255)
    private String idioma;
    @Column(length = 255)
    private String titulo;
    @Column(name = "avaliacao_obs", length = 8000)
    private String avaliacaoObs;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avaliacao_nota", precision = 17, scale = 17)
    private Double avaliacaoNota;
    @Column(name = "data_submissao")
    @Temporal(TemporalType.DATE)
    private Date dataSubmissao;
    @OneToMany(mappedBy = "idPublicacao", fetch = FetchType.LAZY)
    private List<BNotificacao> bNotificacaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bvArtigo", fetch = FetchType.LAZY)
    private List<BvLeitura> bvLeituraList;
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
    public List<BNotificacao> getBNotificacaoList() {
        return bNotificacaoList;
    }

    public void setBNotificacaoList(List<BNotificacao> bNotificacaoList) {
        this.bNotificacaoList = bNotificacaoList;
    }

    @XmlTransient
    public List<BvLeitura> getBvLeituraList() {
        return bvLeituraList;
    }

    public void setBvLeituraList(List<BvLeitura> bvLeituraList) {
        this.bvLeituraList = bvLeituraList;
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
