package nl.springbank.bean;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

/**
 * Bean representing the transaction table. A transaction is associated with a source and a target bank account.
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

    /** The source bank account. Is {@code null} for a deposit or when the source account has been closed. */
    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private BankAccountBean sourceBankAccount;

    /** The target bank account. Is {@code null} if the target account has been closed. */
    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private BankAccountBean targetBankAccount;

    /** The target name of the transaction. Is {@code null} for a deposit. */
    @Column(name = "target_name")
    private String targetName;

    /** The date of the transaction */
    @Column(name = "date")
    private Date date;

    /** The amount of the transaction */
    @Column(name = "amount")
    private Double amount;

    /** The message of the transaction. Is {@code null} for a deposit. */
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

    public BankAccountBean getSourceBankAccount() {
        return sourceBankAccount;
    }

    public void setSourceBankAccount(BankAccountBean sourceBankAccount) {
        this.sourceBankAccount = sourceBankAccount;
    }

    public BankAccountBean getTargetBankAccount() {
        return targetBankAccount;
    }

    public void setTargetBankAccount(BankAccountBean targetBankAccount) {
        this.targetBankAccount = targetBankAccount;
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
    public String toString() {
        return "TransactionBean{" +
                "transactionId=" + transactionId +
                ", sourceBankAccount=" + sourceBankAccount +
                ", targetBankAccount=" + targetBankAccount +
                ", targetName='" + targetName + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", message='" + message + '\'' +
                '}';
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
