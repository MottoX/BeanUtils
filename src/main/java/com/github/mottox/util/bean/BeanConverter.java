package com.github.mottox.util.bean;

import net.sf.cglib.beans.BeanCopier;

/**
 * A bean converter supports property copy and bean conversion between source type and target type.
 *
 * @author Robin Wang
 */
public interface BeanConverter {

    /**
     * Copy the property values of the given source bean into the target bean.
     *
     * @param source the source bean
     * @param target the target bean
     *
     * @see BeanCopier
     */
    void copyProperties(Object source, Object target);

    /**
     * Convert the given source bean to a target bean of specified type.
     *
     * @param source the source bean
     * @param clazz  the class of target bean
     * @param <T>    the type of target bean
     *
     * @return the target bean of type <code>T</code>
     */
    <T> T convert(Object source, Class<T> clazz);

}
