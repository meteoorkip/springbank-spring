package nl.springbank.bean;

import javax.persistence.*;
import java.util.Set;
import java.util.SortedSet;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.InheritanceType.JOINED;

/**
 * Bean representing an abstract account.
 *
 * @author Sven Konings
 */
@Entity
@Table(name = "account")
@Inheritance(strategy = JOINED)
public abstract class AccountBean {
    /*
     * Table values
     */
    /** The account identifier. */
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = AUTO)
    protected Long accountId;

    /** The iban of the account */
    @Column(name = "iban", unique = true)
    protected String iban;

    /** The balance on the account. */
    @Column(name = "balance")
    protected double balance;

    /** The daily minimum balance of the account. */
    @Column(name = "minimum_balance")
    protected double minimumBalance;

    /** The built-up interest of the account. */
    @Column(name = "interest")
    protected double interest;

    /** The overdraft limit of the account. */
    @Column(name = "overdraft_limit")
    protected int overdraftLimit;

    /*
     * Bean methods
     */
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < getMinimumBalance()) {
            setMinimumBalance(balance);
        }
        this.balance = balance;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(int overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    /*
     * Abstract methods
     */
    public abstract UserBean getHolder();

    public abstract void setHolder(UserBean holder);

    public abstract Set<UserBean> getAccessUsers();

    public abstract void setAccessUsers(Set<UserBean> accessUsers);

    public abstract Set<CardBean> getCards();

    public abstract void setCards(Set<CardBean> cards);

    public abstract SortedSet<TransactionBean> getSourceTransactions();

    public abstract void setSourceTransactions(SortedSet<TransactionBean> sourceTransactions);

    public abstract SortedSet<TransactionBean> getTargetTransactions();

    public abstract void setTargetTransactions(SortedSet<TransactionBean> targetTransactions);
}
