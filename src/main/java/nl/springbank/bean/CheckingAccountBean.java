package nl.springbank.bean;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

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

    /** The cards associated with the account. */
    @OneToMany(mappedBy = "checkingAccount", cascade = ALL, orphanRemoval = true)
    private Set<CardBean> cards = Collections.emptySet();

    /** The transactions with the account as the source. */
    @OneToMany(mappedBy = "sourceAccount")
    @OrderBy("date DESC")
    private SortedSet<TransactionBean> sourceTransactions = Collections.emptySortedSet();

    /** The transactions with the account as the target. */
    @OneToMany(mappedBy = "targetAccount")
    @OrderBy("date DESC")
    private SortedSet<TransactionBean> targetTransactions = Collections.emptySortedSet();

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

    @Override
    public Set<CardBean> getCards() {
        return cards;
    }

    @Override
    public void setCards(Set<CardBean> cards) {
        this.cards = cards;
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
