/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ismael
 */
@Embeddable
public class UniformDeaneLocationPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "region")
    private int region;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ship_to")
    private short shipTo;

    public UniformDeaneLocationPK() {
    }

    public UniformDeaneLocationPK(int region, short shipTo) {
        this.region = region;
        this.shipTo = shipTo;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public short getShipTo() {
        return shipTo;
    }

    public void setShipTo(short shipTo) {
        this.shipTo = shipTo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) region;
        hash += (int) shipTo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniformDeaneLocationPK)) {
            return false;
        }
        UniformDeaneLocationPK other = (UniformDeaneLocationPK) object;
        if (this.region != other.region) {
            return false;
        }
        if (this.shipTo != other.shipTo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UniformDeaneLocationPK[ region=" + region + ", shipTo=" + shipTo + " ]";
    }
    
}
