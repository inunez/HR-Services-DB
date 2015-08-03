/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael Nunez
 */
@Entity
@Table(name = "email")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Email.findAll", query = "SELECT e FROM Email e"),
    @NamedQuery(name = "Email.findByIdNumber", query = "SELECT e FROM Email e WHERE e.emailPK.idNumber = :idNumber"),
    @NamedQuery(name = "Email.findByStatus", query = "SELECT e FROM Email e WHERE e.emailPK.status = :status"),
    @NamedQuery(name = "Email.findByEmail", query = "SELECT e FROM Email e WHERE e.email = :email")})
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EmailPK emailPK;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "email")
    private String email;
    @JoinColumns({
        @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false),
        @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Employee employee;

    public Email() {
    }

    public Email(EmailPK emailPK) {
        this.emailPK = emailPK;
    }

    public Email(String idNumber, String status) {
        this.emailPK = new EmailPK(idNumber, status);
    }

    public EmailPK getEmailPK() {
        return emailPK;
    }

    public void setEmailPK(EmailPK emailPK) {
        this.emailPK = emailPK;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        hash += (emailPK != null ? emailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Email)) {
            return false;
        }
        Email other = (Email) object;
        if ((this.emailPK == null && other.emailPK != null) || (this.emailPK != null && !this.emailPK.equals(other.emailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Email[ emailPK=" + emailPK + " ]";
    }
    
}
