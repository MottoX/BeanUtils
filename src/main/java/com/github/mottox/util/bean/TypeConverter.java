package com.github.mottox.util.bean;

/**
 * TypeConverter converts a source object to a target object corresponding to its implementation.
 *
 * @author Robin Wang
 */
@FunctionalInterface
public interface TypeConverter<S, T> {
    /**
     * Convert a source object to a target object.
     *
     * @param source the source object
     *
     * @return the target object converted from the source object
     */
    T convert(S source);
}
