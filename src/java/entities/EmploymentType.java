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
@Table(name = "employment_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmploymentType.findAll", query = "SELECT e FROM EmploymentType e"),
    @NamedQuery(name = "EmploymentType.findByEmploymentType", query = "SELECT e FROM EmploymentType e WHERE e.employmentType = :employmentType"),
    @NamedQuery(name = "EmploymentType.findByemploymentTypeDescription", query = "SELECT e FROM EmploymentType e WHERE e.employmentTypeDescription = :employmentTypeDescription")})
public class EmploymentType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "employment_type")
    private String employmentType;
    @Size(max = 255)
    @Column(name = "employment_type_description")
    private String employmentTypeDescription;
    @OneToMany(mappedBy = "employmentType")
    private Collection<Payroll> payrollCollection;

    public EmploymentType() {
    }

    public EmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getemploymentTypeDescription() {
        return employmentTypeDescription;
    }

    public void setemploymentTypeDescription(String employmentTypeDescription) {
        this.employmentTypeDescription = employmentTypeDescription;
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
        hash += (employmentType != null ? employmentType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmploymentType)) {
            return false;
        }
        EmploymentType other = (EmploymentType) object;
        if ((this.employmentType == null && other.employmentType != null) || (this.employmentType != null && !this.employmentType.equals(other.employmentType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EmploymentType[ employmentType=" + employmentType + " ]";
    }
    
}
