package com.example.addressbook;

import java.util.Objects;

public class Contact {
    private final String name;
    private final String email;
    private final String phone;
    private final String city;

    public Contact(String name, String email, String phone, String city) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCity() { return city; }

    @Override
    public String toString() {
        return name + " (" + email + ", " + phone + ", " + city + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) &&
               Objects.equals(email, contact.email) &&
               Objects.equals(phone, contact.phone) &&
               Objects.equals(city, contact.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, phone, city);
    }
}
