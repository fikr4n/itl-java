/**
 * Copyright 2004, 2013 Arabeyes, Fayez Alhargan, Fikrul Arif
 * 
 * This file is part of Jitl Hijri.
 * 
 * Jitl Hijri is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Jitl Hijri is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jitl Hijri.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arabeyes.itl.hijri;

import java.util.Locale;

/**
 * Simple date returned by {@link HijriCalc}.
 * 
 * @author Fikrul
 *
 */
public class SimpleHijriDate {
    /* This class uses Java convention so that month number is starting from 0 instead of 1.
     * But the original calculator uses 1-based month number.
     */

    /** Before Hijri. */
    public static final int ERA_BH = -1;
    /** After Hijri (anno-hegirae). */
    public static final int ERA_AH = 0;
    
    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    private int monthLength;
    private int era;
    
    SimpleHijriDate(HijriCalculator.sDate d) {
        this.year = d.year;
        this.month = d.month - 1;
        this.dayOfMonth = d.day;
        this.dayOfWeek = d.weekday;
        this.monthLength = d.to_numdays;
        if (HijriCalculator.HIJRI_BH.equals(d.units))
            era = ERA_BH;
        else
            era = ERA_AH;
    }

    /**
     * Start from 1.
     * @return
     */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Start from 0 for Ahad (Sunday).
     * @return
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Start from 0 for Muharram.
     * @return
     */
    public int getMonth() {
        return month;
    }

    /**
     * The year.
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * Number of days in this month.
     * @return
     */
    public int getMonthLength() {
        return monthLength;
    }
    
    /**
     * Could be {@link #ERA_AH} or {@link #ERA_BH}.
     * @return
     */
    public int getEra() {
        return era;
    }
    
    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getDayOfWeekName(Locale locale) {
        return HijriNames.getInstance(locale).getDayName(dayOfWeek);
    }
    
    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getDayOfWeekShortName(Locale locale) {
        return HijriNames.getInstance(locale).getDayShortName(dayOfWeek);
    }
    
    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getMonthName(Locale locale) {
        return HijriNames.getInstance(locale).getMonthName(month);
    }
    
    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getMonthShortName(Locale locale) {
        return HijriNames.getInstance(locale).getMonthShortName(month);
    }
    
    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getEraName(Locale locale) {
        return HijriNames.getInstance(locale).getEraName(era);
    }

}
