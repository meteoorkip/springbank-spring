package nl.springbank.services;

import nl.springbank.bean.BankAccountBean;
import nl.springbank.bean.CardBean;
import nl.springbank.bean.UserBean;
import nl.springbank.dao.CardDao;
import nl.springbank.exceptions.InvalidPINError;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.helper.CardHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Service that does all operations regarding Cards.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Service
public class CardService {

    private final CardDao cardDao;

    @Autowired
    public CardService(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    /**
     * Get the card with the given card id.
     *
     * @param cardId the given card id
     * @return the card
     * @throws InvalidParamValueError if an error occurred or the card doesn't exist
     */
    public CardBean getCard(long cardId) throws InvalidParamValueError {
        CardBean card;
        try {
            card = cardDao.findOne(cardId);
            Assert.notNull(card, "Card not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return card;
    }

    /**
     * Get the card with the given bank account and card number.
     *
     * @param bankAccount the given bank account
     * @param cardNumber  the given card number
     * @return the card
     * @throws InvalidParamValueError if an error occurred or the card doesn't exist
     */
    public CardBean getCard(BankAccountBean bankAccount, String cardNumber) throws InvalidParamValueError {
        CardBean card;
        try {
            card = cardDao.findByBankAccountAndCardNumber(bankAccount, cardNumber);
            Assert.notNull(card, "Card not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return card;
    }

    /**
     * Get all cards
     *
     * @return a list of all cards
     */
    public List<CardBean> getCards() {
        return cardDao.findAll();
    }

    /**
     * Check if the pin code of the card beloning to the given bank account and card number is correct.
     *
     * @param bankAccount the given bank account
     * @param cardNumber  the given card number
     * @param pinCode     the pin code
     * @throws InvalidParamValueError if the bank account-card number combination is incorrect
     * @throws InvalidPINError        if the pin code is incorrect
     */
    public void checkPin(BankAccountBean bankAccount, String cardNumber, String pinCode) throws InvalidParamValueError, InvalidPINError {
        checkPin(getCard(bankAccount, cardNumber), pinCode);
    }

    /**
     * Check if the pin code of the given card is correct.
     *
     * @param cardBean the given card
     * @param pinCode  the pin code
     * @throws InvalidPINError if the pin code is incorrect
     */
    public void checkPin(CardBean cardBean, String pinCode) throws InvalidPINError {
        if (!cardBean.getPin().equals(pinCode)) {
            throw new InvalidPINError("Invalid pin code");
        }
    }

    /**
     * Create a new card with the given bank account and user.
     *
     * @param bankAccount the given bank account
     * @param user        the given user
     * @return the created card
     */
    public CardBean newCard(BankAccountBean bankAccount, UserBean user) {
        return newCard(bankAccount, user, CardHelper.getRandomPin());
    }

    /**
     * Create a new card with the given bank account, user and pin.
     *
     * @param bankAccount the given bank account
     * @param user        the given user
     * @param pin         the given pin
     * @return the created card
     */
    public synchronized CardBean newCard(BankAccountBean bankAccount, UserBean user, String pin) {
        CardBean card = new CardBean();
        card.setBankAccount(bankAccount);
        card.setUser(user);
        card.setCardNumber(CardHelper.getRandomCardNumber(getCards()));
        card.setPin(pin);
        card.setExpirationDate(CardHelper.getExpirationDate());
        return saveCard(card);
    }

    /**
     * Invalidates the given card and creates a new one.
     *
     * @param card   the given card
     * @param newPin whether the pin should be changed
     * @return the new card
     */
    public synchronized CardBean invalidateCard(CardBean card, boolean newPin) {
        deleteCard(card);
        if (newPin) {
            return newCard(card.getBankAccount(), card.getUser());
        } else {
            return newCard(card.getBankAccount(), card.getUser(), card.getPin());
        }
    }

    /**
     * Save the given card in the database.
     *
     * @param card the given card
     * @return the saved card
     */
    public CardBean saveCard(CardBean card) {
        return cardDao.save(card);
    }

    /**
     * Save the given cards in the database.
     *
     * @param cards the given cards
     * @return the list of saved cards
     */
    public List<CardBean> saveCards(Iterable<CardBean> cards) {
        return cardDao.save(cards);
    }

    /**
     * Delete the card with the given id.
     *
     * @param cardId the given id
     */
    public void deleteCard(long cardId) {
        cardDao.delete(cardId);
    }

    /**
     * Delete the given card.
     *
     * @param card the given card
     */
    public void deleteCard(CardBean card) {
        cardDao.delete(card);
    }

    /**
     * Delete the card belonging to the given bank account and user.
     *
     * @param bankAccount the given bank account
     * @param user        the given user
     */
    public void deleteCard(BankAccountBean bankAccount, UserBean user) {
        cardDao.deleteByBankAccountAndUser(bankAccount, user);
    }

    /**
     * Delete the given cards
     *
     * @param cards the given cards
     */
    public void deleteCards(Iterable<CardBean> cards) {
        cardDao.delete(cards);
    }
}
