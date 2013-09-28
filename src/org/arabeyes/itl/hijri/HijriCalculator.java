/************************************************************************
 * $Id: hijri.c,v 1.9 2004/07/16 01:16:32 nadim Exp $
 *
 * ------------
 * Description:
 * ------------
 *  Copyright (c) 2004, 2013 Arabeyes, Nadim Shaikli, Fikrul Arif
 *  
 *  The original program authored by Nadim Shaikli is written in C
 *  and ported to Java by Fikrul Arif.
 *
 *  A Hijri (Islamic) to/from Gregorian (Christian) date conversion library.
 *
 * (*)NOTE: A great deal of inspiration as well as algorithmic insight was
 *          based on the lisp code from GNU Emacs' cal-islam.el which itself
 *          is baed on ``Calendrical Calculations'' by Nachum Dershowitz and
 *          Edward M. Reingold.
 *
 * -----------------
 * Revision Details:    (Updated by Revision Control System)
 * -----------------
 *  $Date: 2004/07/16 01:16:32 $
 *  $Author: nadim $
 *  $Revision: 1.9 $
 *  $Source$
 *
 * (www.arabeyes.org - under LGPL license - see COPYING file)
 ************************************************************************/
package org.arabeyes.itl.hijri;

/**
 * Calculator for converting between Hijri and Gregorian calendar.
 * <p>
 * The content of this class is copied and modified from the original source (in C). I don't if
 * there's some mistake when adapting it to Java, I hope no mistake.
 */
class HijriCalculator {

	public static final String GREGORIAN_AD = "A.D";
	public static final String GREGORIAN_BC = "B.C";
	public static final String HIJRI_AH = "A.H";
	public static final String HIJRI_BH = "B.H";

	/* User-viewable Date structure */
		static final class sDate
		{
		    int day;		/* Day */
		    int month;		/* Month */
		    int year;		/* Year */
		    int weekday;        /* Day of the week (0:Sunday, 1:Monday...) */
		    int frm_numdays;    /* Number of days in specified input  month */
		    int to_numdays;     /* Number of days in resulting output month */
		    int to_numdays2;    /* Number of days in resulting output month+1 */
		    String units;	/* Units used to denote before/after epoch */
//		    String frm_dname;	/* Converting from - Name of day */
//		    String frm_mname;	/* Converting from - Name of month */
//		    String frm_dname_sh;	/* Converting from - Name of day   in short format */
//		    String frm_mname_sh;	/* Converting from - Name of month in short format */
//		    String to_dname;	/* Converting to   - Name of day */
//		    String to_mname;	/* Converting to   - Name of month */
//		    String to_mname2;	/* Converting to   - Name of month+1 */
//		    String to_dname_sh;	/* Converting to   - Name of day   in short format */
//		    String to_mname_sh;	/* Converting to   - Name of month in short format */
//		    String[] event;	/* Important event pertaining to date at hand */
		}

	/* Absolute date of start of Islamic calendar (July 19, 622 Gregorian)*/
	private static final int HijriEpoch		= 227015;
//	private static final int GregorianEpoch	= 1;
	
	// END OF COMMON

	/* Wrapper function to do a division and a floor call */
	private static float
	divf(float x,
	     float y)
	{
	   return (float) ( Math.floor(x / y) );
	}
	
	/* Determine if Hijri passed-in year is a leap year */
	private static int
	h_leapyear(int year)
	{
	   /* True if year is an Islamic leap year */
	
	   if ( Math.abs(((11 * year) + 14) % 30) < 11 )
	      return(1);
	   else
	      return(0);
	}
	
	
	/* Determine the number of days in passed-in hijri month/year */
	private static int
	h_numdays(int month,
		  int year)
	{
	   /* Last day in month during year on the Islamic calendar. */
	
	   if (((month % 2) == 1) || ((month == 12) && h_leapyear(year) != 0))
	      return(30);
	   else
	      return(29);
	}
	
	
	/* Determine Hijri absolute date from passed-in day/month/year */
	private static int
	h_absolute(int day,
		   int month,
		   int year)
	{
	   /* Computes the Islamic date from the absolute date. */
	   return (int) (day				// days so far this month
		  + (29 * (month - 1))		// days so far...
		  + divf(month, 2)		//            ...this year
		  + (354 * (year - 1))		// non-leap days in prior years
		  + divf((3 + (11 * year)), 30)	// leap days in prior years
		  + HijriEpoch - 1);		// days before start of calendar
	}
	
	
	/* Determine Hijri/Islamic date from passed-in Gregorian day/month/year
	   ie. Gregorian -> Hijri
	 */
	static void
	h_date(sDate cdate,
	       int day,
	       int month,
	       int year)
	{
	   int abs_date;
	   int pre_epoch = 0;
	
	   /* Account for Pre-Epoch date correction, year 0 entry */
	   if (year < 0)
	      year++;
	
	   abs_date = g_absolute(day, month, year);
	
	   /* Search forward/backward year by year from approximate year */
	   if (abs_date < HijriEpoch)
	   {
	      cdate.year = 0;
	
	      while (abs_date <= h_absolute(1, 1, cdate.year))
		 cdate.year--;
	   }
	   else
	   {
	      cdate.year = (int) divf((abs_date - HijriEpoch - 1), 355);
	
	      while (abs_date >= h_absolute(1, 1, cdate.year+1))
		 cdate.year++;
	   }
	
	   /* Search forward month by month from Muharram */
	   cdate.month = 1;
	   while (abs_date > h_absolute(h_numdays(cdate.month, cdate.year),
					cdate.month,
					cdate.year))
	      cdate.month++;
	
	   cdate.day = abs_date - h_absolute(1, cdate.month, cdate.year) + 1;
	
	   /* Account for Pre-Hijrah date correction, year 0 entry */
	   if (cdate.year <= 0)
	   {
	      pre_epoch   = 1;
	      cdate.year = ((cdate.year - 1) * -1);
	   }
	
	   /* Set resulting values */
	   cdate.units         = ( pre_epoch != 0 ? HIJRI_BH : HIJRI_AH );
	   cdate.weekday	= (Math.abs(abs_date % 7));
	   cdate.frm_numdays	= g_numdays(month, year);
	   cdate.to_numdays	= h_numdays(cdate.month, cdate.year);
	   cdate.to_numdays2	= h_numdays((cdate.month + 1), cdate.year);
	};
	
	
	/* Determine the number of days in passed-in gregorian month/year */
	private static int
	g_numdays(int month,
		  int year)
	{
	   int y;
	
	   y = Math.abs(year);
	
	   /* Compute the last date of the month for the Gregorian calendar. */
	   switch (month)
	   {
	      case 2:
		 if ( (((y % 4) == 0) && ((y % 100) != 0)) || ((y % 400) == 0) )
		    return(29);
		 else
		    return(28);
	      case 4:
	      case 6:
	      case 9:
	      case 11: return(30);
	      default: return(31);
	   }
	}
	
	
	/* Determine Gregorian absolute date from passed-in day/month/year */
	private static int
	g_absolute(int day,
		   int month,
		   int year)
	{
	   int N = day;           /* days this month */
	   int m;
	
	   for (m = month - 1; m > 0; m--) /* days in prior months this year */
	      N += g_numdays(m, year);
	
	   return (int) (N				// days this year
		  + 365 * (year - 1)		// previous years days ignoring leap
		  + divf((year - 1), 4)		// Julian leap days before this year..
		  - divf((year - 1), 100)	// ..minus prior century years...
		  + divf((year - 1), 400));	// ..plus prior years divisible by 400
	}
	
	
	/* Determine Gregorian date from passed-in Hijri/Islamic day/month/year
	   ie. Hijri -> Gregorian
	 */
	static void
	g_date(sDate cdate,
	       int day,
	       int month,
	       int year)
	{
	   int abs_date;
	   int pre_epoch = 0;
	
	   /* Account for Pre-Epoch date correction, year 0 entry */
	   if (year < 0)
	      year++;
	
	   abs_date = h_absolute(day, month, year);
	
	   /* Search forward year by year from approximate year */
	   cdate.year = (int) divf(abs_date, 366);
	
	   while (abs_date >= g_absolute(1, 1, cdate.year+1))
	      cdate.year++;
	
	   /* Search forward month by month from January */
	   cdate.month = 1;
	   while (abs_date > g_absolute(g_numdays(cdate.month, cdate.year),
					cdate.month,
					cdate.year))
	      cdate.month++;
	
	   cdate.day = abs_date - g_absolute(1, cdate.month, cdate.year) + 1;
	
	   /* Account for Pre-Hijrah date correction, year 0 entry */
	   if (cdate.year <= 0)
	   {
	      pre_epoch   = 1;
	      cdate.year = ((cdate.year - 1) * -1);
	   }
	
	   /* Set resulting values */
	   cdate.units         = ( pre_epoch != 0 ? GREGORIAN_BC : GREGORIAN_AD );
	   cdate.weekday	= (Math.abs(abs_date % 7));
	   cdate.frm_numdays	= h_numdays(month, year);
	   cdate.to_numdays	= g_numdays(cdate.month, cdate.year);
	   cdate.to_numdays2	= g_numdays((cdate.month + 1), cdate.year);
	};
}
