package example;

import org.arabeyes.itl.hijri.ConvertedDate;
import org.arabeyes.itl.hijri.HijriCalc;
import org.arabeyes.itl.prayertime.Dms;
import org.arabeyes.itl.prayertime.PrayerTime;
import org.arabeyes.itl.prayertime.PrayerTimeCalc;
import org.arabeyes.itl.prayertime.StandardMethod;
import org.arabeyes.itl.prayertime.TimeNames;
import org.arabeyes.itl.prayertime.TimeType;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Example {

    public static void main(String[] args) {
        showPrayerTime();
        showHijri();
        showUmmAlqura();
    }
    
    public static void showHijri() {
        System.out.println("=== HIJRI ===");

        HijriCalc calculator = new HijriCalc(null);
        // or
        new HijriCalc(Locale.getDefault());

        ConvertedDate date = calculator.toHijri(2016, 12, 2);
        // or
        calculator.toHijri(new Date());

        System.out.println(date.format("EEEE, d MMMM yyyy G"));
        System.out.println(date.format("EEE, d MMM yyyy G"));
        System.out.println(date.format("EEE, dd-MM-yy G"));
//        System.out.println(date.formatSource("EEEE, d MMMM yyyy G"));
        System.out.println(date.toDate());
        System.out.printf("%s, %s %s %s %s\n",
                date.getDayOfWeekName(),
                date.getDayOfMonth(),
                date.getMonthName(),
                date.getYear(),
                date.getEraName());
        System.out.printf("%s %s-%s-%s\n",
                date.getDayOfWeekShortName(),
                date.getDayOfMonth(),
                date.getMonthShortName(),
                date.getYear());
        System.out.printf("%s-%s-%s\n",
                date.getDayOfMonth(),
                date.getMonth(),
                date.getYear());

        date = calculator.fromHijri(1438, 9, 1);
        System.out.println(date.format("EEEE, d MMMM yyyy G"));
//        System.out.println(date.formatSource("EEEE, d MMMM yyyy G"));
        System.out.println(date.toDate());
    }
    
    public static void showUmmAlqura() {
        System.out.println("=== UMM AL-QURA ===");

        HijriCalc calculator = new HijriCalc(null);
        // or
        new HijriCalc(Locale.getDefault());

        ConvertedDate date = calculator.toUmmAlqura(2016, 12, 2);
        // or
        calculator.toUmmAlqura(new Date());

        System.out.println(date.format("EEEE, d MMMM yyyy G"));
        System.out.println(date.format("EEE, d MMM yyyy G"));
        System.out.println(date.format("EEE, dd-MM-yy G"));
//        System.out.println(date.formatSource("EEEE, d MMMM yyyy G"));
        System.out.println(date.toDate());
        System.out.printf("%s, %s %s %s %s\n",
                date.getDayOfWeekName(),
                date.getDayOfMonth(),
                date.getMonthName(),
                date.getYear(),
                date.getEraName());
        System.out.printf("%s %s-%s-%s\n",
                date.getDayOfWeekShortName(),
                date.getDayOfMonth(),
                date.getMonthShortName(),
                date.getYear());
        System.out.printf("%s-%s-%s\n",
                date.getDayOfMonth(),
                date.getMonth(),
                date.getYear());

        date = calculator.fromUmmAlqura(1438, 9, 1);
        System.out.println(date.format("EEEE, d MMMM yyyy G"));
//        System.out.println(date.formatSource("EEEE, d MMMM yyyy G"));
        System.out.println(date.toDate());
    }
    
    public static void showPrayerTime() {
        System.out.println("=== PRAYER TIME ===");
        
        PrayerTimeCalc calculator = new PrayerTimeCalc()
                .setMethod(StandardMethod.EGYPT_SURVEY) // Egyptian General Authority of Survey
                .setLocation(-6.37812775, 106.8342445, 0) // lat, lng, height AMSL
                .setPressure(1010)
                .setTemperature(10)
                .setDate(new GregorianCalendar()); // today, here
        // or
        calculator.setDate(new Date(), TimeZone.getDefault());

        TimeNames names = TimeNames.getInstance(null);
        // or
        TimeNames.getInstance(Locale.getDefault());

        // Calculate and print each time
        for (TimeType i : TimeType.values()) {
            PrayerTime time = calculator.getTime(i);
            System.out.printf("%s\t%s:%s\n",
                    names.get(i),
                    time.getHour(),
                    time.getMinute());
        }

        // Calculate and print qibla direction
        Dms qibla = calculator.getQibla();
        System.out.println("Qibla: " + qibla);
        System.out.println("Qibla: " + qibla.toDecimal());
    }
}
