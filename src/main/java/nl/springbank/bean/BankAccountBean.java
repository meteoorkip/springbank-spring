package nl.springbank.bean;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

/**
 * Bean representing the bank_account table. A bank account is associated with a holder.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Entity
@Table(name = "bank_account")
public class BankAccountBean {
    /*
     * Table values
     */
    /** Account identifier. */
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = AUTO)
    private Long bankAccountId;

    /** The holder associated with the account. */
    @ManyToOne
    @JoinColumn(name = "holder_user_id")
    private UserBean holder;

    /** The balance on the account. */
    @Column(name = "balance")
    private Double balance;

    /** The daily minimum balance of the account. */
    @Column(name = "minimum_balance")
    private Double minimumBalance;

    /** The built-up interest of the account. Whitdrawn each month */
    @Column(name = "interest")
    private Double interest;

    /** The overdraft limit of the account. */
    @Column(name = "overdraft_limit")
    private Integer overdraftLimit;

    /*
     * Mapped values
     */
    /** The iban associated with this bank account. */
    @OneToOne(mappedBy = "bankAccount", cascade = ALL, orphanRemoval = true)
    private IbanBean iban;

    /** The users that have access to this bank account. */
    @ManyToMany
    @JoinTable(
            name = "user_bank_account",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )
    private Set<UserBean> accessUsers = Collections.emptySet();

    /** The cards associated with this bank account. */
    @OneToMany(mappedBy = "bankAccount", cascade = ALL, orphanRemoval = true)
    private Set<CardBean> cards = Collections.emptySet();

    /** The transactions with this bank account as the source. */
    @OneToMany(mappedBy = "sourceBankAccount", cascade = ALL)
    @OrderBy("date DESC")
    private SortedSet<TransactionBean> sourceTransactions = Collections.emptySortedSet();

    /** The transactions with this bank account as target. */
    @OneToMany(mappedBy = "targetBankAccount", cascade = ALL)
    @OrderBy("date DESC")
    private SortedSet<TransactionBean> targetTransactions = Collections.emptySortedSet();

    /*
     * Bean methods
     */
    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public UserBean getHolder() {
        return holder;
    }

    public void setHolder(UserBean holder) {
        this.holder = holder;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        if (balance < getMinimumBalance()) {
            setMinimumBalance(balance);
        }
        this.balance = balance;
    }

    public Double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Integer getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(Integer overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public IbanBean getIban() {
        return iban;
    }

    public void setIban(IbanBean iban) {
        this.iban = iban;
    }

    public Set<UserBean> getAccessUsers() {
        return accessUsers;
    }

    public void setAccessUsers(Set<UserBean> accessUsers) {
        this.accessUsers = accessUsers;
    }

    public Set<CardBean> getCards() {
        return cards;
    }

    public void setCards(Set<CardBean> cards) {
        this.cards = cards;
    }

    public SortedSet<TransactionBean> getSourceTransactions() {
        return sourceTransactions;
    }

    public void setSourceTransactions(SortedSet<TransactionBean> sourceTransactions) {
        this.sourceTransactions = sourceTransactions;
    }

    public SortedSet<TransactionBean> getTargetTransactions() {
        return targetTransactions;
    }

    public void setTargetTransactions(SortedSet<TransactionBean> targetTransactions) {
        this.targetTransactions = targetTransactions;
    }

    @Override
    public String toString() {
        return "BankAccountBean{" +
                "bankAccountId=" + bankAccountId +
                ", holder=" + holder +
                ", balance=" + balance +
                ", minimumBalance=" + minimumBalance +
                ", interest=" + interest +
                ", overdraftLimit=" + overdraftLimit +
                '}';
    }
}
