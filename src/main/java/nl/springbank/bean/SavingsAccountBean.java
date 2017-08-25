package nl.springbank.bean;

import javax.persistence.*;
import java.util.Set;

/**
 * Bean representing a savings account. A savings account is associated with a checking account.
 *
 * @author Sven Konings
 */
@Entity
@Table(name = "savings_account")
@PrimaryKeyJoinColumn(name = "account_id")
public class SavingsAccountBean extends AccountBean {
    /*
     * Table values
     */
    @OneToOne
    @JoinColumn(name = "checking_account_id")
    private CheckingAccountBean checkingAccount;

    /*
     * Bean methods
     */
    public CheckingAccountBean getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(CheckingAccountBean checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    @Override
    public UserBean getHolder() {
        return checkingAccount.getHolder();
    }

    @Override
    public void setHolder(UserBean holder) {
        checkingAccount.setHolder(holder);
    }

    @Override
    public Set<UserBean> getAccessUsers() {
        return checkingAccount.getAccessUsers();
    }

    @Override
    public void setAccessUsers(Set<UserBean> accessUsers) {
        checkingAccount.setAccessUsers(accessUsers);
    }
}
