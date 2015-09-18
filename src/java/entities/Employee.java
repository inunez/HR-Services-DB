    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findByIdNumber", query = "SELECT e FROM Employee e WHERE e.employeePK.idNumber = :idNumber AND e.employeePK.status = :status"),
    @NamedQuery(name = "Employee.findByStatus", query = "SELECT e FROM Employee e WHERE e.employeePK.status = :status"),
    @NamedQuery(name = "Employee.findBySurname", query = "SELECT e FROM Employee e WHERE e.surname = :surname AND e.employeePK.status = :status"),
    @NamedQuery(name = "Employee.findByFullName", query = "SELECT e FROM Employee e WHERE CONCAT(e.firstName, ' ', e.surname) LIKE :fullName AND e.employeePK.status = :status"),
    @NamedQuery(name = "Employee.findByFirstName", query = "SELECT e FROM Employee e WHERE e.firstName = :firstName"),
    @NamedQuery(name = "Employee.findByMiddleName", query = "SELECT e FROM Employee e WHERE e.middleName = :middleName"),
    @NamedQuery(name = "Employee.findByHomeAddress", query = "SELECT e FROM Employee e WHERE e.homeAddress = :homeAddress"),
    @NamedQuery(name = "Employee.findBySuburb", query = "SELECT e FROM Employee e WHERE e.suburb = :suburb"),
    @NamedQuery(name = "Employee.findByHomePhone", query = "SELECT e FROM Employee e WHERE e.homePhone = :homePhone"),
    @NamedQuery(name = "Employee.findByPostcode", query = "SELECT e FROM Employee e WHERE e.postcode = :postcode"),
    @NamedQuery(name = "Employee.findByDateofBirth", query = "SELECT e FROM Employee e WHERE e.dateofBirth = :dateofBirth"),
    @NamedQuery(name = "Employee.findByHireDate", query = "SELECT e FROM Employee e WHERE e.hireDate = :hireDate"),
    @NamedQuery(name = "Employee.findByTermDate", query = "SELECT e FROM Employee e WHERE e.termDate = :termDate"),
    @NamedQuery(name = "Employee.findByPaidUpToDate", query = "SELECT e FROM Employee e WHERE e.paidUpToDate = :paidUpToDate"),
    @NamedQuery(name = "Employee.findByUnpaidLeave", query = "SELECT e FROM Employee e WHERE e.unpaidLeave = :unpaidLeave"),
    @NamedQuery(name = "Employee.findByMobilePhone", query = "SELECT e FROM Employee e WHERE e.mobilePhone = :mobilePhone"),
    @NamedQuery(name = "Employee.findByBusinessEmail", query = "SELECT e FROM Employee e WHERE e.businessEmail = :businessEmail"),
    @NamedQuery(name = "Employee.findByPersonalEmail", query = "SELECT e FROM Employee e WHERE e.personalEmail = :personalEmail"),
    @NamedQuery(name = "Employee.findEarningDistinct", query = "SELECT DISTINCT e.payDate, e.taxableGross, e.taxAmount, e.netPay FROM Earning e  WHERE e.earningPK.idNumber = :idNumber ORDER BY e.payDate DESC"),
    @NamedQuery(name = "Employee.findByProbationDueDate", query = "SELECT e FROM Employee e WHERE e.probationDueDate = :probationDueDate"),
    @NamedQuery(name = "Employee.findByAccount", query = "SELECT e FROM Employee e JOIN e.payrollCollection p JOIN p.accountNumber a WHERE UPPER(a.accountDescription) LIKE :accountDesc AND e.employeePK.status = :status ORDER BY e.employeePK.idNumber"),
    @NamedQuery(name = "Employee.findByPosition", query = "SELECT e FROM Employee e JOIN e.positionId p WHERE UPPER(p.positionTitle) LIKE :position AND e.employeePK.status = :status ORDER BY e.employeePK.idNumber")
})

public class Employee implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EmployeePK employeePK;
    @Size(max = 50)
    @Column(name = "surname")
    private String surname;
    @Size(max = 50)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 50)
    @Column(name = "middle_name")
    private String middleName;
    @Size(max = 255)
    @Column(name = "home_address")
    private String homeAddress;
    @Size(max = 100)
    @Column(name = "suburb")
    private String suburb;
    @Size(max = 20)
    @Column(name = "home_phone")
    private String homePhone;
    @Column(name = "postcode")
    private Integer postcode;
    @Column(name = "date_of_Birth")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateofBirth;
    @Column(name = "hire_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hireDate;
    @Column(name = "term_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date termDate;
    @Column(name = "paid_up_to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidUpToDate;
    @Size(max = 1)
    @Column(name = "unpaid_leave")
    private String unpaidLeave;
    @Size(max = 20)
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Size(max = 100)
    @Column(name = "business_email")
    private String businessEmail;
    @Size(max = 100)
    @Column(name = "personal_email")
    private String personalEmail;
    @Column(name = "probation_due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date probationDueDate;
    @JoinColumn(name = "classification_id", referencedColumnName = "classification_id")
    @ManyToOne
    private Classification classificationId;
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @ManyToOne
    private Department departmentId;
    @JoinColumn(name = "award_code", referencedColumnName = "award_code")
    @ManyToOne
    private Award awardCode;
    @JoinColumn(name = "gender", referencedColumnName = "gender")
    @ManyToOne
    private Gender gender;
    @JoinColumn(name = "position_id", referencedColumnName = "position_id")
    @ManyToOne
    private Position positionId;
    @JoinColumn(name = "reports_to_position_id", referencedColumnName = "position_id")
    @ManyToOne
    private Position reportsToPositionId;
    @JoinColumn(name = "state_code", referencedColumnName = "state_code")
    @ManyToOne
    private State stateCode;
    @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Status status1;
    @JoinColumn(name = "term_reason", referencedColumnName = "term_reason")
    @ManyToOne
    private TerminationReason termReason;
    @JoinColumn(name = "level2_code", referencedColumnName = "state_code")
    @ManyToOne
    private State level2Code;
    @JoinColumn(name = "level1_code", referencedColumnName = "level1_code")
    @ManyToOne
    private Level1 level1Code;
    @JoinColumn(name = "level3_code", referencedColumnName = "level3_code")
    @ManyToOne
    private Level3 level3Code;
    @JoinColumn(name = "level4_code", referencedColumnName = "level4_code")
    @ManyToOne
    private Level4 level4Code;
    @JoinColumn(name = "level5_code", referencedColumnName = "level5_code")
    @ManyToOne
    private Level5 level5Code;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<EarningLeave> earningLeaveCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Earning> earningCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<EarningSummary> earningSummaryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<PoliceCheck> policeCheckCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<PoliceCheckComment> policeCheckCommentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Visa> visaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<VisaComment> visaCommentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Payroll> payrollCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Uniform> uniformCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Plaxa> plaxaCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "employee")
    private Email email;
    
    @Transient
    private String fullName;
    
    public Employee() {
        fullName = "";
    }

    public Employee(EmployeePK employeePK) {
        this.employeePK = employeePK;
    }

    public Employee(String idNumber, String status) {
        this.employeePK = new EmployeePK(idNumber, status);
    }

    public EmployeePK getEmployeePK() {
        return employeePK;
    }

    public void setEmployeePK(EmployeePK employeePK) {
        this.employeePK = employeePK;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        Employee cloned = (Employee) super.clone();
        
        ArrayList <PoliceCheckComment> newComments = new ArrayList();
        for (PoliceCheckComment newComment : policeCheckCommentCollection) {
            try {
                newComments.add((PoliceCheckComment) newComment.clone());
            } catch (CloneNotSupportedException ex) {
//                Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        cloned.policeCheckCommentCollection = newComments;
        
        return cloned;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public Date getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(Date dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getTermDate() {
        return termDate;
    }

    public void setTermDate(Date termDate) {
        this.termDate = termDate;
    }

    public Date getPaidUpToDate() {
        return paidUpToDate;
    }

    public void setPaidUpToDate(Date paidUpToDate) {
        this.paidUpToDate = paidUpToDate;
    }

    public String getUnpaidLeave() {
        return unpaidLeave;
    }

    public void setUnpaidLeave(String unpaidLeave) {
        this.unpaidLeave = unpaidLeave;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getBusinessEmail() {
        if (businessEmail == null || "".equals(businessEmail)){
            businessEmail = "N/A";
        }
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getPersonalEmail() {
        if (personalEmail == null || "".equals(personalEmail) ) {
            personalEmail = "N/A";
        }
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public Date getProbationDueDate() {
        return probationDueDate;
    }

    public void setProbationDueDate(Date probationDueDate) {
        this.probationDueDate = probationDueDate;
    }

    public Classification getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Classification classificationId) {
        this.classificationId = classificationId;
    }

    public Department getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Department departmentId) {
        this.departmentId = departmentId;
    }

    public Award getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(Award awardCode) {
        this.awardCode = awardCode;
    }

    public String getFullName() {
        fullName = firstName + " " + surname;
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Position getPositionId() {
        return positionId;
    }

    public void setPositionId(Position positionId) {
        this.positionId = positionId;
    }

    public Position getReportsToPositionId() {
        return reportsToPositionId;
    }

    public void setReportsToPositionId(Position reportsToPositionId) {
        this.reportsToPositionId = reportsToPositionId;
    }

//    public String getReportsTo() {
//        String reportsTo = "";
//        if (reportsTo == ""){
//            Collection<Employee> manager = reportsToPositionId.getEmployeeCollection();
//            reportsTo = manager.iterator().next().surname;
//        }
//        return reportsTo;
//    }

//    public void setReportsTo(String reportsTo) {
//        this.reportsTo = reportsTo;
//    }
    
    public State getStateCode() {
        return stateCode;
    }

    public void setStateCode(State stateCode) {
        this.stateCode = stateCode;
    }

    public Status getStatus1() {
        return status1;
    }

    public void setStatus1(Status status1) {
        this.status1 = status1;
    }

    public TerminationReason getTermReason() {
        return termReason;
    }

    public void setTermReason(TerminationReason termReason) {
        this.termReason = termReason;
    }

    @XmlTransient
    public Collection<EarningLeave> getEarningLeaveCollection() {
        return earningLeaveCollection;
    }

    public void setEarningLeaveCollection(Collection<EarningLeave> earningLeaveCollection) {
        this.earningLeaveCollection = earningLeaveCollection;
    }

    @XmlTransient
    public Collection<Earning> getEarningCollection() {
        
        return earningCollection;
    }

    public void setEarningCollection(Collection<Earning> earningCollection) {
        this.earningCollection = earningCollection;
    }

    @XmlTransient
    public Collection<VisaComment> getVisaCommentCollection() {
        return visaCommentCollection;
    }

    public void setVisaCommentCollection(Collection<VisaComment> visaCommentCollection) {
        this.visaCommentCollection = visaCommentCollection;
    }

    @XmlTransient
    public Collection<EarningSummary> getEarningSummaryCollection() {
        return earningSummaryCollection;
    }

    public void setEarningSummaryCollection(Collection<EarningSummary> earningSummaryCollection) {
        this.earningSummaryCollection = earningSummaryCollection;
    }

    @XmlTransient
    public Collection<PoliceCheckComment> getPoliceCheckCommentCollection() {
        return policeCheckCommentCollection;
    }

    public void setPoliceCheckCommentCollection(Collection<PoliceCheckComment> policeCheckCommentCollection) {
        this.policeCheckCommentCollection = policeCheckCommentCollection;
    }

    @XmlTransient
    public Collection<Visa> getVisaCollection() {
        return visaCollection;
    }

    public void setVisaCollection(Collection<Visa> visaCollection) {
        this.visaCollection = visaCollection;
    }

    @XmlTransient
    public Collection<PoliceCheck> getPoliceCheckCollection() {
        return policeCheckCollection;
    }

    public void setPoliceCheckCollection(Collection<PoliceCheck> policeCheckCollection) {
        this.policeCheckCollection = policeCheckCollection;
    }
    
    @XmlTransient
    public Collection<Payroll> getPayrollCollection() {
        return payrollCollection;
    }

    public void setPayrollCollection(Collection<Payroll> payrollCollection) {
        this.payrollCollection = payrollCollection;
    }
    
    public Collection<Earning> getEarningCollectionDistinct() {
        return (Collection<Earning>) earningCollection.stream().distinct();
    }
 
    @XmlTransient
    public Collection<Uniform> getUniformCollection() {
        return uniformCollection;
    }

    public void setUniformCollection(Collection<Uniform> uniformCollection) {
        this.uniformCollection = uniformCollection;
    }

    @XmlTransient
    public Collection<Plaxa> getPlaxaCollection() {
        return plaxaCollection;
    }

    public void setPlaxaCollection(Collection<Plaxa> plaxaCollection) {
        this.plaxaCollection = plaxaCollection;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeePK != null ? employeePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeePK == null && other.employeePK != null) || (this.employeePK != null && !this.employeePK.equals(other.employeePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Employee[ employeePK=" + employeePK + " ]";
    }

    public State getLevel2Code() {
        return level2Code;
    }

    public void setLevel2Code(State level2Code) {
        this.level2Code = level2Code;
    }

    public Level1 getLevel1Code() {
        return level1Code;
    }

    public void setLevel1Code(Level1 level1Code) {
        this.level1Code = level1Code;
    }

    public Level3 getLevel3Code() {
        return level3Code;
    }

    public void setLevel3Code(Level3 level3Code) {
        this.level3Code = level3Code;
    }

    public Level4 getLevel4Code() {
        return level4Code;
    }

    public void setLevel4Code(Level4 level4Code) {
        this.level4Code = level4Code;
    }

    public Level5 getLevel5Code() {
        return level5Code;
    }

    public void setLevel5Code(Level5 level5Code) {
        this.level5Code = level5Code;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

}
