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
@Table(name = "police_check_comment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PoliceCheckComment.findAll", query = "SELECT p FROM PoliceCheckComment p"),
    @NamedQuery(name = "PoliceCheckComment.findByIdNumber", query = "SELECT p FROM PoliceCheckComment p WHERE p.policeCheckCommentPK.idNumber = :idNumber"),
    @NamedQuery(name = "PoliceCheckComment.findByCommentDate", query = "SELECT p FROM PoliceCheckComment p WHERE p.policeCheckCommentPK.commentDate = :commentDate"),
    @NamedQuery(name = "PoliceCheckComment.findByComment", query = "SELECT p FROM PoliceCheckComment p WHERE p.comment = :comment"),
    @NamedQuery(name = "PoliceCheckComment.findByUser", query = "SELECT p FROM PoliceCheckComment p WHERE p.user = :user"),
    @NamedQuery(name = "PoliceCheckComment.findByInfraTicket", query = "SELECT p FROM PoliceCheckComment p WHERE p.infraTicket = :infraTicket")})
public class PoliceCheckComment implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PoliceCheckCommentPK policeCheckCommentPK;
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

    public PoliceCheckComment() {
    }

    public PoliceCheckComment(PoliceCheckCommentPK policeCheckCommentPK) {
        this.policeCheckCommentPK = policeCheckCommentPK;
    }

    public PoliceCheckComment(String idNumber, String status, Date commentDate) {
        this.policeCheckCommentPK = new PoliceCheckCommentPK(idNumber, status, commentDate);
    }

    public PoliceCheckCommentPK getPoliceCheckCommentPK() {
        return policeCheckCommentPK;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setPoliceCheckCommentPK(PoliceCheckCommentPK policeCheckCommentPK) {
        this.policeCheckCommentPK = policeCheckCommentPK;
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
        hash += (policeCheckCommentPK != null ? policeCheckCommentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PoliceCheckComment)) {
            return false;
        }
        PoliceCheckComment other = (PoliceCheckComment) object;
        if ((this.policeCheckCommentPK == null && other.policeCheckCommentPK != null) || (this.policeCheckCommentPK != null && !this.policeCheckCommentPK.equals(other.policeCheckCommentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PoliceCheckComment[ policeCheckCommentPK=" + policeCheckCommentPK + " ]";
    }

}
