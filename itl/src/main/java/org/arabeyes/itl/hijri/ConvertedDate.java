/* Copyright (c) 2017, Fikrul Arif
 * (under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.hijri;

import org.arabeyes.itl.hijri.HijriModule.sDate;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Represents converted date as well as the input date.
 */
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

    /**
     * Format converted date. The format is subset of {@link java.text.SimpleDateFormat} format
     * pattern. Use single quote " ' " for escaping and double single-quote " '' " for single quote.
     * <ul>
     * <li>E - day of week (text)
     * <li>d - day of moth (number)
     * <li>M - month (text if >= 3 characters, otherwise number)
     * <li>y - year (number, if == 2 characters will be truncated to 2 last digits)
     * <li>G - era (text, empty string if not available)
     * </ul>
     * Notes:
     * <ul>
     * <li>text: full text if >= 4 characters, otherwise short/abbreviated form
     * <li>number: will be zero padded based on number of characters
     * <li>era is not always available
     * <li>character other than 'a'-'z', 'A'-'Z', and single quote will be formatted as is
     * </ul>
     *
     * @param f format pattern, must not be null
     * @return formatted date
     * @throws IllegalArgumentException if contains unquoted character 'a'-'z' or 'A'-'Z' other than
     *                                  format patterns above
     */
    public String format(String f) {
        return format(f, this);
    }

    /**
     * Format input date. See {@link #format(String, ConvertedDate)} for format pattern.
     */
    public String formatSource(String f) {
        return format(f, new SourceDate(this));
    }

    private static String format(String f, ConvertedDate d) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        boolean inQuote = false;
        for (int i = 0, last = f.length() - 1; i <= last; ++i) {
            char c = f.charAt(i);
            if (i > 0 && c == f.charAt(i - 1))
                count++;
            else
                count = 1;

            if (c == '\'') {
                if (inQuote) {
                    if (count == 2) result.append(c);
                    inQuote = false;
                    count = 0;
                } else {
                    inQuote = true;
                }
                continue;
            } else if (inQuote) {
                result.append(c);
                continue;
            }

            if (i != last && c == f.charAt(i + 1))
                continue;

            if (c == 'E') {
                result.append(count >= 4 ? d.getDayOfWeekName() : d.getDayOfWeekShortName());
            } else if (c == 'd') {
                result.append(String.format("%0" + count + "d", d.getDayOfMonth()));
            } else if (c == 'M') {
                if (count >= 3)
                    result.append(count >= 4 ? d.getMonthName() : d.getMonthShortName());
                else
                    result.append(String.format("%0" + count + "d", d.getMonth()));
            } else if (c == 'y') {
                String s = String.format("%0" + count + "d", d.getYear());
                result.append(count == 2 && s.length() > 2 ? s.substring(s.length() - 2) : s);
            } else if (c == 'G') {
                result.append(d.getEraName());
            } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                throw new IllegalArgumentException("Illegal pattern character '" + c + "'");
            } else {
                for (int j = 0; j < count; ++j) result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Create {@link Date} instance based on this converted/input date. Note that, because era is
     * not always available, it will be assumed AD (anno domini) if unknown.
     */
    public Date toDate() {
        if (type == TYPE_GREGORIAN) {
            int year = date.year;
            if (HijriModule.ERA_BC.equals(date.units) && year != 0)
                year = 1 - date.year;
            return new GregorianCalendar(year, date.month - 1, date.day).getTime();
        } else {
            return new GregorianCalendar(sourceYear, sourceMonth - 1, sourceDay).getTime();
        }
    }

    /**
     * Create string representation of the converted date in "EEE dd MMM yyyy G" format.
     */
    @Override
    public String toString() {
        return format("EEE dd MMM yyyy G");
    }

    // Accessor for target

    /**
     * Starts from 1.
     */
    public int getDayOfMonth() {
        return date.day;
    }

    /**
     * Starts from 0 for Ahad/Sunday.
     */
    public int getDayOfWeek() {
        return date.weekday;
    }

    /**
     * Starts from 1 for Muharram/January. It is different with standard Java calendar/date classes
     * which month starts from 0.
     */
    public int getMonth() {
        return date.month;
    }

    /**
     * Always positive. See {@link #getEraName()} to get the era.
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

    /**
     * Day name.
     */
    public String getDayOfWeekName() {
        return names.get(date.to_dname);
    }

    /**
     * Abbreviated day name.
     */
    public String getDayOfWeekShortName() {
        return names.get(date.to_dname_sh);
    }

    /**
     * Month name.
     */
    public String getMonthName() {
        return names.get(date.to_mname);
    }

    /**
     * Abbreviated month name.
     */
    public String getMonthShortName() {
        return names.get(date.to_mname_sh);
    }

    /**
     * Era name.
     */
    public String getEraName() {
        return date.units == null ? "" : names.get(date.units);
    }

    // Extra accessor for target

    public int getNextMonthLength() {
        return date.to_numdays2;
    }

    public String getNextMonthName() {
        return names.get(date.to_mname2);
    }

    // Accessor for source

    public int getSourceDayOfMonth() {
        return sourceDay;
    }

//    public int getSourceDayOfWeek() {
//        throw new UnsupportedOperationException();
//    }

    /**
     * Input month.
     */
    public int getSourceMonth() {
        return sourceMonth;
    }

    /**
     * Input year, can be negative.
     */
    public int getSourceYear() {
        return sourceYear;
    }

    /**
     * Number of days in input month.
     */
    public int getSourceMonthLength() {
        return date.frm_numdays;
    }

    /**
     * Day name of input date.
     */
    public String getSourceDayOfWeekName() {
        return names.get(date.frm_dname);
    }

    /**
     * Abbreviated day name of input date.
     */
    public String getSourceDayOfWeekShortName() {
        return names.get(date.frm_dname_sh);
    }

    /**
     * Month name of input date.
     */
    public String getSourceMonthName() {
        return names.get(date.frm_mname);
    }

    /**
     * Abbreviated month name of input date.
     */
    public String getSourceMonthShortName() {
        return names.get(date.frm_mname_sh);
    }

//    public String getSourceEraName() {
//        throw new UnsupportedOperationException();
//    }

    /**
     * Such a hack.
     */
    private static class SourceDate extends ConvertedDate {

        SourceDate(ConvertedDate o) {
            super(o.date, o.sourceYear, o.sourceMonth, o.sourceDay, o.names, o.type);
        }

        public int getDayOfMonth() {
            return getSourceDayOfMonth();
        }

        public int getDayOfWeek() {
            throw new UnsupportedOperationException();
        }

        public int getMonth() {
            return getSourceMonth();
        }

        public int getYear() {
            return getSourceYear();
        }

        public int getMonthLength() {
            return getSourceMonthLength();
        }

        public String getDayOfWeekName() {
            return getSourceDayOfWeekName();
        }

        public String getDayOfWeekShortName() {
            return getSourceDayOfWeekShortName();
        }

        public String getMonthName() {
            return getSourceMonthName();
        }

        public String getMonthShortName() {
            return getSourceMonthShortName();
        }

        public String getEraName() {
            return "";
        }

        public int getNextMonthLength() {
            throw new UnsupportedOperationException();
        }

        public String getNextMonthName() {
            throw new UnsupportedOperationException();
        }
    }
}
