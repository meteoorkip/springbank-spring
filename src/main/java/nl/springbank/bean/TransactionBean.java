package nl.springbank.bean;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

/**
 * Bean representing a transaction.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Entity
@Table(name = "transaction")
public class TransactionBean implements Comparable<TransactionBean> {
    /*
     * Table values
     */
    /** Transaction identifier. */
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = AUTO)
    private Long transactionId;

    /** The source account. */
    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private AccountBean sourceAccount;

    /** The target account. */
    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private AccountBean targetAccount;

    /** The target name of the transaction. */
    @Column(name = "target_name")
    private String targetName;

    /** The date of the transaction */
    @Column(name = "date")
    private Date date;

    /** The amount of the transaction */
    @Column(name = "amount")
    private Double amount;

    /** The message of the transaction. */
    @Column(name = "message")
    private String message;

    /*
     * Bean methods
     */
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public AccountBean getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(AccountBean sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public AccountBean getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(AccountBean targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int compareTo(TransactionBean that) {
        if (this.getDate() == null || that.getDate() == null) {
            return 0;
        }
        // Reverse date ordering
        return that.getDate().compareTo(this.getDate());
    }
}
