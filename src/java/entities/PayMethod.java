/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author Ismael
 */
@Entity
@Table(name = "pay_method")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PayMethod.findAll", query = "SELECT p FROM PayMethod p"),
    @NamedQuery(name = "PayMethod.findByPayMethodCode", query = "SELECT p FROM PayMethod p WHERE p.payMethodCode = :payMethodCode"),
    @NamedQuery(name = "PayMethod.findByPayMethodDescription", query = "SELECT p FROM PayMethod p WHERE p.payMethodDescription = :payMethodDescription")})
public class PayMethod implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pay_method_code")
    private String payMethodCode;
    @Size(max = 255)
    @Column(name = "pay_method_description")
    private String payMethodDescription;
    @OneToMany(mappedBy = "payMethodCode")
    private Collection<Payroll> payrollCollection;

    public PayMethod() {
    }

    public PayMethod(String payMethodCode) {
        this.payMethodCode = payMethodCode;
    }

    public String getPayMethodCode() {
        return payMethodCode;
    }

    public void setPayMethodCode(String payMethodCode) {
        this.payMethodCode = payMethodCode;
    }

    public String getPayMethodDescription() {
        return payMethodDescription;
    }

    public void setPayMethodDescription(String payMethodDescription) {
        this.payMethodDescription = payMethodDescription;
    }

    @XmlTransient
    public Collection<Payroll> getPayrollCollection() {
        return payrollCollection;
    }

    public void setPayrollCollection(Collection<Payroll> payrollCollection) {
        this.payrollCollection = payrollCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (payMethodCode != null ? payMethodCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PayMethod)) {
            return false;
        }
        PayMethod other = (PayMethod) object;
        if ((this.payMethodCode == null && other.payMethodCode != null) || (this.payMethodCode != null && !this.payMethodCode.equals(other.payMethodCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PayMethod[ payMethodCode=" + payMethodCode + " ]";
    }
    
}
