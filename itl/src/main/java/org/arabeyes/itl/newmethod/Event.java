package org.arabeyes.itl.newmethod;

import org.arabeyes.itl.util.Formatter;

import java.text.DateFormatSymbols;

/**
 * Holds the time of a single event (i.e., prayer or sunrise)
 */
public class Event implements Formatter.Mapper {
    int julian_day;  /* The Julian day of the event */
    int hour;        /* Hour */
    int minute;      /* Minute */

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String format(String f) {
        return Formatter.format(f, this);
    }

    @Override
    public String toString() {
        return format("HH:mm");
    }

    @Override
    public Object applyFormat(char pattern, int count) {
        int tmp;
        switch (pattern) {
            case 'a':
                return DateFormatSymbols.getInstance().getAmPmStrings()[getHour() < 12 ? 0 : 1];
            case 'H':
                return getHour();
            case 'k':
                tmp = getHour();
                return tmp == 0 ? 24 : tmp;
            case 'K':
                return getHour() % 12;
            case 'h':
                tmp = getHour() % 12;
                return tmp == 0 ? 12 : tmp;
            case 'm':
                return getMinute();
            default:
                return null;
        }
    }
}
