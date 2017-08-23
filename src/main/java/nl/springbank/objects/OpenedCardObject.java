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

    public OpenedCardObject(CardBean cardBean) {
        this(cardBean, true);
    }

    public OpenedCardObject(CardBean cardBean, boolean newPin) {
        this.pinCard = cardBean.getCardNumber();
        if (!newPin) {
            this.pinCode = cardBean.getPin();
        }
        this.expirationDate = cardBean.getExpirationDate().toString();
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
