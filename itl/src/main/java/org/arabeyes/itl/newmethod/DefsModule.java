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
     * The approximate Sun coordinates for a given Julian Day Number
     */
    static class approx_sun_coord {
        double D;   /* Declination of the Sun */
        double EqT; /* The Equation of Time */
        double R;   /* The distance of the Sun from the Earth
                   in astronomical units (AU)  */
        double SD;  /* The angular semidiameter of the Sun in degrees */
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
        AsrMethod asr_method;  /* Asr Method: Shafii or Hanafi */
        CalcMethod calc_method; /* Fajr and Isha Calculation method */
        ExtremeMethod extr_method; /* Extreme latitude method */
        String name;    /* Observer's location name */
    }

    static class date_t {
        int year, month, day;

        /**
         * @param day starts from 1
         * @param month starts from 1, like the old ITL
         */
        date_t set(int day, int month, int year) {
            this.year = year;
            this.month = month;
            this.day = day;
            return this;
        }
    }
}
