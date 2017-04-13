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
