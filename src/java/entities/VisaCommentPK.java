/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ismael
 */
@Embeddable
public class VisaCommentPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "id_number")
    private String idNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "comment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentDate;

    public VisaCommentPK() {
    }

    public VisaCommentPK(String idNumber, Date commentDate) {
        this.idNumber = idNumber;
        this.commentDate = commentDate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNumber != null ? idNumber.hashCode() : 0);
        hash += (commentDate != null ? commentDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VisaCommentPK)) {
            return false;
        }
        VisaCommentPK other = (VisaCommentPK) object;
        if ((this.idNumber == null && other.idNumber != null) || (this.idNumber != null && !this.idNumber.equals(other.idNumber))) {
            return false;
        }
        if ((this.commentDate == null && other.commentDate != null) || (this.commentDate != null && !this.commentDate.equals(other.commentDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.VisaCommentPK[ idNumber=" + idNumber + ", commentDate=" + commentDate + " ]";
    }
    
}
