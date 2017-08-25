package nl.springbank.bean;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

/**
 * Bean representing a card.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Entity
@Table(name = "card", uniqueConstraints = @UniqueConstraint(columnNames = {
        "account_id", "card_number"
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

    /** The account associated with the card. */
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountBean account;

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

    /** The number of incorrect attempts. */
    @Column(name = "attempts")
    private int attempts;

    /*
     * Bean methods
     */
    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
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

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
