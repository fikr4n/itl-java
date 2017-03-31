package org.arabeyes.itl.hijri;

public class ConversionException extends RuntimeException {
    public ConversionException(String s) {
        super(s);
    }

    public ConversionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
