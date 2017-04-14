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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ClassUtils;

import net.jodah.typetools.TypeResolver;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

/**
 * Different from {@link BeanUtils}, this implementation supports JavaBean conversion with custom mapping strategy.
 * Since the internal property converter and BeanCopier map are not static, it is better to make the instance static
 * for global use.
 *
 * @author Robin Wang
 */
public class BeanConverterImpl implements BeanConverter {

    /**
     * The cglib converter that provides custom property conversion strategy.
     */
    private final Converter converter;

    /**
     * The map to store {@link BeanCopier} of source type and class type for conversion.
     */
    private final Map<String, BeanCopier> beanCopierMap;

    /**
     * Construct a new instance of {@link BeanConverter}.
     *
     * @param converters the provided type converters
     */
    BeanConverterImpl(List<TypeConverter<?, ?>> converters) {
        this.converter = new ConverterAdapter(converters);
        this.beanCopierMap = new ConcurrentHashMap<>();
    }

    /**
     * Copy the property values of the given source bean into the target bean.
     *
     * @param source the source bean
     * @param target the target bean
     *
     * @see BeanCopier
     */
    @Override
    public void copyProperties(Object source, Object target) {
        Objects.requireNonNull(source, "source must not be null");
        Objects.requireNonNull(target, "target must not be null");

        BeanCopier beanCopier = getBeanCopier(source.getClass(), target.getClass());
        beanCopier.copy(source, target, converter);
    }

    /**
     * Convert the given source bean to a target bean of specified type.
     *
     * @param source the source bean
     * @param clazz  the class of target bean
     * @param <T>    the type of target bean
     *
     * @return the target bean of type <code>T</code>
     */
    @Override
    public <T> T convert(Object source, Class<T> clazz) {
        // Initialize a new instance of the target type.
        T result;
        try {
            result = clazz.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("fail to create instance of type" + clazz.getCanonicalName(), e);
        }

        copyProperties(source, result);
        return result;
    }

    /**
     * Get the {@link BeanCopier} of source class and target class.
     * Create a new one if it is not contained in <code>BEAN_COPIER_MAP</code>.
     *
     * @param source the source class
     * @param target the target class
     *
     * @return the bean copier of source class and target class
     */
    private BeanCopier getBeanCopier(Class<?> source, Class<?> target) {
        String key = generateKey(source, target);
        return beanCopierMap.computeIfAbsent(key, x -> BeanCopier.create(source, target, true));
    }

    /**
     * Get key of <code>beanCopierMap</code> by source class and target class.
     *
     * @param source the source class
     * @param target the target class
     *
     * @return the key of <code>beanCopierMap</code> by source class and target class
     */
    private String generateKey(Class<?> source, Class<?> target) {
        return source.getCanonicalName().concat(target.getCanonicalName());
    }

    /**
     * The adapter class that turns {@link TypeConverter} into {@link Converter}.
     */
    static class ConverterAdapter implements Converter {

        private final Map<Class, List<TypeResolvedConverter>> converterMap;

        ConverterAdapter(List<TypeConverter<?, ?>> converterMap) {
            this.converterMap = converterMap.stream()
                    .map(this::resolveTypeOfConverter)
                    .collect(Collectors.groupingBy(TypeResolvedConverter::getSourceType));
        }

        @Override
        public Object convert(Object value, Class targetType, Object context) {
            if (value == null) {
                return null;
            }

            Class sourceType = value.getClass();

            /*
             * Check if target type is assignable from source type.
             * If true, it's okay to copy property from source to target.
             * Also note that the source type will be a wrapper data type if the original property is primitive data
             * type
             * and java.lang.Class#isAssignableFrom returns false when using for primitive and wrapper data type,
             * it's a good idea to use ClassUtils from Apache Commons to simplify code.
             */
            if (ClassUtils.isAssignable(sourceType, targetType, true)) {
                return value;
            }

            //  Try to find mapping strategy from converters.
            for (Map.Entry<Class, List<TypeResolvedConverter>> entry : converterMap.entrySet()) {
                Class converterSourceType = entry.getKey();
                List<TypeResolvedConverter> converters = entry.getValue();

                // Determine if the source type of converter is assignable from the type of value
                if (ClassUtils.isAssignable(sourceType, converterSourceType, true)) {
                    for (TypeResolvedConverter converter : converters) {
                        // Determine if the target type to convert is assignable from the target type of converter
                        if (ClassUtils.isAssignable(converter.getTargetType(), targetType, true)) {
                            @SuppressWarnings("unchecked")
                            Object result = converter.convert(value);
                            return result;
                        }
                    }
                }
            }

            // Return null if unable to convert value
            return null;
        }

        @SuppressWarnings("unchecked")
        private <S, T> TypeResolvedConverter<S, T> resolveTypeOfConverter(TypeConverter<S, T> converter) {
            Class<?>[] classes = TypeResolver.resolveRawArguments(TypeConverter.class, converter.getClass());
            return new TypeResolvedConverter<>(converter, (Class<S>) classes[0], (Class<T>) classes[1]);
        }

        /**
         * Implementation of {@link TypeConverter} with source and target type resolved.
         *
         * @param <S> the source type
         * @param <T> the target type
         */
        static class TypeResolvedConverter<S, T> implements TypeConverter<S, T> {

            private final TypeConverter<S, T> delegatingConverter;

            private final Class<S> sourceType;

            private final Class<T> targetType;

            TypeResolvedConverter(TypeConverter<S, T> delegatingConverter, Class<S> sourceType,
                                  Class<T> targetType) {
                this.delegatingConverter = delegatingConverter;
                this.sourceType = sourceType;
                this.targetType = targetType;
            }

            public Class<S> getSourceType() {
                return sourceType;
            }

            public Class<T> getTargetType() {
                return targetType;
            }

            @Override
            public T convert(S source) {
                return delegatingConverter.convert(source);
            }

        }
    }

}
