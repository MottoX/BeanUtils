package com.github.mottox.util.bean;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test file of {@link BeanUtils}.
 *
 * @author Robin Wang
 */
public class BeanUtilsTest {

    @Test
    public void testCopyProperties() throws Exception {
        SourceBean source = new SourceBean("Peter", 34, Gender.MALE, 1.85, BigDecimal.valueOf(123456789.87654321));
        TargetBean target = new TargetBean("Lisa", 26, Gender.FEMALE, 1.64, BigDecimal.valueOf(321.123));

        BeanUtils.copyProperties(source, target);

        Assert.assertEquals(source.name, target.name);
        Assert.assertEquals(source.age, target.age);
        Assert.assertEquals(source.gender, target.gender);
        Assert.assertEquals(source.height, target.height);
        Assert.assertEquals(source.wealth, target.wealth);
    }

    @Test
    public void testConvert() throws Exception {
        SourceBean source = new SourceBean("Peter", 34, Gender.MALE, 1.85, BigDecimal.valueOf(123456789.87654321));

        TargetBean target = BeanUtils.convert(source, TargetBean.class);

        Assert.assertEquals(source.name, target.name);
        Assert.assertEquals(source.age, target.age);
        Assert.assertEquals(source.gender, target.gender);
        Assert.assertEquals(source.height, target.height);
        Assert.assertEquals(source.wealth, target.wealth);
    }

    private enum Gender {
        MALE,
        FEMALE
    }

    static class SourceBean {
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

    static class TargetBean {
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
}