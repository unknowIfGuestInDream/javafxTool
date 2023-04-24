package com.tlcsdm.core.util.groovy;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/24 21:44
 */
public class Person {
    public String name;
    public String address;
    public Integer age;

    public Person(String name, String addr, Integer age) {
        this.name = name;
        this.address = addr;
        this.age = age;
    }

    public String toString() {
        return String.format("[Person: name:%s, address:%s, age:%s]", name, address, age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
