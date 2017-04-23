package org.arabeyes.itl.newmethod;

import org.arabeyes.itl.prayertime.Dms;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Prayer {
    private DefsModule.date_t date;
    private DefsModule.location location;

    public Prayer() {
        location = new DefsModule.location();
        location.latitude = Double.NaN;
        location.longitude = Double.NaN;
        location.extr_method = ExtremeMethod.NONE;
        location.asr_method = AsrMethod.SHAFII;
    }

    private void requireLocation() {
        if (Double.isNaN(location.latitude) || Double.isNaN(location.longitude))
            throw new IllegalStateException("Location is not set");
    }

    private void requireMethods() {
        if (location.asr_method == null || location.calc_method == null ||
                location.extr_method == null)
            throw new IllegalStateException("Method is not set");
    }

    private void requireDate() {
        if (date == null)
            throw new IllegalStateException("Date is not set");
    }

    public Prayer setCalculationMethod(CalcMethod method) {
        this.location.calc_method = method;
        return this;
    }

    public Prayer setCalculationMethod(MethodId method) {
        for (CalcMethod i : PrayerModule.calc_methods) {
            if (i.id == method) {
                setCalculationMethod(i);
                return this;
            }
        }
        throw new IllegalArgumentException("Unknown method: " + method);
    }

    /**
     * Default value is {@link AsrMethod#SHAFII}
     */
    public Prayer setAsrMethod(AsrMethod method) {
        this.location.asr_method = method;
        return this;
    }

    /**
     * Default value is {@link ExtremeMethod#NONE}
     */
    public Prayer setExtremeMethod(ExtremeMethod method) {
        this.location.extr_method = method;
        return this;
    }

    public Prayer setLocation(double latitude, double longitude, double altitude) {
        location.latitude = latitude;
        location.longitude = longitude;
        location.altitude = altitude;
        return this;
    }

    public Prayer setDate(GregorianCalendar cal) {
        this.date = new DefsModule.date_t().set(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        this.location.timezone = cal.get(Calendar.ZONE_OFFSET) / (1000.0 * 60 * 60);
        this.location.daylight = (int) Math.round(cal.get(Calendar.DST_OFFSET) / (1000.0 * 60 * 60));
        return this;
    }

    public Prayer setDate(Date date, TimeZone timeZone) {
        GregorianCalendar cal = new GregorianCalendar(timeZone);
        cal.setTime(date);
        return setDate(cal);
    }

    public PrayerTimes getPrayerTimes() {
        requireMethods();
        requireDate();
        requireLocation();

        PrayerTimes result = new PrayerTimes();
        PrayerModule.get_prayer_times(date, location, result);
        return result;
    }

    public Dms getQiblaDirection() {
        requireLocation();
        return Dms.fromDecimal(PrayerModule.get_qibla_direction(location));
    }
}
