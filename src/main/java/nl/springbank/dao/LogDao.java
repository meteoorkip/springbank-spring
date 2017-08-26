package nl.springbank.dao;

import nl.springbank.bean.LogBean;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

/**
 * LogDao. Communicates with the database and returns objects of type {@link LogBean}.
 *
 * @author Sven Konings
 */
@Transactional
public interface LogDao extends JpaRepository<LogBean, Long> {
    /**
     * Get the logs between the given begin and end timestamp.
     *
     * @param begin the given begin timestamp
     * @param end   the given end timestamp
     * @return a list of logs
     */
    List<LogBean> findByTimestampBetweenOrderByTimestampAsc(Timestamp begin, Timestamp end);
}
