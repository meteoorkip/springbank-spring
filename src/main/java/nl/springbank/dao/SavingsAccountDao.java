package nl.springbank.dao;

import nl.springbank.bean.SavingsAccountBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * SavingsAccountDao. Communicates with the database and returns objects of type {@link SavingsAccountBean}.
 *
 * @author Sven Konings
 */
@Transactional
public interface SavingsAccountDao extends JpaRepository<SavingsAccountBean, Long> {
    /**
     * Get the savings account belonging to the given iban.
     *
     * @param iban the given iban
     * @return the savings account, or {@code null} if it doesn't exist
     */
    SavingsAccountBean findByIban(String iban);
}
