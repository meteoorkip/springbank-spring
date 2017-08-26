package nl.springbank.services;

import io.jsonwebtoken.lang.Assert;
import nl.springbank.bean.LogBean;
import nl.springbank.config.ApplicationConfig;
import nl.springbank.dao.LogDao;
import nl.springbank.exceptions.InvalidParamValueError;
import nl.springbank.helper.jsonrpc.JsonRpcInvocationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Service that does all operations regarding logs.
 *
 * @author Sven Konings
 */
@Service
public class LogService {

    private final LogDao logDao;

    @Autowired
    public LogService(LogDao logDao) {
        this.logDao = logDao;
        ApplicationConfig.getJsonRpcInvocationListener().setLogService(this);
    }

    /**
     * Get the log with the given log id.
     *
     * @param logId the given log id
     * @return the log
     * @throws InvalidParamValueError if an error occurred or the log doesn't exist.
     */
    public LogBean getLog(long logId) throws InvalidParamValueError {
        LogBean log;
        try {
            log = logDao.findOne(logId);
            Assert.notNull(log);
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return log;
    }

    /**
     * Get the logs between the given begin and end date.
     *
     * @param begin the given begin date
     * @param end   the given end date
     * @return the list of logs
     * @throws InvalidParamValueError if an error occurred
     */
    public List<LogBean> getLogs(Date begin, Date end) throws InvalidParamValueError {
        List<LogBean> logs;
        try {
            logs = logDao.findByTimestampBetweenOrderByTimestampAsc(Timestamp.from(begin.toInstant()), Timestamp.from(end.toInstant()));
        } catch (IllegalArgumentException e) {
            throw new InvalidParamValueError(e);
        }
        return logs;
    }

    /**
     * Get all logs.
     *
     * @return the list of logs
     */
    public List<LogBean> getLogs() {
        return logDao.findAll();
    }

    /**
     * Create a new log with the given event. the args will be formatted using {@link String#format(String, Object...)}.
     *
     * @param event the given event
     * @param args  the args for formatting
     * @return the created log
     */
    public LogBean newLog(String event, Object... args) {
        LogBean log = new LogBean();
        log.setTimestamp(Timestamp.from(Instant.now()));
        log.setEvent(String.format(event, args));
        return saveLog(log);
    }

    /**
     * Create a new error log with the given event. the args will be formatted using {@link String#format(String,
     * Object...)}.
     *
     * @param event the given event
     * @param args  the args for formatting
     * @return the created log
     */
    public LogBean newErrorLog(String event, Object... args) {
        LogBean log = new LogBean();
        log.setTimestamp(Timestamp.from(Instant.now()));
        log.setEvent("ERROR: " + String.format(event, args));
        return saveLog(log);
    }

    /**
     * Save the given log in the database.
     *
     * @param log the given log
     * @return the saved log
     */
    public LogBean saveLog(LogBean log) {
        return logDao.save(log);
    }

    /**
     * Save the given logs in the database.
     *
     * @param logs the given logs
     * @return the list of saved logs
     */
    public List<LogBean> saveLogs(Iterable<LogBean> logs) {
        return logDao.save(logs);
    }

    /**
     * Delete the log with the given id.
     *
     * @param logId the given id
     */
    public void deleteLog(long logId) {
        logDao.delete(logId);
    }

    /**
     * Delete the given log.
     *
     * @param log the given log
     */
    public void deleteLog(LogBean log) {
        logDao.delete(log);
    }

    /**
     * Delete the given logs.
     *
     * @param logs the given logs
     */
    public void deleteLogs(Iterable<LogBean> logs) {
        logDao.delete(logs);
    }

    /**
     * Delete all logs.
     */
    public void deleteLogs() {
        logDao.deleteAll();
    }
}
