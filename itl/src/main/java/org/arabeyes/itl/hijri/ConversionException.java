/* Copyright (c) 2017, Fikrul Arif
 * (under LGPL license - see COPYING file)
 */
package org.arabeyes.itl.hijri;

/**
 * Thrown when conversion to/from Hijri is failed, usually because of wrong input.
 */
@SuppressWarnings("WeakerAccess")
public class ConversionException extends RuntimeException {
    public ConversionException(String s) {
        super(s);
    }

    public ConversionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
