package example;

import org.arabeyes.itl.newmethod.MethodId;
import org.arabeyes.itl.newmethod.Prayer;
import org.arabeyes.itl.newmethod.PrayerTimes;

import java.util.GregorianCalendar;

public class NewMethod {
    public static void main(String[] args) {
        System.out.println("=== PRAYER TIME (NEW METHOD) ===");

        Prayer calculator = new Prayer()
                .setCalculationMethod(MethodId.EGAS)
                .setDate(new GregorianCalendar())
                .setLocation(-6.37812775, 106.8342445, 0); // lat, lng, height AMSL

        PrayerTimes times = calculator.getPrayerTimes();
        System.out.printf("Fajr\t%02d:%02d\n",
                times.getFajr().getHour(), times.getFajr().getMinute());
        System.out.printf("Sunrise\t%s\n",
                times.getSunrise().format("HH:mm"));
        System.out.println("Zuhr\t" + times.getDhuhr());
        System.out.println("Assr\t" + times.getAsr());
        System.out.println("Maghrib\t" + times.getMaghrib());
        System.out.println("Ishaa\t" + times.getIsha());

        System.out.println("Qibla: " + calculator.getQiblaDirection());
    }
}
