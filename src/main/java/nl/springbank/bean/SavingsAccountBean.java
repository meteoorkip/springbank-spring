package nl.springbank.bean;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import static javax.persistence.CascadeType.ALL;

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
     * Mapped values
     */
    /** The transactions with this bank account as the source. */
    @OneToMany(mappedBy = "sourceAccount", cascade = ALL)
    @OrderBy("date DESC")
    private SortedSet<TransactionBean> sourceTransactions = Collections.emptySortedSet();

    /** The transactions with this bank account as target. */
    @OneToMany(mappedBy = "targetAccount", cascade = ALL)
    @OrderBy("date DESC")
    private SortedSet<TransactionBean> targetTransactions = Collections.emptySortedSet();

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

    @Override
    public Set<CardBean> getCards() {
        return checkingAccount.getCards();
    }

    @Override
    public void setCards(Set<CardBean> cards) {
        checkingAccount.setCards(cards);
    }

    @Override
    public SortedSet<TransactionBean> getSourceTransactions() {
        return sourceTransactions;
    }

    @Override
    public void setSourceTransactions(SortedSet<TransactionBean> sourceTransactions) {
        this.sourceTransactions = sourceTransactions;
    }

    @Override
    public SortedSet<TransactionBean> getTargetTransactions() {
        return targetTransactions;
    }

    @Override
    public void setTargetTransactions(SortedSet<TransactionBean> targetTransactions) {
        this.targetTransactions = targetTransactions;
    }
}
