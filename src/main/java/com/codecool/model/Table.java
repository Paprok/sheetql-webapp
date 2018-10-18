package com.codecool.model;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private Entry headers;
    private List<Entry> tableContent;
    private List<EntryWrapper> wrappedContent;

    public Table(Entry headers, List<Entry> tableContent) {
        this.headers = headers;
        this.tableContent = tableContent;
        this.wrappedContent = wrapContent();
    }

    private List<EntryWrapper> wrapContent() {
        List<EntryWrapper> result = new ArrayList<>();
        tableContent.forEach(e -> result.add(new EntryWrapper(e, headers)));
        return result;
    }

    public Entry getHeaders() {
        return headers;
    }

    public List<Entry> getTableContent() {
        return tableContent;
    }

    public List<EntryWrapper> getWrappedContent() {
        return wrappedContent;
    }

    public void setHeaders(Entry headers) {
        this.headers = headers;
    }

    public void setTableContent(List<Entry> tableContent) {
        this.tableContent = tableContent;
    }

    public void setWrappedContent(List<EntryWrapper> wrappedContent) {
        this.wrappedContent = wrappedContent;
    }

    public void remove(EntryWrapper entity) {
        int indexToRemove = wrappedContent.indexOf(entity);
        wrappedContent.remove(indexToRemove);
        tableContent.remove(indexToRemove);
    }
}
