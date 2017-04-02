/*  Copyright (c) 2003-2006, 2009-2010 Arabeyes, Thamer Mahmoud, Fikrul Arif
 * (www.arabeyes.org - under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.prayertime;

@SuppressWarnings("WeakerAccess")
public enum Mathhab {
    /**
     * 1: Shaf'i and jumhur/majority (default)
     */
    SHAFII {
        @Override
        public int assrRatio() {
            return 1;
        }
    },
    /**
     * 2: Hanafi
     */
    HANAFI {
        @Override
        public int assrRatio() {
            return 2;
        }
    };

    public abstract int assrRatio();
}
