package example;

import org.arabeyes.itl.hijri.ConvertedDate;
import org.arabeyes.itl.hijri.Hijri;
import org.arabeyes.itl.hijri.UmmAlqura;
import org.arabeyes.itl.prayertime.Dms;
import org.arabeyes.itl.prayertime.Method;
import org.arabeyes.itl.prayertime.Prayer;
import org.arabeyes.itl.prayertime.PrayerTime;
import org.arabeyes.itl.prayertime.StandardMethod;
import org.arabeyes.itl.prayertime.TimeNames;
import org.arabeyes.itl.prayertime.TimeType;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Example {
    // TODO: 2017-04-01 mark exported classes and methods
    // TODO: 2017-04-01 implement new method algorithm
    // TODO: 2017-04-01 create unit tests

    public static void main(String[] args) {
        showPrayerTime();
        showHijri();
        showUmmAlqura();
    }

    private static void showPrayerTime() {
        System.out.println("=== PRAYER TIME ===");

        // Initialize
        Prayer calculator = new Prayer()
                .setMethod(StandardMethod.EGYPT_SURVEY) // Egyptian General Authority of Survey
                .setLocation(-6.37812775, 106.8342445, 0) // lat, lng, height AMSL
                .setPressure(1010)
                .setTemperature(10)
                .setDate(new GregorianCalendar()); // today, here
        // or
        calculator.setDate(new Date(), TimeZone.getDefault());
        // or
        calculator.setMethod(Method.fromStandard(StandardMethod.EGYPT_NEW)
                .setUseOffset(true)
                .setOffsetMinutes(new double[] {
                        0, // fajr
                        -0.5, // sunrise -30 sec
                        2, // zuhr +2 min
                        0, // assr
                        0, // maghrib
                        0, // ishaa
                }));

        TimeNames names = TimeNames.getInstance(null);
        // or
        TimeNames.getInstance(Locale.getDefault());

        // Calculate and print each time
        for (Map.Entry<TimeType, PrayerTime> time : calculator.getPrayerTimes().entrySet()) {
            System.out.printf("%s\t%02d:%02d\n",
                    names.get(time.getKey()),
                    time.getValue().getHour(),
                    time.getValue().getMinute());
        }
        // or
        for (PrayerTime time : calculator.getPrayerTimeArray()) {
            System.out.println(time);
        }
        System.out.printf("%s:\t%s\n", names.get(TimeType.IMSAAK), calculator.getImsaak());
        System.out.printf("%s:\t%s\n", names.get(TimeType.NEXTFAJR), calculator.getNextDayFajr());
        System.out.println("Tomorrow Imsaak: " + calculator.getNextDayImsaak());

        // Calculate and print qibla direction
        Dms qibla = calculator.getNorthQibla();
        System.out.println("Qibla: " + qibla);
        System.out.println("Qibla: " + qibla.toDecimal());
    }
    
    private static void showHijri() {
        System.out.println("=== TO HIJRI ===");

        // Initialize
        Hijri calculator = new Hijri(null);
        // or
        new Hijri(Locale.getDefault());

        ConvertedDate date;

        // Gregorian to Hijri
        date = calculator.hDate(2016, 12, 2);
        // or
        calculator.hDate(new Date());
        // Print
        System.out.println(date.format("EEEE, d MMMM yyyy G ('long')"));
        System.out.println(date.format("EEE, d MMM yyyy G '''''''MMMedium'"));
        System.out.println(date.format("EEE, dd-MM-yy G '''short'''"));
        System.out.println(date.formatSource("EEEE, d MMMM yyyy"));
        System.out.println(date.formatSource("EEE, d MMM yyyy"));
        System.out.println(date.formatSource("EEE, dd-MM-yy"));
        System.out.println(date.toDate());
        // or manually
        System.out.printf("Long: %s, %s %s %s %s\n",
                date.getDayOfWeekName(),
                date.getDayOfMonth(),
                date.getMonthName(),
                date.getYear(),
                date.getEraName());
        System.out.printf("Medium: %s %s-%s-%s\n",
                date.getDayOfWeekShortName(),
                date.getDayOfMonth(),
                date.getMonthShortName(),
                date.getYear());
        System.out.printf("Short: %s-%s-%s\n",
                date.getDayOfMonth(),
                date.getMonth(),
                date.getYear());

        System.out.println("=== FROM HIJRI ===");

        // Hijri to Gregorian
        date = calculator.gDate(1438, 9, 1);
        // Print
        System.out.println(date.format("EEEE, d MMMM yyyy G"));
        System.out.println(date.format("EEE, d MMM yyyy G"));
        System.out.println(date.format("EEE, dd-MM-yy G"));
        System.out.println(date.formatSource("EEEE, d MMMM yyyy"));
        System.out.println(date.formatSource("EEE, d MMM yyyy"));
        System.out.println(date.formatSource("EEE, dd-MM-yy"));
        System.out.println(date.toDate());
    }
    
    private static void showUmmAlqura() {
        System.out.println("=== TO UMM AL-QURA ===");

        // Initialize
        UmmAlqura calculator = new UmmAlqura(null);
        // or
        new UmmAlqura(Locale.getDefault());

        ConvertedDate date;

        // Gregorian to Hijri
        date = calculator.g2h(2016, 12, 2);
        // or
        calculator.g2h(new Date());
        // Print
        System.out.println(date.format("EEEE, d MMMM yyyy G"));
        System.out.println(date.format("EEE, d MMM yyyy G"));
        System.out.println(date.format("EEE, dd-MM-yy G"));
        System.out.println(date.formatSource("EEEE, d MMMM yyyy"));
        System.out.println(date.formatSource("EEE, d MMM yyyy"));
        System.out.println(date.formatSource("EEE, dd-MM-yy"));
        System.out.println(date.toDate());

        System.out.println("=== FROM UMM AL-QURA ===");

        // Hijri to Gregorian
        date = calculator.h2g(1438, 9, 1);
        // Print
        System.out.println(date.format("EEEE, d MMMM yyyy G"));
        System.out.println(date.format("EEE, d MMM yyyy G"));
        System.out.println(date.format("EEE, dd-MM-yy G"));
        System.out.println(date.formatSource("EEEE, d MMMM yyyy"));
        System.out.println(date.formatSource("EEE, d MMM yyyy"));
        System.out.println(date.formatSource("EEE, dd-MM-yy"));
        System.out.println(date.toDate());
    }
}
