/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "plaxa_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaxaRole.findAll", query = "SELECT p FROM PlaxaRole p"),
    @NamedQuery(name = "PlaxaRole.findByRoleId", query = "SELECT p FROM PlaxaRole p WHERE p.roleId = :roleId"),
    @NamedQuery(name = "PlaxaRole.findByRoleDescription", query = "SELECT p FROM PlaxaRole p WHERE p.roleDescription = :roleDescription")})
public class PlaxaRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "role_id")
    private Short roleId;
    @Size(max = 45)
    @Column(name = "role_description")
    private String roleDescription;

    public PlaxaRole() {
    }

    public PlaxaRole(Short roleId) {
        this.roleId = roleId;
    }

    public Short getRoleId() {
        return roleId;
    }

    public void setRoleId(Short roleId) {
        this.roleId = roleId;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaxaRole)) {
            return false;
        }
        PlaxaRole other = (PlaxaRole) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PlaxaRole[ roleId=" + roleId + " ]";
    }
    
}
