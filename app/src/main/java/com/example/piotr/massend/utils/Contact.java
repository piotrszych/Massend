package com.example.piotr.massend.utils;

public class Contact {

    String name;
    String phone;
    String group;

    public Contact(String name, String phone, String group) {
        this.name = name;
        this.phone = phone;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "Contact: " + name + ", phone: " + phone + ", from group: " + group;
    }
}
