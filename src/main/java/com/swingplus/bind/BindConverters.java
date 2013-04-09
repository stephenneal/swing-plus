package com.swingplus.bind;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.beansbinding.Converter;

public class BindConverters {

    /**
     * Convert a {@code Double} value to a {@code String}.
     */
    static final Converter<Double, String> DOUBLE_TO_STRING_CONVERTER = new Converter<Double, String>() {
        @Override
        public String convertForward(Double value) {
            return value == null || value.isNaN() ? null : Double.toString(value);
        }

        @Override
        public Double convertReverse(String value) {
            return StringUtils.isBlank(value) ? null : Double.parseDouble(value);
        }
    };

    /**
     * Convert a {@code Double} value to a {@code String} that represents currency.
     */
    static final Converter<Double, String> DOUBLE_TO_CURRENCY_CONVERTER = new Converter<Double, String>() {
        @Override
        public String convertForward(Double value) {
            return value == null || value.isNaN() ? null : Double.toString(value);
        }

        @Override
        public Double convertReverse(String value) {
            return StringUtils.isBlank(value) ? null : Double.parseDouble(value);
        }
    };
}
