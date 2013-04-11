package com.swingplus.bind.bbb;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.jdesktop.beansbinding.Converter;

/**
 * Converters for binding in addition to the default converters provided by BetterBeansBinding {@link Converter}.
 * 
 * @author Stephen Neal
 * @since 10/04/2013
 */
abstract class Converter2<S, T> extends Converter<S, T> {

    /**
     * Default converter for a {@code Date} value to/from a {@code String}.
     */
    static Converter<Date, String> newDateString(final DateFormat format) {
        return new Converter<Date, String>() {
            @Override
            public String convertForward(Date value) {
                return value == null ? null : format.format(value);
            }

            @Override
            public Date convertReverse(String value) {
                if (value == null) {
                    return null;
                }
                try {
                    return format.parse(value);
                } catch (ParseException e) {
                }
                return null;
            }
        };
    }

}
