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
@Table(name = "personnel_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonnelGroup.findAll", query = "SELECT p FROM PersonnelGroup p"),
    @NamedQuery(name = "PersonnelGroup.findByPersonnelGroupCode", query = "SELECT p FROM PersonnelGroup p WHERE p.personnelGroupCode = :personnelGroupCode"),
    @NamedQuery(name = "PersonnelGroup.findByPersonnelGroupDescription", query = "SELECT p FROM PersonnelGroup p WHERE p.personnelGroupDescription = :personnelGroupDescription")})
public class PersonnelGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "personnel_group_code")
    private String personnelGroupCode;
    @Size(max = 255)
    @Column(name = "personnel_group_description")
    private String personnelGroupDescription;
    @OneToMany(mappedBy = "personnelGroupCode")
    private Collection<Payroll> payrollCollection;

    public PersonnelGroup() {
    }

    public PersonnelGroup(String personnelGroupCode) {
        this.personnelGroupCode = personnelGroupCode;
    }

    public String getPersonnelGroupCode() {
        return personnelGroupCode;
    }

    public void setPersonnelGroupCode(String personnelGroupCode) {
        this.personnelGroupCode = personnelGroupCode;
    }

    public String getPersonnelGroupDescription() {
        return personnelGroupDescription;
    }

    public void setPersonnelGroupDescription(String personnelGroupDescription) {
        this.personnelGroupDescription = personnelGroupDescription;
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
        hash += (personnelGroupCode != null ? personnelGroupCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonnelGroup)) {
            return false;
        }
        PersonnelGroup other = (PersonnelGroup) object;
        if ((this.personnelGroupCode == null && other.personnelGroupCode != null) || (this.personnelGroupCode != null && !this.personnelGroupCode.equals(other.personnelGroupCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PersonnelGroup[ personnelGroupCode=" + personnelGroupCode + " ]";
    }
    
}
