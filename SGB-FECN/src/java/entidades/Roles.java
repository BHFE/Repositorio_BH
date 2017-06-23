/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nobrega
 */
@Entity
@Table(catalog = "sgb", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r"),
    @NamedQuery(name = "Roles.findByIdGrupo", query = "SELECT r FROM Roles r WHERE r.rolesPK.idGrupo = :idGrupo"),
    @NamedQuery(name = "Roles.findByIdCategoria", query = "SELECT r FROM Roles r WHERE r.rolesPK.idCategoria = :idCategoria"),
    @NamedQuery(name = "Roles.findByIdItem", query = "SELECT r FROM Roles r WHERE r.rolesPK.idItem = :idItem"),
    @NamedQuery(name = "Roles.findByDescricao", query = "SELECT r FROM Roles r WHERE r.descricao = :descricao")})
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RolesPK rolesPK;
    @Column(length = 255)
    private String descricao;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Categoria categoria;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Grupo grupo;
    @JoinColumn(name = "id_item", referencedColumnName = "item", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Item item;

    public Roles() {
    }

    public Roles(RolesPK rolesPK) {
        this.rolesPK = rolesPK;
    }

    public Roles(String idGrupo, String idCategoria, String idItem) {
        this.rolesPK = new RolesPK(idGrupo, idCategoria, idItem);
    }

    public RolesPK getRolesPK() {
        return rolesPK;
    }

    public void setRolesPK(RolesPK rolesPK) {
        this.rolesPK = rolesPK;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolesPK != null ? rolesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roles)) {
            return false;
        }
        Roles other = (Roles) object;
        if ((this.rolesPK == null && other.rolesPK != null) || (this.rolesPK != null && !this.rolesPK.equals(other.rolesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Roles[ rolesPK=" + rolesPK + " ]";
    }
    
}
