package nl.springbank.bean;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

/**
 * Bean representing the card table. A card is associated with a bank account.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Entity
@Table(name = "card", uniqueConstraints = @UniqueConstraint(columnNames = {
        "bank_account_id", "card_number"
}))
public class CardBean {
    /*
     * Table values
     */
    /** Card identifier. */
    @Id
    @Column(name = "card_id")
    @GeneratedValue(strategy = AUTO)
    private Long cardId;

    /** The bank account associated with the card. */
    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccountBean bankAccount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBean user;

    /** The card number. */
    @Column(name = "card_number")
    private String cardNumber;

    /** The pin code of the card. */
    @Column(name = "pin")
    private String pin;

    /** The expiration date of the card. */
    @Column(name = "expiration_date")
    private Date expirationDate;

    /*
     * Bean methods
     */
    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public BankAccountBean getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountBean bankAccount) {
        this.bankAccount = bankAccount;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "CardBean{" +
                "cardId=" + cardId +
                ", bankAccount=" + bankAccount +
                ", user=" + user +
                ", cardNumber='" + cardNumber + '\'' +
                ", pin='" + pin + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
