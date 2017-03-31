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

    public ConvertedDate g2h(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        if (cal.get(Calendar.ERA) == GregorianCalendar.BC)
            year = 1 - year;
        return g2h(year, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    public ConvertedDate h2g(int year, int month, int day) {
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
