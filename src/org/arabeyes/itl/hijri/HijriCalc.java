/**
 * Copyright 2004, 2013 Arabeyes, Fayez Alhargan, Fikrul Arif
 * 
 * This file is part of Jitl Hijri.
 * 
 * Jitl Hijri is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Jitl Hijri is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jitl Hijri.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arabeyes.itl.hijri;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The key class you could use for converting from/to Hijri or Umm Al-Qura calendar.
 * 
 * @author Fikrul
 *
 */
public class HijriCalc {
	
	private static void error(String msg) {
		System.err.println("HijriCalc: " + msg);
	}

	public static SimpleHijriDate toHijri(Date date) {
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(date);
		
		if (gcal.get(Calendar.ERA) != GregorianCalendar.AD)
			error("Function h_date doesn't provide era input, it should be AD");
		
		HijriCalculator.sDate mydate = new HijriCalculator.sDate();
		HijriCalculator.h_date(mydate, gcal.get(Calendar.DAY_OF_MONTH),
				gcal.get(Calendar.MONTH) + 1, gcal.get(Calendar.YEAR));
		return new SimpleHijriDate(mydate);
	}
	
	public static Date fromHijri(int year, int month, int day) {
		HijriCalculator.sDate mydate = new HijriCalculator.sDate();
		HijriCalculator.g_date(mydate, day, month + 1, year);
		GregorianCalendar result = new GregorianCalendar(mydate.year, mydate.month - 1, mydate.day);
		if (HijriCalculator.GREGORIAN_BC.equals(mydate.units))
			result.set(Calendar.ERA, GregorianCalendar.BC);
		return result.getTime();
	}

	/**
	 * Only valid for 1420 H to 1450 H.
	 * @param gcal
	 * @return
	 */
	public static SimpleHijriDate toUmmAlqura(Date date) {
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(date);
		
		if (gcal.get(Calendar.ERA) != GregorianCalendar.AD)
			error("Function G2H doesn't provide era input, it should be AD");
		
		HijriCalculator.sDate mydate = new HijriCalculator.sDate();
		UmmAlquraCalculator.G2H(mydate, gcal.get(Calendar.DAY_OF_MONTH),
				gcal.get(Calendar.MONTH) + 1, gcal.get(Calendar.YEAR));
		if (mydate.year < 1420 || mydate.year > 1450)
			error("Invalid year for Umm Al-Qura returned by function G2H: " + mydate.year);
		return new SimpleHijriDate(mydate);
	}
	
	/**
	 * 
	 * @param year between 1420 and 1450
	 * @param month start from 0 (Muharram)
	 * @param day start from 1
	 * @return
	 */
	public static Date fromUmmAlqura(int year, int month, int day) {
		if (year < 1420 || year > 1450)
			error("Invalid year for Umm Al-Qura: " + year);
		HijriCalculator.sDate mydate = new HijriCalculator.sDate();
		UmmAlquraCalculator.H2G(mydate, day, month + 1, year);
		return new GregorianCalendar(mydate.year, mydate.month - 1, mydate.day).getTime();
	}

}
