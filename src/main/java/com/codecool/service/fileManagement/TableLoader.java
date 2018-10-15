package com.codecool.service.fileManagement;

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
    public Table getTable(String tableName) throws FileNotFoundException {
        Scanner scanner = getTableScanner(tableName);
        Entry headers = new Entry(scanner.nextLine(), ","); // Get first line of table, and store it as headers

        List<Entry> tableContent = new ArrayList<>();
        getTableContentAsStrings(scanner).forEach(str -> tableContent.add(new Entry(str, ",")));
        return new Table(headers, tableContent);
    }

    private Scanner getTableScanner(String tableName) throws FileNotFoundException {
        File file = getFile(tableName);
        return new Scanner(file);
    }

    private List<String> getTableContentAsStrings(Scanner scanner) {
        List<String> tableContentAsStrings = new ArrayList<>();
        while (scanner.hasNext()) {
            tableContentAsStrings.add(scanner.nextLine());
        }
        return tableContentAsStrings;
    }

    private File getFile(String tableName) {
        ClassLoader classLoader = getClass().getClassLoader();
        tableName = prepareTableName(tableName);
        return new File(Objects.requireNonNull(classLoader.getResource(tableName)).getFile());
    }

    private String prepareTableName(String tableName) {
        return tableName.toLowerCase().replace(";", "") + ".csv";
    }
}
