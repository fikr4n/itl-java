/*  Copyright (c) 2003-2006, 2009-2010 Arabeyes, Thamer Mahmoud, Fikrul Arif
 * (www.arabeyes.org - under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.prayertime;

import org.arabeyes.itl.util.Formatter;

import java.text.DateFormatSymbols;

/**
 * This structure holds the prayer time output for a single prayer.
 */
@SuppressWarnings("WeakerAccess")
public class PrayerTime implements Formatter.Mapper, Comparable<PrayerTime> {
    int hour;
    int minute;
    int second;

    /**
     * Extreme calculation status. The 'getPrayerTimes'
     * function sets this variable to 1 to indicate that
     * this particular prayer time has been calculated
     * through extreme latitude methods and NOT by
     * conventional means of calculation.
     */
    byte isExtreme;

    PrayerTime() {
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    /**
     * Whether this particular prayer time has been calculated
     * through extreme latitude methods and NOT by
     * conventional means of calculation.
     *
     * @return true if extreme latitude methods is used
     */
    public boolean isExtreme() {
        return isExtreme != 0;
    }

    /**
     * Format time. The format is subset of {@link java.text.SimpleDateFormat} format
     * pattern. Use single quote " ' " for escaping and double single-quote " '' " for single quote.
     * <ul>
     * <li>a - am/pm (text, based on current locale)
     * <li>H - hour (number, 0-23)
     * <li>k - hour (number, 1-24)
     * <li>K - hour (number, 0-11)
     * <li>h - hour (number, 1-12)
     * <li>m - minute (number)
     * <li>s - second (number)
     * </ul>
     * Notes:
     * <ul>
     * <li>number: will be zero padded based on number of characters
     * <li>character other than 'a'-'z', 'A'-'Z', and single quote will be formatted as is
     * </ul>
     *
     * @param f format pattern, must not be null
     * @return formatted time
     * @throws IllegalArgumentException if contains unquoted character 'a'-'z' or 'A'-'Z' other than
     *                                  format patterns above
     */
    public String format(String f) {
        return Formatter.format(f, this);
    }

    /**
     * Returns string representation of this time in "HH:mm:ss [(extreme)]" format.
     */
    @Override
    public String toString() {
        return format("HH:mm:ss") + (isExtreme != 0 ? " (extreme)" : "");
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
            case 's':
                return getSecond();
            default:
                return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrayerTime that = (PrayerTime) o;
        return hour == that.hour && minute == that.minute && second == that.second &&
                isExtreme == that.isExtreme;
    }

    @Override
    public int hashCode() {
        int result = hour;
        result = 31 * result + minute;
        result = 31 * result + second;
        result = 31 * result + (int) isExtreme;
        return result;
    }

    /**
     * See {@link Comparable#compareTo(Object)}. Extreme status ({@link #isExtreme()}) is compared
     * last.
     *
     * @throws ClassCastException if different class
     */
    @Override
    public int compareTo(PrayerTime o) {
        if (getClass() != o.getClass())
            throw new ClassCastException("Can't compare with instance of " + o.getClass());

        if (hour != o.hour) return hour - o.hour;
        if (minute != o.minute) return minute - o.minute;
        if (second != o.second) return second - o.second;
        return isExtreme - o.isExtreme;
    }
}
