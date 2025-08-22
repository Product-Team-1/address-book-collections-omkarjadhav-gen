package com.example.addressbook;

public class InvalidContactFormatException extends Exception {
    public InvalidContactFormatException(String message) {
        super(message);
    }
}
