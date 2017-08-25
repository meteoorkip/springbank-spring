package nl.springbank.services;

import nl.springbank.bean.AccountBean;
import nl.springbank.bean.CardBean;
import nl.springbank.bean.UserBean;
import nl.springbank.dao.CardDao;
import nl.springbank.exceptions.InvalidPINError;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.exceptions.NoEffectError;
import nl.springbank.helper.CardHelper;
import nl.springbank.helper.DateHelper;
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
        checkNotExpired(card);
        return card;
    }

    /**
     * Get the card with the given account and card number.
     *
     * @param account    the given account
     * @param cardNumber the given card number
     * @return the card
     * @throws InvalidParamValueError if an error occurred or the card doesn't exist
     */
    public CardBean getCard(AccountBean account, String cardNumber) throws InvalidParamValueError {
        CardBean card;
        try {
            card = cardDao.findByAccountAndCardNumber(account, cardNumber);
            Assert.notNull(card, "Card not found");
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        checkNotExpired(card);
        return card;
    }

    /**
     * Checker whether the given card has not expired.
     *
     * @param card the given card
     * @throws InvalidParamValueError if the card has expired
     */
    private void checkNotExpired(CardBean card) throws InvalidParamValueError {
        if (DateHelper.getTime().after(card.getExpirationDate())) {
            throw new InvalidParamValueError("Card is expired");
        }
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
     * Check if the pin code of the card beloning to the given account and card number is correct.
     *
     * @param account    the given account
     * @param cardNumber the given card number
     * @param pinCode    the pin code
     * @throws InvalidParamValueError if the account-card number combination is incorrect
     * @throws InvalidPINError        if the pin code is incorrect
     */
    public void checkPin(AccountBean account, String cardNumber, String pinCode) throws InvalidParamValueError, InvalidPINError {
        checkPin(getCard(account, cardNumber), pinCode);
    }

    /**
     * Check if the pin code of the given card is correct.
     *
     * @param card    the given card
     * @param pinCode the pin code
     * @throws InvalidPINError if the pin code is incorrect
     */
    public void checkPin(CardBean card, String pinCode) throws InvalidPINError {
        if (card.getAttempts() >= 3) {
            throw new InvalidPINError("The card is blocked");
        } else if (!card.getPin().equals(pinCode)) {
            card.setAttempts(card.getAttempts() + 1);
            saveCard(card);
            throw new InvalidPINError("Invalid pin code");
        } else if (card.getAttempts() > 0) {
            card.setAttempts(0);
            saveCard(card);
        }
    }

    /**
     * Create a new card with the given account and user.
     *
     * @param account the given account
     * @param user    the given user
     * @return the created card
     */
    public CardBean newCard(AccountBean account, UserBean user) {
        return newCard(account, user, CardHelper.getRandomPin());
    }

    /**
     * Create a new card with the given account, user and pin.
     *
     * @param account the given account
     * @param user    the given user
     * @param pin     the given pin
     * @return the created card
     */
    public synchronized CardBean newCard(AccountBean account, UserBean user, String pin) {
        CardBean card = new CardBean();
        card.setAccount(account);
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
            return newCard(card.getAccount(), card.getUser());
        } else {
            return newCard(card.getAccount(), card.getUser(), card.getPin());
        }
    }

    /**
     * Unblock the given card.
     *
     * @param card the given card.
     * @throws NoEffectError if the card is already unblocked
     */
    public void unblockCard(CardBean card) throws NoEffectError {
        if (card.getAttempts() < 3) {
            throw new NoEffectError("The card is already unblocked");
        }
        card.setAttempts(0);
        saveCard(card);
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
     * Delete the card belonging to the given account and user.
     *
     * @param account the given account
     * @param user    the given user
     */
    public void deleteCard(AccountBean account, UserBean user) {
        cardDao.deleteByAccountAndUser(account, user);
    }

    /**
     * Delete the given cards
     *
     * @param cards the given cards
     */
    public void deleteCards(Iterable<CardBean> cards) {
        cardDao.delete(cards);
    }

    /**
     * Delete all cards.
     */
    public void deleteCards() {
        cardDao.deleteAll();
    }
}
