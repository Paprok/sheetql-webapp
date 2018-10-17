package com.codecool.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntryWrapper {

    private final Entry entry;
    private final Entry headers;

    EntryWrapper(Entry entry, Entry headers) {
        this.entry = entry;
        this.headers = headers;
    }

    public String get(String key) {
        int index = getKeyIndex(key);
        if (index >= 0 && index < entry.getContent().length) {
            return entry.getContent()[index];
        }
        return "NULL";
    }

    private int getKeyIndex(String key) {
        String searchedKey = Arrays.stream(headers.getContent())
                .filter(word -> word.equalsIgnoreCase(key))
                .findFirst().orElse("NOT FOUND");

        return Arrays.asList(headers.getContent()).indexOf(searchedKey);
    }

    public Entry getEntryForKeys(List<String> keys) {
        List<String> requestedData = new ArrayList<>();
        keys.forEach(key -> requestedData.add(get(key)));
        return new Entry(requestedData.toArray(new String[0]));
    }
}
