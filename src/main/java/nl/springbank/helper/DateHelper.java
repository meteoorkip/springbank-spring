package nl.springbank.helper;

import nl.springbank.exceptions.InvalidParamValueError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class DateHelper {
    private static final Calendar calendar = Calendar.getInstance();
    private static final Date initialTime = calendar.getTime();

    /**
     * Get the simulated calendar date.
     *
     * @return the calendar date
     */
    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTime());
        return calendar;
    }

    /**
     * Get the simulated date.
     *
     * @return the date
     */
    public static Date getTime() {
        return calendar.getTime();
    }

    /**
     * Parse the given string to a date.
     *
     * @param dateString the given string
     * @return the resulting date
     * @throws InvalidParamValueError if the string couldn't be parsed
     */
    public static Date getDateFromString(String dateString) throws InvalidParamValueError {
        try {
            return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(dateString);
        } catch (ParseException e) {
            throw new InvalidParamValueError(e);
        }
    }

    /**
     * Add the given number of days to the date.
     *
     * @param nrOfDays the given number of days
     * @throws InvalidParamValueError if the number of days is negative
     */
    public static void addDays(int nrOfDays) throws InvalidParamValueError {
        if (nrOfDays < 0) {
            throw new InvalidParamValueError("The number of days can't be negative");
        }
        calendar.add(Calendar.DATE, nrOfDays);
    }

    /**
     * Reset the date to the original time.
     */
    public static void resetDate() {
        calendar.setTime(initialTime);
    }
}
