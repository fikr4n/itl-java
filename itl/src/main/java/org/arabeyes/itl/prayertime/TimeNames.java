package org.arabeyes.itl.prayertime;

import java.util.Locale;
import java.util.ResourceBundle;

public class TimeNames {

    private static TimeNames instance;

    private ResourceBundle res;
    private Locale locale;

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
