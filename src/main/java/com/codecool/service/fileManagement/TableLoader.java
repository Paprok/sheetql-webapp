package com.codecool.service.fileManagement;

import com.codecool.exception.MalformedQueryException;
import com.codecool.model.Entry;
import com.codecool.model.Table;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Service
public class TableLoader implements ITableLoader {
    @Override
    public Table getTable(String tableName) throws MalformedQueryException {
        Scanner scanner = getTableScanner(tableName);
        Entry headers = new Entry(scanner.nextLine(), ","); // Get first line of table, and store it as headers

        List<Entry> tableContent = new ArrayList<>();
        getTableContentAsStrings(scanner).forEach(str -> tableContent.add(new Entry(str, ",")));
        return new Table(headers, tableContent);
    }

    private Scanner getTableScanner(String tableName) throws MalformedQueryException {
        File file = getFile(tableName);
        try {
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new MalformedQueryException("No such table was found");
        }
    }

    private List<String> getTableContentAsStrings(Scanner scanner) {
        List<String> tableContentAsStrings = new ArrayList<>();
        scanner.forEachRemaining(tableContentAsStrings::add);
        return tableContentAsStrings;
    }

    private File getFile(String tableName) throws MalformedQueryException {
        ClassLoader classLoader = getClass().getClassLoader();
        tableName = prepareTableName(tableName);
        try {
            return new File(Objects.requireNonNull(classLoader.getResource(tableName)).getFile());
        } catch (NullPointerException e) {
            throw new MalformedQueryException("No such table was found");
        }
    }

    private String prepareTableName(String tableName) {
        return tableName.toLowerCase().replace(";", "") + ".csv";
    }
}
