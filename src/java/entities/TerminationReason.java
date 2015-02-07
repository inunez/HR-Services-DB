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
@Table(name = "termination_reason")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TerminationReason.findAll", query = "SELECT t FROM TerminationReason t"),
    @NamedQuery(name = "TerminationReason.findByTermReason", query = "SELECT t FROM TerminationReason t WHERE t.termReason = :termReason"),
    @NamedQuery(name = "TerminationReason.findByTermDescription", query = "SELECT t FROM TerminationReason t WHERE t.termDescription = :termDescription")})
public class TerminationReason implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "term_reason")
    private String termReason;
    @Size(max = 255)
    @Column(name = "term_description")
    private String termDescription;
    @OneToMany(mappedBy = "termReason")
    private Collection<Employee> employeeCollection;

    public TerminationReason() {
    }

    public TerminationReason(String termReason) {
        this.termReason = termReason;
    }

    public String getTermReason() {
        return termReason;
    }

    public void setTermReason(String termReason) {
        this.termReason = termReason;
    }

    public String getTermDescription() {
        return termDescription;
    }

    public void setTermDescription(String termDescription) {
        this.termDescription = termDescription;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (termReason != null ? termReason.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TerminationReason)) {
            return false;
        }
        TerminationReason other = (TerminationReason) object;
        if ((this.termReason == null && other.termReason != null) || (this.termReason != null && !this.termReason.equals(other.termReason))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TerminationReason[ termReason=" + termReason + " ]";
    }
    
}
