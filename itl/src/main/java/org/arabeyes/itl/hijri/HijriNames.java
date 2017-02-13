package org.arabeyes.itl.hijri;

import java.util.Locale;
import java.util.ResourceBundle;

public class HijriNames {
    
    private static HijriNames instance;
    
    private ResourceBundle res;
    private Locale locale;
    
    public HijriNames(Locale locale) {
        this.res = ResourceBundle.getBundle("org.arabeyes.itl.hijri.name", locale);
        this.locale = locale;
    }
    
    public Locale getLocale() {
        return locale;
    }

    public String getDayName(int dayOfWeek) {
        return res.getString("day.long." + dayOfWeek);
    }
    
    public String getDayShortName(int dayOfWeek) {
        return res.getString("day.short." + dayOfWeek);
    }
    
    public String getMonthName(int month) {
        return res.getString("month.long." + month);
    }
    
    public String getMonthShortName(int month) {
        return res.getString("month.short." + month);
    }
    
    public String getEraName(int era) {
        if (era == SimpleHijriDate.ERA_BH)
            return res.getString("era.bh");
        else if (era == SimpleHijriDate.ERA_AH)
            return res.getString("era.ah");
        else
            throw new IllegalArgumentException("Invalid Hijri era: " + era);
    }
    
    public static HijriNames getInstance(Locale locale) {
        if (instance == null || !instance.locale.equals(locale))
            instance = new HijriNames(locale);
        return instance;
    }
}
