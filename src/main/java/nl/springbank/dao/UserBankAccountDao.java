package nl.springbank.dao;

import nl.springbank.bean.UserBankAccountBean;
import org.springframework.data.repository.CrudRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserBankAccountDao. Communicates with the database and returns objects of type
 * <code>nl.springbank.bean.UserBankAccountBean</code>
 *
 * @author Tristan de Boer.
 */
@Transactional
public interface UserBankAccountDao extends CrudRepository<UserBankAccountBean, Long> {
    Iterable<UserBankAccountBean> findByBankAccountId(long bankAccountId);

    void deleteByUserIdAndBankAccountId(long userId, long bankAccountId);
}
