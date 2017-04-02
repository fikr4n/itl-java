/* Copyright (c) 2017, Fikrul Arif
 * (under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.prayertime;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is used for getting time names.
 */
@SuppressWarnings("WeakerAccess")
public class TimeNames {

    private static TimeNames instance;

    private ResourceBundle res;
    private Locale locale;

    /**
     * @param locale if null then the default locale is used
     */
    public TimeNames(Locale locale) {
        if (locale == null)
            locale = Locale.getDefault();

        this.res = ResourceBundle.getBundle("org.arabeyes.itl.prayertime.name", locale);
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public String get(TimeType type) {
        return res.getString(type.name());
    }

    /**
     * Get cached instance. This class caches only one last locale used. If locale argument is
     * different with the last, new instance will be created and cached.
     */
    public static TimeNames getInstance(Locale locale) {
        if (locale == null)
            locale = Locale.getDefault();

        TimeNames r = instance;
        if (r == null || !r.locale.equals(locale)) {
            r = new TimeNames(locale);
            instance = r;
        }
        return r;
    }
}
