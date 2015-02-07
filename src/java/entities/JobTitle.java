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
@Table(name = "job_title")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobTitle.findAll", query = "SELECT j FROM JobTitle j"),
    @NamedQuery(name = "JobTitle.findByJobTitle", query = "SELECT j FROM JobTitle j WHERE j.jobTitle = :jobTitle"),
    @NamedQuery(name = "JobTitle.findByJobTitleDescription", query = "SELECT j FROM JobTitle j WHERE j.jobTitleDescription = :jobTitleDescription")})
public class JobTitle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "job_title")
    private String jobTitle;
    @Size(max = 10)
    @Column(name = "job_title_description")
    private String jobTitleDescription;
    @OneToMany(mappedBy = "jobTitleCode")
    private Collection<Earning> earningCollection;
    @OneToMany(mappedBy = "jobTitle")
    private Collection<Payroll> payrollCollection;

    public JobTitle() {
    }

    public JobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitleDescription() {
        return jobTitleDescription;
    }

    public void setJobTitleDescription(String jobTitleDescription) {
        this.jobTitleDescription = jobTitleDescription;
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
        hash += (jobTitle != null ? jobTitle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobTitle)) {
            return false;
        }
        JobTitle other = (JobTitle) object;
        if ((this.jobTitle == null && other.jobTitle != null) || (this.jobTitle != null && !this.jobTitle.equals(other.jobTitle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.JobTitle[ jobTitle=" + jobTitle + " ]";
    }
    
}
