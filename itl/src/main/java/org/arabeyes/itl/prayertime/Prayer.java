/* Copyright (c) 2017, Fikrul Arif
 * (under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.prayertime;

import org.arabeyes.itl.prayertime.PrayerModule.Location;
import org.arabeyes.itl.prayertime.PrayerModule.SDate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.TimeZone;

public class Prayer {

    private static final Method[] METHODS_CACHE = new Method[StandardMethod.values().length];

    private final Location location;
    private Method method;
    private SDate date;

    public Prayer() {
        this.location = new Location();
        this.location.degreeLat = Double.NaN;
    }

    /**
     * @return self, for chaining
     */
    @SuppressWarnings("WeakerAccess")
    public Prayer setMethod(Method method) {
        this.method = method;

        return this;
    }

    /**
     * Call this if using standard method without any modification.
     *
     * @return self, for chaining
     */
    public Prayer setMethod(StandardMethod method) {
        Method m = METHODS_CACHE[method.ordinal()];
        if (m == null) {
            m = Method.fromStandard(method);
            METHODS_CACHE[method.ordinal()] = m;
        }
        return setMethod(m);
    }

    /**
     * @param lat      latitude in decimal degrees
     * @param lon      longitude in decimal degrees
     * @param seaLevel height (altitude/elevation) above sea level in meters
     * @return self, for chaining
     */
    public Prayer setLocation(double lat, double lon, double seaLevel) {
        this.location.degreeLat = lat;
        this.location.degreeLong = lon;
        this.location.seaLevel = seaLevel;

        return this;
    }

    /**
     * @param pressure atmospheric pressure in millibars (the astronomical standard value is 1010)
     * @return self, for chaining
     */
    public Prayer setPressure(double pressure) {
        this.location.pressure = pressure;

        return this;
    }

    /**
     * @param temperature temperature in celsius degree (the astronomical standard value is 10)
     * @return self, for chaining
     */
    public Prayer setTemperature(double temperature) {
        this.location.temperature = temperature;

        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public Prayer setDate(GregorianCalendar calendar) {
        this.date = new SDate();
        this.date.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.date.month = calendar.get(Calendar.MONTH) + 1;
        this.date.year = calendar.get(Calendar.YEAR);
        this.location.gmtDiff = calendar.get(Calendar.ZONE_OFFSET) / (1000d * 60 * 60);
        this.location.dst = Math.round(calendar.get(Calendar.DST_OFFSET) / (1000f * 60 * 60));

        return this;
    }

    public Prayer setDate(Date date, TimeZone timeZone) {
        GregorianCalendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(date);
        return setDate(calendar);
    }

    private void checkConfig() {
        if (method == null || date == null || Double.isNaN(location.degreeLat))
            throw new IllegalStateException("Method, location, or date is not set");
    }

    /**
     * Calculate prayer times and Shurooq (sunrise). Location, date, and method must be set before
     * calling this method.
     * @return time of Fajr, Shurooq, Zuhr, Assr, Maghrib, Ishaa respectively
     */
    @SuppressWarnings("WeakerAccess")
    public PrayerTime[] getPrayerTimeArray() {
        checkConfig();
        return PrayerModule.getPrayerTimes(location, method, date);
    }

    /**
     * Calculate prayer times and Shurooq (sunrise).
     * @return time of Fajr, Shurooq, Zuhr, Assr, Maghrib, Ishaa respectively
     */
    public LinkedHashMap<TimeType, PrayerTime> getPrayerTimes() {
        PrayerTime[] array = getPrayerTimeArray();
        LinkedHashMap<TimeType, PrayerTime> result = new LinkedHashMap<TimeType, PrayerTime>(6);
        TimeType[] types = TimeType.values();
        for (int i = 0; i < 6; ++i) {
            result.put(types[i], array[i]);
        }
        return result;
    }

    public PrayerTime getImsaak() {
        checkConfig();
        return PrayerModule.getImsaak(location, method, date);
    }

    public PrayerTime getNextDayFajr() {
        checkConfig();
        return PrayerModule.getNextDayFajr(location, method, date);
    }

    public PrayerTime getNextDayImsaak() {
        checkConfig();
        return PrayerModule.getNextDayImsaak(location, method, date);
    }

    /**
     * Calculate Qibla direction. To call this method, only the location is the prerequisite.
     * @return degrees from North CCW
     */
    public Dms getNorthQibla() {
        if (Double.isNaN(this.location.degreeLat))
            throw new IllegalStateException("Location is not set");

        double r = PrayerModule.getNorthQibla(this.location);
        return Dms.fromDecimal(r);
    }
}
