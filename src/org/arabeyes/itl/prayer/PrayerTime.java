package org.arabeyes.itl.prayer;

/**
 * This class encapsulates prayer times.
 */
public class PrayerTime {
	
	private int hour; /* prayer time hour */
	private int minute; /* prayer time minute */
	private int second; /* prayer time second */
	private boolean extreme; /*  */
	
	public PrayerTime() {
	}
	
	public PrayerTime(int hour, int minute, int second, boolean extreme) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.extreme = extreme;
	}
	
	public PrayerTime(PrayerTime src) {
		this.hour = src.hour;
		this.minute = src.minute;
		this.second = src.second;
		this.extreme = src.extreme;
	}
	
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	/**
	 * @return Extreme calculation switch. The 'getPrayerTimes'
	 * function sets this switch to true to indicate that this
	 * particular prayer time has been calculated through
	 * extreme latitude methods and NOT by conventional
	 * means of calculation.
	 */
	public boolean isExtreme() {
		return extreme;
	}
	
	public void setExtreme(boolean extreme) {
		this.extreme = extreme;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public int getSecond() {
		return second;
	}
	
	public void setSecond(int second) {
		this.second = second;
	}
	
	public String toString() {
		return hour + ":" + (minute < 10 ? "0" + minute : minute + "") + ":" + (second < 10 ? "0" + second : second + "");
	}
}
