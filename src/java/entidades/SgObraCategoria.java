/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "sg_obra_categoria", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SgObraCategoria.findAll", query = "SELECT s FROM SgObraCategoria s")
    , @NamedQuery(name = "SgObraCategoria.findByCategoria", query = "SELECT s FROM SgObraCategoria s WHERE s.categoria = :categoria")
    , @NamedQuery(name = "SgObraCategoria.findByDescricao", query = "SELECT s FROM SgObraCategoria s WHERE s.descricao = :descricao")})
public class SgObraCategoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(mappedBy = "dominio", fetch = FetchType.LAZY)
    private Collection<SgObra> sgObraCollection;

    public SgObraCategoria() {
    }

    public SgObraCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<SgObra> getSgObraCollection() {
        return sgObraCollection;
    }

    public void setSgObraCollection(Collection<SgObra> sgObraCollection) {
        this.sgObraCollection = sgObraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoria != null ? categoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgObraCategoria)) {
            return false;
        }
        SgObraCategoria other = (SgObraCategoria) object;
        if ((this.categoria == null && other.categoria != null) || (this.categoria != null && !this.categoria.equals(other.categoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SgObraCategoria[ categoria=" + categoria + " ]";
    }
    
}
