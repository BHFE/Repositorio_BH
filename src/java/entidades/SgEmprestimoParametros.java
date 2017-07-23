/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "sg_emprestimo_parametros", catalog = "bh", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgEmprestimoParametros.findAll", query = "SELECT s FROM SgEmprestimoParametros s")
    , @NamedQuery(name = "SgEmprestimoParametros.findByIdparametro", query = "SELECT s FROM SgEmprestimoParametros s WHERE s.idparametro = :idparametro")
    , @NamedQuery(name = "SgEmprestimoParametros.findByDataDefinicao", query = "SELECT s FROM SgEmprestimoParametros s WHERE s.dataDefinicao = :dataDefinicao")
    , @NamedQuery(name = "SgEmprestimoParametros.findByTaxa", query = "SELECT s FROM SgEmprestimoParametros s WHERE s.taxa = :taxa")
    , @NamedQuery(name = "SgEmprestimoParametros.findByDescricao", query = "SELECT s FROM SgEmprestimoParametros s WHERE s.descricao = :descricao")})
public class SgEmprestimoParametros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long idparametro;
    @Column(name = "data_definicao", length = 255)
    private String dataDefinicao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 8, scale = 8)
    private Float taxa;
    @Column(length = 2147483647)
    private String descricao;
    @OneToMany(mappedBy = "parametrosRef", fetch = FetchType.LAZY)
    private List<SgEmprestimo> sgEmprestimoList;
    @OneToMany(mappedBy = "idParametroActualizacao", fetch = FetchType.LAZY)
    private List<BLeitor> bLeitorList;
    @OneToMany(mappedBy = "idParametroRegisto", fetch = FetchType.LAZY)
    private List<BLeitor> bLeitorList1;
    @JoinColumn(name = "agente_bibliotecario", referencedColumnName = "utilizador")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users agenteBibliotecario;

    public SgEmprestimoParametros() {
    }

    public SgEmprestimoParametros(Long idparametro) {
        this.idparametro = idparametro;
    }

    public Long getIdparametro() {
        return idparametro;
    }

    public void setIdparametro(Long idparametro) {
        this.idparametro = idparametro;
    }

    public String getDataDefinicao() {
        return dataDefinicao;
    }

    public void setDataDefinicao(String dataDefinicao) {
        this.dataDefinicao = dataDefinicao;
    }

    public Float getTaxa() {
        return taxa;
    }

    public void setTaxa(Float taxa) {
        this.taxa = taxa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public List<SgEmprestimo> getSgEmprestimoList() {
        return sgEmprestimoList;
    }

    public void setSgEmprestimoList(List<SgEmprestimo> sgEmprestimoList) {
        this.sgEmprestimoList = sgEmprestimoList;
    }

    @XmlTransient
    public List<BLeitor> getBLeitorList() {
        return bLeitorList;
    }

    public void setBLeitorList(List<BLeitor> bLeitorList) {
        this.bLeitorList = bLeitorList;
    }

    @XmlTransient
    public List<BLeitor> getBLeitorList1() {
        return bLeitorList1;
    }

    public void setBLeitorList1(List<BLeitor> bLeitorList1) {
        this.bLeitorList1 = bLeitorList1;
    }

    public Users getAgenteBibliotecario() {
        return agenteBibliotecario;
    }

    public void setAgenteBibliotecario(Users agenteBibliotecario) {
        this.agenteBibliotecario = agenteBibliotecario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idparametro != null ? idparametro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEmprestimoParametros)) {
            return false;
        }
        SgEmprestimoParametros other = (SgEmprestimoParametros) object;
        if ((this.idparametro == null && other.idparametro != null) || (this.idparametro != null && !this.idparametro.equals(other.idparametro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgEmprestimoParametros[ idparametro=" + idparametro + " ]";
    }
    
}
