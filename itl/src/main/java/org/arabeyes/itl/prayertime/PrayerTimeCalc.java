package org.arabeyes.itl.prayertime;

import org.arabeyes.itl.prayertime.PrayerModule.Location;
import org.arabeyes.itl.prayertime.PrayerModule.SimpleDate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class PrayerTimeCalc {

    private static final Method[] METHODS_CACHE = new Method[StandardMethod.values().length];

    private final Location location;
    private Method method;
    private SimpleDate date;
    private PrayerTime[] result;

    public PrayerTimeCalc() {
        this.location = new Location();
        this.location.degreeLat = Double.NaN;
    }

    public PrayerTimeCalc setMethod(Method method) {
        this.method = method;

        this.result = null;
        return this;
    }

    public PrayerTimeCalc setMethod(StandardMethod method) {
        Method m = METHODS_CACHE[method.ordinal()];
        if (m == null) {
            m = Method.fromStandard(method);
            METHODS_CACHE[method.ordinal()] = m;
        }
        return setMethod(m);
    }

    public PrayerTimeCalc setLocation(double lat, double lon, double seaLevel) {
        this.location.degreeLat = lat;
        this.location.degreeLong = lon;
        this.location.seaLevel = seaLevel;

        this.result = null;
        return this;
    }

    public PrayerTimeCalc setPressure(double pressure) {
        this.location.pressure = pressure;

        this.result = null;
        return this;
    }

    public PrayerTimeCalc setTemperature(double temperature) {
        this.location.temperature = temperature;

        this.result = null;
        return this;
    }

    public PrayerTimeCalc setDate(GregorianCalendar calendar) {
        this.date = new SimpleDate();
        this.date.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.date.month = calendar.get(Calendar.MONTH) + 1;
        this.date.year = calendar.get(Calendar.YEAR);
        this.location.gmtDiff = calendar.get(Calendar.ZONE_OFFSET) / (1000 * 60 * 60);
        this.location.dst = calendar.get(Calendar.DST_OFFSET) / (1000 * 60 * 60);

        result = null;
        return this;
    }

    public PrayerTimeCalc setDate(Date date, TimeZone timeZone) {
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
