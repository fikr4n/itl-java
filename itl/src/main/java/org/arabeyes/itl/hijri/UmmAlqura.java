package org.arabeyes.itl.hijri;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class UmmAlqura {
    private final HijriNames names;

    public UmmAlqura(Locale locale) {
        this.names = new HijriNames(locale);
    }

    public ConvertedDate g2h(int year, int month, int day) {
        HijriModule.sDate d = new HijriModule.sDate();
        int error = UmmAlquraModule.G2H(d, day, month, year);
        if (error != 0)
            throw new ConversionException("Failed to convert (error=" + error + ")");
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_UMM_ALQURA);
    }

    public ConvertedDate g2h(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (cal.get(Calendar.ERA) != GregorianCalendar.AD)
            throw new IllegalArgumentException("Era other than AD is not supported"); // TODO: 2017-03-28 handle

        return g2h(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }

    public ConvertedDate h2g(int year, int month, int day) {
        HijriModule.sDate d = new HijriModule.sDate();
        int error = UmmAlquraModule.H2G(d, day, month, year);
        if (error != 0)
            throw new ConversionException("Failed to convert (error=" + error + ")");
        return new ConvertedDate(d, year, month, day, names, ConvertedDate.TYPE_GREGORIAN);
    }
}
