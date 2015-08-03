/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael Nunez
 */
@Entity
@Table(name = "manager_position")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ManagerPosition.findAll", query = "SELECT m FROM ManagerPosition m"),
    @NamedQuery(name = "ManagerPosition.findByLocationCode", query = "SELECT m FROM ManagerPosition m WHERE m.managerPositionPK.locationCode = :locationCode"),
    @NamedQuery(name = "ManagerPosition.findByAccountNumber", query = "SELECT m FROM ManagerPosition m WHERE m.managerPositionPK.accountNumber = :accountNumber"),
    @NamedQuery(name = "ManagerPosition.findByPositionId", query = "SELECT m FROM ManagerPosition m WHERE m.managerPositionPK.positionId = :positionId")})
public class ManagerPosition implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ManagerPositionPK managerPositionPK;
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account account;
    @JoinColumn(name = "location_code", referencedColumnName = "location_code", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Location location;
    @JoinColumn(name = "position_id", referencedColumnName = "position_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Position position;

    public ManagerPosition() {
    }

    public ManagerPosition(ManagerPositionPK managerPositionPK) {
        this.managerPositionPK = managerPositionPK;
    }

    public ManagerPosition(String locationCode, String accountNumber, String positionId) {
        this.managerPositionPK = new ManagerPositionPK(locationCode, accountNumber, positionId);
    }

    public ManagerPositionPK getManagerPositionPK() {
        return managerPositionPK;
    }

    public void setManagerPositionPK(ManagerPositionPK managerPositionPK) {
        this.managerPositionPK = managerPositionPK;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (managerPositionPK != null ? managerPositionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ManagerPosition)) {
            return false;
        }
        ManagerPosition other = (ManagerPosition) object;
        if ((this.managerPositionPK == null && other.managerPositionPK != null) || (this.managerPositionPK != null && !this.managerPositionPK.equals(other.managerPositionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ManagerPosition[ managerPositionPK=" + managerPositionPK + " ]";
    }
    
}
