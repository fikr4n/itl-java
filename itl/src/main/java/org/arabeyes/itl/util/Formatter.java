/* Copyright (c) 2017, Fikrul Arif
 * (under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.util;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {
    public static String format(String f, Mapper mapper) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ROOT);
        nf.setGroupingUsed(false);

        StringBuilder result = new StringBuilder();
        int count = 0;
        boolean inQuote = false;
        for (int i = 0, last = f.length() - 1; i <= last; ++i) {
            char c = f.charAt(i);
            if (i > 0 && c == f.charAt(i - 1))
                count++;
            else
                count = 1;

            // handle quote and in-quote
            if (c == '\'') {
                if (inQuote) {
                    if (count == 2) result.append(c);
                    inQuote = false;
                    count = 0;
                } else {
                    inQuote = true;
                }
                continue;
            } else if (inQuote) {
                result.append(c);
                continue;
            }

            if (i != last && c == f.charAt(i + 1)) // collect and count first
                continue;

            // handle format pattern and punctuation
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                Object val = mapper.applyFormat(c, count);
                if (val == null) {
                    throw new IllegalArgumentException("Illegal pattern character '" + c + "'");
                } else if (val instanceof Number) {
                    nf.setMinimumIntegerDigits(count);
                    result.append(nf.format(val));
                } else {
                    result.append(val);
                }
            } else {
                // punctuation
                for (int j = 0; j < count; ++j) result.append(c);
            }
        }
        return result.toString();
    }

    public interface Mapper {
        Object applyFormat(char pattern, int count);
    }
}
