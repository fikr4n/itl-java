/* Copyright (c) 2014, Mohamed A.M. Bamakhrama <mohamed@alumni.tum.de>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Mohamed A.M. Bamakhrama nor the names of other
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.arabeyes.itl.newmethod;

class DefsModule {
    static final double M_PI = (3.141592653589793);

    /* Latitude and Longitude of Ka'aba. Obtained using Google Maps */
    static final double KAABA_LATITUDE = (21.42249);
    static final double KAABA_LONGITUDE = (39.82620);

    /* One second and one minute as fractions of one hour
       Used for safety margins */
    private static final double ONE_SECOND = (1.0 / 3600.0);
    static final double ONE_MINUTE = (60.0 / 3600.0);

    /**
     * Rounding method used for seconds
     */
    enum round_t {
        UP, DOWN, NEAREST
    }

    /**
     * Asr Method
     */
    enum asr_method_t {
        SHAFII, HANAFI
    }

    /**
     * Umm Al-Qura uses an offset instead of an angle
     */
    enum isha_flag_t {
        ANGLE, OFFSET
    }

    enum method_id_t {MWL, ISNA, EGAS, UMAQ, UIS}

    /**
     * Extreme Latitude Method
     * TODO: extreme latitudes are still WIP
     */
    enum extr_method_t {
        NONE,
        ANGLE_BASED,
        ONE_SEVENTH_OF_NIGHT,
        MIDDLE_OF_NIGHT
    }

    /**
     * The approximate Sun coordinates for a given Julian Day Number
     */
    static class approx_sun_coord {
        double D;   /* Declination of the Sun */
        double EqT; /* The Equation of Time */
        double R;   /* The distance of the Sun from the Earth
                   in astronomical units (AU)  */
        double SD;  /* The angular semidiameter of the Sun in degrees */
    }

    static class calc_method_t {
        /* In original C: The order of struct members below guarantees
         * that the compiler doesn't add padding bytes automatically
         * on 64-bit architectures */

        method_id_t id;         /* Method ID */
        String name;   /* Full name of the method */
        isha_flag_t isha_type;  /* Angle or offset? */
        double fajr;       /* Fajr angle */
        double isha;       /* Value for Isha angle/offset */

        calc_method_t(method_id_t id, String name, isha_flag_t isha_type, double fajr, double isha) {
            this.id = id;
            this.name = name;
            this.isha_type = isha_type;
            this.fajr = fajr;
            this.isha = isha;
        }
    }

    /**
     * Holds all the information related to the observer location
     */
    static class location {
        /* In original C: The struct members ordering guarantees that the compiler does
         * not add any padding bytes when compiling for 64-bit machines */

        double longitude;   /* Observer's longitude */
        double latitude;    /* Observer's latitude */
        double altitude;    /* Observer's altitude in meters */
        double timezone;    /* Observer's timezone (in hours) relative
                                  to Universal Coordinated Time (UTC) */
        int daylight;    /* Daylight Savings Time (DST) Flag
                                  Set to 1 if DST is on, 0 otherwise */
        asr_method_t asr_method;  /* Asr Method: Shafii or Hanafi */
        calc_method_t calc_method; /* Fajr and Isha Calculation method */
        extr_method_t extr_method; /* Extreme latitude method */
        String name;    /* Observer's location name */
    }

    /**
     * Holds the time of a single event (i.e., prayer or sunrise)
     */
    static class event_t {
        int julian_day;  /* The Julian day of the event */
        int hour;        /* Hour */
        int minute;      /* Minute */
    }

    /**
     * Holds all the prayers/events times of a given day
     */
    static class prayer_times {
        event_t fajr = new event_t();
        event_t sunrise = new event_t();
        event_t dhuhr = new event_t();
        event_t asr = new event_t();
        event_t maghrib = new event_t();
        event_t isha = new event_t();
    }

    static class date_t {
        int year, month, day;

        date_t set(int day, int month, int year) {
            this.year = year;
            this.month = month;
            this.day = day;
            return this;
        }
    }
}
