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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "sg_obra_autor", catalog = "bh", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgObraAutor.findAll", query = "SELECT s FROM SgObraAutor s")
    , @NamedQuery(name = "SgObraAutor.findByIdautor", query = "SELECT s FROM SgObraAutor s WHERE s.idautor = :idautor")
    , @NamedQuery(name = "SgObraAutor.findByDataAlocacao", query = "SELECT s FROM SgObraAutor s WHERE s.dataAlocacao = :dataAlocacao")})
public class SgObraAutor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idautor", nullable = false)
    private Long idautor;
    @Basic(optional = false)
    @Column(name = "data_alocacao", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataAlocacao;
    @JoinColumn(name = "idautor", referencedColumnName = "idautor", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private SgAutor sgAutor;
    @JoinColumn(name = "idlivro", referencedColumnName = "idlivro", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SgObra idlivro;

    public SgObraAutor() {
    }

    public SgObraAutor(Long idautor) {
        this.idautor = idautor;
    }

    public SgObraAutor(Long idautor, Date dataAlocacao) {
        this.idautor = idautor;
        this.dataAlocacao = dataAlocacao;
    }

    public Long getIdautor() {
        return idautor;
    }

    public void setIdautor(Long idautor) {
        this.idautor = idautor;
    }

    public Date getDataAlocacao() {
        return dataAlocacao;
    }

    public void setDataAlocacao(Date dataAlocacao) {
        this.dataAlocacao = dataAlocacao;
    }

    public SgAutor getSgAutor() {
        return sgAutor;
    }

    public void setSgAutor(SgAutor sgAutor) {
        this.sgAutor = sgAutor;
    }

    public SgObra getIdlivro() {
        return idlivro;
    }

    public void setIdlivro(SgObra idlivro) {
        this.idlivro = idlivro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idautor != null ? idautor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgObraAutor)) {
            return false;
        }
        SgObraAutor other = (SgObraAutor) object;
        if ((this.idautor == null && other.idautor != null) || (this.idautor != null && !this.idautor.equals(other.idautor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgObraAutor[ idautor=" + idautor + " ]";
    }
    
}
