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

### Prayer time and qibla direction ###

// TODO

### Hijri and Umm Al-Qura date ###

// TODO

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
- **Morocco Awqaf, Morocco**

## Porting notes ##

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
