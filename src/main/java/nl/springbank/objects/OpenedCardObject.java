package nl.springbank.objects;

import nl.springbank.bean.CardBean;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class OpenedCardObject {

    private String pinCard;
    private String pinCode;
    private String expirationDate;

    public OpenedCardObject(CardBean card) {
        this(card, true);
    }

    public OpenedCardObject(CardBean card, boolean newPin) {
        this.pinCard = card.getCardNumber();
        if (!newPin) {
            this.pinCode = card.getPin();
        }
        this.expirationDate = card.getExpirationDate().toString();
    }

    public String getPinCard() {
        return pinCard;
    }

    public void setPinCard(String pinCard) {
        this.pinCard = pinCard;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
