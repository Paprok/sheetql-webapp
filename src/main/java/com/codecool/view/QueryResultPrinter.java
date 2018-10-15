package com.codecool.view;

import com.codecool.model.Entry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryResultPrinter {

    public void print(List<Entry> entries) {
        if (entries.isEmpty()) {
            System.out.println("No matching result.");
        }
        entries.stream()
                .map(Entry::toString)
                .forEach(System.out::println);
    }
}
