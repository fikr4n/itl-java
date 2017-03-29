package org.arabeyes.itl.hijri;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Hijri {
    private final HijriNames names;

    public Hijri(Locale locale) {
        this.names = new HijriNames(locale);
    }

    /**
     * Convert to Hijri.
     */
    public ConvertedDate hDate(int year, int month, int day) {
        HijriModule.sDate d = new HijriModule.sDate();
        int error = HijriModule.h_date(d, day, month, year);
        if (error != 0)
            throw new ConversionException("Failed to convert (error=" + error + ")");
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_HIJRI);
    }

    /**
     * Convert to Hijri.
     */
    public ConvertedDate hDate(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (cal.get(Calendar.ERA) != GregorianCalendar.AD) // TODO: 2017-03-28 handle
            throw new IllegalArgumentException("Era other than AD is not supported");

        return hDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Convert to Gregorian.
     */
    public ConvertedDate gDate(int year, int month, int day) {
        HijriModule.sDate d = new HijriModule.sDate();
        int error = HijriModule.g_date(d, day, month, year);
        if (error != 0)
            throw new ConversionException("Failed to convert (error=" + error + ")");
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_GREGORIAN);
    }
}
