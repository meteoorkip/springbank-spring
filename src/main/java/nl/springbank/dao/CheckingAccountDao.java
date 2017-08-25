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
}
