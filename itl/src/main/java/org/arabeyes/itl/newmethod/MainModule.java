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

import org.arabeyes.itl.newmethod.ConfigModule.output_t;
import org.arabeyes.itl.newmethod.DefsModule.date_t;
import org.arabeyes.itl.newmethod.DefsModule.location;

import java.util.GregorianCalendar;

import static org.arabeyes.itl.newmethod.ConfigModule.parse_arguments;
import static org.arabeyes.itl.newmethod.PrayerModule.get_prayer_times;
import static org.arabeyes.itl.newmethod.PrayerModule.get_qibla_direction;

class MainModule {
    private static final int EXIT_FAILURE = -1;

    public static void main(String[] args) {
        final location loc = new location();
        final date_t date = new date_t();
        double qibla;
        final PrayerTimes pt = new PrayerTimes();
        final output_t[] output = new output_t[1];
        int rsp;

        rsp = parse_arguments(args, loc, date, output);
        if (rsp != 0) {
            System.err.printf("Error in parsing arguments!\n");
            System.exit(EXIT_FAILURE);
        }

        qibla = get_qibla_direction(loc);
        get_prayer_times(date, loc, pt);

        if (output[0] == output_t.OUTPUT_NORMAL) {
            System.out.printf("Computing Prayer Times on %s\n",
                    new GregorianCalendar(date.year, date.month - 1, date.day).getTime());
            System.out.printf("Current location is: %s " +
                            "(Latitude = %f, Longitude = %f)\n",
                    loc.name, loc.latitude, loc.longitude);

            System.out.printf("Qibla direction is %f degrees" +
                    " from North clockwise\n", qibla);

            System.out.printf("Current Timezone is UTC%s%.2f" +
                            " (daylight is %s)\n",
                    (loc.timezone >= 0 ? "+" : "-"),
                    loc.timezone, (loc.daylight == 1 ? "on" : "off"));
            System.out.printf("Calculation Method Used: " +
                            "%s, Fajr angle: %.2f," +
                            " Isha %s: %.2f\n",
                    loc.calc_method.name, loc.calc_method.fajr,
                    (loc.calc_method.isha_type == IshaFlag.ANGLE ?
                            "angle" : "offset"),
                    loc.calc_method.isha);

            System.out.printf("\n Fajr\t\tSunrise\t\tDhuhr" +
                    "\t\tAsr\t\tSunset\t\tIsha\n");
            System.out.printf("--------------------------" +
                    "--------------------------");
            System.out.printf("---------------------------------\n");
            System.out.printf(" %d:%02d\t\t%d:%02d\t\t%d:%02d\t\t" +
                            "%d:%02d\t\t%d:%02d\t\t%d:%02d\n\n",
                    pt.fajr.hour, pt.fajr.minute,
                    pt.sunrise.hour, pt.sunrise.minute,
                    pt.dhuhr.hour, pt.dhuhr.minute,
                    pt.asr.hour, pt.asr.minute,
                    pt.maghrib.hour, pt.maghrib.minute,
                    pt.isha.hour, pt.isha.minute);
        } else if (output[0] == output_t.OUTPUT_JSON) {
            System.out.printf("{ \"times\" : [\n");
            System.out.printf("\t { \"fajr\": \"%d:%02d\" } ,\n" +
                            "\t { \"dhuhr\": \"%d:%02d\" } ,\n" +
                            "\t { \"sunrise\": \"%d:%02d\" } ,\n" +
                            "\t { \"asr\": \"%d:%02d\" } ,\n" +
                            "\t { \"maghrib\": \"%d:%02d\" } ,\n" +
                            "\t { \"isha\": \"%d:%02d\" } \n" +
                            "] }\n",
                    pt.fajr.hour, pt.fajr.minute,
                    pt.sunrise.hour, pt.sunrise.minute,
                    pt.dhuhr.hour, pt.dhuhr.minute,
                    pt.asr.hour, pt.asr.minute,
                    pt.maghrib.hour, pt.maghrib.minute,
                    pt.isha.hour, pt.isha.minute);
        }
    }
}
