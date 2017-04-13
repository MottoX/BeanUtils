# BeanUtils
[![Build Status](https://travis-ci.org/MottoX/BeanUtils.svg?branch=master)](https://travis-ci.org/MottoX/BeanUtils)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mottox/bean-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mottox/bean-utils)

A wrapper for [cglib BeanCopier](https://github.com/cglib/cglib/blob/master/cglib/src/main/java/net/sf/cglib/beans/BeanCopier.java) providing conversion between JavaBeans.

## Introduction
Provides simple and convenient APIs to copy properties/do conversion between JavaBeans. For performance, [cglib](https://github.com/cglib/cglib) is chosen as the underlying library.
Currently, there are two tool classes for conversion, [BeanUtils](src/main/java/com/github/mottox/util/bean/BeanUtils.java) and [BeanConverter](src/main/java/com/github/mottox/util/bean/BeanConverter.java).
`BeanUtils` provides a straight way to use but it requires that types of properties to map **MUST** be the same, while `BeanConverter` provides a more flexible way to use.

## Usage
### Maven Dependency
Add the following dependency to your project's pom.xml.
```
<dependency>
    <groupId>com.github.mottox</groupId>
    <artifactId>bean-utils</artifactId>
    <version>1.1.0</version>
</dependency>
```

### API
[BeanUtils](src/main/java/com/github/mottox/util/bean/BeanUtils.java) provides the following methods:
*  `copyProperties(Object source, Object target)`
<br>Copy the property values of the given source bean into the target bean.
*  `<T> T convert(Object source, Class<T> clazz)`
<br>Convert the given source bean to a target bean of specified type.

[BeanConverter](src/main/java/com/github/mottox/util/bean/BeanConverter.java) provides the same methods above.
Below is the list of differences between `BeanUtils` and `BeanConverter`.
1. The above two methods are static in `BeanUtils`, but non static in `BeanConverter`.
2. `BeanConverter` provides relatively flexible conversion strategy, so you can create your custom mapping method for a `BeanConverter` instance.

## Examples
It's quite easy and convenient to use BeanUtils for JavaBean conversion.

### BeanUtils
```java
public class SourceBean {
    private String name;

    private Integer age;

    private Gender gender;

    private Double height;

    private BigDecimal wealth;

    SourceBean(String name, Integer age, Gender gender, Double height, BigDecimal wealth) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.wealth = wealth;
    }

    // Getters and setters omitted
}

public class TargetBean {
    private String name;

    private Integer age;

    private Gender gender;

    private Double height;

    private BigDecimal wealth;

    TargetBean() {
    }

    TargetBean(String name, Integer age, Gender gender, Double height, BigDecimal wealth) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.wealth = wealth;
    }

    // Getters and setters omitted
}
```

```java
SourceBean source = new SourceBean("Peter", 34, Gender.MALE, 1.85, BigDecimal.valueOf(123456789.87654321));

TargetBean target = BeanUtils.convert(source, TargetBean.class);
Assert.assertEquals(source.name, target.name);
Assert.assertEquals(source.age, target.age);
Assert.assertEquals(source.gender, target.gender);
Assert.assertEquals(source.height, target.height);
Assert.assertEquals(source.wealth, target.wealth);
```

### BeanConverter
```java
public class SourceBean {
    private String name;

    private Integer age;

    private Gender gender;

    private Double height;

    private BigDecimal wealth;

    public SourceBean(String name, Integer age, Gender gender, Double height, BigDecimal wealth) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.wealth = wealth;
    }

    // Getters and setters omitted
}

public class TargetBean {
    private String name;

    private int age;

    private int gender;

    private BigDecimal height;

    private String wealth;

    public TargetBean() {
    }

    public TargetBean(String name, int age, int gender, BigDecimal height, String wealth) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.wealth = wealth;
    }

    // Getters and setters omitted
}
```

```java
BeanConverter converter = BeanConverterBuilder.create()
        .registerConverter((TypeConverter<Gender, Integer>) Gender::getValue)
        .registerConverter((TypeConverter<Double, BigDecimal>) BigDecimal::valueOf)
        .registerConverter((TypeConverter<BigDecimal, String>) BigDecimal::toPlainString)
        .build();

TargetBean target = converter.convert(source, TargetBean.class);

Assert.assertEquals(source.name, target.name);
Assert.assertEquals((int) source.age, target.age);
Assert.assertEquals(source.gender.value, target.gender);
Assert.assertTrue(BigDecimal.valueOf(source.height).compareTo(target.height) == 0);
Assert.assertTrue(source.wealth.compareTo(new BigDecimal(target.wealth)) == 0);
```

Note that cglib BeanCopier **will copy null properties**.

## License
[MIT License](LICENSE)
