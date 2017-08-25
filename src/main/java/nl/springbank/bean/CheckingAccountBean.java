package nl.springbank.bean;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

/**
 * Bean representing a checking account.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Entity
@Table(name = "checking_account")
@PrimaryKeyJoinColumn(name = "account_id")
public class CheckingAccountBean extends AccountBean {
    /*
     * Table values
     */
    /** The holder associated with the account. */
    @ManyToOne
    @JoinColumn(name = "holder_user_id")
    private UserBean holder;

    /*
     * Mapped values
     */
    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "checkingAccount")
    private SavingsAccountBean savingsAccount;

    /** The users that have access to the account. */
    @ManyToMany
    @JoinTable(
            name = "access",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )
    private Set<UserBean> accessUsers = Collections.emptySet();

    /*
     * Bean methods
     */
    @Override
    public UserBean getHolder() {
        return holder;
    }

    @Override
    public void setHolder(UserBean holder) {
        this.holder = holder;
    }

    public SavingsAccountBean getSavingsAccount() {
        return savingsAccount;
    }

    public void setSavingsAccount(SavingsAccountBean savingsAccount) {
        this.savingsAccount = savingsAccount;
    }

    @Override
    public Set<UserBean> getAccessUsers() {
        return accessUsers;
    }

    @Override
    public void setAccessUsers(Set<UserBean> accessUsers) {
        this.accessUsers = accessUsers;
    }
}
