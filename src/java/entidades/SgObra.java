/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "sg_obra", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgObra.findAll", query = "SELECT s FROM SgObra s")
    , @NamedQuery(name = "SgObra.findByIdlivro", query = "SELECT s FROM SgObra s WHERE s.idlivro = :idlivro")
    , @NamedQuery(name = "SgObra.findByCadastroData", query = "SELECT s FROM SgObra s WHERE s.cadastroData = :cadastroData")
    , @NamedQuery(name = "SgObra.findByCodigoBarra", query = "SELECT s FROM SgObra s WHERE s.codigoBarra = :codigoBarra")
    , @NamedQuery(name = "SgObra.findByCota", query = "SELECT s FROM SgObra s WHERE s.cota = :cota")
    , @NamedQuery(name = "SgObra.findByDirectorio", query = "SELECT s FROM SgObra s WHERE s.directorio = :directorio")
    , @NamedQuery(name = "SgObra.findByEdicao", query = "SELECT s FROM SgObra s WHERE s.edicao = :edicao")
    , @NamedQuery(name = "SgObra.findByEdicaoCidade", query = "SELECT s FROM SgObra s WHERE s.edicaoCidade = :edicaoCidade")
    , @NamedQuery(name = "SgObra.findByEditora", query = "SELECT s FROM SgObra s WHERE s.editora = :editora")
    , @NamedQuery(name = "SgObra.findByFormato", query = "SELECT s FROM SgObra s WHERE s.formato = :formato")
    , @NamedQuery(name = "SgObra.findByIdioma", query = "SELECT s FROM SgObra s WHERE s.idioma = :idioma")
    , @NamedQuery(name = "SgObra.findByIsbn", query = "SELECT s FROM SgObra s WHERE s.isbn = :isbn")
    , @NamedQuery(name = "SgObra.findByPublicacaoAno", query = "SELECT s FROM SgObra s WHERE s.publicacaoAno = :publicacaoAno")
    , @NamedQuery(name = "SgObra.findByTitulo", query = "SELECT s FROM SgObra s WHERE s.titulo = :titulo")
    , @NamedQuery(name = "SgObra.findByVolume", query = "SELECT s FROM SgObra s WHERE s.volume = :volume")
    , @NamedQuery(name = "SgObra.findByCapaDir", query = "SELECT s FROM SgObra s WHERE s.capaDir = :capaDir")
    , @NamedQuery(name = "SgObra.findByTipoObra", query = "SELECT s FROM SgObra s WHERE s.tipoObra = :tipoObra")
    , @NamedQuery(name = "SgObra.findByNome", query = "SELECT s FROM SgObra s WHERE s.nome = :nome")
    , @NamedQuery(name = "SgObra.findByMotivoRemocao", query = "SELECT s FROM SgObra s WHERE s.motivoRemocao = :motivoRemocao")})
public class SgObra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idlivro")
    private Long idlivro;
    @Column(name = "cadastro_data")
    @Temporal(TemporalType.DATE)
    private Date cadastroData;
    @Column(name = "codigo_barra")
    private String codigoBarra;
    @Column(name = "cota")
    private String cota;
    @Column(name = "directorio")
    private String directorio;
    @Column(name = "edicao")
    private String edicao;
    @Column(name = "edicao_cidade")
    private String edicaoCidade;
    @Column(name = "editora")
    private String editora;
    @Column(name = "formato")
    private String formato;
    @Column(name = "idioma")
    private String idioma;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "publicacao_ano")
    private String publicacaoAno;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "volume")
    private String volume;
    @Column(name = "capa_dir")
    private String capaDir;
    @Column(name = "tipo_obra")
    private String tipoObra;
    @Column(name = "nome")
    private String nome;
    @Column(name = "motivo_remocao")
    private String motivoRemocao;
    @JoinColumn(name = "curso", referencedColumnName = "id_curso")
    @ManyToOne(fetch = FetchType.LAZY)
    private Curso curso;
    @JoinColumn(name = "area", referencedColumnName = "idarea")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgObraArea area;
    @JoinColumn(name = "dominio", referencedColumnName = "categoria")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgObraCategoria dominio;
    @JoinColumn(name = "bibliotecario", referencedColumnName = "utilizador")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users bibliotecario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sgObra", fetch = FetchType.LAZY)
    private Collection<SgObraAutor> sgObraAutorCollection;
    @OneToMany(mappedBy = "obraRef", fetch = FetchType.LAZY)
    private Collection<SgExemplar> sgExemplarCollection;
    private List<SgExemplar> SgExemplarlist;
    private Collection<? extends SgObraAutor> SgObraAutorlist;

    public SgObra() {
    }

    public SgObra(Long idlivro) {
        this.idlivro = idlivro;
    }

    public Long getIdlivro() {
        return idlivro;
    }

    public void setIdlivro(Long idlivro) {
        this.idlivro = idlivro;
    }

    public Date getCadastroData() {
        return cadastroData;
    }

    public void setCadastroData(Date cadastroData) {
        this.cadastroData = cadastroData;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCota() {
        return cota;
    }

    public void setCota(String cota) {
        this.cota = cota;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getEdicaoCidade() {
        return edicaoCidade;
    }

    public void setEdicaoCidade(String edicaoCidade) {
        this.edicaoCidade = edicaoCidade;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublicacaoAno() {
        return publicacaoAno;
    }

    public void setPublicacaoAno(String publicacaoAno) {
        this.publicacaoAno = publicacaoAno;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCapaDir() {
        return capaDir;
    }

    public void setCapaDir(String capaDir) {
        this.capaDir = capaDir;
    }

    public String getTipoObra() {
        return tipoObra;
    }

    public void setTipoObra(String tipoObra) {
        this.tipoObra = tipoObra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMotivoRemocao() {
        return motivoRemocao;
    }

    public void setMotivoRemocao(String motivoRemocao) {
        this.motivoRemocao = motivoRemocao;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public SgObraArea getArea() {
        return area;
    }

    public void setArea(SgObraArea area) {
        this.area = area;
    }

    public SgObraCategoria getDominio() {
        return dominio;
    }

    public void setDominio(SgObraCategoria dominio) {
        this.dominio = dominio;
    }

    public Users getBibliotecario() {
        return bibliotecario;
    }

    public void setBibliotecario(Users bibliotecario) {
        this.bibliotecario = bibliotecario;
    }

    @XmlTransient
    public Collection<SgObraAutor> getSgObraAutorCollection() {
        return sgObraAutorCollection;
    }

    public void setSgObraAutorCollection(Collection<SgObraAutor> sgObraAutorCollection) {
        this.sgObraAutorCollection = sgObraAutorCollection;
    }

    @XmlTransient
    public Collection<SgExemplar> getSgExemplarCollection() {
        return sgExemplarCollection;
    }

    public void setSgExemplarCollection(Collection<SgExemplar> sgExemplarCollection) {
        this.sgExemplarCollection = sgExemplarCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idlivro != null ? idlivro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgObra)) {
            return false;
        }
        SgObra other = (SgObra) object;
        if ((this.idlivro == null && other.idlivro != null) || (this.idlivro != null && !this.idlivro.equals(other.idlivro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgObra[ idlivro=" + idlivro + " ]";
    }

    public List<SgExemplar> getSgExemplarList() {
        return SgExemplarlist;
    }

    public Collection<? extends SgObraAutor> getSgObraAutorList() {
        return SgObraAutorlist;
    }
    
}
