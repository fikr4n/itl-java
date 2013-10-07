package example;

import java.util.Date;
import java.util.Locale;

import org.arabeyes.itl.hijri.HijriCalc;
import org.arabeyes.itl.hijri.SimpleHijriDate;
import org.arabeyes.itl.prayer.Method;
import org.arabeyes.itl.prayer.PrayerTime;
import org.arabeyes.itl.prayer.PrayerTimeCalc;
import org.arabeyes.itl.prayer.PrayerTimes;
import org.arabeyes.itl.prayer.TimeNames;
import org.arabeyes.itl.prayer.astro.Location;

public class Example {

	public static void main(String[] args) {
		showPrayerTime();
		showHijri();
		showUmmAlqura();
	}
	
	public static void showHijri() {
		System.out.println("=== HIJRI ===");
		
		/* We use the default locale to print the name of days and months. */
		Locale locale = Locale.getDefault();
		
		/* Convert current date to Hijri calendar. */
		SimpleHijriDate hdate = HijriCalc.toHijri(new Date());
		
		/* Print it. */
		System.out.printf("%s, %s %s %s %s\n",
				hdate.getDayOfWeekName(locale),
				hdate.getDayOfMonth(),
				hdate.getMonthName(locale),
				hdate.getYear(),
				hdate.getEraName(locale));
		System.out.printf("%s %s-%s-%s\n",
				hdate.getDayOfWeekShortName(locale),
				hdate.getDayOfMonth(),
				hdate.getMonthShortName(locale),
				hdate.getYear());
		System.out.printf("%s-%s-%s\n",
				hdate.getDayOfMonth(),
				hdate.getMonth() + 1, // Month (and also day of week) is started from 0
				hdate.getYear());
	}
	
	public static void showUmmAlqura() {
		System.out.println("=== UMM AL-QURA ===");
		
		/* We use the default locale to print the name of days and months. */
		Locale locale = Locale.getDefault();
		
		/* Convert current date to Umm Al-Qura calendar. */
		SimpleHijriDate hdate = HijriCalc.toUmmAlqura(new Date());
		
		/* Print it. */
		System.out.printf("%s, %s %s %s %s\n",
				hdate.getDayOfWeekName(locale),
				hdate.getDayOfMonth(),
				hdate.getMonthName(locale),
				hdate.getYear(),
				hdate.getEraName(locale));
		System.out.printf("%s %s-%s-%s\n",
				hdate.getDayOfWeekShortName(locale),
				hdate.getDayOfMonth(),
				hdate.getMonthShortName(locale),
				hdate.getYear());
		System.out.printf("%s-%s-%s\n",
				hdate.getDayOfMonth(),
				hdate.getMonth() + 1, // Month (as well as day of week) is started from 0
				hdate.getYear());
	}
	
	public static void showPrayerTime() {
		System.out.println("=== PRAYER TIME ===");
		
		/* As an example, we use the location of Depok (West Java, Indonesia) and
		 * Egyptian General Authority of Survey method. */
		Location location = new Location(-6.37812775, 106.8342445, +7, 0);
		Method method = Method.EGYPT_SURVEY;
		
		/* Instantiate the calculator. */
		PrayerTimeCalc calculator = new PrayerTimeCalc(location, method);
		
		/* Calculate prayer times for today. */
		Date date = new Date();
		PrayerTimes prayerTimes = calculator.getPrayerTimes(date);
		PrayerTime imsakTime = calculator.getImsak(date);
		
		/* Print it (using default locale). */
		TimeNames names = TimeNames.getInstance(Locale.getDefault());
		System.out.printf("%s\t%s:%s\n",
				names.getImsak(),
				imsakTime.getHour(),
				imsakTime.getMinute());
		for (int i = 0; i < 6; ++i) {
			System.out.printf("%s\t%s:%s\n",
					names.get(i),
					prayerTimes.get(i).getHour(),
					prayerTimes.get(i).getMinute());
		}
		System.out.println("Qibla direction: " + calculator.getNorthQibla());
	}
}
