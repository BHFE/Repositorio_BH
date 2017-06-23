/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Migueljr
 */
@Entity
@Table(name = "users_usergrupo", catalog = "fecn2", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersUsergrupo.findAll", query = "SELECT u FROM UsersUsergrupo u")
    , @NamedQuery(name = "UsersUsergrupo.findByUsersUtilizador", query = "SELECT u FROM UsersUsergrupo u WHERE u.usersUsergrupoPK.usersUtilizador = :usersUtilizador")
    , @NamedQuery(name = "UsersUsergrupo.findByDataAlocacao", query = "SELECT u FROM UsersUsergrupo u WHERE u.usersUsergrupoPK.dataAlocacao = :dataAlocacao")
    , @NamedQuery(name = "UsersUsergrupo.findByUtilizador", query = "SELECT u FROM UsersUsergrupo u WHERE u.usersUsergrupoPK.utilizador = :utilizador")
    , @NamedQuery(name = "UsersUsergrupo.findByIdGrupo", query = "SELECT u FROM UsersUsergrupo u WHERE u.usersUsergrupoPK.idGrupo = :idGrupo")})
public class UsersUsergrupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsersUsergrupoPK usersUsergrupoPK;

    public UsersUsergrupo() {
    }

    public UsersUsergrupo(UsersUsergrupoPK usersUsergrupoPK) {
        this.usersUsergrupoPK = usersUsergrupoPK;
    }

    public UsersUsergrupo(String usersUtilizador, Date dataAlocacao, String utilizador, String idGrupo) {
        this.usersUsergrupoPK = new UsersUsergrupoPK(usersUtilizador, dataAlocacao, utilizador, idGrupo);
    }

    public UsersUsergrupoPK getUsersUsergrupoPK() {
        return usersUsergrupoPK;
    }

    public void setUsersUsergrupoPK(UsersUsergrupoPK usersUsergrupoPK) {
        this.usersUsergrupoPK = usersUsergrupoPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usersUsergrupoPK != null ? usersUsergrupoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersUsergrupo)) {
            return false;
        }
        UsersUsergrupo other = (UsersUsergrupo) object;
        if ((this.usersUsergrupoPK == null && other.usersUsergrupoPK != null) || (this.usersUsergrupoPK != null && !this.usersUsergrupoPK.equals(other.usersUsergrupoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.UsersUsergrupo[ usersUsergrupoPK=" + usersUsergrupoPK + " ]";
    }
    
}
