package org.arabeyes.itl.prayertime;

import java.text.DecimalFormat;

import static java.lang.Math.abs;

public class Dms {
    private final int deg;
    private final int min;
    private final double sec;

    public Dms(int deg, int min, double sec) {
        if (min > 60 || sec > 60.0 ||
                (deg > 0 && (min < 0 || sec < 0)) ||
                (deg < 0 && (min > 0 || sec > 0)))
            throw new IllegalArgumentException(String.format("deg=%s, min=%s, sec=%s", deg, min, sec));
        this.deg = deg;
        this.min = min;
        this.sec = sec;
    }

    public static Dms fromDecimal(double decimal) {
        return PrayerModule.decimal2Dms(decimal);
    }

    public int getDegree() {
        return deg;
    }

    public int getMinute() {
        return min;
    }

    public double getSecond() {
        return sec;
    }

    public double toDecimal() {
        return PrayerModule.dms2Decimal(deg, min, sec, ' ');
    }

    @Override
    public String toString() {
        String sign = "";
        if (deg < 0 || min < 0 || sec < 0.0)
            sign = "-";
        return sign + abs(deg) + '°' + abs(min) + '′' + abs(sec) + '″';
    }

    public String toString(DecimalFormat secFormat) {
        String pre = deg < 0 || min < 0 || sec < 0.0 ? secFormat.getNegativePrefix() :
                secFormat.getPositivePrefix();
        String post = deg < 0 || min < 0 || sec < 0.0 ? secFormat.getNegativeSuffix() :
                secFormat.getPositiveSuffix();
        return pre + abs(deg) + '°' + abs(min) + '′' + secFormat.format(abs(sec)) + '″' + post;
    }
}
