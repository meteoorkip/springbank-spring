package nl.springbank.helper;

/**
 * @author Tristan de Boer.
 */
public class OpenAccountObject {
    private String name;
    private String surname;
    private String initials;
    private String dob;
    private String ssn;
    private String address;
    private String telephoneNumber;
    private String email;
    private String username;
    private String password;

    public OpenAccountObject(String name, String surname, String initials, String dob, String ssn, String address, String telephoneNumber, String email, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.initials = initials;
        this.dob = dob;
        this.ssn = ssn;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
