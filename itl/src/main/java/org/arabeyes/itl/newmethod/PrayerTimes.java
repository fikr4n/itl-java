package org.arabeyes.itl.newmethod;

/**
 * Holds all the prayers/events times of a given day
 */
public class PrayerTimes {
    Event fajr = new Event();
    Event sunrise = new Event();
    Event dhuhr = new Event();
    Event asr = new Event();
    Event maghrib = new Event();
    Event isha = new Event();

    public Event getFajr() {
        return fajr;
    }

    public Event getSunrise() {
        return sunrise;
    }

    public Event getDhuhr() {
        return dhuhr;
    }

    public Event getAsr() {
        return asr;
    }

    public Event getMaghrib() {
        return maghrib;
    }

    public Event getIsha() {
        return isha;
    }
}
