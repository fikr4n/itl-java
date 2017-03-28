package org.arabeyes.itl.hijri;

import org.arabeyes.itl.hijri.HijriModule.sDate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class HijriCalc {

    private final HijriNames names;

    public HijriCalc(Locale locale) {
        this.names = new HijriNames(locale);
    }

    public ConvertedDate toHijri(int year, int month, int day) {
        sDate d = new sDate();
        int error = HijriModule.h_date(d, day, month, year);
        if (error != 0)
            throw new HijriConversionException("Failed to convert (error=" + error + ")");
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_HIJRI);
    }

    public ConvertedDate toHijri(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (cal.get(Calendar.ERA) != GregorianCalendar.AD) // TODO: 2017-03-28 handle
            throw new IllegalArgumentException("Era other than AD is not supported");

        return toHijri(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }

    public ConvertedDate fromHijri(int year, int month, int day) {
        sDate d = new sDate();
        int error = HijriModule.g_date(d, day, month, year);
        if (error != 0)
            throw new HijriConversionException("Failed to convert (error=" + error + ")");
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_GREGORIAN);
    }

    public ConvertedDate toUmmAlqura(int year, int month, int day) {
        sDate d = new sDate();
        int error = UmmAlquraModule.G2H(d, day, month, year);
        if (error != 0)
            throw new HijriConversionException("Failed to convert (error=" + error + ")");
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_UMM_ALQURA);
    }

    public ConvertedDate toUmmAlqura(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (cal.get(Calendar.ERA) != GregorianCalendar.AD)
            throw new IllegalArgumentException("Era other than AD is not supported"); // TODO: 2017-03-28 handle

        return toUmmAlqura(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
    }

    public ConvertedDate fromUmmAlqura(int year, int month, int day) {
        sDate d = new sDate();
        int error = UmmAlquraModule.H2G(d, day, month, year);
        if (error != 0)
            throw new HijriConversionException("Failed to convert (error=" + error + ")");
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_GREGORIAN);
    }
}
