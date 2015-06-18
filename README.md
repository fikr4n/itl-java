# بسم الله الرّحمٰن الرّحيم #

ITL Java is a Java library based on LGPL-licensed
[ITL (Islamic Tools and Libraries)](http://projects.arabeyes.org/project.php?proj=ITL),
currently includes prayer times (salat), Hijri date, and qibla direction.

_The license is LGPL, for more flexible library please see [ICLib](https://github.com/fikr4n/iclib-java)._

## Usage examples ##

Examples below will show you how simple using this library for calculating prayer time, qibla
direction, and Hijri calendar. Besides that, built-in names are also included so you can
display it directly to the user.

### Prayer time ###

**Basic usage**

``` java
	/* As an example, we use Depok (West Java, Indonesia) as the location
	 * and Egyptian General Authority of Survey as the method. */
	
	// new Location(latitude, longitude, GMT diff, daylight saving time)
	Location location = new Location(-6.37812775, 106.8342445, +7, 0);
	PrayerTimeCalc calculator = new PrayerTimeCalc(location, Method.EGYPT_SURVEY);
	
	/* Calculate prayer times for today. */
    
	PrayerTimes prayerTimes = calculator.getPrayerTimes(new Date());
	
	/* Print Fajr and sunrise time. */
    
	PrayerTime fajr = prayerTimes.get(PrayerTimes.FAJR);
	System.out.printl(fajr.getHour() + ":" + fajr.getMinute());
```

**Getting qibla direction**

``` java
	PrayerTimeCalc calculator = . . .
	System.out.println(calculator.getNorthQibla());
	
	// Or simply
	
	Location location = . . .
	System.out.println(PrayerTimeCalc.getNorthQibla(location));
```

**Displaying along with built-in prayer names**

``` java
	PrayerTimes prayerTimes = . . .
	
	/* Print all prayers (Fajr .. Isha) and sunrise using default locale. */
	
	TimeNames names = TimeNames.getInstance(Locale.getDefault());
	for (int i = 0; i < 6; ++i) {
	
		/* PrayerTimes.FAJR, PrayerTimes.ZUHR, etc are integer constants from 0 to 5. */
		System.out.printf("%s\t%s:%s\n",
				names.get(i),
				prayerTimes.get(i).getHour(),
				prayerTimes.get(i).getMinute());
	}
```

**Using imsak**

Imsak (minutes before saum/fasting) is calculated separately, i.e. not using
`PrayerTimeCalc.getPrayerTimes()`.

``` java
	/* Print imsak for today. */
	
	PrayerTimeCalc calculator = . . .
	PrayerTime imsakTime = calculator.getImsak(new Date());
	System.out.println(imsakTime.getHour() + ":" + imsakTime.getMinute());
```

### Hijri (and Umm Al-Qura) date ###

*Caution:* the calculation result is only an estimation, not to be used as the
real reference. The error can be +1 or -1 day... Hmmm, I don't know if it can
also be +2 or -2 :).

**Basic usage**

``` java
	/* Convert current date to Hijri calendar. */
	
	SimpleHijriDate hdate = HijriCalc.toHijri(new Date());
	
	/* Like in standard Java, month starts from 0. */
	
	System.out.println(hdate.getYear() + "-" +
			(hdate.getMonth() + 1) + "-" +
			hdate.getDayOfMonth());
```

**Displaying along with built-in month names**

``` java
	SimpleHijriDate hdate = . . .
	
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
```
	
`get*Name()` above are the shortcuts for `HijriNames.getInstance(locale).get*Name()`.

**Using Umm Al-Qura**

``` java
	/* Convert current date to Umm Al-Qura calendar. */
	
	SimpleHijriDate hdate = HijriCalc.toUmmAlqura(new Date());
```

## Prayer times calculation methods ##

You can adjust your own method before calculating. However, it is encouraged
to use one of this built-in methods. They are available as constants in
`Method` class.

- **Egyptian General Authority of Survey**  
  Usually used in Indonesia, Iraq, Jordan, Lebanon, Malaysia, Singapore, Syria,
  parts of Africa, and parts of United States.
- **University of Islamic Sciences, Karachi (Shafi'i)**  
  Usually used in Iran, Kuwait, and parts of Europe.
- **University of Islamic Sciences, Karachi (Hanafi)**  
  Usually used in Afghanistan, Bangladesh, and India.
- **Islamic Society of North America**  
  Usually used in Canada, Parts of UK, and parts of United States.
- **Muslim World League (MWL)**  
  Usually used in parts of Europe, Far East, and parts of United States.
- **Umm Al-Qurra University**  
  Usually used in Saudi Arabia.
- **Fixed Ishaa Angle Interval (always 90)**  
  Usually used in Bahrain, Oman, Qatar, United Arab Emirates.

## License ##

The original ITL is licensed under
[GNU LGPL (Lesser General Public License)](https://www.gnu.org/licenses/lgpl.html)
which means it is *free* and *remains free*: not only the "original" is free but
also the "derived". So that, ITL Java is also LGPL-licensed. If legal words make
you dizzy (like me), type "lgpl v3 in short", "lgpl v3 tl;dr", etc in your
favorite search engine.

## Contribute ##

Do you want to contribute?

- Testing the calculation result (and improving the algorithm if necessary).
- Improving the Hijri calendar so that user can input the location.
- Translation (i18n); the name of prayers or months can be
  vary, for example in Indonesia Fajr is called Shubuh.
- Etc.

**Salam and have fun!**

