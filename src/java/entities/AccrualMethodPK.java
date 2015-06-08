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
 * @author Ismael
 */
@Embeddable
public class AccrualMethodPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "classification_code")
    private String classificationCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "accrual_method_code")
    private String accrualMethodCode;

    public AccrualMethodPK() {
    }

    public AccrualMethodPK(String classificationCode, String accrualMethodCode) {
        this.classificationCode = classificationCode;
        this.accrualMethodCode = accrualMethodCode;
    }

    public String getClassificationCode() {
        return classificationCode;
    }

    public void setClassificationCode(String classificationCode) {
        this.classificationCode = classificationCode;
    }

    public String getAccrualMethodCode() {
        return accrualMethodCode;
    }

    public void setAccrualMethodCode(String accrualMethodCode) {
        this.accrualMethodCode = accrualMethodCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (classificationCode != null ? classificationCode.hashCode() : 0);
        hash += (accrualMethodCode != null ? accrualMethodCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccrualMethodPK)) {
            return false;
        }
        AccrualMethodPK other = (AccrualMethodPK) object;
        if ((this.classificationCode == null && other.classificationCode != null) || (this.classificationCode != null && !this.classificationCode.equals(other.classificationCode))) {
            return false;
        }
        if ((this.accrualMethodCode == null && other.accrualMethodCode != null) || (this.accrualMethodCode != null && !this.accrualMethodCode.equals(other.accrualMethodCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccrualMethodPK[ classificationCode=" + classificationCode + ", accrualMethodCode=" + accrualMethodCode + " ]";
    }
    
}
