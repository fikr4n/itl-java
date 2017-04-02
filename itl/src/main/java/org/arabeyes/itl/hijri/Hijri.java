/* Copyright (c) 2017, Fikrul Arif
 * (under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.hijri;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Hijri {
    private final HijriNames names;

    /**
     * @param locale locale information used for name (e.g. month and day names), if null then the
     *               default is used
     */
    public Hijri(Locale locale) {
        this.names = new HijriNames(locale);
    }

    /**
     * Convert Gregorian to Hijri.
     *
     * @param day   day of month, starts from 1
     * @param month starts from 1 for January instead of 0
     * @param year  negative for before epoch, e.g. -1 for 1 BC
     */
    @SuppressWarnings("WeakerAccess")
    public ConvertedDate hDate(int day, int month, int year) {
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
     * Convert Gregorian to Hijri.
     */
    public ConvertedDate hDate(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        if (cal.get(Calendar.ERA) == GregorianCalendar.BC)
            year = 1 - year;
        return hDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, year);
    }

    /**
     * Convert Hijri to Gregorian.
     *
     * @param day   day of month, starts from 1
     * @param month starts from 1 for Muharram instead of 0
     * @param year  negative for before epoch, e.g. -1 for 1 BH
     */
    public ConvertedDate gDate(int day, int month, int year) {
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
