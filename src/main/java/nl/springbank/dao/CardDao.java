package nl.springbank.dao;

import nl.springbank.bean.CardBean;
import org.springframework.data.repository.CrudRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CardDao. Communicates with the database and returns objects of type <code>nl.springbank.bean.CardBean</code>
 *
 * @author Tristan de Boer.
 */
@Transactional
public interface CardDao extends CrudRepository<CardBean, Long> {
    Iterable<CardBean> findByBankAccountId(long bankAccountId);

    void deleteByUserIdAndBankAccountId(long userId, long bankAccountId);
}
