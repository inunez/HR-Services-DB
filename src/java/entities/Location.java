/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
 * @author Ismael Nunez
 */
@Entity
@Table(name = "location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
    @NamedQuery(name = "Location.findByLocationCode", query = "SELECT l FROM Location l WHERE l.locationCode = :locationCode"),
    @NamedQuery(name = "Location.findByLocationDescription", query = "SELECT l FROM Location l WHERE l.locationDescription = :locationDescription")})
public class Location implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    private Collection<ManagerPosition> managerPositionCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "location_code")
    private String locationCode;
    @Size(max = 255)
    @Column(name = "location_description")
    private String locationDescription;
    
    public Location() {
    }

    public Location(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (locationCode != null ? locationCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.locationCode == null && other.locationCode != null) || (this.locationCode != null && !this.locationCode.equals(other.locationCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Location[ locationCode=" + locationCode + " ]";
    }

    @XmlTransient
    public Collection<ManagerPosition> getManagerPositionCollection() {
        return managerPositionCollection;
    }

    public void setManagerPositionCollection(Collection<ManagerPosition> managerPositionCollection) {
        this.managerPositionCollection = managerPositionCollection;
    }
    
}
