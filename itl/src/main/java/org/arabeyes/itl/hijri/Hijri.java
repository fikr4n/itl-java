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
        int error;
        try {
            error = HijriModule.h_date(d, day, month, year);
        } catch (Exception e) {
            throw new ConversionException(String.format("Error while converting (%d, %d, %d)",
                    year, month, day), e);
        }
        if (error != 0)
            throw new ConversionException(String.format("Error while converting (%d, %d, %d) (error=%d)",
                    year, month, day, error));
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_HIJRI);
    }

    /**
     * Convert to Hijri.
     */
    public ConvertedDate hDate(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        if (cal.get(Calendar.ERA) == GregorianCalendar.BC)
            year = 1 - year;
        return hDate(year, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Convert to Gregorian.
     */
    public ConvertedDate gDate(int year, int month, int day) {
        HijriModule.sDate d = new HijriModule.sDate();
        int error;
        try {
            error = HijriModule.g_date(d, day, month, year);
        } catch (Exception e) {
            throw new ConversionException(String.format("Error while converting (%d, %d, %d)",
                    year, month, day), e);
        }
        if (error != 0)
            throw new ConversionException(String.format("Error while converting (%d, %d, %d) (error=%d)",
                    year, month, day, error));
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_GREGORIAN);
    }
}
