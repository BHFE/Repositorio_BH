/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
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
 * @author Nobrega
 */
@Entity
@Table(catalog = "sgb", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c"),
    @NamedQuery(name = "Curso.findByIdCurso", query = "SELECT c FROM Curso c WHERE c.idCurso = :idCurso"),
    @NamedQuery(name = "Curso.findByCodigoCurso", query = "SELECT c FROM Curso c WHERE c.codigoCurso = :codigoCurso"),
    @NamedQuery(name = "Curso.findByDescricao", query = "SELECT c FROM Curso c WHERE c.descricao = :descricao"),
    @NamedQuery(name = "Curso.findByFaculdade", query = "SELECT c FROM Curso c WHERE c.faculdade = :faculdade"),
    @NamedQuery(name = "Curso.findByQtdSemestres", query = "SELECT c FROM Curso c WHERE c.qtdSemestres = :qtdSemestres"),
    @NamedQuery(name = "Curso.findByAbreviatura", query = "SELECT c FROM Curso c WHERE c.abreviatura = :abreviatura")})
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_curso", nullable = false)
    private Long idCurso;
    @Column(name = "codigo_curso", length = 45)
    private String codigoCurso;
    @Column(length = 45)
    private String descricao;
    private Integer faculdade;
    @Column(name = "qtd_semestres")
    private Integer qtdSemestres;
    @Column(length = 11)
    private String abreviatura;
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<SgObra> sgObraList;
    @OneToMany(mappedBy = "cursoAlvo", fetch = FetchType.LAZY)
    private List<BvArtigo> bvArtigoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoingresso", fetch = FetchType.LAZY)
    private List<Estudante> estudanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursocurrente", fetch = FetchType.LAZY)
    private List<Estudante> estudanteList1;

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
    public List<SgObra> getSgObraList() {
        return sgObraList;
    }

    public void setSgObraList(List<SgObra> sgObraList) {
        this.sgObraList = sgObraList;
    }

    @XmlTransient
    public List<BvArtigo> getBvArtigoList() {
        return bvArtigoList;
    }

    public void setBvArtigoList(List<BvArtigo> bvArtigoList) {
        this.bvArtigoList = bvArtigoList;
    }

    @XmlTransient
    public List<Estudante> getEstudanteList() {
        return estudanteList;
    }

    public void setEstudanteList(List<Estudante> estudanteList) {
        this.estudanteList = estudanteList;
    }

    @XmlTransient
    public List<Estudante> getEstudanteList1() {
        return estudanteList1;
    }

    public void setEstudanteList1(List<Estudante> estudanteList1) {
        this.estudanteList1 = estudanteList1;
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
