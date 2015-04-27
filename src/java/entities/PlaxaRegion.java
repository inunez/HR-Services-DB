/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "plaxa_region")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaxaRegion.findAll", query = "SELECT p FROM PlaxaRegion p"),
    @NamedQuery(name = "PlaxaRegion.findByRegionId", query = "SELECT p FROM PlaxaRegion p WHERE p.regionId = :regionId"),
    @NamedQuery(name = "PlaxaRegion.findByRegionDescription", query = "SELECT p FROM PlaxaRegion p WHERE p.regionDescription = :regionDescription")})
public class PlaxaRegion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "region_id")
    private Short regionId;
    @Size(max = 45)
    @Column(name = "region_description")
    private String regionDescription;
    @OneToMany(mappedBy = "regionId")
    private Collection<PlaxaAccount> plaxaAccountCollection;

    public PlaxaRegion() {
    }

    public PlaxaRegion(Short regionId) {
        this.regionId = regionId;
    }

    public Short getRegionId() {
        return regionId;
    }

    public void setRegionId(Short regionId) {
        this.regionId = regionId;
    }

    public String getRegionDescription() {
        return regionDescription;
    }

    public void setRegionDescription(String regionDescription) {
        this.regionDescription = regionDescription;
    }

    @XmlTransient
    public Collection<PlaxaAccount> getPlaxaAccountCollection() {
        return plaxaAccountCollection;
    }

    public void setPlaxaAccountCollection(Collection<PlaxaAccount> plaxaAccountCollection) {
        this.plaxaAccountCollection = plaxaAccountCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (regionId != null ? regionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaxaRegion)) {
            return false;
        }
        PlaxaRegion other = (PlaxaRegion) object;
        if ((this.regionId == null && other.regionId != null) || (this.regionId != null && !this.regionId.equals(other.regionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PlaxaRegion[ regionId=" + regionId + " ]";
    }
    
}
