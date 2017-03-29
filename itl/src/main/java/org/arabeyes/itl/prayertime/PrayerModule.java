/*  Copyright (c) 2003-2006, 2009, Arabeyes, Thamer Mahmoud
 *
 *  A full featured Muslim Prayer Times calculator
 *
 * (www.arabeyes.org - under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.prayertime;

import org.arabeyes.itl.prayertime.AstroModule.Astro;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static org.arabeyes.itl.prayertime.AstroModule.DEG_TO_10_BASE;
import static org.arabeyes.itl.prayertime.AstroModule.DEG_TO_RAD;
import static org.arabeyes.itl.prayertime.AstroModule.INVALID_TRIGGER;
import static org.arabeyes.itl.prayertime.AstroModule.RAD_TO_DEG;
import static org.arabeyes.itl.prayertime.AstroModule.getAstroValuesByDay;
import static org.arabeyes.itl.prayertime.AstroModule.getJulianDay;
import static org.arabeyes.itl.prayertime.AstroModule.getSunrise;
import static org.arabeyes.itl.prayertime.AstroModule.getSunset;
import static org.arabeyes.itl.prayertime.AstroModule.getTransit;

class PrayerModule {
    // Compatible with ITL 59bd87234aa87712a92b3fffb5497a43f121abf2

    /* This holds the current date info. */
    static class SDate {
        int day;
        int month;
        int year;

        SDate() {
        }

        SDate(SDate src) {
            this.day = src.day;
            this.month = src.month;
            this.year = src.year;
        }
    }

    /**
     * This holds the location info.
     */
    static class Location {
        double degreeLong;  /* Longitude in decimal degree. */
        double degreeLat;   /* Latitude in decimal degree. */
        double gmtDiff;     /* GMT difference at regular time. */
        int dst;            /* Daylight savings time switch (0 if not used).
                               Setting this to 1 should add 1 hour to all the
                               calculated prayer times */
        double seaLevel;    /* Height above Sea level in meters */
        double pressure;    /* Atmospheric pressure in millibars (the
                               astronomical standard value is 1010) */
        double temperature; /* Temperature in Celsius degree (the astronomical
                               standard value is 10) */

        Location() {
        }

        Location(Location src) {
            this.degreeLong = src.degreeLong;
            this.degreeLat = src.degreeLat;
            this.gmtDiff = src.gmtDiff;
            this.dst = src.dst;
            this.seaLevel = src.seaLevel;
            this.pressure = src.pressure;
            this.temperature = src.temperature;
        }
    }

    /* Defaults */
    private static final double KAABA_LAT = 21.423333;
    private static final double KAABA_LONG = 39.823333;
    private static final double DEF_NEAREST_LATITUDE = 48.5;
    private static final double DEF_EXTREME_LATITUDE = 55.0;
    private static final double DEF_IMSAAK_ANGLE = 1.5;
    private static final int DEF_IMSAAK_INTERVAL = 10;
    private static final int DEF_ROUND_SEC = 30;
    private static final int AGGRESSIVE_ROUND_SEC = 1;

    private PrayerModule() {
    }

    /* Astro astroCache;  This global variable is used for caching values between
     * multiple getPrayerTimesByDay() calls. You can disable this
     * caching feature by moving this line to the start of the
     * getPrayerTimesByDay() function. */

    /**
     * The "getPrayerTimes" function fills an array of six Prayer structures
     * "Prayer[6]". This list contains the prayer minutes and hours information
     * like this:
     * <p>
     * - Prayer[0].minute    is today's Fajr minutes
     * - Prayer[1].hour      is today's Shorooq hours
     * - ... and so on until...
     * - Prayer[5].minute    is today's Ishaa minutes
     */
    static PrayerTime[] getPrayerTimes(Location loc, Method conf, SDate date) {
        PrayerTime[] pt = new PrayerTime[6];
        for (int i = 0; i < pt.length; ++i)
            pt[i] = new PrayerTime();
        DayInfo di = getDayInfo(date, loc.gmtDiff);
        getPrayerTimesByDay(loc, conf, di.lastDay, di.dayOfYear, di.julianDay, pt, TimeType.FAJR);
        return pt;
    }

    private static void getPrayerTimesByDay(Location loc, Method conf,
                                            int lastDay, int dayOfYear, double julianDay,
                                            PrayerTime[] pt, TimeType type) {

        int i, invalid;
        double zu, sh, mg, fj, is, ar;
        double lat, lon, dec;
        double[] tempPrayer = new double[6];
        /* made as a local variable to avoid race condition between threads */
        Astro astroCache = new Astro();
        Astro tAstro = new Astro();

        lat = loc.degreeLat;
        lon = loc.degreeLong;
        invalid = 0;

        /* Start by filling the tAstro structure with the appropriate astronomical
         * values for this day. We also pass the cache structure to update and check
         * if the actual values are already available. */
        getAstroValuesByDay(julianDay, loc, astroCache, tAstro);
        dec = DEG_TO_RAD(tAstro.dec[1]);

        /* Get Prayer Times formulae results for this day of year and this
         * setLocation. The results are NOT the actual prayer times */
        fj = getFajIsh(lat, dec, conf.fajrAng);
        sh = getSunrise(loc, tAstro);
        zu = getZuhr(lon, tAstro);
        ar = getAssr(lat, dec, conf.mathhab);
        mg = getSunset(loc, tAstro);
        is = getFajIsh(lat, dec, conf.ishaaAng);
    
        /* Calculate all prayer times as Base-10 numbers in Normal circumstances */
        /* Fajr */
        if (fj == 99) {
            tempPrayer[0] = 99;
            if (conf.method != StandardMethod.MOONSIGHTING_COMMITTEE) {
                invalid = 1;
            }
        } else tempPrayer[0] = zu - fj;

        if (sh == 99)
            invalid = 1;
        tempPrayer[1] = sh;

        tempPrayer[2] = zu;

        /* Assr */
        if (ar == 99) {
            tempPrayer[3] = 99;
            invalid = 1;
        } else tempPrayer[3] = zu + ar;


        if (mg == 99)
            invalid = 1;
        tempPrayer[4] = mg;


        /* Ishaa */
        if (is == 99) {
            tempPrayer[5] = 99;
            if (conf.method != StandardMethod.MOONSIGHTING_COMMITTEE) {
                invalid = 1;
            }
        } else tempPrayer[5] = zu + is;


        if (conf.method == StandardMethod.MOONSIGHTING_COMMITTEE) {
            tempPrayer[0] = getSeasonalFajr(lat, dayOfYear, tempPrayer[0], tempPrayer[1]);
            tempPrayer[5] = getSeasonalIsha(lat, dayOfYear, tempPrayer[5], tempPrayer[4]);

            if (tempPrayer[0] == 99 || tempPrayer[5] == 99) {
                invalid = 1;
            }

            if (tempPrayer[2] != 99) {
                tempPrayer[2] += (5.0 / 60.0);
            }

            if (tempPrayer[4] != 99) {
                tempPrayer[4] += (3.0 / 60.0);
            }
        }
    
        /* Re-calculate Fajr and Ishaa in Extreme Latitudes */
        if (lat > conf.extremeLat) {
            tempPrayer[0] = 99;
            tempPrayer[5] = 99;
            invalid = 1;
        }

        /* Reset status of extreme switches */
        for (i = 0; i < 6; i++)
            pt[i].isExtreme = 0;

        if ((conf.extreme != ExtremeMethod.NONE_EX) && ((invalid == 1) ||
                (conf.extreme == ExtremeMethod.LAT_ALL ||
                        conf.extreme == ExtremeMethod.LAT_ALWAYS ||
                        conf.extreme == ExtremeMethod.GOOD_ALL ||
                        conf.extreme == ExtremeMethod.SEVEN_NIGHT_ALWAYS ||
                        conf.extreme == ExtremeMethod.SEVEN_DAY_ALWAYS ||
                        conf.extreme == ExtremeMethod.HALF_ALWAYS ||
                        conf.extreme == ExtremeMethod.MIN_ALWAYS)
        )) {
            double exdecPrev, exdecNext;
            double exZu = 99, exFj = 99, exIs = 99, exAr = 99, exSh = 99, exMg = 99;
            double portion = 0;
            double nGoodDay;
            int exinterval;
            final Location exLoc = new Location(loc);
            final Astro exAstroPrev;
            final Astro exAstroNext;
            double fajrDiff, ishaDiff;

            switch (conf.extreme) {
                /* Angle Based */
                case ANGLE_BASED:
                    portion = ((24 - tempPrayer[4]) + tempPrayer[1]);
                    fajrDiff = (1 / 60.0 * conf.fajrAng) * portion;
                    ishaDiff = (1 / 60.0 * conf.ishaaAng) * portion;

                    tempPrayer[0] = tempPrayer[1] - fajrDiff;
                    tempPrayer[5] = tempPrayer[4] + ishaDiff;
                    pt[0].isExtreme = 1;
                    pt[5].isExtreme = 1;
                    break;
            
                /* Nearest Latitude (Method.nearestLat) */
                case LAT_ALL:
                case LAT_ALWAYS:
                case LAT_INVALID:

                    /* FIXIT: we cannot compute this when interval is set because
                     * angle==0 . Only the if-invalid methods would work */
                    exLoc.degreeLat = conf.nearestLat;
                    exFj = getFajIsh(conf.nearestLat, dec, conf.fajrAng);
                    /*exIm = getFajIsh(conf.nearestLat, dec, conf.imsaakAng);*/
                    exSh = getSunrise(exLoc, tAstro);
                    exAr = getAssr(conf.nearestLat, dec, conf.mathhab);
                    exMg = getSunset(exLoc, tAstro);
                    exIs = getFajIsh(conf.nearestLat, dec, conf.ishaaAng);


                    switch (conf.extreme) {
                        case LAT_ALL:
                            tempPrayer[0] = zu - exFj;
                            tempPrayer[1] = exSh;
                            tempPrayer[3] = zu + exAr;
                            tempPrayer[4] = exMg;
                            tempPrayer[5] = zu + exIs;
                            pt[0].isExtreme = 1;
                            pt[1].isExtreme = 1;
                            pt[2].isExtreme = 1;
                            pt[3].isExtreme = 1;
                            pt[4].isExtreme = 1;
                            pt[5].isExtreme = 1;
                            break;

                        case LAT_ALWAYS:
                            tempPrayer[0] = zu - exFj;
                            tempPrayer[5] = zu + exIs;
                            pt[0].isExtreme = 1;
                            pt[5].isExtreme = 1;
                            break;

                        case LAT_INVALID:
                            if (tempPrayer[0] == 99) {
                                tempPrayer[0] = zu - exFj;
                                pt[0].isExtreme = 1;
                            }
                            if (tempPrayer[5] == 99) {
                                tempPrayer[5] = zu + exIs;
                                pt[5].isExtreme = 1;
                            }
                            break;
                    }
                    break;


                /* Nearest Good Day */
                case GOOD_ALL:
                case GOOD_INVALID:
                case GOOD_INVALID_SAME:

                    exAstroPrev = new Astro(astroCache);
                    exAstroNext = new Astro(astroCache);

                    /* Start by getting last or next nearest Good Day */
                    for (i = 0; i <= lastDay; i++) {

                        /* Last closest day */
                        nGoodDay = julianDay - i;
                        getAstroValuesByDay(nGoodDay, loc, exAstroPrev, tAstro);
                        exdecPrev = DEG_TO_RAD(tAstro.dec[1]);
                        exFj = getFajIsh(lat, exdecPrev, conf.fajrAng);
                        if (exFj != 99) {
                            exIs = getFajIsh(lat, exdecPrev, conf.ishaaAng);
                            if (exIs != 99) {
                                exZu = getZuhr(lon, tAstro);
                                exSh = getSunrise(loc, tAstro);
                                exAr = getAssr(lat, exdecPrev, conf.mathhab);
                                exMg = getSunset(loc, tAstro);
                                break;
                            }
                        }

                        /* Next closest day */
                        nGoodDay = julianDay + i;
                        getAstroValuesByDay(nGoodDay, loc, exAstroNext, tAstro);
                        exdecNext = DEG_TO_RAD(tAstro.dec[1]);
                        exFj = getFajIsh(lat, exdecNext, conf.fajrAng);
                        if (exFj != 99) {
                            exIs = getFajIsh(lat, exdecNext, conf.ishaaAng);
                            if (exIs != 99) {
                                exZu = getZuhr(lon, tAstro);
                                exSh = getSunrise(loc, tAstro);
                                exAr = getAssr(lat, exdecNext, conf.mathhab);
                                exMg = getSunset(loc, tAstro);
                                break;
                            }
                        }
                    }

                    switch (conf.extreme) {
                        case GOOD_ALL:
                            tempPrayer[0] = exZu - exFj;
                            tempPrayer[1] = exSh;
                            tempPrayer[2] = exZu;
                            tempPrayer[3] = exZu + exAr;
                            tempPrayer[4] = exMg;
                            tempPrayer[5] = exZu + exIs;
                            for (i = 0; i < 6; i++)
                                pt[i].isExtreme = 1;
                            break;
                        case GOOD_INVALID:
                            if (tempPrayer[0] == 99) {
                                tempPrayer[0] = exZu - exFj;
                                pt[0].isExtreme = 1;
                            }
                            if (tempPrayer[5] == 99) {
                                tempPrayer[5] = exZu + exIs;
                                pt[5].isExtreme = 1;
                            }
                            break;
                        case GOOD_INVALID_SAME:
                            if ((tempPrayer[0] == 99) || (tempPrayer[5] == 99)) {
                                tempPrayer[0] = exZu - exFj;
                                pt[0].isExtreme = 1;
                                tempPrayer[5] = exZu + exIs;
                                pt[5].isExtreme = 1;
                            }
                            break;
                    }
                    break;

                case SEVEN_NIGHT_ALWAYS:
                case SEVEN_NIGHT_INVALID:
                case SEVEN_DAY_ALWAYS:
                case SEVEN_DAY_INVALID:
                case HALF_ALWAYS:
                case HALF_INVALID:

                    /* FIXIT: For clarity, we may need to move the HALF_* methods
                     * into their own separate case statement. */
                    switch (conf.extreme) {
                        case SEVEN_NIGHT_ALWAYS:
                        case SEVEN_NIGHT_INVALID:
                            portion = (24 - (tempPrayer[4] - tempPrayer[1])) * (1 / 7.0);
                            break;
                        case SEVEN_DAY_ALWAYS:
                        case SEVEN_DAY_INVALID:
                            portion = (tempPrayer[4] - tempPrayer[1]) * (1 / 7.0);
                            break;
                        case HALF_ALWAYS:
                        case HALF_INVALID:
                            portion = (24 - tempPrayer[4] - tempPrayer[1]) * (1 / 2.0);
                            break;
                    }


                    if (conf.extreme == ExtremeMethod.SEVEN_NIGHT_INVALID ||
                            conf.extreme == ExtremeMethod.SEVEN_DAY_INVALID ||
                            conf.extreme == ExtremeMethod.HALF_INVALID) {
                        if (tempPrayer[0] == 99) {
                            if (conf.extreme == ExtremeMethod.HALF_INVALID)
                                tempPrayer[0] = portion - (conf.fajrInv / 60.0);
                            else tempPrayer[0] = tempPrayer[1] - portion;
                            pt[0].isExtreme = 1;
                        }
                        if (tempPrayer[5] == 99) {
                            if (conf.extreme == ExtremeMethod.HALF_INVALID)
                                tempPrayer[5] = portion + (conf.ishaaInv / 60.0);
                            else tempPrayer[5] = tempPrayer[4] + portion;
                            pt[5].isExtreme = 1;
                        }
                    } else { /* for the always methods */

                        if (conf.extreme == ExtremeMethod.HALF_ALWAYS) {
                            tempPrayer[0] = portion - (conf.fajrInv / 60.0);
                            tempPrayer[5] = portion + (conf.ishaaInv / 60.0);
                        } else {
                            tempPrayer[0] = tempPrayer[1] - portion;
                            tempPrayer[5] = tempPrayer[4] + portion;
                        }
                        pt[0].isExtreme = 1;
                        pt[5].isExtreme = 1;
                    }
                    break;

                case MIN_ALWAYS:
                    /* Do nothing here because this is implemented through fajrInv and
                     * ishaaInv structure members */
                    tempPrayer[0] = tempPrayer[1];
                    tempPrayer[5] = tempPrayer[4];
                    pt[0].isExtreme = 1;
                    pt[5].isExtreme = 1;
                    break;

                case MIN_INVALID:
                    if (tempPrayer[0] == 99) {
                        exinterval = (int) (conf.fajrInv / 60.0);
                        tempPrayer[0] = tempPrayer[1] - exinterval;
                        pt[0].isExtreme = 1;
                    }
                    if (tempPrayer[5] == 99) {
                        exinterval = (int) (conf.ishaaInv / 60.0);
                        tempPrayer[5] = tempPrayer[4] + exinterval;
                        pt[5].isExtreme = 1;
                    }
                    break;
            } /* end switch */
        } /* end extreme */

        /* Apply intervals if set */
        if (conf.extreme != ExtremeMethod.MIN_INVALID &&
                conf.extreme != ExtremeMethod.HALF_INVALID &&
                conf.extreme != ExtremeMethod.HALF_ALWAYS) {
            if (conf.fajrInv != 0) {
                if (tempPrayer[1] != 99)
                    tempPrayer[0] = tempPrayer[1] - (conf.fajrInv / 60.0);
                else tempPrayer[0] = 99;
            }

            if (conf.ishaaInv != 0) {
                if (tempPrayer[4] != 99)
                    tempPrayer[5] = tempPrayer[4] + (conf.ishaaInv / 60.0);
                else tempPrayer[5] = 99;
            }
        }

        /* Final Step: Fill the Prayer array by doing decimal degree to
         * Prayer structure conversion */
        if (type == TimeType.IMSAAK || type == TimeType.NEXTFAJR)
            base6hm(tempPrayer[0], loc, conf, pt[0], type);
        else {
            for (i = 0; i < 6; i++)
                base6hm(tempPrayer[i], loc, conf, pt[i], TimeType.values()[i]);
        }
    }

    private static void base6hm(double bs, Location loc, Method conf,
                                PrayerTime pt, TimeType type) {
        double min, sec;

        /* Set to 99 and return if prayer is invalid */
        if (bs == 99) {
            pt.hour = 99;
            pt.minute = 99;
            pt.second = 0;
            return;
        }

        /* Add offsets */
        if (conf.offset == 1) {
            if (type == TimeType.IMSAAK || type == TimeType.NEXTFAJR)
                bs += (conf.offList[0] / 60.0);
            else bs += (conf.offList[type.ordinal()] / 60.0);
        }

        /* Fix after minus offsets before midnight */
        if (bs < 0) {
            while (bs < 0)
                bs = 24 + bs;
        }

        min = (bs - floor(bs)) * 60;
        sec = (min - floor(min)) * 60;

        /* Add rounding minutes */
        if (conf.round == Rounding.NORMAL) {
            if (sec >= DEF_ROUND_SEC)
                bs += 1 / 60.0;
        /* compute again */
            min = (bs - floor(bs)) * 60;
            sec = 0;

        } else if (conf.round == Rounding.SPECIAL || conf.round == Rounding.AGGRESSIVE) {
            switch (type) {
                case FAJR:
                case ZUHR:
                case ASSR:
                case MAGHRIB:
                case ISHAA:
                case NEXTFAJR:

                    if (conf.round == Rounding.SPECIAL) {
                        if (sec >= DEF_ROUND_SEC) {
                            bs += 1 / 60.0;
                            min = (bs - floor(bs)) * 60;
                        }
                    } else if (conf.round == Rounding.AGGRESSIVE) {
                        if (sec >= AGGRESSIVE_ROUND_SEC) {
                            bs += 1 / 60.0;
                            min = (bs - floor(bs)) * 60;
                        }
                    }
                    sec = 0;
                    break;

                case SHUROOQ:
                case IMSAAK:
                    sec = 0;
                    break;
            }
        }

        /* Add daylight saving time and fix after midnight times */
        bs += loc.dst;
        if (bs >= 24)
            bs = bs % 24;

        pt.hour = (int) bs;
        pt.minute = (int) min;
        pt.second = (int) sec;
    }

    static PrayerTime getImsaak(Location loc, Method conf, SDate date) {

        Method tmpConf;
        final PrayerTime[] temp = new PrayerTime[6];
        for (int i = 0; i < temp.length; ++i)
            temp[i] = new PrayerTime();

        tmpConf = new Method(conf);

        if (conf.fajrInv != 0) {
            if (conf.imsaakInv == 0)
                tmpConf.fajrInv += DEF_IMSAAK_INTERVAL;
            else tmpConf.fajrInv += conf.imsaakInv;

        } else if (conf.imsaakInv != 0) {
        /* use an inv even if al-Fajr is computed (Indonesia?) */
            tmpConf.offList[0] += (conf.imsaakInv * -1);
            tmpConf.offset = 1;
        } else {
            tmpConf.fajrAng += conf.imsaakAng;
        }

        DayInfo di = getDayInfo(date, loc.gmtDiff);
        getPrayerTimesByDay(loc, tmpConf, di.lastDay, di.dayOfYear, di.julianDay, temp, TimeType.IMSAAK);

        /* FIXIT: We probably need to check whether it's possible to compute
         * Imsaak normally for some extreme methods first */
        /* In case of an extreme Fajr time calculation use intervals for Imsaak and
         * compute again */
        if (temp[0].isExtreme != 0) {
            tmpConf = new Method(conf);
            if (conf.imsaakInv == 0) {
                tmpConf.offList[0] -= DEF_IMSAAK_INTERVAL;
                tmpConf.offset = 1;
            } else {
                tmpConf.offList[0] -= conf.imsaakInv;
                tmpConf.offset = 1;
            }
            getPrayerTimesByDay(loc, tmpConf, di.lastDay, di.dayOfYear, di.julianDay, temp, TimeType.IMSAAK);
        }

        return temp[0];

    }

    static PrayerTime getNextDayImsaak(Location loc, Method conf, SDate date) {
        /* Copy the date structure and increment for next day.*/
        SDate tempd = new SDate(date);
        tempd.day++;

        return getImsaak(loc, conf, tempd);

    }

    static PrayerTime getNextDayFajr(Location loc, Method conf, SDate date) {
        PrayerTime[] temp = new PrayerTime[6];
        for (int i = 0; i < temp.length; ++i)
            temp[i] = new PrayerTime();

        DayInfo di = getDayInfo(date, loc.gmtDiff);
        getPrayerTimesByDay(loc, conf, di.lastDay, di.dayOfYear + 1, di.julianDay + 1, temp, TimeType.NEXTFAJR);

        return temp[0];
    }

    private static double getFajIsh(double lat, double dec, double Ang) {
        double rlat = DEG_TO_RAD(lat);

        /* Compute the hour angle */
        double part1 = sin(DEG_TO_RAD(-Ang)) - (sin(rlat) * sin(dec));
        double part2 = cos(rlat) * cos(dec);
        double part3 = part1 / part2;

        if (part3 < -INVALID_TRIGGER || part3 > INVALID_TRIGGER)
            return 99;

        return DEG_TO_10_BASE * RAD_TO_DEG(acos(part3));
    }

    private static double getZuhr(double lon, Astro astro) {
        return getTransit(lon, astro);
    }

    private static double getAssr(double lat, double dec, Mathhab mathhab) {
        double part1, part2, part3, part4;
        double rlat = DEG_TO_RAD(lat);

        part1 = mathhab.assrRatio() + tan(abs(rlat - dec));
        part2 = atan(1.0 / part1);

        /* Compute the hour angle */
        part3 = sin(part2) - (sin(rlat) * sin(dec));
        part4 = (part3 / (cos(rlat) * cos(dec)));

        if (part4 < -INVALID_TRIGGER || part4 > INVALID_TRIGGER) {
            return 99;
        }

        return DEG_TO_10_BASE * RAD_TO_DEG(acos(part4));
    }

    private static double getSeasonalFajr(double lat, int day, double fajr, double sunrise) {
        double A, B, C, D;
        int DYY;
        double adjustedFajr;

        DYY = getSeasonDay(day, lat);

        A = 75 + 28.65 / 55.0 * abs(lat);
        B = 75 + 19.44 / 55.0 * abs(lat);
        C = 75 + 32.74 / 55.0 * abs(lat);
        D = 75 + 48.1 / 55.0 * abs(lat);

        if (DYY < 91) {
            A = A + (B - A) / 91.0 * DYY;
        } else if (DYY < 137) {
            A = B + (C - B) / 46.0 * (DYY - 91);
        } else if (DYY < 183) {
            A = C + (D - C) / 46.0 * (DYY - 137);
        } else if (DYY < 229) {
            A = D + (C - D) / 46.0 * (DYY - 183);
        } else if (DYY < 275) {
            A = C + (B - C) / 46.0 * (DYY - 229);
        } else if (DYY >= 275) {
            A = B + (A - B) / 91.0 * (DYY - 275);
        }

        adjustedFajr = sunrise - (floor(A) / 60.0);
        if (adjustedFajr > fajr || fajr == 99) {
            fajr = adjustedFajr;
        }

        return fajr;
    }

    private static double getSeasonalIsha(double lat, int day, double isha, double sunset) {
        double A, B, C, D;
        int DYY;
        double adjustedIsha;

        DYY = getSeasonDay(day, lat);

        A = 75 + 25.6 / 55.0 * abs(lat);
        B = 75 + 2.05 / 55.0 * abs(lat);
        C = 75 - 9.21 / 55.0 * abs(lat);
        D = 75 + 6.14 / 55.0 * abs(lat);

        if (DYY < 91) {
            A = A + (B - A) / 91.0 * DYY;
        } else if (DYY < 137) {
            A = B + (C - B) / 46.0 * (DYY - 91);
        } else if (DYY < 183) {
            A = C + (D - C) / 46.0 * (DYY - 137);
        } else if (DYY < 229) {
            A = D + (C - D) / 46.0 * (DYY - 183);
        } else if (DYY < 275) {
            A = C + (B - C) / 46.0 * (DYY - 229);
        } else if (DYY >= 275) {
            A = B + (A - B) / 91.0 * (DYY - 275);
        }

        adjustedIsha = sunset + (ceil(A) / 60.0);
        if (adjustedIsha < isha || isha == 99) {
            isha = adjustedIsha;
        }

        return isha;
    }

    private static int getSeasonDay(int dayOfYear, double lat) {
        int seasonDay;

        if (lat >= 0) {
            seasonDay = dayOfYear + 10;
            if (seasonDay > 365) {
                seasonDay = seasonDay - 365;
            }
        } else {
            seasonDay = dayOfYear - 172;
            if (seasonDay < 0) {
                seasonDay = seasonDay + 365;
            }
        }

        return seasonDay;
    }

    private static final char[][] getDayofYear_dayList = {
            {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
            {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
    };

    private static int getDayofYear(int year, int month, int day) {
        int i;
        int isLeap = ((year & 3) == 0) && ((year % 100) != 0
                || (year % 400) == 0) ? 1 : 0;

        for (i = 1; i < month; i++)
            day += getDayofYear_dayList[isLeap][i];

        return day;
    }

    static double dms2Decimal(int deg, int min, double sec, char dir) {
        double sum = deg + ((min / 60.0) + (sec / 3600.0));
        if (dir == 'S' || dir == 'W' || dir == 's' || dir == 'w')
            return sum * (-1.0);
        return sum;
    }

    static Dms decimal2Dms(double decimal) {
        int n1, n2;
        double tempmin, tempsec;

        n1 = (int) decimal;
        tempmin = (decimal - n1) * 60.0;
        n2 = (int) tempmin;
        tempsec = (tempmin - n2) * 60.0;

        return new Dms(n1, n2, tempsec);
    }

    private static class DayInfo {
        int lastDay;
        int dayOfYear;
        double julianDay;
    }

    private static DayInfo getDayInfo(SDate date, double gmt) {
        int ld;
        int dy;
        double jd;
        ld = getDayofYear(date.year, 12, 31);
        dy = getDayofYear(date.year, date.month, date.day);
        jd = getJulianDay(date, gmt);
        DayInfo dayInfo = new DayInfo();
        dayInfo.lastDay = ld;
        dayInfo.dayOfYear = dy;
        dayInfo.julianDay = jd;
        return dayInfo;
    }

    /**
     * This function is used to auto fill the Method structure with predefined
     * data. The supported auto-fill methods for calculations at normal
     * circumstances are:
     * <p>
     * 0: none. Set to default or 0
     * 1: Egyptian General Authority of Survey
     * 2: University of Islamic Sciences, Karachi (Shaf'i)
     * 3: University of Islamic Sciences, Karachi (Hanafi)
     * 4: Islamic Society of North America
     * 5: Muslim World League (MWL)
     * 6: Umm Al-Qurra, Saudi Arabia
     * 7: Fixed Ishaa Interval (always 90)
     * 8: Egyptian General Authority of Survey (Egypt)
     * 9: Umm Al-Qurra Ramadan, Saudi Arabia
     * 10: Moonsighting Committee Worldwide
     * 11: Morocco Awqaf, Morocco
     */
    static Method getMethod(StandardMethod n) {
        final Method conf = new Method();
        int i;
        conf.method = n;
        conf.fajrInv = 0;
        conf.ishaaInv = 0;
        conf.imsaakInv = 0;
        conf.mathhab = Mathhab.SHAFII;
        conf.round = Rounding.SPECIAL;
        conf.nearestLat = DEF_NEAREST_LATITUDE;
        conf.imsaakAng = DEF_IMSAAK_ANGLE;
        conf.extreme = ExtremeMethod.GOOD_INVALID;
        conf.extremeLat = DEF_EXTREME_LATITUDE;
        conf.offset = 0;
        for (i = 0; i < 6; i++) {
            conf.offList[i] = 0;
        }

        switch (n) {
            case NONE:
                conf.fajrAng = 0.0;
                conf.ishaaAng = 0.0;
                break;

            case EGYPT_SURVEY:
                conf.fajrAng = 20;
                conf.ishaaAng = 18;
                break;

            case KARACHI_SHAF:
                conf.fajrAng = 18;
                conf.ishaaAng = 18;
                break;

            case KARACHI_HANAF:
                conf.fajrAng = 18;
                conf.ishaaAng = 18;
                conf.mathhab = Mathhab.HANAFI;
                break;

            case NORTH_AMERICA:
                conf.fajrAng = 15;
                conf.ishaaAng = 15;
                break;

            case MUSLIM_LEAGUE:
                conf.fajrAng = 18;
                conf.ishaaAng = 17;
                break;

            case UMM_ALQURRA:
                conf.fajrAng = 18;
                conf.ishaaAng = 0.0;
                conf.ishaaInv = 90;
                break;

            case FIXED_ISHAA:
                conf.fajrAng = 19.5;
                conf.ishaaAng = 0.0;
                conf.ishaaInv = 90;
                break;

            case EGYPT_NEW:
                conf.fajrAng = 19.5;
                conf.ishaaAng = 17.5;
                break;

            case UMM_ALQURRA_RAMADAN:
                conf.fajrAng = 18;
                conf.ishaaAng = 0.0;
                conf.ishaaInv = 120;
                break;

            case MOONSIGHTING_COMMITTEE:
                conf.fajrAng = 18;
                conf.ishaaAng = 18;
                break;

            case MOROCCO_AWQAF:
                conf.fajrAng = 19;
                conf.ishaaAng = 17;
                conf.offset = 1;
                conf.offList[2] = 5;
                conf.offList[4] = 5;
                break;
        }
        return conf;
    }

    /**
     * Obtaining the direction of the shortest distance towards Qibla by using the
     * great circle formula
     */
    static double getNorthQibla(Location loc) {
        /* FIXIT: reduce DEG_TO_RAD usage */
        double num, denom;
        num = sin(DEG_TO_RAD(loc.degreeLong) - DEG_TO_RAD(KAABA_LONG));
        denom = (cos(DEG_TO_RAD(loc.degreeLat)) * tan(DEG_TO_RAD(KAABA_LAT))) -
                (sin(DEG_TO_RAD(loc.degreeLat)) * ((cos((DEG_TO_RAD(loc.degreeLong) -
                        DEG_TO_RAD(KAABA_LONG))))));
        return RAD_TO_DEG(atan2(num, denom));

    }
}
