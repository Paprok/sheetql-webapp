package com.codecool.model;

import java.util.Arrays;

public class Entry {

    private final String[] content;

    public Entry(String[] content) {
        this.content = content;
    }

    public Entry(String contentAsString, String delimiter) {
        this.content = contentAsString.split(delimiter);
    }

    public String[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return Arrays.toString(content);
    }
}
