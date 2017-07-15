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
@Table(name = "usergrupo", catalog = "bh", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usergrupo.findAll", query = "SELECT u FROM Usergrupo u")
    , @NamedQuery(name = "Usergrupo.findByIdGrupo", query = "SELECT u FROM Usergrupo u WHERE u.idGrupo = :idGrupo")
    , @NamedQuery(name = "Usergrupo.findByDataAlocacao", query = "SELECT u FROM Usergrupo u WHERE u.dataAlocacao = :dataAlocacao")})
public class Usergrupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_grupo", nullable = false, length = 2147483647)
    private String idGrupo;
    @Basic(optional = false)
    @Column(name = "data_alocacao", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataAlocacao;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Grupo grupo;
    @JoinColumn(name = "utilizador", referencedColumnName = "utilizador", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users utilizador;

    public Usergrupo() {
    }

    public Usergrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Usergrupo(String idGrupo, Date dataAlocacao) {
        this.idGrupo = idGrupo;
        this.dataAlocacao = dataAlocacao;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Date getDataAlocacao() {
        return dataAlocacao;
    }

    public void setDataAlocacao(Date dataAlocacao) {
        this.dataAlocacao = dataAlocacao;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Users getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Users utilizador) {
        this.utilizador = utilizador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrupo != null ? idGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usergrupo)) {
            return false;
        }
        Usergrupo other = (Usergrupo) object;
        if ((this.idGrupo == null && other.idGrupo != null) || (this.idGrupo != null && !this.idGrupo.equals(other.idGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Usergrupo[ idGrupo=" + idGrupo + " ]";
    }
    
}
