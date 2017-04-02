/*  Copyright (c) 2003-2006, 2009-2010 Arabeyes, Thamer Mahmoud, Fikrul Arif
 * (www.arabeyes.org - under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.prayertime;

@SuppressWarnings("WeakerAccess")
public enum Rounding {
    /**
     * 0: No Rounding. "seconds" is set to the
     * amount of computed seconds.
     */
    NONE,
    /**
     * 1: Normal Rounding. If seconds are equal to
     * 30 or above, add 1 minute. Sets
     * "seconds" to zero.
     */
    NORMAL,
    /**
     * 2: Special Rounding (default). Similar to normal rounding
     * but we always round down for Shurooq and
     * Imsaak times.
     */
    SPECIAL,
    /**
     * 3: Aggressive Rounding. Similar to Special
     * Rounding but we add 1 minute if the seconds
     * value is equal to 1 second or more.
     */
    AGGRESSIVE,
}
