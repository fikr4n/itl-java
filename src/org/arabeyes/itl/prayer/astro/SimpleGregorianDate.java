package org.arabeyes.itl.prayer.astro;

import java.util.GregorianCalendar;

public class SimpleGregorianDate {
	
	private int day;
	private int month;
	private int year;
	
	public SimpleGregorianDate(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public SimpleGregorianDate(SimpleGregorianDate src) {
		this.day = src.day;
		this.month = src.month;
		this.year = src.year;
	}
	
	public SimpleGregorianDate(GregorianCalendar gCalendar) {
		this.day = gCalendar.get(GregorianCalendar.DATE);
		this.month = gCalendar.get(GregorianCalendar.MONTH);
		this.year = gCalendar.get(GregorianCalendar.YEAR);		
	}
	
	public int getDay() {
		return day;
	}
	
	int getMonthCompat() {
		return month + 1;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;
	}
	
}
