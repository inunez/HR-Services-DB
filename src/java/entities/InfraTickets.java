/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ismaelnunez
 */
@Entity
@Table(name = "infra_tickets")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InfraTickets.findAll", query = "SELECT i FROM InfraTickets i"),
    @NamedQuery(name = "InfraTickets.findByCallNumber", query = "SELECT i FROM InfraTickets i WHERE i.callNumber = :callNumber"),
    @NamedQuery(name = "InfraTickets.findByTier1", query = "SELECT i FROM InfraTickets i WHERE i.tier1 = :tier1"),
    @NamedQuery(name = "InfraTickets.findByTier2", query = "SELECT i FROM InfraTickets i WHERE i.tier2 = :tier2"),
    @NamedQuery(name = "InfraTickets.findByTier3", query = "SELECT i FROM InfraTickets i WHERE i.tier3 = :tier3"),
    @NamedQuery(name = "InfraTickets.findByCurrentOfficer", query = "SELECT i FROM InfraTickets i WHERE i.currentOfficer = :currentOfficer"),
    @NamedQuery(name = "InfraTickets.findByDescription", query = "SELECT i FROM InfraTickets i WHERE i.description = :description"),
    @NamedQuery(name = "InfraTickets.findByTimeSpent", query = "SELECT i FROM InfraTickets i WHERE i.timeSpent = :timeSpent"),
    @NamedQuery(name = "InfraTickets.findByLoggedBy", query = "SELECT i FROM InfraTickets i WHERE i.loggedBy = :loggedBy"),
    @NamedQuery(name = "InfraTickets.findByLogDate", query = "SELECT i FROM InfraTickets i WHERE i.logDate = :logDate"),
    @NamedQuery(name = "InfraTickets.findByStatus", query = "SELECT i FROM InfraTickets i WHERE i.status = :status"),
    @NamedQuery(name = "InfraTickets.findByResolveDate", query = "SELECT i FROM InfraTickets i WHERE i.resolveDate = :resolveDate"),
    @NamedQuery(name = "InfraTickets.findByForwardedTo", query = "SELECT i FROM InfraTickets i WHERE i.forwardedTo = :forwardedTo"),
    @NamedQuery(name = "InfraTickets.findByLastActionDate", query = "SELECT i FROM InfraTickets i WHERE i.lastActionDate = :lastActionDate")})
public class InfraTickets implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "call_number")
    private Integer callNumber;
    @Size(max = 255)
    @Column(name = "tier1")
    private String tier1;
    @Size(max = 255)
    @Column(name = "tier2")
    private String tier2;
    @Size(max = 255)
    @Column(name = "tier3")
    private String tier3;
    @Size(max = 255)
    @Column(name = "current_officer")
    private String currentOfficer;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "time_spent")
    private Double timeSpent;
    @Size(max = 255)
    @Column(name = "logged_by")
    private String loggedBy;
    @Column(name = "log_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Column(name = "resolve_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolveDate;
    @Size(max = 255)
    @Column(name = "forwarded_to")
    private String forwardedTo;
    @Column(name = "last_action_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActionDate;
    @OneToMany(mappedBy = "infraTicket")
    private Collection<VisaComment> visaCommentCollection;
    @OneToMany(mappedBy = "infraTicket")
    private Collection<PoliceCheckComment> policeCheckCommentCollection;

    public InfraTickets() {
    }

    public InfraTickets(Integer callNumber) {
        this.callNumber = callNumber;
    }

    public Integer getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(Integer callNumber) {
        this.callNumber = callNumber;
    }

    public String getTier1() {
        return tier1;
    }

    public void setTier1(String tier1) {
        this.tier1 = tier1;
    }

    public String getTier2() {
        return tier2;
    }

    public void setTier2(String tier2) {
        this.tier2 = tier2;
    }

    public String getTier3() {
        return tier3;
    }

    public void setTier3(String tier3) {
        this.tier3 = tier3;
    }

    public String getCurrentOfficer() {
        return currentOfficer;
    }

    public void setCurrentOfficer(String currentOfficer) {
        this.currentOfficer = currentOfficer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getLoggedBy() {
        return loggedBy;
    }

    public void setLoggedBy(String loggedBy) {
        this.loggedBy = loggedBy;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getResolveDate() {
        return resolveDate;
    }

    public void setResolveDate(Date resolveDate) {
        this.resolveDate = resolveDate;
    }

    public String getForwardedTo() {
        return forwardedTo;
    }

    public void setForwardedTo(String forwardedTo) {
        this.forwardedTo = forwardedTo;
    }

    public Date getLastActionDate() {
        return lastActionDate;
    }

    public void setLastActionDate(Date lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    @XmlTransient
    public Collection<VisaComment> getVisaCommentCollection() {
        return visaCommentCollection;
    }

    public void setVisaCommentCollection(Collection<VisaComment> visaCommentCollection) {
        this.visaCommentCollection = visaCommentCollection;
    }

    @XmlTransient
    public Collection<PoliceCheckComment> getPoliceCheckCommentCollection() {
        return policeCheckCommentCollection;
    }

    public void setPoliceCheckCommentCollection(Collection<PoliceCheckComment> policeCheckCommentCollection) {
        this.policeCheckCommentCollection = policeCheckCommentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (callNumber != null ? callNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InfraTickets)) {
            return false;
        }
        InfraTickets other = (InfraTickets) object;
        if ((this.callNumber == null && other.callNumber != null) || (this.callNumber != null && !this.callNumber.equals(other.callNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InfraTickets[ callNumber=" + callNumber + " ]";
    }
    
}
