package com.github.mottox.util.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder class that builds a {@link BeanConverterImpl}.
 *
 * @author Robin Wang
 */
public class BeanConverterBuilder {

    /**
     * The list of {@link TypeConverter} for conversion.
     */
    private List<TypeConverter<?, ?>> converters;

    /**
     * Private constructors.
     */
    private BeanConverterBuilder() {
        this.converters = new ArrayList<>();
    }

    /**
     * Construct a new {@link BeanConverterBuilder}.
     *
     * @return a new {@link BeanConverterBuilder}
     */
    public static BeanConverterBuilder create() {
        return new BeanConverterBuilder();
    }

    /**
     * Put a {@link TypeConverter} into the builder.
     *
     * @param converter the {@link TypeConverter} to put
     *
     * @return the original builder
     */
    public BeanConverterBuilder registerConverter(TypeConverter<?, ?> converter) {
        converters.add(converter);
        return this;
    }

    /**
     * Build a {@link BeanConverterImpl}.
     *
     * @return a {@link BeanConverterImpl}
     */
    public BeanConverter build() {
        return new BeanConverterImpl(converters);
    }
}
