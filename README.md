# BeanUtils [![Build Status](https://travis-ci.org/MottoX/BeanUtils.svg?branch=master)](https://travis-ci.org/MottoX/BeanUtils)
BeanUtils is a wrapper for [cglib](https://github.com/cglib/cglib) BeanCopier providing conversion between JavaBeans.

## Usage
BeanUtils provides the following APIs:
*  `copyProperties(Object source, Object target)` to copy the property values of the given source bean into the target bean.
*  `<T> T convert(Object source, Class<T> clazz)` to convert the given source bean to a target bean of specified type.

It's quite easy and convenient to use BeanUtils for JavaBean conversion.

```java
SourceBean source = new SourceBean("Peter", 34, Gender.MALE, 1.85, BigDecimal.valueOf(123456789.87654321));
TargetBean target = new TargetBean("Lisa", 26, Gender.FEMALE, 1.64, BigDecimal.valueOf(321.123));

BeanUtils.copyProperties(source, target);
```

```java
SourceBean source = new SourceBean("Peter", 34, Gender.MALE, 1.85, BigDecimal.valueOf(123456789.87654321));

TargetBean target = BeanUtils.convert(source, TargetBean.class);
```
