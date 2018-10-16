package com.codecool.model;

import java.util.ArrayList;
import java.util.List;

public class EntryWrapper {

    private final Entry entry;
    private final Entry headers;

    EntryWrapper(Entry entry, Entry headers) {
        this.entry = entry;
        this.headers = headers;
    }

    public String get(String key) {
        for (int i = 0; i < headers.getContent().length; i++) {
            if (headers.getContent()[i].equalsIgnoreCase(key)) {
                if (i < entry.getContent().length) {
                    return entry.getContent()[i];
                }
                return "null";
            }
        }
        return null;
    }

    public Entry getEntryForKeys(List<String> keys) {
        List<String> requestedData = new ArrayList<>();
        keys.forEach(key -> requestedData.add(get(key)));
        return new Entry(requestedData.toArray(new String[0]));
    }
}
