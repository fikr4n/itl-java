package org.arabeyes.itl.prayertime;

import org.arabeyes.itl.prayertime.PrayerModule.Location;
import org.arabeyes.itl.prayertime.PrayerModule.SDate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Prayer {

    private static final Method[] METHODS_CACHE = new Method[StandardMethod.values().length];

    private final Location location;
    private Method method;
    private SDate date;
    private PrayerTime[] result;

    public Prayer() {
        this.location = new Location();
        this.location.degreeLat = Double.NaN;
    }

    public Prayer setMethod(Method method) {
        this.method = method;

        this.result = null;
        return this;
    }

    public Prayer setMethod(StandardMethod method) {
        Method m = METHODS_CACHE[method.ordinal()];
        if (m == null) {
            m = Method.fromStandard(method);
            METHODS_CACHE[method.ordinal()] = m;
        }
        return setMethod(m);
    }

    public Prayer setLocation(double lat, double lon, double seaLevel) {
        this.location.degreeLat = lat;
        this.location.degreeLong = lon;
        this.location.seaLevel = seaLevel;

        this.result = null;
        return this;
    }

    public Prayer setPressure(double pressure) {
        this.location.pressure = pressure;

        this.result = null;
        return this;
    }

    public Prayer setTemperature(double temperature) {
        this.location.temperature = temperature;

        this.result = null;
        return this;
    }

    public Prayer setDate(GregorianCalendar calendar) {
        this.date = new SDate();
        this.date.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.date.month = calendar.get(Calendar.MONTH) + 1;
        this.date.year = calendar.get(Calendar.YEAR);
        this.location.gmtDiff = calendar.get(Calendar.ZONE_OFFSET) / (1000d * 60 * 60);
        this.location.dst = Math.round(calendar.get(Calendar.DST_OFFSET) / (1000f * 60 * 60));

        result = null;
        return this;
    }

    public Prayer setDate(Date date, TimeZone timeZone) {
        GregorianCalendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(date);
        return setDate(calendar);
    }

    public PrayerTime getTime(TimeType type) {
        if (method == null || date == null || Double.isNaN(location.degreeLat))
            throw new IllegalStateException("Method, location, or date is not set");

        if (type == TimeType.IMSAAK) {
            return PrayerModule.getImsaak(location, method, date);
        } else if (type == TimeType.NEXTFAJR) {
            return PrayerModule.getNextDayFajr(location, method, date);
        } else {
            PrayerTime[] times = this.result;
            if (times == null) {
                times = PrayerModule.getPrayerTimes(location, method, date);
                this.result = times;
            }
            return times[type.ordinal()];
        }
    }

    public Dms getQibla() {
        if (Double.isNaN(this.location.degreeLat))
            throw new IllegalStateException("Location is not set");

        double r = PrayerModule.getNorthQibla(this.location);
        return Dms.fromDecimal(r);
    }
}
