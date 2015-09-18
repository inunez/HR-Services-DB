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
@Table(name = "award")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Award.findAll", query = "SELECT a FROM Award a"),
    @NamedQuery(name = "Award.findByAwardCode", query = "SELECT a FROM Award a WHERE a.awardCode = :awardCode"),
    @NamedQuery(name = "Award.findByAwardDescription", query = "SELECT a FROM Award a WHERE a.awardDescription = :awardDescription")})
public class Award implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "award_code")
    private String awardCode;
    @Size(max = 255)
    @Column(name = "award_description")
    private String awardDescription;
    @Size(max = 255)
    @Column(name = "award_title")
    private String awardTitle;
    @OneToMany(mappedBy = "awardCode")
    private Collection<Employee> employeeCollection;
    @OneToMany(mappedBy = "awardCode")
    private Collection<Earning> earningCollection;
    @OneToMany(mappedBy = "awardCode")
    private Collection<Payroll> payrollCollection;

    public Award() {
    }

    public Award(String awardCode) {
        this.awardCode = awardCode;
    }

    public String getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(String awardCode) {
        this.awardCode = awardCode;
    }

    public String getAwardDescription() {
        return awardDescription;
    }

    public void setAwardDescription(String awardDescription) {
        this.awardDescription = awardDescription;
    }

    public String getAwardTitle() {
        return awardTitle;
    }

    public void setAwardTitle(String awardTitle) {
        this.awardTitle = awardTitle;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    @XmlTransient
    public Collection<Earning> getEarningCollection() {
        return earningCollection;
    }

    public void setEarningCollection(Collection<Earning> earningCollection) {
        this.earningCollection = earningCollection;
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
        hash += (awardCode != null ? awardCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Award)) {
            return false;
        }
        Award other = (Award) object;
        if ((this.awardCode == null && other.awardCode != null) || (this.awardCode != null && !this.awardCode.equals(other.awardCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return awardTitle;
    }
    
    public String toString(boolean onlyCode) {
        return "entities.Award[ awardCode=" + awardCode + " ]";
    }
}
