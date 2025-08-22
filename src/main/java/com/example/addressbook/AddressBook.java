package com.example.addressbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class AddressBook {

    /** Load contacts from a CSV InputStream. Skips header, logs and skips invalid rows. */
    public static List<Contact> loadFromCsv(InputStream input) throws IOException {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) { header = false; continue; }
                if (line.trim().isEmpty()) continue;
                try {
                    Contact c = parseLine(line);
                    contacts.add(c);
                } catch (InvalidContactFormatException e) {
                    System.err.println("Skipping invalid row: " + e.getMessage());
                }
            }
        }
        return contacts;
    }

    /** Parse a single CSV line into a Contact or throw InvalidContactFormatException. */
    public static Contact parseLine(String line) throws InvalidContactFormatException {
        String[] parts = line.split(",", -1); // keep empty fields
        if (parts.length != 4) {
            throw new InvalidContactFormatException("Wrong number of fields: " + line);
        }
        String name = parts[0].trim();
        String email = parts[1].trim();
        String phone = parts[2].trim();
        String city = parts[3].trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || city.isEmpty()) {
            throw new InvalidContactFormatException("Missing required field(s): " + line);
        }
        if (!isLikelyEmail(email)) {
            throw new InvalidContactFormatException("Invalid email: " + email);
        }
        return new Contact(name, email, phone, city);
    }

    private static boolean isLikelyEmail(String email) {
        int at = email.indexOf('@');
        return at > 0 && at < email.length() - 1 && email.indexOf(' ', 0) < 0;
    }

    /** Return contacts whose name contains query (case-insensitive). */
    public static List<Contact> searchByName(List<Contact> contacts, String query) {
        if (contacts == null || query == null) return Collections.emptyList();
        String q = query.toLowerCase();
        return contacts.stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    /** Return contacts whose city equals the given city (case-insensitive). */
    public static List<Contact> filterByCity(List<Contact> contacts, String city) {
        if (contacts == null || city == null) return Collections.emptyList();
        return contacts.stream()
                .filter(c -> c.getCity() != null && c.getCity().equals(city))
                .collect(Collectors.toList());
    }

    /** Return contacts whose phone starts with the given prefix. */
    public static List<Contact> filterByPhonePrefix(List<Contact> contacts, String prefix) {
        if (contacts == null || prefix == null) return Collections.emptyList();
        return contacts.stream()
                .filter(c -> c.getPhone() != null && c.getPhone().startsWith(prefix))
                .collect(Collectors.toList());
    }

    /** Return a set of unique city names. */
    public static Set<String> uniqueCities(List<Contact> contacts) {
        if (contacts == null) return Collections.emptySet();
        return contacts.stream()
                .map(Contact::getCity)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /** Return a list sorted by name (case-insensitive). */
    public static List<Contact> sortedByName(List<Contact> contacts) {
        if (contacts == null) return Collections.emptyList();
        return contacts.stream()
                .sorted(Comparator.comparing(c -> c.getName().toLowerCase()))
                .collect(Collectors.toList());
    }

    /** Group contacts by city and count them. */
    public static Map<String, Long> groupCountByCity(List<Contact> contacts) {
        if (contacts == null) return Collections.emptyMap();
        return contacts.stream()
                .collect(Collectors.groupingBy(Contact::getCity, Collectors.counting()));
    }

    // Optional convenience main for manual runs
    public static void main(String[] args) throws Exception {
        try (InputStream in = AddressBook.class.getResourceAsStream("/contacts.csv")) {
            List<Contact> contacts = loadFromCsv(in);
            System.out.println("Loaded contacts: " + contacts.size());
            System.out.println("Cities: " + uniqueCities(contacts));
        }
    }
}
