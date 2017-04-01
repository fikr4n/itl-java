/*  Copyright (c) 2003-2006, 2009-2010 Arabeyes, Thamer Mahmoud
 * (www.arabeyes.org - under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.prayertime;

/**
 * Supported methods for Extreme Latitude calculations
 */
public enum ExtremeMethod {
    /**
     * 0:  none. if unable to calculate, leave as 99:99
     */
    NONE_EX,
    /**
     * 1:  Nearest Latitude: All prayers Always
     */
    LAT_ALL,
    /**
     * 2:  Nearest Latitude: Fajr Ishaa Always
     */
    LAT_ALWAYS,
    /**
     * 3:  Nearest Latitude: Fajr Ishaa if invalid
     */
    LAT_INVALID,
    /**
     * 4:  Nearest Good Day: All prayers Always
     */
    GOOD_ALL,
    /**
     * 5:  Nearest Good Day: Fajr Ishaa if invalid (default)
     */
    GOOD_INVALID,
    /**
     * 6:  1/7th of Night: Fajr Ishaa Always
     */
    SEVEN_NIGHT_ALWAYS,
    /**
     * 7:  1/7th of Night: Fajr Ishaa if invalid
     */
    SEVEN_NIGHT_INVALID,
    /**
     * 8:  1/7th of Day: Fajr Ishaa Always
     */
    SEVEN_DAY_ALWAYS,
    /**
     * 9:  1/7th of Day: Fajr Ishaa if invalid
     */
    SEVEN_DAY_INVALID,
    /**
     * 10: Half of the Night: Fajr Ishaa Always
     */
    HALF_ALWAYS,
    /**
     * 11: Half of the Night: Fajr Ishaa if invalid
     */
    HALF_INVALID,
    /**
     * 12: Minutes from Shorooq/Maghrib: Fajr Ishaa Always (e.g. Maghrib=Ishaa)
     */
    MIN_ALWAYS,
    /**
     * 13: Minutes from Shorooq/Maghrib: Fajr Ishaa If invalid
     */
    MIN_INVALID,
    /**
     * 14: Nearest Good Day: Fajr Ishaa if either is invalid
     */
    GOOD_INVALID_SAME,
    /**
     * 15: Angle based: Fajr Ishaa if invalid
     */
    ANGLE_BASED,
}
