package nl.springbank.objects;

import nl.springbank.helper.DateHelper;

import java.util.Date;

/**
 * @author Sven Konings
 */
public class DateObject {
    private String date;

    public DateObject(Date date) {
        this.date = DateHelper.getStringFromDate(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
