# بسم الله الرّحمٰن الرّحيم #

ITL Java is a Java library for calculating prayer (salat) times, qibla direction, and Hijri date.
This library is a porting of _the legendary_
[ITL (Islamic Tools and Libraries)](http://projects.arabeyes.org/project.php?proj=ITL) with Java
flavor. The original C version is available at
[arabeyes-org/ITL](https://github.com/arabeyes-org/ITL) repository.

## License ##

The original ITL is licensed under
[GNU LGPL (Lesser General Public License)](https://www.gnu.org/licenses/lgpl.html)
which means it is *free* and *remains free*: not only the "original" is free but
also the "derived", which implies that ITL Java is also LGPL-licensed. Any modification of the
library must be released (open-sourced) under LGPL license. Any program using this library must make
the users *free* (provide a flexible way) to change the library implementation/version used.

_For more flexible license please see [ICLib](https://github.com/fikr4n/iclib-java)._

## Usage examples ##

Examples below will show how simple using this library. Longer example is available in
[`Example.java`](app/src/main/java/example/Example.java)

**1. Prayer times and qibla direction**

```java
    // Initialize
    Prayer calculator = new Prayer()
            .setMethod(StandardMethod.EGYPT_SURVEY) // Egyptian General Authority of Survey
            .setLocation(-6.37812775, 106.8342445, 0) // lat, lng, height AMSL
            .setPressure(1010)
            .setTemperature(10)
            .setDate(new Date(), TimeZone.getDefault()); // today, here
    TimeNames names = TimeNames.getInstance(Locale.getDefault()); // for getting prayer names

    // Calculate prayer times
    for (Map.Entry<TimeType, PrayerTime> time : calculator.getPrayerTimes().entrySet()) {
        System.out.printf("%s %s\n",
                names.get(time.getKey()), // prayer name
                time.getValue()); // prayer time
    }
    // or
    for (PrayerTime time : calculator.getPrayerTimeArray()) {
        System.out.println(time.format("HH:mm:ss"));
    }

    // Calculate qibla direction
    System.out.println("Qibla: " + calculator.getNorthQibla());
```

**2. Hijri date**

```java
    // Initialize
    Hijri calculator = new Hijri(Locale.getDefault()); // locale used for names (months, etc)
    ConvertedDate date;

    // Gregorian to Hijri
    date = calculator.hDate(2, 12, 2016); // or hDate(new Date());
    System.out.println(date.format("EEEE, d MMMM yyyy G")); // converted date
    System.out.printf("%s-%s-%s\n", // converted date
            date.getDayOfMonth(),
            date.getMonth(),
            date.getYear());
    System.out.println(date.formatSource("EEE, dd-MM-yy")); // source date (before converted)
    System.out.println(date.toDate());

    // Hijri to Gregorian
    date = calculator.gDate(1, 9, 1438);
    System.out.println(date.format("EEE, d MMM yyyy G"));
```

**3. Umm Al-Qura date**

```java
    // Initialize
    UmmAlqura calculator = new UmmAlqura(Locale.getDefault());
    ConvertedDate date;

    // Gregorian to Hijri
    date = calculator.g2h(2, 12, 2016);
    System.out.println(date.format("EEEE, d MMMM yyyy"));

    // Hijri to Gregorian
    date = calculator.h2g(1, 9, 1438);
    System.out.println(date.format("EEE, d MMM yyyy"));
```

## Prayer times calculation methods ##

You can adjust your own method before calculating. However, it is encouraged
to use one of these built-in methods.

- **Egyptian General Authority of Survey** - Usually used in Indonesia, Iraq, Jordan, Lebanon,
  Malaysia, Singapore, Syria, parts of Africa, and parts of United States.
- **University of Islamic Sciences, Karachi (Shafi'i)** - Usually used in Iran, Kuwait, and parts of
  Europe.
- **University of Islamic Sciences, Karachi (Hanafi)** - Usually used in Afghanistan, Bangladesh,
  and India.
- **Islamic Society of North America** - Usually used in Canada, Parts of UK, and parts of United
  States.
- **Muslim World League (MWL)** - Usually used in parts of Europe, Far East, and parts of United
  States.
- **Umm Al-Qurra University** - Usually used in Saudi Arabia.
- **Fixed Ishaa Angle Interval (always 90)** - Usually used in Bahrain, Oman, Qatar, United Arab
  Emirates.
- **Moonsighting Committee Worldwide**
- **Morocco Awqaf Ministry, Morocco**

## Porting notes ##

Some differences between the original C and this Java version:

- `hijri/hijri.h` and `hijri/hijri.c` &rarr;
  `org.arabeyes.itl.hijri.HijriModule` and `org.arabeyes.itl.hijri.Hijri`
- `hijri/hijri.h/sDate` &rarr;
  `org.arabeyes.itl.hijri.HijriModule.sDate` and `org.arabeyes.itl.hijri.ConvertedDate`
- `hijri/umm_alqura.c` &rarr;
  `org.arabeyes.itl.hijri.UmmAlquraModule` and `org.arabeyes.itl.hijri.UmmAlqura`
- `prayertime/astro.h` and `prayertime/astro.c` &rarr;
  `org.arabeyes.itl.prayertime.AstroModule`
- `prayertime/prayer.h` and `prayertime/prayer.c` &rarr;
  `org.arabeyes.itl.prayertime.PrayerModule` and `org.arabeyes.itl.prayertime.Prayer`
- `prayertime/prayer.h/Prayer` &rarr;
  `org.arabeyes.itl.prayertime.PrayerTime`
- `prayertime/prayer.h/Method` &rarr;
  `org.arabeyes.itl.prayertime.Method`
- `prayertime/prayer.c/methods` &rarr;
  `org.arabeyes.itl.prayertime.StandardMethod`
- `prayertime/prayer.c/exmethods` &rarr;
  `org.arabeyes.itl.prayertime.ExtremeMethod`
- `prayertime/prayer.c/salatType` &rarr;
  `org.arabeyes.itl.prayertime.TimeType`
