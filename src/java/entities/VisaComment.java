/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "visa_comment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VisaComment.findAll", query = "SELECT v FROM VisaComment v"),
    @NamedQuery(name = "VisaComment.findByIdNumber", query = "SELECT v FROM VisaComment v WHERE v.visaCommentPK.idNumber = :idNumber"),
    @NamedQuery(name = "VisaComment.findByCommentDate", query = "SELECT v FROM VisaComment v WHERE v.visaCommentPK.commentDate = :commentDate"),
    @NamedQuery(name = "VisaComment.findByComment", query = "SELECT v FROM VisaComment v WHERE v.comment = :comment"),
    @NamedQuery(name = "VisaComment.findByUser", query = "SELECT v FROM VisaComment v WHERE v.user = :user"),
    @NamedQuery(name = "VisaComment.findByInfraTicket", query = "SELECT v FROM VisaComment v WHERE v.infraTicket = :infraTicket")})
public class VisaComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VisaCommentPK visaCommentPK;
    @Size(max = 255)
    @Column(name = "comment")
    private String comment;
    @Size(max = 50)
    @Column(name = "user")
    private String user;
    @Column(name = "infra_ticket")
    private Integer infraTicket;
//    @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)
    @JoinColumns({
    @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false),
    @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Employee employee;

    public VisaComment() {
    }

    public VisaComment(VisaCommentPK visaCommentPK) {
        this.visaCommentPK = visaCommentPK;
    }

    public VisaComment(String idNumber, Date commentDate) {
        this.visaCommentPK = new VisaCommentPK(idNumber, commentDate);
    }
    
    public VisaComment(String idNumber, String status, Date commentDate) {
        this.visaCommentPK = new VisaCommentPK(idNumber, status, commentDate);
    }

    public VisaCommentPK getVisaCommentPK() {
        return visaCommentPK;
    }

    public void setVisaCommentPK(VisaCommentPK visaCommentPK) {
        this.visaCommentPK = visaCommentPK;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getInfraTicket() {
        return infraTicket;
    }

    public void setInfraTicket(Integer infraTicket) {
        this.infraTicket = infraTicket;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (visaCommentPK != null ? visaCommentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VisaComment)) {
            return false;
        }
        VisaComment other = (VisaComment) object;
        if ((this.visaCommentPK == null && other.visaCommentPK != null) || (this.visaCommentPK != null && !this.visaCommentPK.equals(other.visaCommentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.VisaComment[ visaCommentPK=" + visaCommentPK + " ]";
    }

}
