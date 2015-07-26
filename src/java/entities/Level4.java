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
@Table(name = "level4")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Level4.findAll", query = "SELECT l FROM Level4 l"),
    @NamedQuery(name = "Level4.findByLevel4Code", query = "SELECT l FROM Level4 l WHERE l.level4Code = :level4Code"),
    @NamedQuery(name = "Level4.findByLevel4CodeDescription", query = "SELECT l FROM Level4 l WHERE l.level4CodeDescription = :level4CodeDescription")})
public class Level4 implements Serializable {
    @OneToMany(mappedBy = "level4Code")
    private Collection<Employee> employeeCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "level4_code")
    private String level4Code;
    @Size(max = 255)
    @Column(name = "level4_code_description")
    private String level4CodeDescription;
    @OneToMany(mappedBy = "level4Code")
    private Collection<Payroll> payrollCollection;

    public Level4() {
    }

    public Level4(String level4Code) {
        this.level4Code = level4Code;
    }

    public String getLevel4Code() {
        return level4Code;
    }

    public void setLevel4Code(String level4Code) {
        this.level4Code = level4Code;
    }

    public String getLevel4CodeDescription() {
        return level4CodeDescription;
    }

    public void setLevel4CodeDescription(String level4CodeDescription) {
        this.level4CodeDescription = level4CodeDescription;
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
        hash += (level4Code != null ? level4Code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Level4)) {
            return false;
        }
        Level4 other = (Level4) object;
        if ((this.level4Code == null && other.level4Code != null) || (this.level4Code != null && !this.level4Code.equals(other.level4Code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Level4[ level4Code=" + level4Code + " ]";
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }
    
}
