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

import org.arabeyes.itl.newmethod.DefsModule.date_t;
import org.arabeyes.itl.newmethod.DefsModule.location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.arabeyes.itl.newmethod.PrayerModule.calc_methods;

class ConfigModule {

    /**
     * Output type
     */
    enum output_t {
        OUTPUT_NORMAL, /*< Textual output */
        OUTPUT_JSON    /*< JSON output */
    }

    /**
     * key names
     */
    private static class key_names {
        String name;
        String latitude;
        String longitude;
        String altitude;
        String asr_method;
        String calc_method;
        String extr_method;
        String timezone;
        String daylight;
    }

    /**
     * List of valid key names in the config file
     */
    private static final key_names valid_keys = new key_names();

    static {
        valid_keys.name = "name";
        valid_keys.latitude = "latitude";
        valid_keys.longitude = "longitude";
        valid_keys.altitude = "altitude";
        valid_keys.asr_method = "asr_method";
        valid_keys.calc_method = "calc_method";
        valid_keys.extr_method = "extr_method";
        valid_keys.timezone = "timezone";
        valid_keys.daylight = "daylight";
    }

    /**
     * Trims the leading and trailing whitespace from a string
     * Original C: Taken from: http://stackoverflow.com/a/122721
     * Java: slightly different in defining what space is
     *
     * @return Trimmed string
     */
    private static String trim_whitespace(String str) {
        return str.trim();
    }

    /**
     * Adds a new &lt;key,value> pair to the loc struct
     *
     * @return 0 if successful, 1 otherwise
     */
    private static int add_key_value(String key,
                                     String value,
                                     location loc) {
        int method_id;

        assert (key != null);
        assert (value != null);
        assert (loc != null);

        try {
            if (valid_keys.name.equals(key)) {
                loc.name = value;
            } else if (valid_keys.latitude.equals(key)) {
                loc.latitude = Double.parseDouble(value);
            } else if (valid_keys.longitude.equals(key)) {
                loc.longitude = Double.parseDouble(value);
            } else if (valid_keys.altitude.equals(key)) {
                loc.altitude = Double.parseDouble(value);
            } else if (valid_keys.asr_method.equals(key)) {
                loc.asr_method = AsrMethod.values()[Integer.parseInt(value)];
            } else if (valid_keys.calc_method.equals(key)) {
                method_id = Integer.parseInt(value);
                loc.calc_method = calc_methods[method_id];
            } else if (valid_keys.extr_method.equals(key)) {
                loc.extr_method = ExtremeMethod.values()[Integer.parseInt(value)];
            } else if (valid_keys.timezone.equals(key)) {
                loc.timezone = Double.parseDouble(value);
            } else if (valid_keys.daylight.equals(key)) {
                loc.daylight = Integer.parseInt(value);
            } else {
                return 1;
            }
        } catch (NumberFormatException e) {
            return 1;
        }
        return 0;
    }

    private static void set_default_location(location loc) {
        assert (loc != null);

        /* The following is for testing purposes only */
        loc.name = "Eindhoven, Netherlands";
        loc.latitude = 51.408311;
        loc.longitude = 5.454939;
        loc.altitude = 5;
        loc.asr_method = AsrMethod.SHAFII;
        loc.calc_method = calc_methods[MethodId.MWL.ordinal()];
        loc.extr_method = ExtremeMethod.NONE;
        loc.timezone = 1;
        loc.daylight = 0;
    }

    private static void print_usage() {
        String prog = "java " + MainModule.class.getName();
        System.err.printf("%s [-d YYYY-MM-DD] " +
                "[-f config_filename] [-j] [-h]\n\n", prog);
        System.err.printf("where:\n");
        System.err.printf(" * YYYY-MM-DD: is the date at which" +
                " you want to compute the prayer times\n");
        System.err.printf(" * config_filename: is the path to " +
                "configuration file\n");
        System.err.printf(" * -j: make the program produce output in " +
                "JSON format instead of normal text\n");
        System.err.printf(" * -h: print this help message\n");
    }

    private static int load_config_from_file(String config_filename,
                                             location loc) {
        BufferedReader fp = null;
        final String delimiter = ":";
        String key;
        String value;
        String line;
        int r;

        assert (config_filename != null);
        assert (loc != null);

        try {
            fp = new BufferedReader(new FileReader(config_filename));
            while ((line = fp.readLine()) != null) {
                String[] tokens = line.split(delimiter);
                if (tokens.length < 2) throw new IOException();
                key = tokens[0];
                key = trim_whitespace(key);
                value = tokens[1];
                value = trim_whitespace(value);
                r = add_key_value(key, value, loc);
                if (r != 0) throw new IOException();
            }
            fp.close();
            return 0;
        } catch (IOException e) {
            try {
                if (fp != null) fp.close();
            } catch (IOException ignored) {
                assert false;
            }
            return 1;
        }
    }

    /**
     * Parses the command line arguments and constructs the loc
     * data structure out of it
     *
     * @return 0 if all went fine, 1 otherwise
     */
    static int parse_arguments(String[] args,
                               location loc,
                               date_t date,
                               output_t[] output) {
        int rsp, i;
        int yyyy = 0, mm = 0, dd = 0;
        int date_set = 0;
        int config_from_file = 0;
        output[0] = output_t.OUTPUT_NORMAL;

        assert output.length == 1;
        assert (args.length >= 0);
        assert (loc != null);
        assert (date != null);

        /* make sure that date is cleared */
        date.set(0, 0, 0);

        /* getopt is not portable...
         * Therefore, we do things manually :-( */
        i = 0;
        while (i < args.length) {
            if ("-j".equals(args[i])) {
                output[0] = output_t.OUTPUT_JSON;
                i++;
            } else if ("-d".equals(args[i])) {
                i++;
                /* make sure that the next token is a date */
                try {
                    rsp = 3;
                    Matcher matcher = Pattern.compile("(\\d{1,4})-(\\d{1,2})-(\\d{1,2})").
                            matcher(args[i]);
                    if (matcher.matches()) {
                        yyyy = Integer.parseInt(matcher.group(1));
                        mm = Integer.parseInt(matcher.group(2));
                        dd = Integer.parseInt(matcher.group(3));
                    } else {
                        rsp = 0;
                    }
                } catch (NumberFormatException e) {
                    rsp = 0;
                }
                if ((rsp != 3) ||
                        (yyyy < 2000 || yyyy > 2199) ||
                        (mm < 1 || mm > 12) ||
                        (dd < 1 || dd > 31)
                        ) {
                    System.err.printf("Error! invalid date. ");
                    System.err.printf("Date format is:\n");
                    System.err.printf(" - year must be in [2000,2199]\n");
                    System.err.printf(" - month must be in [1,12]\n");
                    System.err.printf(" - day must be in [1,31]\n");
                    return 1;
                }
                date.year = yyyy;
                date.month = mm;
                date.day = dd;
                date_set = 1;
                i++;
            } else if ("-f".equals(args[i])) {
                i++;
                /* make sure we have a file */
                rsp = load_config_from_file(args[i], loc);
                if (rsp != 0) {
                    System.err.printf("Invalid argument to option -f\n");
                    print_usage();
                    return 1;
                }
                config_from_file = 1;
                i++;
            } else if ("-h".equals(args[i])) {
                i++;
                print_usage();
                return 1;
            } else {
                System.err.printf("Invalid option\n");
                print_usage();
                return 1;
            }
        }

        /* By this point, command line parsing should have
         * been done successfully... */
        if (date_set == 0) {
            /* Original C: TODO: localtime is not thread safe, however,
             * it is the only portable one... */
            localtime(date);
        }

        if (config_from_file == 0) {
            set_default_location(loc);
        }
        return 0;
    }

    private static void localtime(date_t date) {
        GregorianCalendar cal = new GregorianCalendar();
        date.set(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));
    }
}
