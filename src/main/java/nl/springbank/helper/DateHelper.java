package nl.springbank.helper;

import nl.springbank.exceptions.InvalidParamValueError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class DateHelper {
    private static final Calendar calendar = Calendar.getInstance();

    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTime());
        return calendar;
    }

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
            return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateString);
        } catch (ParseException e) {
            throw new InvalidParamValueError(e);
        }
    }
}
