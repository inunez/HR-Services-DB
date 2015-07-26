/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "level1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Level1.findAll", query = "SELECT l FROM Level1 l"),
    @NamedQuery(name = "Level1.findByLevel1Code", query = "SELECT l FROM Level1 l WHERE l.level1Code = :level1Code"),
    @NamedQuery(name = "Level1.findByLevel1CodeDescription", query = "SELECT l FROM Level1 l WHERE l.level1CodeDescription = :level1CodeDescription")})
public class Level1 implements Serializable {
    @OneToMany(mappedBy = "level1Code")
    private Collection<Employee> employeeCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "level1_code")
    private String level1Code;
    @Size(max = 255)
    @Column(name = "level1_code_description")
    private String level1CodeDescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "level1")
    private Collection<Payroll> payrollCollection;

    public Level1() {
    }

    public Level1(String level1Code) {
        this.level1Code = level1Code;
    }

    public String getLevel1Code() {
        return level1Code;
    }

    public void setLevel1Code(String level1Code) {
        this.level1Code = level1Code;
    }

    public String getLevel1CodeDescription() {
        return level1CodeDescription;
    }

    public void setLevel1CodeDescription(String level1CodeDescription) {
        this.level1CodeDescription = level1CodeDescription;
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
        hash += (level1Code != null ? level1Code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Level1)) {
            return false;
        }
        Level1 other = (Level1) object;
        if ((this.level1Code == null && other.level1Code != null) || (this.level1Code != null && !this.level1Code.equals(other.level1Code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Level1[ level1Code=" + level1Code + " ]";
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }
    
}
