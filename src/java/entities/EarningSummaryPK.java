/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ismael
 */
@Embeddable
public class EarningSummaryPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "id_number")
    private String idNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private short version;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paid_up_to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidUpToDate;

    public EarningSummaryPK() {
    }

    public EarningSummaryPK(String idNumber, String status, short version, Date paidUpToDate) {
        this.idNumber = idNumber;
        this.status = status;
        this.version = version;
        this.paidUpToDate = paidUpToDate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public Date getPaidUpToDate() {
        return paidUpToDate;
    }

    public void setPaidUpToDate(Date paidUpToDate) {
        this.paidUpToDate = paidUpToDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNumber != null ? idNumber.hashCode() : 0);
        hash += (status != null ? status.hashCode() : 0);
        hash += (int) version;
        hash += (paidUpToDate != null ? paidUpToDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EarningSummaryPK)) {
            return false;
        }
        EarningSummaryPK other = (EarningSummaryPK) object;
        if ((this.idNumber == null && other.idNumber != null) || (this.idNumber != null && !this.idNumber.equals(other.idNumber))) {
            return false;
        }
        if ((this.status == null && other.status != null) || (this.status != null && !this.status.equals(other.status))) {
            return false;
        }
        if (this.version != other.version) {
            return false;
        }
        if ((this.paidUpToDate == null && other.paidUpToDate != null) || (this.paidUpToDate != null && !this.paidUpToDate.equals(other.paidUpToDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EarningSummaryPK[ idNumber=" + idNumber + ", status=" + status + ", version=" + version + ", paidUpToDate=" + paidUpToDate + " ]";
    }
    
}
