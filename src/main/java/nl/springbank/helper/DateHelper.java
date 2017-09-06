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
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final Date INITIAL_TIME = CALENDAR.getTime();

    /**
     * Get the simulatid calendar.
     *
     * @return the calendar
     */
    public static Calendar getSystemCalendar() {
        return CALENDAR;
    }

    /**
     * Get a copy of the simulated calendar.
     *
     * @return the calendar
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
        return CALENDAR.getTime();
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
     * Returns a string representation of the given date
     *
     * @param date the given date
     * @return the resulting string
     */
    public static String getStringFromDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
    }

    /**
     * Reset the date to the original time.
     */
    public static void resetDate() {
        CALENDAR.setTime(INITIAL_TIME);
    }
}
