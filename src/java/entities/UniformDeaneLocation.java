/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "uniform_deane_location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UniformDeaneLocation.findAll", query = "SELECT u FROM UniformDeaneLocation u"),
    @NamedQuery(name = "UniformDeaneLocation.findByRegion", query = "SELECT u FROM UniformDeaneLocation u WHERE u.uniformDeaneLocationPK.region = :region"),
    @NamedQuery(name = "UniformDeaneLocation.findByShipTo", query = "SELECT u FROM UniformDeaneLocation u WHERE u.uniformDeaneLocationPK.shipTo = :shipTo"),
    @NamedQuery(name = "UniformDeaneLocation.findByAddress", query = "SELECT u FROM UniformDeaneLocation u WHERE u.address = :address")})
public class UniformDeaneLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UniformDeaneLocationPK uniformDeaneLocationPK;
    @Size(max = 45)
    @Column(name = "address")
    private String address;
    @Size(max = 60)
    @Column(name = "facility")
    private String facility;
    @OneToMany(mappedBy = "uniformDeaneLocation")
    private Collection<UniformAccount> uniformAccountCollection;

    public UniformDeaneLocation() {
    }

    public UniformDeaneLocation(UniformDeaneLocationPK uniformDeaneLocationPK) {
        this.uniformDeaneLocationPK = uniformDeaneLocationPK;
    }

    public UniformDeaneLocation(int region, short shipTo) {
        this.uniformDeaneLocationPK = new UniformDeaneLocationPK(region, shipTo);
    }

    public UniformDeaneLocationPK getUniformDeaneLocationPK() {
        return uniformDeaneLocationPK;
    }

    public void setUniformDeaneLocationPK(UniformDeaneLocationPK uniformDeaneLocationPK) {
        this.uniformDeaneLocationPK = uniformDeaneLocationPK;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlTransient
    public Collection<UniformAccount> getUniformAccountCollection() {
        return uniformAccountCollection;
    }

    public void setUniformAccountCollection(Collection<UniformAccount> uniformAccountCollection) {
        this.uniformAccountCollection = uniformAccountCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uniformDeaneLocationPK != null ? uniformDeaneLocationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniformDeaneLocation)) {
            return false;
        }
        UniformDeaneLocation other = (UniformDeaneLocation) object;
        if ((this.uniformDeaneLocationPK == null && other.uniformDeaneLocationPK != null) || (this.uniformDeaneLocationPK != null && !this.uniformDeaneLocationPK.equals(other.uniformDeaneLocationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UniformDeaneLocation[ uniformDeaneLocationPK=" + uniformDeaneLocationPK + " ]";
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }
    
}
