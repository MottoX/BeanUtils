package com.github.mottox.util.bean;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link BeanConverterImpl}.
 *
 * @author Robin Wang
 */
public class BeanConverterImplTest {

    @Test
    public void testCopyProperties() throws Exception {
        SourceBean source = new SourceBean("Peter", 34, Gender.MALE, 1.85, BigDecimal.valueOf(123456789.87654321));

        TargetBean target = new TargetBean();

        BeanConverter converter = BeanConverterBuilder.create()
                .registerConverter((TypeConverter<Gender, Integer>) Gender::getValue)
                .registerConverter((TypeConverter<Double, BigDecimal>) BigDecimal::valueOf)
                .registerConverter((TypeConverter<BigDecimal, String>) BigDecimal::toPlainString)
                .build();

        converter.copyProperties(source, target);

        Assert.assertEquals(source.name, target.name);
        Assert.assertEquals((int) source.age, target.age);
        Assert.assertEquals(source.gender.value, target.gender);
        Assert.assertTrue(BigDecimal.valueOf(source.height).compareTo(target.height) == 0);
        Assert.assertTrue(source.wealth.compareTo(new BigDecimal(target.wealth)) == 0);

    }

    @Test
    public void testConvert() throws Exception {
        SourceBean source = new SourceBean("Peter", 34, Gender.MALE, 1.85, BigDecimal.valueOf(123456789.87654321));

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
    }

    private enum Gender {
        MALE(0),
        FEMALE(1);
        private final int value;

        Gender(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static class SourceBean {
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        public BigDecimal getWealth() {
            return wealth;
        }

        public void setWealth(BigDecimal wealth) {
            this.wealth = wealth;
        }
    }

    private static class TargetBean {
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public BigDecimal getHeight() {
            return height;
        }

        public void setHeight(BigDecimal height) {
            this.height = height;
        }

        public String getWealth() {
            return wealth;
        }

        public void setWealth(String wealth) {
            this.wealth = wealth;
        }
    }

}