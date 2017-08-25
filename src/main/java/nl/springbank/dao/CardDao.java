package nl.springbank.dao;

import nl.springbank.bean.AccountBean;
import nl.springbank.bean.CardBean;
import nl.springbank.bean.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * CardDao. Communicates with the database and returns objects of type {@link CardBean}.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Transactional
public interface CardDao extends JpaRepository<CardBean, Long> {
    /**
     * Get the card with the given account and card number.
     *
     * @param account    the given account
     * @param cardNumber the given card number
     * @return the card, or {@code null} if it doesn't exist
     */
    CardBean findByAccountAndCardNumber(AccountBean account, String cardNumber);

    /**
     * Delete the cards with the given account and user.
     *
     * @param account the given account
     * @param user    the given user
     */
    void deleteByAccountAndUser(AccountBean account, UserBean user);
}
