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
@Table(name = "level5")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Level5.findAll", query = "SELECT l FROM Level5 l"),
    @NamedQuery(name = "Level5.findByLevel5Code", query = "SELECT l FROM Level5 l WHERE l.level5Code = :level5Code"),
    @NamedQuery(name = "Level5.findByLevel5CodeDescription", query = "SELECT l FROM Level5 l WHERE l.level5CodeDescription = :level5CodeDescription")})
public class Level5 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "level5_code")
    private String level5Code;
    @Size(max = 255)
    @Column(name = "level5_code_description")
    private String level5CodeDescription;
    @OneToMany(mappedBy = "level5Code")
    private Collection<Payroll> payrollCollection;

    public Level5() {
    }

    public Level5(String level5Code) {
        this.level5Code = level5Code;
    }

    public String getLevel5Code() {
        return level5Code;
    }

    public void setLevel5Code(String level5Code) {
        this.level5Code = level5Code;
    }

    public String getLevel5CodeDescription() {
        return level5CodeDescription;
    }

    public void setLevel5CodeDescription(String level5CodeDescription) {
        this.level5CodeDescription = level5CodeDescription;
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
        hash += (level5Code != null ? level5Code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Level5)) {
            return false;
        }
        Level5 other = (Level5) object;
        if ((this.level5Code == null && other.level5Code != null) || (this.level5Code != null && !this.level5Code.equals(other.level5Code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Level5[ level5Code=" + level5Code + " ]";
    }
    
}
