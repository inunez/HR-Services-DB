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
public class EarningLeavePK implements Serializable {
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
    @Column(name = "date_accrued_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAccruedTo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "classification")
    private String classification;

    public EarningLeavePK() {
    }

    public EarningLeavePK(String idNumber, String status, short version, Date dateAccruedTo, String classification) {
        this.idNumber = idNumber;
        this.status = status;
        this.version = version;
        this.dateAccruedTo = dateAccruedTo;
        this.classification = classification;
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

    public Date getDateAccruedTo() {
        return dateAccruedTo;
    }

    public void setDateAccruedTo(Date dateAccruedTo) {
        this.dateAccruedTo = dateAccruedTo;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNumber != null ? idNumber.hashCode() : 0);
        hash += (status != null ? status.hashCode() : 0);
        hash += (int) version;
        hash += (dateAccruedTo != null ? dateAccruedTo.hashCode() : 0);
        hash += (classification != null ? classification.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EarningLeavePK)) {
            return false;
        }
        EarningLeavePK other = (EarningLeavePK) object;
        if ((this.idNumber == null && other.idNumber != null) || (this.idNumber != null && !this.idNumber.equals(other.idNumber))) {
            return false;
        }
        if ((this.status == null && other.status != null) || (this.status != null && !this.status.equals(other.status))) {
            return false;
        }
        if (this.version != other.version) {
            return false;
        }
        if ((this.dateAccruedTo == null && other.dateAccruedTo != null) || (this.dateAccruedTo != null && !this.dateAccruedTo.equals(other.dateAccruedTo))) {
            return false;
        }
        if ((this.classification == null && other.classification != null) || (this.classification != null && !this.classification.equals(other.classification))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EarningLeavePK[ idNumber=" + idNumber + ", status=" + status + ", version=" + version + ", dateAccruedTo=" + dateAccruedTo + ", classification=" + classification + " ]";
    }
    
}
