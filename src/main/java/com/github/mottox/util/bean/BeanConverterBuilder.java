/*
 * Copyright (c) 2017 Robin Wang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
