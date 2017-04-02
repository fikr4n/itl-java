/* Copyright (c) 2017, Fikrul Arif
 * (under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.hijri;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class UmmAlqura {
    private final HijriNames names;

    /**
     * @param locale locale information used for name (e.g. month and day names), if null then the
     *               default is used
     */
    public UmmAlqura(Locale locale) {
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
    public ConvertedDate g2h(int day, int month, int year) {
        HijriModule.sDate d = new HijriModule.sDate();
        int flag;
        try {
            flag = UmmAlquraModule.G2H(d, day, month, year);
        } catch (Exception e) {
            throw new ConversionException(String.format("Error while converting (%d, %d, %d)",
                    year, month, day), e);
        }
        if (flag != 1)
            throw new ConversionException(String.format("Error while converting (%d, %d, %d) (flag=%d)",
                    year, month, day, flag));
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_UMM_ALQURA);
    }

    /**
     * Convert Gregorian to Hijri.
     */
    public ConvertedDate g2h(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        if (cal.get(Calendar.ERA) == GregorianCalendar.BC)
            year = 1 - year;
        return g2h(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, year);
    }

    /**
     * Convert Hijri to Gregorian.
     *
     * @param day   day of month, starts from 1
     * @param month starts from 1 for Muharram instead of 0
     * @param year  negative for before epoch, e.g. -1 for 1 BH
     */
    public ConvertedDate h2g(int day, int month, int year) {
        HijriModule.sDate d = new HijriModule.sDate();
        int flag;
        try {
            flag = UmmAlquraModule.H2G(d, day, month, year);
        } catch (Exception e) {
            throw new ConversionException(String.format("Error while converting (%d, %d, %d)",
                    year, month, day), e);
        }
        if (flag != 1)
            throw new ConversionException(String.format("Error while converting (%d, %d, %d) (flag=%d)",
                    year, month, day, flag));
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_GREGORIAN);
    }
}
