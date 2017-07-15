/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "b_reserva", catalog = "bh", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BReserva.findAll", query = "SELECT b FROM BReserva b")
    , @NamedQuery(name = "BReserva.findByIdagenda", query = "SELECT b FROM BReserva b WHERE b.idagenda = :idagenda")
    , @NamedQuery(name = "BReserva.findByDataDevolucao", query = "SELECT b FROM BReserva b WHERE b.dataDevolucao = :dataDevolucao")
    , @NamedQuery(name = "BReserva.findByDataReserva", query = "SELECT b FROM BReserva b WHERE b.dataReserva = :dataReserva")
    , @NamedQuery(name = "BReserva.findByEstado", query = "SELECT b FROM BReserva b WHERE b.estado = :estado")
    , @NamedQuery(name = "BReserva.findByVia", query = "SELECT b FROM BReserva b WHERE b.via = :via")
    , @NamedQuery(name = "BReserva.findByBibliotecario", query = "SELECT b FROM BReserva b WHERE b.bibliotecario = :bibliotecario")
    , @NamedQuery(name = "BReserva.findByDataEmprestimo", query = "SELECT b FROM BReserva b WHERE b.dataEmprestimo = :dataEmprestimo")})
public class BReserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idagenda", nullable = false)
    private Integer idagenda;
    @Column(name = "data_devolucao")
    @Temporal(TemporalType.DATE)
    private Date dataDevolucao;
    @Column(name = "data_reserva")
    @Temporal(TemporalType.DATE)
    private Date dataReserva;
    @Column(name = "estado", length = 255)
    private String estado;
    @Column(name = "via", length = 255)
    private String via;
    @Column(name = "bibliotecario")
    private BigInteger bibliotecario;
    @Column(name = "data_emprestimo")
    @Temporal(TemporalType.DATE)
    private Date dataEmprestimo;
    @OneToMany(mappedBy = "reservaRef", fetch = FetchType.LAZY)
    private List<SgEmprestimo> sgEmprestimoList;
    @JoinColumn(name = "leitor", referencedColumnName = "nr_cartao")
    @ManyToOne(fetch = FetchType.LAZY)
    private BLeitor leitor;
    @JoinColumn(name = "livro", referencedColumnName = "nr_registo")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgExemplar livro;

    public BReserva() {
    }

    public BReserva(Integer idagenda) {
        this.idagenda = idagenda;
    }

    public Integer getIdagenda() {
        return idagenda;
    }

    public void setIdagenda(Integer idagenda) {
        this.idagenda = idagenda;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Date getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public BigInteger getBibliotecario() {
        return bibliotecario;
    }

    public void setBibliotecario(BigInteger bibliotecario) {
        this.bibliotecario = bibliotecario;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    @XmlTransient
    public List<SgEmprestimo> getSgEmprestimoList() {
        return sgEmprestimoList;
    }

    public void setSgEmprestimoList(List<SgEmprestimo> sgEmprestimoList) {
        this.sgEmprestimoList = sgEmprestimoList;
    }

    public BLeitor getLeitor() {
        return leitor;
    }

    public void setLeitor(BLeitor leitor) {
        this.leitor = leitor;
    }

    public SgExemplar getLivro() {
        return livro;
    }

    public void setLivro(SgExemplar livro) {
        this.livro = livro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idagenda != null ? idagenda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BReserva)) {
            return false;
        }
        BReserva other = (BReserva) object;
        if ((this.idagenda == null && other.idagenda != null) || (this.idagenda != null && !this.idagenda.equals(other.idagenda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.BReserva[ idagenda=" + idagenda + " ]";
    }
    
}
