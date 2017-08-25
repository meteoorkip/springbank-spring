package nl.springbank.dao;

import nl.springbank.bean.CheckingAccountBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * CheckingAccountDao. Communicates with the database and returns objects of type {@link CheckingAccountBean}.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Transactional
public interface CheckingAccountDao extends JpaRepository<CheckingAccountBean, Long> {
    /**
     * Get the checking account belonging to the given iban.
     *
     * @param iban the given iban
     * @return the checking account, or {@code null} if it doesn't exist
     */
    CheckingAccountBean findByIban(String iban);
}
