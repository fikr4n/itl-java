package org.arabeyes.itl.hijri;

import java.util.Locale;
import java.util.ResourceBundle;

final class HijriNames {

    private ResourceBundle res;

    public HijriNames(Locale locale) {
        if (locale == null)
            locale = Locale.getDefault();

        this.res = ResourceBundle.getBundle("org.arabeyes.itl.hijri.name", locale);
    }

    String get(String key) {
        return res.getString(key);
    }
}
