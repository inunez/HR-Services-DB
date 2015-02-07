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
@Table(name = "level3")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Level3.findAll", query = "SELECT l FROM Level3 l"),
    @NamedQuery(name = "Level3.findByLevel3Code", query = "SELECT l FROM Level3 l WHERE l.level3Code = :level3Code"),
    @NamedQuery(name = "Level3.findByLevel3CodeDescription", query = "SELECT l FROM Level3 l WHERE l.level3CodeDescription = :level3CodeDescription")})
public class Level3 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "level3_code")
    private String level3Code;
    @Size(max = 255)
    @Column(name = "level3_code_description")
    private String level3CodeDescription;
    @OneToMany(mappedBy = "level3Code")
    private Collection<Payroll> payrollCollection;

    public Level3() {
    }

    public Level3(String level3Code) {
        this.level3Code = level3Code;
    }

    public String getLevel3Code() {
        return level3Code;
    }

    public void setLevel3Code(String level3Code) {
        this.level3Code = level3Code;
    }

    public String getLevel3CodeDescription() {
        return level3CodeDescription;
    }

    public void setLevel3CodeDescription(String level3CodeDescription) {
        this.level3CodeDescription = level3CodeDescription;
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
        hash += (level3Code != null ? level3Code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Level3)) {
            return false;
        }
        Level3 other = (Level3) object;
        if ((this.level3Code == null && other.level3Code != null) || (this.level3Code != null && !this.level3Code.equals(other.level3Code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Level3[ level3Code=" + level3Code + " ]";
    }
    
}
