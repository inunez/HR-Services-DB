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
@Table(name = "personnel_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonnelType.findAll", query = "SELECT p FROM PersonnelType p"),
    @NamedQuery(name = "PersonnelType.findByPersonnelType", query = "SELECT p FROM PersonnelType p WHERE p.personnelType = :personnelType"),
    @NamedQuery(name = "PersonnelType.findByPersonnelTypeDescription", query = "SELECT p FROM PersonnelType p WHERE p.personnelTypeDescription = :personnelTypeDescription")})
public class PersonnelType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "personnel_type")
    private String personnelType;
    @Size(max = 255)
    @Column(name = "personnel_type_description")
    private String personnelTypeDescription;
    @OneToMany(mappedBy = "personnelType")
    private Collection<Payroll> payrollCollection;

    public PersonnelType() {
    }

    public PersonnelType(String personnelType) {
        this.personnelType = personnelType;
    }

    public String getPersonnelType() {
        return personnelType;
    }

    public void setPersonnelType(String personnelType) {
        this.personnelType = personnelType;
    }

    public String getPersonnelTypeDescription() {
        return personnelTypeDescription;
    }

    public void setPersonnelTypeDescription(String personnelTypeDescription) {
        this.personnelTypeDescription = personnelTypeDescription;
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
        hash += (personnelType != null ? personnelType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonnelType)) {
            return false;
        }
        PersonnelType other = (PersonnelType) object;
        if ((this.personnelType == null && other.personnelType != null) || (this.personnelType != null && !this.personnelType.equals(other.personnelType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PersonnelType[ personnelType=" + personnelType + " ]";
    }
    
}
