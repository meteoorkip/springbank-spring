package nl.springbank.dao;

import nl.springbank.bean.AccountBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * AccountDao. Communicates with the database and returns objects of type {@link AccountBean}.
 *
 * @author Sven Konings
 */
@Transactional
public interface AccountDao extends JpaRepository<AccountBean, Long> {
    /**
     * Get the account belonging to the given iban.
     *
     * @param iban the given iban
     * @return the account, or {@code null} if it doesn't exist
     */
    AccountBean findByIban(String iban);
}
