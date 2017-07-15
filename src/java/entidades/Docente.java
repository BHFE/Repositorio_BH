/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "docente", catalog = "bh", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Docente.findAll", query = "SELECT d FROM Docente d")
    , @NamedQuery(name = "Docente.findByIddocente", query = "SELECT d FROM Docente d WHERE d.iddocente = :iddocente")
    , @NamedQuery(name = "Docente.findByArea", query = "SELECT d FROM Docente d WHERE d.area = :area")
    , @NamedQuery(name = "Docente.findByGrau", query = "SELECT d FROM Docente d WHERE d.grau = :grau")
    , @NamedQuery(name = "Docente.findByNomePai", query = "SELECT d FROM Docente d WHERE d.nomePai = :nomePai")})
public class Docente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "iddocente", nullable = false)
    private Long iddocente;
    @Column(name = "area", length = 45)
    private String area;
    @Column(name = "grau", length = 45)
    private String grau;
    @Column(name = "nome_pai", length = 45)
    private String nomePai;
    @JoinColumn(name = "iddocente", referencedColumnName = "id_funcionario", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;

    public Docente() {
    }

    public Docente(Long iddocente) {
        this.iddocente = iddocente;
    }

    public Long getIddocente() {
        return iddocente;
    }

    public void setIddocente(Long iddocente) {
        this.iddocente = iddocente;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGrau() {
        return grau;
    }

    public void setGrau(String grau) {
        this.grau = grau;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddocente != null ? iddocente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docente)) {
            return false;
        }
        Docente other = (Docente) object;
        if ((this.iddocente == null && other.iddocente != null) || (this.iddocente != null && !this.iddocente.equals(other.iddocente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Docente[ iddocente=" + iddocente + " ]";
    }
    
}
