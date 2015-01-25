package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author nbuser
 */
@Entity
@Table(name = "client")
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
    @NamedQuery(name = "Client.findByClientName", query = "SELECT c FROM Client c WHERE c.clientPK.clientName = :clientName"),
    @NamedQuery(name = "Client.findByClientDepartmentNumber", query = "SELECT c FROM Client c WHERE c.clientPK.clientDepartmentNumber = :clientDepartmentNumber"),
    @NamedQuery(name = "Client.findByContactEmail", query = "SELECT c FROM Client c WHERE c.contactEmail = :contactEmail"),
    @NamedQuery(name = "Client.findByContactPassword", query = "SELECT c FROM Client c WHERE c.contactPassword = :contactPassword")})
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClientPK clientPK;
    @Column(name = "contact_email")
    private String contactEmail;
    @Column(name = "contact_password")
    private String contactPassword;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private Collection<Project> projectCollection;
    @JoinColumn(name = "billing_address", referencedColumnName = "address_id")
    @OneToOne(optional = false)
    private Address billingAddress;
    @OneToMany(mappedBy = "client")
    private Collection<Recruiter> recruiterCollection;

    public Client() {
    }

    public Client(ClientPK clientPK) {
        this.clientPK = clientPK;
    }

    public Client(String clientName, short clientDepartmentNumber) {
        this.clientPK = new ClientPK(clientName, clientDepartmentNumber);
    }

    public ClientPK getClientPK() {
        return clientPK;
    }

    public void setClientPK(ClientPK clientPK) {
        this.clientPK = clientPK;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPassword() {
        return contactPassword;
    }

    public void setContactPassword(String contactPassword) {
        this.contactPassword = contactPassword;
    }

    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Collection<Recruiter> getRecruiterCollection() {
        return recruiterCollection;
    }

    public void setRecruiterCollection(Collection<Recruiter> recruiterCollection) {
        this.recruiterCollection = recruiterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientPK != null ? clientPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.clientPK == null && other.clientPK != null) || (this.clientPK != null && !this.clientPK.equals(other.clientPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Client[clientPK=" + clientPK + "]";
    }

}
