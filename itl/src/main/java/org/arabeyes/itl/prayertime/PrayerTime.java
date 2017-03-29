package org.arabeyes.itl.prayertime;

/**
 * This structure holds the prayer time output for a single prayer.
 */
public class PrayerTime {
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

    public boolean isExtreme() {
        return isExtreme != 0;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d%s", hour, minute, second, isExtreme != 0 ?
                " (extreme)" : "");
    }

    // TODO: 2017-03-29 format(String) or toDate()
}
