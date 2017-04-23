/*
 Islamic Prayer Times and Qibla Direction Library
 By Mohamed A.M. Bamakhrama (mohamed@alumni.tum.de)
 Licensed under the BSD license shown in file LICENSE

 This is a "clean-room" implementation that uses simpler but
 approximate methods. This implementation should be easier
 to maintain and debug. However, we need to first to test it
 throughly before replacing the old method. Currently, it
 supports calculating the prayer times and qibla location for
 "normal" latitudes. However, "extreme latitude" methods are
 still missing.
*/
package org.arabeyes.itl.newmethod;

import org.arabeyes.itl.newmethod.DefsModule.approx_sun_coord;
import org.arabeyes.itl.newmethod.DefsModule.date_t;
import org.arabeyes.itl.newmethod.DefsModule.location;
import org.arabeyes.itl.newmethod.DefsModule.round_t;

import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static org.arabeyes.itl.newmethod.DefsModule.KAABA_LATITUDE;
import static org.arabeyes.itl.newmethod.DefsModule.KAABA_LONGITUDE;
import static org.arabeyes.itl.newmethod.DefsModule.M_PI;
import static org.arabeyes.itl.newmethod.DefsModule.ONE_MINUTE;

class PrayerModule {
    static CalcMethod calc_methods[] = {
            new CalcMethod(MethodId.MWL, "Muslim World League (MWL)",
                    IshaFlag.ANGLE, 18, 17),
            new CalcMethod(MethodId.ISNA, "Islamic Society of North America (ISNA)",
                    IshaFlag.ANGLE, 15, 15),
            new CalcMethod(MethodId.EGAS, "Egyptian General Authority of Survey",
                    IshaFlag.ANGLE, 19.5, 17.5),
            new CalcMethod(MethodId.UMAQ, "Umm Al-Qura University, Makkah",
                    IshaFlag.OFFSET, 18.5, 90),
            new CalcMethod(MethodId.UIS, "University of Islamic Sciences, Karachi",
                    IshaFlag.ANGLE, 18, 18)
    };

    /**
     * Convert a given angle specified in degrees into radians
     */
    private static double to_radians(double x) {
        return ((x) * M_PI) / 180.0;
    }

    /**
     * Convert a given angle specified in radians into degrees
     */
    private static double to_degrees(double x) {
        return ((x) * 180.0) / M_PI;
    }

    /**
     * Compute the arccotangent of a given value
     */
    private static double arccot(double x) {
        return atan2(1.0, (x));
    }

    /**
     * Normalizes the given value x to be within the range [0,N]
     */
    private static double normalize(double x, double N) {
        double n;
        assert (N > 0.0);
        n = x - (N * floor(x / N));
        assert (n <= N);
        return n;
    }

    /**
     * round is avalilable only in C99. Therefore, we use a custom one
     *
     * @return The rounded value of x
     */
    private static double custom_round(double x) {
        return floor(x + 0.5);
    }

    /**
     * Compute the Julian Day Number for a given date
     * Source: https://en.wikipedia.org/wiki/Julian_day
     *
     * @return The Julian day number
     */
    private static int get_julian_day_number(date_t date) {
        double a, y, m, jdn;

        assert (date != null);

        a = floor((14.0 - (double) (date.month)) / 12.0);
        y = (double) (date.year) + 4800.0 - a;
        m = (double) (date.month) + 12.0 * a - 3.0;
        jdn = (double) date.day +
                floor((153.0 * m + 2.0) / 5.0) +
                365.0 * y +
                floor(y / 4.0) -
                floor(y / 100.0) +
                floor(y / 400.0) -
                32045.0;

        return (int) jdn;
    }

    /**
     * Compute the approximate Sun coordinates from the given Julian Day
     * Number jdn according to the method given the US Naval Observatory (USNO)
     * on: http://aa.usno.navy.mil/faq/docs/SunApprox.php
     * The computed values are stored in struct coord.
     */
    private static void get_approx_sun_coord(final int jdn,
                                             approx_sun_coord coord) {
        double d;   /* Julian Day Number from 1 January 2000 */
        double g;   /* Mean anomaly of the Sun */
        double q;   /* Mean longitude of the Sun */
        double L;   /* Geocentric apparent ecliptic longitude of the Sun
                   (adjusted for aberration) */
        double R;   /* The approximated distance of the Sun from the Earth
                   in astronomical units (AU) */
        double e;   /* The mean obliquity of the ecliptic */
        double RA;  /* The Sun's right ascension */
        double D;   /* The Sun's Declination */
        double EqT; /* The Equation of Time */
        double SD;  /* The angular semidiameter of the Sun in degrees */

        assert (coord != null);
        assert (jdn > 2451545);

        d = (double) (jdn - 2451545); /* Remove the offset from jdn */
        g = 357.529 + 0.98560028 * d;
        q = 280.459 + 0.98564736 * d;
        L = q + 1.915 * sin(to_radians(g)) +
                0.020 * sin(to_radians(2.0 * g));
        R = 1.00014 - 0.01671 * cos(to_radians(g)) -
                0.00014 * cos(to_radians(2.0 * g));
        e = 23.439 - 0.00000036 * d;
        RA = to_degrees(atan2(cos(to_radians(e)) * sin(to_radians(L)),
                cos(to_radians(L))) / 15.0);
        D = to_degrees(asin(sin(to_radians(e)) * sin(to_radians(L))));
        EqT = q / 15.0 - RA;

        /* Resulting EqT Can be larger than 360.
           Therefore, it needs normalization  */
        EqT = normalize(EqT, 360.0);
        SD = 0.2666 / R;

        coord.D = D;
        coord.EqT = EqT;
        coord.R = R;
        coord.SD = SD;
    }

    /**
     * T function used to compute Sunrise, Sunset, Fajr, and Isha
     * Taken from: http://praytimes.org/calculation/
     */
    private static double T(final double alpha,
                            final double latitude,
                            final double D) {
        double p1 = 1.0 / 15.0;
        double p2 = cos(to_radians(latitude)) * cos(to_radians(D));
        double p3 = sin(to_radians(latitude)) * sin(to_radians(D));
        double p4 = -1.0 * sin(to_radians(alpha));
        double p5 = to_degrees(acos((p4 - p3) / p2));
        double r = p1 * p5;
        return r;
    }

    /**
     * A function used to compute Asr
     * Taken from: http://praytimes.org/calculation/
     */
    private static double A(final double t,
                            final double latitude,
                            final double D) {
        double p1 = 1.0 / 15.0;
        double p2 = cos(to_radians(latitude)) * cos(to_radians(D));
        double p3 = sin(to_radians(latitude)) * sin(to_radians(D));
        double p4 = tan(to_radians((latitude - D)));
        double p5 = arccot(t + p4);
        double p6 = sin(p5);
        double p7 = acos((p6 - p3) / p2);
        double r = p1 * to_degrees(p7);
        return r;
    }

    /**
     * Compute the Dhuhr prayer time
     */
    private static double get_dhuhr(location loc,
                                    approx_sun_coord coord) {
        double dhuhr = 0.0;

        assert (loc != null);
        assert (coord != null);

        dhuhr = 12.0 + loc.timezone - loc.longitude / 15.0 - coord.EqT;
        dhuhr = normalize(dhuhr, 24.0);
        return dhuhr;
    }

    /**
     * Compute the Asr prayer time
     */
    private static double get_asr(final double dhuhr,
                                  location loc,
                                  approx_sun_coord coord) {
        double asr = 0.0;

        assert (dhuhr > 0.0);
        assert (loc != null);
        assert (coord != null);
        assert (loc.asr_method == AsrMethod.SHAFII || loc.asr_method == AsrMethod.HANAFI);

        switch (loc.asr_method) {
            case SHAFII:
                asr = dhuhr + A(1.0, loc.latitude, coord.D);
                break;
            case HANAFI:
                asr = dhuhr + A(2.0, loc.latitude, coord.D);
                break;
        }
        asr = normalize(asr, 24.0);
        return asr;
    }

    /**
     * Compute the Sunrise time
     * TODO: Check if this is valid for extreme altitudes
     */
    private static double get_sunrise(final double dhuhr,
                                      location loc,
                                      approx_sun_coord coord) {
        assert (loc != null);
        assert (coord != null);

        return (dhuhr - T(0.8333 + 0.0347 * sqrt(loc.altitude),
                loc.latitude, coord.D));
    }

    /**
     * Compute the Sunset time
     * TODO: Check if this is valid for extreme altitudes
     */
    private static double get_sunset(final double dhuhr,
                                     location loc,
                                     approx_sun_coord coord) {
        assert (loc != null);
        assert (coord != null);

        return (dhuhr + T(0.8333 + 0.0347 * sqrt(loc.altitude),
                loc.latitude, coord.D));
    }


    /**
     * Compute the Fajr prayer time
     */
    private static double get_fajr(final double dhuhr,
                                   final double sunset,
                                   final double sunrise,
                                   location loc,
                                   approx_sun_coord coord) {
        double fajr = 0.0;

        assert (dhuhr > 0.0);
        assert (loc != null);
        assert (coord != null);

        if (loc.extr_method == ExtremeMethod.NONE) {
            /* Normal latitude. Use the classical methods */
            fajr = dhuhr - T(loc.calc_method.fajr,
                    loc.latitude,
                    coord.D);
        } else {
            /* TODO: Extreme methods */
            throw new UnsupportedOperationException("Extreme methods are not implemented yet");
        }

        return fajr;
    }

    /**
     * Compute the Isha prayer time
     */
    private static double get_isha(final double dhuhr,
                                   final double sunset,
                                   final double sunrise,
                                   location loc,
                                   approx_sun_coord coord) {
        double isha = 0.0;

        assert (dhuhr > 0.0);
        assert (loc != null);
        assert (coord != null);

        if (loc.extr_method == ExtremeMethod.NONE) {
            if (loc.calc_method.isha_type == IshaFlag.OFFSET) {
                /* Umm Al-Qura uses a fixed offset */
                isha = sunset + loc.calc_method.isha * ONE_MINUTE;
            } else {
                isha = dhuhr + T(loc.calc_method.isha,
                        loc.latitude,
                        coord.D);
            }
        } else {
            /* TODO: Extreme latitude */
            throw new UnsupportedOperationException("Extreme altitudes are not implemented yet!");
        }

        isha = normalize(isha, 24.0);
        return isha;
    }

    /**
     * Convert a given time in decimal format to
     * hh:mm format and store it in the given event struct.
     * The minutes part can be rounded up or down based on the
     * round flag
     */
    private static void conv_time_to_event(final int julian_day,
                                           final double decimal_time,
                                           final round_t rounding,
                                           Event t) {
        double r = 0.0, f = 0.0;

        assert (julian_day > 0);
        assert (decimal_time >= 0.0 && decimal_time <= 24.0);
        assert (t != null);
        assert (rounding == round_t.UP ||
                rounding == round_t.DOWN ||
                rounding == round_t.NEAREST);

        t.julian_day = julian_day;
        f = floor(decimal_time);
        t.hour = (int) f;
        r = (decimal_time - f) * 60.0;
        switch (rounding) {
            case UP:
                t.minute = (int) (ceil(r));
                break;
            case DOWN:
                t.minute = (int) (floor(r));
                break;
            case NEAREST:
                t.minute = (int) (custom_round(r));
                break;
        }
    }

    /**
     * Compute the Qibla direction from North clockwise
     * using the Equation (1) given in references/qibla.pdf
     */
    static double get_qibla_direction(location loc) {
        double p1, p2, p3, qibla;

        assert (loc != null);

        p1 = sin(to_radians(KAABA_LONGITUDE - loc.longitude));
        p2 = cos(to_radians(loc.latitude)) *
                tan(to_radians(KAABA_LATITUDE));
        p3 = sin(to_radians(loc.latitude)) *
                cos(to_radians(KAABA_LONGITUDE - loc.longitude));
        qibla = to_degrees(atan2(p1, (p2 - p3)));

        return qibla;
    }

    static void get_prayer_times(date_t date,
                                 location loc,
                                 PrayerTimes pt) {
        int jdn, jdn_next, jdn_prev;
        double true_noon, sunrise, sunset;
        double noon_next, sunrise_next;
        double noon_prev, sunset_prev;
        double fajr, dhuhr, asr, maghrib, isha;
        final approx_sun_coord coord = new approx_sun_coord(), coord_next = new approx_sun_coord(),
                coord_prev = new approx_sun_coord();

        assert (date != null);
        assert (loc != null);
        assert (pt != null);

        jdn = get_julian_day_number(date);
        jdn_next = jdn + 1;
        jdn_prev = jdn - 1;

        get_approx_sun_coord(jdn, coord);
        get_approx_sun_coord(jdn_next, coord_next);
        get_approx_sun_coord(jdn_prev, coord_prev);

        true_noon = get_dhuhr(loc, coord);
        noon_next = get_dhuhr(loc, coord_next);
        noon_prev = get_dhuhr(loc, coord_prev);

        sunrise = get_sunrise(true_noon, loc, coord);
        sunset = get_sunset(true_noon, loc, coord);
        sunrise_next = get_sunrise(noon_next, loc, coord_next);
        sunset_prev = get_sunset(noon_prev, loc, coord_prev);

        fajr = get_fajr(true_noon, sunset_prev, sunrise, loc, coord);
        dhuhr = true_noon;
        asr = get_asr(true_noon, loc, coord);
        maghrib = sunset;
        isha = get_isha(true_noon, sunset, sunrise_next, loc, coord);

        /* TODO: Find what Fiqh says regarding the rounding... */
        conv_time_to_event(jdn, fajr, round_t.NEAREST, (pt.fajr));
        conv_time_to_event(jdn, sunrise, round_t.NEAREST, (pt.sunrise));
        conv_time_to_event(jdn, dhuhr, round_t.NEAREST, (pt.dhuhr));
        conv_time_to_event(jdn, asr, round_t.NEAREST, (pt.asr));
        conv_time_to_event(jdn, maghrib, round_t.NEAREST, (pt.maghrib));
        conv_time_to_event(jdn, isha, round_t.NEAREST, (pt.isha));
    }
}
