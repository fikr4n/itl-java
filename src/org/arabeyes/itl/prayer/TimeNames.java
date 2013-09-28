package org.arabeyes.itl.prayer;

import java.util.Locale;
import java.util.ResourceBundle;

public class TimeNames {
	
	private static TimeNames instance;
	
	private ResourceBundle res;
	private Locale locale;
	
	public TimeNames(Locale locale) {
		this.res = ResourceBundle.getBundle("org.arabeyes.itl.prayer.name", locale);
		this.locale = locale;
	}
	
	public Locale getLocale() {
		return locale;
	}

	/**
	 * 
	 * @param type one of prayer time constants in {@link PrayerTimes}, including {@link PrayerTimes#SUNRISE}
	 * @return
	 */
	public String get(int type) {
		if (type >= 6 || type < 0)
			throw new IllegalArgumentException("Invalid time type: " + type);
		return res.getString("time." + type);
	}

	public String getImsak() {
		return res.getString("time.imsak");
	}
	
	public static TimeNames getInstance(Locale locale) {
		if (instance == null || !instance.locale.equals(locale))
			instance = new TimeNames(locale);
		return instance;
	}

}
