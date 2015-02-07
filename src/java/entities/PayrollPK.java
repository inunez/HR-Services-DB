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
public class PayrollPK implements Serializable {
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
    @Size(min = 1, max = 15)
    @Column(name = "level1_code")
    private String level1Code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "effective_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;

    public PayrollPK() {
    }

    public PayrollPK(String idNumber, String status, String level1Code, Date effectiveDate) {
        this.idNumber = idNumber;
        this.status = status;
        this.level1Code = level1Code;
        this.effectiveDate = effectiveDate;
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
    
    public String getLevel1Code() {
        return level1Code;
    }

    public void setLevel1Code(String level1Code) {
        this.level1Code = level1Code;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNumber != null ? idNumber.hashCode() : 0);
        hash += (status != null ? status.hashCode() : 0);
        hash += (level1Code != null ? level1Code.hashCode() : 0);
        hash += (effectiveDate != null ? effectiveDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PayrollPK)) {
            return false;
        }
        PayrollPK other = (PayrollPK) object;
        if ((this.idNumber == null && other.idNumber != null) || (this.idNumber != null && !this.idNumber.equals(other.idNumber))) {
            return false;
        }
        if ((this.status == null && other.status != null) || (this.status != null && !this.status.equals(other.status))) {
            return false;
        }
        if ((this.level1Code == null && other.level1Code != null) || (this.level1Code != null && !this.level1Code.equals(other.level1Code))) {
            return false;
        }
        if ((this.effectiveDate == null && other.effectiveDate != null) || (this.effectiveDate != null && !this.effectiveDate.equals(other.effectiveDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PayrollPK[ idNumber=" + idNumber + ", status=" + status + ", level1Code=" + level1Code + ", effectiveDate=" + effectiveDate + " ]";
    }
    
}
