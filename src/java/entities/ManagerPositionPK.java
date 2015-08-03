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
import javax.validation.constraints.Size;

/**
 *
 * @author Ismael Nunez
 */
@Embeddable
public class ManagerPositionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "location_code")
    private String locationCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "account_number")
    private String accountNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "position_id")
    private String positionId;

    public ManagerPositionPK() {
    }

    public ManagerPositionPK(String locationCode, String accountNumber, String positionId) {
        this.locationCode = locationCode;
        this.accountNumber = accountNumber;
        this.positionId = positionId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (locationCode != null ? locationCode.hashCode() : 0);
        hash += (accountNumber != null ? accountNumber.hashCode() : 0);
        hash += (positionId != null ? positionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ManagerPositionPK)) {
            return false;
        }
        ManagerPositionPK other = (ManagerPositionPK) object;
        if ((this.locationCode == null && other.locationCode != null) || (this.locationCode != null && !this.locationCode.equals(other.locationCode))) {
            return false;
        }
        if ((this.accountNumber == null && other.accountNumber != null) || (this.accountNumber != null && !this.accountNumber.equals(other.accountNumber))) {
            return false;
        }
        if ((this.positionId == null && other.positionId != null) || (this.positionId != null && !this.positionId.equals(other.positionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ManagerPositionPK[ locationCode=" + locationCode + ", accountNumber=" + accountNumber + ", positionId=" + positionId + " ]";
    }
    
}
