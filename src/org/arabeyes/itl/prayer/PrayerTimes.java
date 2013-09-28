package org.arabeyes.itl.prayer;


/**
 * Encapsulates the list of prayers time and shuruq time
 */
public class PrayerTimes {
	
	public static final int FAJR = 0;
	public static final int SUNRISE = 1;
	public static final int ZUHR = 2;
	public static final int ASR = 3;
	public static final int MAGHRIB = 4;
	public static final int ISHA = 5;
	
	private PrayerTime[] prayers = new PrayerTime[6];
	
	public PrayerTimes() {
		for (int i = 0; i < prayers.length; i++)
			prayers[i] = new PrayerTime();
	}
	
	public PrayerTime get(int type) {
		return prayers[type];
	}
	
	/**
	 * Convert prayer times to a string.
	 * @return prayer times as a string. It contains 6 lines
	 */
	@Override
	public String toString() {
		String ret = "";
		for (int i = 0; i < 6; i++) {
			ret += prayers[i].toString() + "\n";
		}
		return ret;
	}
	
	PrayerTime fajr() {
		return prayers[0];
	}
	
	PrayerTime isha() {
		return prayers[5];
	}
	
	PrayerTime[] getPrayerTime() {
		return prayers;
	}
	
	void setAllExtreme(boolean extreme) {
		for (int i = 0; i < 6; i++) {
			prayers[i].setExtreme(extreme);
		}
	}
}
