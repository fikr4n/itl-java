/*  Copyright (c) 2004, Arabeyes, Nadim Shaikli
 *
 *  A Hijri (Islamic) to/from Gregorian (Christian) date conversion library.
 *
 * (*)NOTE: A great deal of inspiration as well as algorithmic insight was
 *          based on the lisp code from GNU Emacs' cal-islam.el which itself
 *          is baed on ``Calendrical Calculations'' by Nachum Dershowitz and
 *          Edward M. Reingold.
 *
 * (www.arabeyes.org - under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.hijri;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

class HijriModule {

    /**
     * User-viewable Date structure
     */
    static class sDate {
        int day;		/* Day */
        int month;		/* Month */
        int year;		/* Year */
        int weekday;        /* Day of the week (0:Sunday, 1:Monday...) */
        int frm_numdays;    /* Number of days in specified input  month */
        int to_numdays;     /* Number of days in resulting output month */
        int to_numdays2;    /* Number of days in resulting output month+1 */
        String units;	/* Units used to denote before/after epoch */
        String frm_dname;	/* Converting from - Name of day */
        String frm_mname;	/* Converting from - Name of month */
        String frm_dname_sh;	/* Converting from - Name of day   in short format */
        String frm_mname_sh;	/* Converting from - Name of month in short format */
        String to_dname;	/* Converting to   - Name of day */
        String to_mname;	/* Converting to   - Name of month */
        String to_mname2;	/* Converting to   - Name of month+1 */
        String to_dname_sh;	/* Converting to   - Name of day   in short format */
        String to_mname_sh;	/* Converting to   - Name of month in short format */
        String[] event;	/* Important event pertaining to date at hand */
    }

    /**
     * Table populated structure
     */
    private static class sEvent {
        int day;
        int month;
        String event;

        sEvent(int day, int month, String event) {
            this.day = day;
            this.month = month;
            this.event = event;
        }
    }

    /**
     * Various Islamic/Hijri Important Events
     */
    private static sEvent[] h_events_table = {
                    /* d   m     Event String */
            new sEvent(1, 1, "Islamic New Year"),
            new sEvent(15, 1, "Battle of Qadisiah (14 A.H)"),
            new sEvent(10, 1, "Aashura"),
            new sEvent(10, 2, "Start of Omar ibn Abd Al-Aziz Khilafah (99 A.H)"),
            new sEvent(4, 3, "Start of Islamic calander by Omar Ibn Al-Khattab (16 A.H)"),
            new sEvent(12, 3, "Birth of the Prophet (PBUH)"),
            new sEvent(20, 3, "Liberation of Bait AL-Maqdis by Omar Ibn Al-Khattab (15 A.H)"),
            new sEvent(25, 4, "Battle of Hitteen (583 A.H)"),
            new sEvent(5, 5, "Battle of Muatah (8 A.H)"),
            new sEvent(27, 7, "Salahuddin liberates Bait Al-Maqdis from crusaders"),
            new sEvent(27, 7, "Al-Israa wa Al-Miaaraj"),
            new sEvent(1, 9, "First day of month-long Fasting"),
            new sEvent(17, 9, "Battle of Badr (2 A.H)"),
            new sEvent(21, 9, "Liberation of Makkah (8 A.H)"),
            new sEvent(21, 9, "Quran Revealed - day #1"),
            new sEvent(22, 9, "Quran Revealed - day #2"),
            new sEvent(23, 9, "Quran Revealed - day #3"),
            new sEvent(24, 9, "Quran Revealed - day #4"),
            new sEvent(25, 9, "Quran Revealed - day #5"),
            new sEvent(26, 9, "Quran Revealed - day #6"),
            new sEvent(27, 9, "Quran Revealed - day #7"),
            new sEvent(28, 9, "Quran Revealed - day #8"),
            new sEvent(29, 9, "Quran Revealed - day #9"),
            new sEvent(1, 10, "Eid Al-Fitr"),
            new sEvent(6, 10, "Battle of Uhud (3 A.H)"),
            new sEvent(10, 10, "Battle of Hunian (8 A.H)"),
            new sEvent(8, 12, "Hajj to Makkah - day #1"),
            new sEvent(9, 12, "Hajj to Makkah - day #2"),
            new sEvent(9, 12, "Day of Arafah"),
            new sEvent(10, 12, "Hajj to Makkah - day #3"),
            new sEvent(10, 12, "Eid Al-Adhaa - day #1"),
            new sEvent(11, 12, "Eid Al-Adhaa - day #2"),
            new sEvent(12, 12, "Eid Al-Adhaa - day #3"),
    };

    /**
     * Absolute date of start of Islamic calendar (July 19, 622 Gregorian)
     */
    private static final int HijriEpoch = 227015;
    private static final int GregorianEpoch = 1;

    /* Various user day/month tangibles */

    static final String[] g_day = {"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    static final String[] h_day = {"Ahad", "Ithnain", "Thulatha", "Arbiaa",
            "Khamees", "Jumaa", "Sabt"};

    static final String[] g_day_short = {"Sun", "Mon", "Tue", "Wed",
            "Thu", "Fri", "Sat"};

    static final String[] h_day_short = {"Ahd", "Ith", "Tha", "Arb",
            "Kha", "Jum", "Sab"};

    static final String[] g_month = {"skip",
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};

    static final String[] h_month = {"skip",
            "Muharram", "Safar", "Rabi I", "Rabi II",
            "Jumada I", "Jumada II", "Rajab", "Shaaban",
            "Ramadan", "Shawwal", "Thul-Qiaadah", "Thul-Hijja"};

    static final String[] g_month_short = {"skip",
            "Jan", "Feb", "Mar", "Apr",
            "May.sh", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};

    static final String[] h_month_short = {"skip",
            "Muh", "Saf", "Rb1", "Rb2",
            "Jm1", "Jm2", "Raj", "Shaa",
            "Ram", "Shaw", "Qid", "Hij"};

    private HijriModule() {
    }

    /* Store-off any events for passed-in date */
    private static String[] get_events(sEvent[] events_table,
                                       int day,
                                       int month) {
        int table_depth;
        int count;
        int found = 0;
        final String[] arr_ptrs;

        /* Calculate the table's depth for iternation count */
        table_depth = events_table == null ? 0 : events_table.length;

        /* Traverse teh table to see if there are events matching passed-in date */
        for (count = 0; count < table_depth; count++) {
            if ((events_table[count].day == day) &&
                    (events_table[count].month == month)) {
                found++;
            }
        }

        /* Allocate memory for the return pointer(s)
         * - we'll always need at least 1 for NULL
         */
        arr_ptrs = new String[found];

        /* Populate those entries that were found (if any) */
        if (found != 0) {
            /* Reinitiate and now populate newly malloced memory with events */
            found = 0;
            for (count = 0; count < table_depth; count++) {
                if ((events_table[count].day == day) &&
                        (events_table[count].month == month)) {
                    arr_ptrs[found++] = events_table[count].event;
                }
            }
        }

        /* Assign contents of the pointer to new address */
        return arr_ptrs;
    }

    /**
     * Fill-in the rest of the sDate struct with various nicities
     */
    static int fill_datestruct(sDate fdate,
                               int weekday,
                               int frm_month_num,
                               int to_month_num,
                               String[] frm_day,
                               String[] frm_day_short,
                               String[] frm_month,
                               String[] frm_month_short,
                               String[] to_day,
                               String[] to_day_short,
                               String[] to_month,
                               String[] to_month_short,
                               sEvent[] farr_table) {
        int error_event;

        fdate.frm_dname = frm_day[weekday];
        fdate.frm_dname_sh = frm_day_short[weekday];
        fdate.frm_mname = frm_month[frm_month_num];
        fdate.frm_mname_sh = frm_month_short[frm_month_num];

        fdate.to_dname = to_day[weekday];
        fdate.to_dname_sh = to_day_short[weekday];
        fdate.to_mname = to_month[to_month_num];
        fdate.to_mname_sh = to_month_short[to_month_num];

        if (to_month_num == 12)
            fdate.to_mname2 = to_month[1];
        else
            fdate.to_mname2 = to_month[to_month_num + 1];

        fdate.event = get_events(farr_table,
                fdate.day,
                fdate.month);
        error_event = 0;

        return (error_event);
    }

    /**
     * Wrapper function to do a division and a floor call
     */
    private static double divf(double x,
                               double y) {
        return (floor(x / y));
    }

    /**
     * Determine if Hijri passed-in year is a leap year
     */
    private static boolean h_leapyear(int year) {
        /* True if year is an Islamic leap year */

        if (abs(((11 * year) + 14) % 30) < 11)
            return (true);
        else
            return (false);
    }

    /**
     * Determine the number of days in passed-in hijri month/year
     */
    private static int h_numdays(int month,
                                 int year) {
        /* Last day in month during year on the Islamic calendar. */

        if (((month % 2) == 1) || ((month == 12) && h_leapyear(year)))
            return (30);
        else
            return (29);
    }

    /**
     * Determine Hijri absolute date from passed-in day/month/year
     */
    private static int h_absolute(int day,
                                  int month,
                                  int year) {
        /* Computes the Islamic date from the absolute date. */
        return (int) (day				/* days so far this month */
                + (29 * (month - 1))		/* days so far... */
                + divf(month, 2)		/* ...this year */
                + (354 * (year - 1))		/* non-leap days in prior years */
                + divf((3 + (11 * year)), 30)	/* leap days in prior years */
                + HijriEpoch - 1);		/* days before start of calendar */
    }

    /**
     * Determine Hijri/Islamic date from passed-in Gregorian day/month/year
     * ie. Gregorian . Hijri
     */
    static int h_date(sDate cdate,
                      int day,
                      int month,
                      int year) {
        int abs_date;
        boolean pre_epoch = false;
        int error_fill;

        /* Account for Pre-Epoch date correction, year 0 entry */
        if (year < 0)
            year++;

        abs_date = g_absolute(day, month, year);

        /* Search forward/backward year by year from approximate year */
        if (abs_date < HijriEpoch) {
            cdate.year = 0;

            while (abs_date <= h_absolute(1, 1, cdate.year))
                cdate.year--;
        } else {
            cdate.year = (int) divf((abs_date - HijriEpoch - 1), 355);

            while (abs_date >= h_absolute(1, 1, cdate.year + 1))
                cdate.year++;
        }

        /* Search forward month by month from Muharram */
        cdate.month = 1;
        while (abs_date > h_absolute(h_numdays(cdate.month, cdate.year),
                cdate.month,
                cdate.year))
            cdate.month++;

        cdate.day = abs_date - h_absolute(1, cdate.month, cdate.year) + 1;

        /* Account for Pre-Hijrah date correction, year 0 entry */
        if (cdate.year <= 0) {
            pre_epoch = true;
            cdate.year = ((cdate.year - 1) * -1);
        }

        /* Set resulting values */
        cdate.units = (pre_epoch ? "B.H" : "A.H");
        cdate.weekday = (abs(abs_date % 7));
        cdate.frm_numdays = g_numdays(month, year);
        cdate.to_numdays = h_numdays(cdate.month, cdate.year);
        cdate.to_numdays2 = h_numdays((cdate.month + 1), cdate.year);

        /* Fill-in the structure with various nicities a user might need */
        error_fill = fill_datestruct(cdate, cdate.weekday, month, cdate.month,
                g_day, g_day_short, g_month, g_month_short,
                h_day, h_day_short, h_month, h_month_short,
                h_events_table);

        return (error_fill);
    }

    /**
     * Determine the number of days in passed-in gregorian month/year
     */
    private static int g_numdays(int month,
                                 int year) {
        int y;

        y = abs(year);

        /* Compute the last date of the month for the Gregorian calendar. */
        switch (month) {
            case 2:
                if ((((y % 4) == 0) && ((y % 100) != 0)) || ((y % 400) == 0))
                    return (29);
                else
                    return (28);
            case 4:
            case 6:
            case 9:
            case 11:
                return (30);
            default:
                return (31);
        }
    }

    /**
     * Determine Gregorian absolute date from passed-in day/month/year
     */
    private static int g_absolute(int day,
                                  int month,
                                  int year) {
        int N = day;           /* days this month */
        int m;

        for (m = month - 1; m > 0; m--) /* days in prior months this year */
            N += g_numdays(m, year);

        return (int) (N				/* days this year */
                + 365 * (year - 1)		/* previous years days ignoring leap */
                + divf((year - 1), 4)		/* Julian leap days before this year.. */
                - divf((year - 1), 100)	/* ..minus prior century years... */
                + divf((year - 1), 400));	/* ..plus prior years divisible by 400 */
    }

    /**
     * Determine Gregorian date from passed-in Hijri/Islamic day/month/year
     * ie. Hijri . Gregorian
     */
    static int g_date(sDate cdate,
                      int day,
                      int month,
                      int year) {
        int abs_date;
        boolean pre_epoch = false;
        int error_fill;

        /* Account for Pre-Epoch date correction, year 0 entry */
        if (year < 0)
            year++;

        abs_date = h_absolute(day, month, year);

        /* Search forward year by year from approximate year */
        cdate.year = (int) divf(abs_date, 366);

        while (abs_date >= g_absolute(1, 1, cdate.year + 1))
            cdate.year++;

        /* Search forward month by month from January */
        cdate.month = 1;
        while (abs_date > g_absolute(g_numdays(cdate.month, cdate.year),
                cdate.month,
                cdate.year))
            cdate.month++;

        cdate.day = abs_date - g_absolute(1, cdate.month, cdate.year) + 1;

        /* Account for Pre-Hijrah date correction, year 0 entry */
        if (cdate.year <= 0) {
            pre_epoch = true;
            cdate.year = ((cdate.year - 1) * -1);
        }

        /* Set resulting values */
        cdate.units = (pre_epoch ? "B.C" : "A.D");
        cdate.weekday = (abs(abs_date % 7));
        cdate.frm_numdays = h_numdays(month, year);
        cdate.to_numdays = g_numdays(cdate.month, cdate.year);
        cdate.to_numdays2 = g_numdays((cdate.month + 1), cdate.year);

        /* Place holder for if/when a gregorian table is added */
        /* Fill-in the structure with various nicities a user might need */
        error_fill = fill_datestruct(cdate, cdate.weekday, month, cdate.month,
                h_day, h_day_short, h_month, h_month_short,
                g_day, g_day_short, g_month, g_month_short,
                null);

        return (error_fill);
    }
}
