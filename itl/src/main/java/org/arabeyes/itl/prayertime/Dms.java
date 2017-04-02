/* Copyright (c) 2017, Fikrul Arif
 * (under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.prayertime;

import java.text.DecimalFormat;

import static java.lang.Math.abs;

/**
 * Represents degree, minute, and second of an angle.
 */
@SuppressWarnings("WeakerAccess")
public class Dms implements Comparable<Dms> {
    private final int deg;
    private final int min;
    private final double sec;

    /**
     * All numbers should be either positive or negative. Mixed sign has no meaning.
     *
     * @param deg degree
     * @param min -60 <= minute <= 60
     * @param sec -60 <= second <= 60
     * @throws IllegalArgumentException if sign is not consistent or exceeds the range
     */
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

    /**
     * Returns string representation in d°m′s.sss...″ format.
     */
    @Override
    public String toString() {
        String sign = "";
        if (deg < 0 || min < 0 || sec < 0.0)
            sign = "-";
        return sign + abs(deg) + '°' + abs(min) + '′' + abs(sec) + '″';
    }

    /**
     * Returns string representation in d°m′s.sss...″ format.
     *
     * @param secFormat used for positive/negative prefix/suffix of DMS and decimal format of
     *                  second
     */
    public String toString(DecimalFormat secFormat) {
        String pre = deg < 0 || min < 0 || sec < 0.0 ? secFormat.getNegativePrefix() :
                secFormat.getPositivePrefix();
        String post = deg < 0 || min < 0 || sec < 0.0 ? secFormat.getNegativeSuffix() :
                secFormat.getPositiveSuffix();
        return pre + abs(deg) + '°' + abs(min) + '′' + secFormat.format(abs(sec)) + '″' + post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dms dms = (Dms) o;
        return deg == dms.deg && min == dms.min && Double.compare(dms.sec, sec) == 0;
    }

    @Override
    public int hashCode() {
        int result = deg;
        result = 31 * result + min;
        long temp = Double.doubleToLongBits(sec);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * See {@link Comparable#compareTo(Object)}
     *
     * @throws ClassCastException if different class
     */
    @Override
    public int compareTo(Dms dms) {
        if (getClass() != dms.getClass())
            throw new ClassCastException("Can't compare with instance of " + dms.getClass());

        if (deg != dms.deg) return deg - dms.deg;
        if (min != dms.min) return min - dms.min;
        return Double.compare(sec, dms.sec);
    }
}
