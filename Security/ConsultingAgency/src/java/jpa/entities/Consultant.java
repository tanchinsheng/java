package jpa.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nbuser
 */
@Entity
@Table(name = "consultant")
@NamedQueries({
    @NamedQuery(name = "Consultant.findAll", query = "SELECT c FROM Consultant c"),
    @NamedQuery(name = "Consultant.findByConsultantId", query = "SELECT c FROM Consultant c WHERE c.consultantId = :consultantId"),
    @NamedQuery(name = "Consultant.findByEmail", query = "SELECT c FROM Consultant c WHERE c.email = :email"),
    @NamedQuery(name = "Consultant.findByPassword", query = "SELECT c FROM Consultant c WHERE c.password = :password"),
    @NamedQuery(name = "Consultant.findByHourlyRate", query = "SELECT c FROM Consultant c WHERE c.hourlyRate = :hourlyRate"),
    @NamedQuery(name = "Consultant.findByBillableHourlyRate", query = "SELECT c FROM Consultant c WHERE c.billableHourlyRate = :billableHourlyRate"),
    @NamedQuery(name = "Consultant.findByHireDate", query = "SELECT c FROM Consultant c WHERE c.hireDate = :hireDate")})
public class Consultant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "consultant_id")
    private Integer consultantId;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;
    @Basic(optional = false)
    @Column(name = "billable_hourly_rate")
    private BigDecimal billableHourlyRate;
    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    private Date hireDate;
    @Lob
    @Column(name = "resume")
    private String resume;
    @JoinTable(name = "project_consultant", joinColumns = {
        @JoinColumn(name = "consultant_id", referencedColumnName = "consultant_id")}, inverseJoinColumns = {
        @JoinColumn(name = "client_name", referencedColumnName = "client_name"),
        @JoinColumn(name = "client_department_number", referencedColumnName = "client_department_number"),
        @JoinColumn(name = "project_name", referencedColumnName = "project_name")})
    @ManyToMany
    private Collection<Project> projectCollection;
    @JoinColumn(name = "recruiter_id", referencedColumnName = "recruiter_id")
    @ManyToOne
    private Recruiter recruiterId;
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(optional = false)
    private ConsultantStatus statusId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consultantId")
    private Collection<Billable> billableCollection;

    public Consultant() {
    }

    public Consultant(Integer consultantId) {
        this.consultantId = consultantId;
    }

    public Consultant(Integer consultantId, String email, String password, BigDecimal hourlyRate, BigDecimal billableHourlyRate) {
        this.consultantId = consultantId;
        this.email = email;
        this.password = password;
        this.hourlyRate = hourlyRate;
        this.billableHourlyRate = billableHourlyRate;
    }

    public Integer getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(Integer consultantId) {
        this.consultantId = consultantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getBillableHourlyRate() {
        return billableHourlyRate;
    }

    public void setBillableHourlyRate(BigDecimal billableHourlyRate) {
        this.billableHourlyRate = billableHourlyRate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }

    public Recruiter getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Recruiter recruiterId) {
        this.recruiterId = recruiterId;
    }

    public ConsultantStatus getStatusId() {
        return statusId;
    }

    public void setStatusId(ConsultantStatus statusId) {
        this.statusId = statusId;
    }

    public Collection<Billable> getBillableCollection() {
        return billableCollection;
    }

    public void setBillableCollection(Collection<Billable> billableCollection) {
        this.billableCollection = billableCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consultantId != null ? consultantId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consultant)) {
            return false;
        }
        Consultant other = (Consultant) object;
        if ((this.consultantId == null && other.consultantId != null) || (this.consultantId != null && !this.consultantId.equals(other.consultantId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Consultant[consultantId=" + consultantId + "]";
    }

}
