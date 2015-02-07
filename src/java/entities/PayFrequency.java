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
@Table(name = "pay_frequency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PayFrequency.findAll", query = "SELECT p FROM PayFrequency p"),
    @NamedQuery(name = "PayFrequency.findByPayFrequencyCode", query = "SELECT p FROM PayFrequency p WHERE p.payFrequencyCode = :payFrequencyCode"),
    @NamedQuery(name = "PayFrequency.findByPayFrequencyDescription", query = "SELECT p FROM PayFrequency p WHERE p.payFrequencyDescription = :payFrequencyDescription")})
public class PayFrequency implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pay_frequency_code")
    private String payFrequencyCode;
    @Size(max = 255)
    @Column(name = "pay_frequency_description")
    private String payFrequencyDescription;
    @OneToMany(mappedBy = "payFrequencyCode")
    private Collection<Payroll> payrollCollection;

    public PayFrequency() {
    }

    public PayFrequency(String payFrequencyCode) {
        this.payFrequencyCode = payFrequencyCode;
    }

    public String getPayFrequencyCode() {
        return payFrequencyCode;
    }

    public void setPayFrequencyCode(String payFrequencyCode) {
        this.payFrequencyCode = payFrequencyCode;
    }

    public String getPayFrequencyDescription() {
        return payFrequencyDescription;
    }

    public void setPayFrequencyDescription(String payFrequencyDescription) {
        this.payFrequencyDescription = payFrequencyDescription;
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
        hash += (payFrequencyCode != null ? payFrequencyCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PayFrequency)) {
            return false;
        }
        PayFrequency other = (PayFrequency) object;
        if ((this.payFrequencyCode == null && other.payFrequencyCode != null) || (this.payFrequencyCode != null && !this.payFrequencyCode.equals(other.payFrequencyCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PayFrequency[ payFrequencyCode=" + payFrequencyCode + " ]";
    }
    
}
