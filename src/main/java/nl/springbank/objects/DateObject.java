package nl.springbank.objects;

import java.util.Date;

/**
 * @author Sven Konings
 */
public class DateObject {
    private String date;

    public DateObject(Date date) {
        this.date = date.toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
