/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "curso", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c")
    , @NamedQuery(name = "Curso.findByIdCurso", query = "SELECT c FROM Curso c WHERE c.idCurso = :idCurso")
    , @NamedQuery(name = "Curso.findByCodigoCurso", query = "SELECT c FROM Curso c WHERE c.codigoCurso = :codigoCurso")
    , @NamedQuery(name = "Curso.findByDescricao", query = "SELECT c FROM Curso c WHERE c.descricao = :descricao")
    , @NamedQuery(name = "Curso.findByFaculdade", query = "SELECT c FROM Curso c WHERE c.faculdade = :faculdade")
    , @NamedQuery(name = "Curso.findByQtdSemestres", query = "SELECT c FROM Curso c WHERE c.qtdSemestres = :qtdSemestres")
    , @NamedQuery(name = "Curso.findByAbreviatura", query = "SELECT c FROM Curso c WHERE c.abreviatura = :abreviatura")})
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_curso")
    private Long idCurso;
    @Column(name = "codigo_curso")
    private String codigoCurso;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "faculdade")
    private Integer faculdade;
    @Column(name = "qtd_semestres")
    private Integer qtdSemestres;
    @Column(name = "abreviatura")
    private String abreviatura;
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private Collection<SgObra> sgObraCollection;
    @OneToMany(mappedBy = "cursoAlvo", fetch = FetchType.LAZY)
    private Collection<BvArtigo> bvArtigoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursocurrente", fetch = FetchType.LAZY)
    private Collection<Estudante> estudanteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoingresso", fetch = FetchType.LAZY)
    private Collection<Estudante> estudanteCollection1;

    public Curso() {
    }

    public Curso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Integer faculdade) {
        this.faculdade = faculdade;
    }

    public Integer getQtdSemestres() {
        return qtdSemestres;
    }

    public void setQtdSemestres(Integer qtdSemestres) {
        this.qtdSemestres = qtdSemestres;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    @XmlTransient
    public Collection<SgObra> getSgObraCollection() {
        return sgObraCollection;
    }

    public void setSgObraCollection(Collection<SgObra> sgObraCollection) {
        this.sgObraCollection = sgObraCollection;
    }

    @XmlTransient
    public Collection<BvArtigo> getBvArtigoCollection() {
        return bvArtigoCollection;
    }

    public void setBvArtigoCollection(Collection<BvArtigo> bvArtigoCollection) {
        this.bvArtigoCollection = bvArtigoCollection;
    }

    @XmlTransient
    public Collection<Estudante> getEstudanteCollection() {
        return estudanteCollection;
    }

    public void setEstudanteCollection(Collection<Estudante> estudanteCollection) {
        this.estudanteCollection = estudanteCollection;
    }

    @XmlTransient
    public Collection<Estudante> getEstudanteCollection1() {
        return estudanteCollection1;
    }

    public void setEstudanteCollection1(Collection<Estudante> estudanteCollection1) {
        this.estudanteCollection1 = estudanteCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCurso != null ? idCurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.idCurso == null && other.idCurso != null) || (this.idCurso != null && !this.idCurso.equals(other.idCurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Curso[ idCurso=" + idCurso + " ]";
    }
    
}
