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
        return format(f, this);
    }

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

    @Override
    public String toString() {
        return format("EEE dd MMM yyyy G");
    }

    // Accessor for target

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

    public int getSourceMonth() {
        return sourceMonth;
    }

    public int getSourceYear() {
        return sourceYear;
    }

    public int getSourceMonthLength() {
        return date.frm_numdays;
    }

    public String getSourceDayOfWeekName() {
        return names.get(date.frm_dname);
    }

    public String getSourceDayOfWeekShortName() {
        return names.get(date.frm_dname_sh);
    }

    public String getSourceMonthName() {
        return names.get(date.frm_mname);
    }

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
