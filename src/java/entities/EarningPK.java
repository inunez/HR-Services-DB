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
public class EarningPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "id_number")
    private String idNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payslip_number")
    private double payslipNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "print_sequence")
    private short printSequence;

    public EarningPK() {
    }

    public EarningPK(String idNumber, double payslipNumber, short printSequence) {
        this.idNumber = idNumber;
        this.payslipNumber = payslipNumber;
        this.printSequence = printSequence;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public double getPayslipNumber() {
        return payslipNumber;
    }

    public void setPayslipNumber(double payslipNumber) {
        this.payslipNumber = payslipNumber;
    }

    public short getPrintSequence() {
        return printSequence;
    }

    public void setPrintSequence(short printSequence) {
        this.printSequence = printSequence;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNumber != null ? idNumber.hashCode() : 0);
        hash += (int) payslipNumber;
        hash += (int) printSequence;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EarningPK)) {
            return false;
        }
        EarningPK other = (EarningPK) object;
        if ((this.idNumber == null && other.idNumber != null) || (this.idNumber != null && !this.idNumber.equals(other.idNumber))) {
            return false;
        }
        if (this.payslipNumber != other.payslipNumber) {
            return false;
        }
        if (this.printSequence != other.printSequence) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EarningPK[ idNumber=" + idNumber + ", payslipNumber=" + payslipNumber + ", printSequence=" + printSequence + " ]";
    }
    
}
