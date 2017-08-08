package nl.springbank.bean;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

/**
 * Bean representing the bank_account table.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Entity
@Table(name = "user")
public class UserBean {
    /*
     * Table values
     */
    /** User identifier. */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    /** The name of the user. */
    @Column(name = "name")
    private String name;

    /** The surname of the user. */
    @Column(name = "surname")
    private String surname;

    /** The initials of the user. */
    @Column(name = "initials")
    private String initials;

    /** The date of birth of the user. */
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    /** The BSN of the user. */
    @Column(name = "bsn", unique = true)
    private String bsn;

    /** The street address of the user. */
    @Column(name = "street_address")
    private String streetAddress;

    /** The telephone number of the user. */
    @Column(name = "telephone_number")
    private String telephoneNumber;

    /** The username of the user. */
    @Column(name = "username", unique = true)
    private String username;

    /** The password of the user. */
    @Column(name = "password")
    private String password;

    /** The email of the user. */
    @Column(name = "email")
    private String email;

    /*
     * Mapped values
     */
    /** The accounts this user is the holder of. */
    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<BankAccountBean> holderAccounts;

    /** The accounts this user has access to. */
    @ManyToMany(mappedBy = "accessUsers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<BankAccountBean> accessAccounts;

    /** The cards of the user. */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CardBean> cards;

    /*
     * Bean methods
     */
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<BankAccountBean> getHolderAccounts() {
        return holderAccounts;
    }

    public void setHolderAccounts(Set<BankAccountBean> holderAccounts) {
        this.holderAccounts = holderAccounts;
    }

    public Set<BankAccountBean> getAccessAccounts() {
        return accessAccounts;
    }

    public void setAccessAccounts(Set<BankAccountBean> accessAccounts) {
        this.accessAccounts = accessAccounts;
    }

    public Set<CardBean> getCards() {
        return cards;
    }

    public void setCards(Set<CardBean> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", initials='" + initials + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", bsn='" + bsn + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
