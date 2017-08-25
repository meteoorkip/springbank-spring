package nl.springbank.dao;

import nl.springbank.bean.AccountBean;
import nl.springbank.bean.TransactionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TransactionDao. Communicates with the database and returns objects of type {@link TransactionBean}.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
@Transactional
public interface TransactionDao extends JpaRepository<TransactionBean, Long> {
    /**
     * Get the transaction with the given source or target account.
     *
     * @param sourceAccount the given source account
     * @param targetAccount the given target account
     * @return the list of transactions
     */
    List<TransactionBean> findBySourceAccountOrTargetAccountOrderByDateDesc(AccountBean sourceAccount, AccountBean targetAccount);

    /**
     * Get the transaction with the given source and target account.
     *
     * @param sourceAccount the given source account
     * @param targetAccount the given target account
     * @return the list of transactions
     */
    List<TransactionBean> findBySourceAccountAndTargetAccountOrderByDateDesc(AccountBean sourceAccount, AccountBean targetAccount);
}
