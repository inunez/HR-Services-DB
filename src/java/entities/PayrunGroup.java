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
@Table(name = "payrun_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PayrunGroup.findAll", query = "SELECT p FROM PayrunGroup p"),
    @NamedQuery(name = "PayrunGroup.findByPayrunGroupCode", query = "SELECT p FROM PayrunGroup p WHERE p.payrunGroupCode = :payrunGroupCode"),
    @NamedQuery(name = "PayrunGroup.findByPayrunGroupDescription", query = "SELECT p FROM PayrunGroup p WHERE p.payrunGroupDescription = :payrunGroupDescription")})
public class PayrunGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "payrun_group_code")
    private String payrunGroupCode;
    @Size(max = 255)
    @Column(name = "payrun_group_description")
    private String payrunGroupDescription;
    @OneToMany(mappedBy = "payrunGroupCode")
    private Collection<Payroll> payrollCollection;

    public PayrunGroup() {
    }

    public PayrunGroup(String payrunGroupCode) {
        this.payrunGroupCode = payrunGroupCode;
    }

    public String getPayrunGroupCode() {
        return payrunGroupCode;
    }

    public void setPayrunGroupCode(String payrunGroupCode) {
        this.payrunGroupCode = payrunGroupCode;
    }

    public String getPayrunGroupDescription() {
        return payrunGroupDescription;
    }

    public void setPayrunGroupDescription(String payrunGroupDescription) {
        this.payrunGroupDescription = payrunGroupDescription;
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
        hash += (payrunGroupCode != null ? payrunGroupCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PayrunGroup)) {
            return false;
        }
        PayrunGroup other = (PayrunGroup) object;
        if ((this.payrunGroupCode == null && other.payrunGroupCode != null) || (this.payrunGroupCode != null && !this.payrunGroupCode.equals(other.payrunGroupCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PayrunGroup[ payrunGroupCode=" + payrunGroupCode + " ]";
    }
    
}
