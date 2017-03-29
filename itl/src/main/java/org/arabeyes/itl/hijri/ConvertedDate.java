package org.arabeyes.itl.hijri;

import org.arabeyes.itl.hijri.HijriModule.sDate;

import java.util.Date;
import java.util.GregorianCalendar;

public class ConvertedDate {

    static final int TYPE_HIJRI = 0;
    static final int TYPE_UMM_ALQURA = 1;
    static final int TYPE_GREGORIAN = 2;

    private final sDate date;
    private final int sourceYear, sourceMonth, sourceDay;
    private final HijriNames names;
    private final int type;

    ConvertedDate(sDate date, int sourceYear, int sourceMonth, int sourceDay, HijriNames names,
                  int type) {
        this.date = date;
        this.sourceYear = sourceYear;
        this.sourceMonth = sourceMonth;
        this.sourceDay = sourceDay;
        this.names = names;
        this.type = type;
    }

    public String format(String f) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (int i = 0, last = f.length() - 1; i <= last; ++i) {
            char c = f.charAt(i);
            if (i > 0 && c == f.charAt(i - 1))
                count++;
            else
                count = 1;

            if (i == last || c != f.charAt(i + 1)) {
                if (c == 'E') {
                    result.append(count >= 4 ? getDayOfWeekName() : getDayOfWeekShortName());
                } else if (c == 'd') {
                    result.append(String.format("%0" + count + "d", getDayOfMonth()));
                } else if (c == 'M') {
                    if (count >= 3) result.append(count >= 4 ? getMonthName() : getMonthShortName());
                    else result.append(String.format("%0" + count + "d", getMonth()));
                } else if (c == 'y') {
                    String s = Integer.toString(getYear());
                    result.append(count == 2 && s.length() > 2 ? s.substring(s.length() - 2) : s);
                } else if (c == 'G') {
                    result.append(getEraName());
                } else if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z')) {
                    for (int j = 0; j < count; ++j) result.append(c);
                }
            }
        }
        return result.toString();
    }

    /**
     * Start from 1.
     */
    public int getDayOfMonth() {
        return date.day;
    }

    /**
     * Start from 0 for Ahad (Sunday).
     */
    public int getDayOfWeek() {
        return date.weekday;
    }

    /**
     * Start from 1 for Muharram.
     */
    public int getMonth() {
        return date.month;
    }

    /**
     * The year.
     */
    public int getYear() {
        return date.year;
    }

    /**
     * Number of days in this month.
     */
    public int getMonthLength() {
        return date.to_numdays;
    }

    public String getDayOfWeekName() {
        return names.get(date.to_dname);
    }

    public String getDayOfWeekShortName() {
        return names.get(date.to_dname_sh);
    }

    public String getMonthName() {
        return names.get(date.to_mname);
    }

    public String getMonthShortName() {
        return names.get(date.to_mname_sh);
    }

    public String getEraName() {
        return names.get(date.units);
    }

    public Date toDate() {
        if (type == TYPE_GREGORIAN) {
            return new GregorianCalendar(date.year, date.month, date.day).getTime();
        } else {
            return new GregorianCalendar(sourceYear, sourceMonth, sourceDay).getTime();
        }
    }
}
