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
@Table(name = "plaxa_location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaxaLocation.findAll", query = "SELECT p FROM PlaxaLocation p"),
    @NamedQuery(name = "PlaxaLocation.findByLocationId", query = "SELECT p FROM PlaxaLocation p WHERE p.locationId = :locationId"),
    @NamedQuery(name = "PlaxaLocation.findByLocationDescription", query = "SELECT p FROM PlaxaLocation p WHERE p.locationDescription = :locationDescription")})
public class PlaxaLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "location_id")
    private Short locationId;
    @Size(max = 100)
    @Column(name = "location_description")
    private String locationDescription;
    @OneToMany(mappedBy = "locationId")
    private Collection<PlaxaAccount> plaxaAccountCollection;

    public PlaxaLocation() {
    }

    public PlaxaLocation(Short locationId) {
        this.locationId = locationId;
    }

    public Short getLocationId() {
        return locationId;
    }

    public void setLocationId(Short locationId) {
        this.locationId = locationId;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
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
        hash += (locationId != null ? locationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaxaLocation)) {
            return false;
        }
        PlaxaLocation other = (PlaxaLocation) object;
        if ((this.locationId == null && other.locationId != null) || (this.locationId != null && !this.locationId.equals(other.locationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PlaxaLocation[ locationId=" + locationId + " ]";
    }
    
}
