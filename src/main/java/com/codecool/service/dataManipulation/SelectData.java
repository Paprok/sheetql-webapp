package com.codecool.service.dataManipulation;

import com.codecool.exception.MalformedQueryException;
import com.codecool.model.Entry;
import com.codecool.model.EntryWrapper;
import com.codecool.model.Table;
import com.codecool.service.fileManagement.ITableLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelectData implements ISelectData {

    private final ITableLoader tableLoader;
    private final ILogicalOperationEvaluator operationEvaluator;

    @Autowired
    public SelectData(ITableLoader tableLoader, ILogicalOperationEvaluator operationEvaluator) {
        this.tableLoader = tableLoader;
        this.operationEvaluator = operationEvaluator;
    }

    @Override
    public List<Entry> handleQuery(String query) throws MalformedQueryException, FileNotFoundException {
        Table table = getRequestedTable(query);
        List<String> columnNames = getColumnsToRetrieve(query, table.getHeaders());
        return filterDataIfRequired(query, table, columnNames);
    }

    private List<Entry> filterDataIfRequired(String query, Table table, List<String> columnNames) throws MalformedQueryException {
        if (query.toLowerCase().contains("where")) {
            updateTableBasedOnWhereClause(query, table);
        }
        return pickRequiredColumnsFromTable(table, columnNames);
    }

    private List<Entry> pickRequiredColumnsFromTable(Table table, List<String> columnNames) {
        List<Entry> result = new ArrayList<>();
        for (EntryWrapper entryWrapper : table.getWrappedContent()) {
            result.add(entryWrapper.getEntryForKeys(columnNames));
        }
        return result;
    }

    private void updateTableBasedOnWhereClause(String query, Table table) throws MalformedQueryException {
        final String logicalOperation = operationEvaluator.prepareQuery(getLogicalOperationFromQuery(query));
        List<EntryWrapper> toDelete = table.getWrappedContent().stream()
                .filter(e -> {
                    String queryForGivenEntity = logicalOperation.toLowerCase();
                    for (String header : table.getHeaders().getContent()) {
                        header = header.toLowerCase();
                        queryForGivenEntity = queryForGivenEntity.replace(header, "'" + e.get(header) + "'");
                    }
                    return !operationEvaluator.isTrue(queryForGivenEntity);
                })
                .collect(Collectors.toList());

        toDelete.forEach(table::remove);
    }

    List<String> getColumnsToRetrieve(String query, Entry headers) {
        List<String> columns = new ArrayList<>();
        if (query.toUpperCase().contains("SELECT * FROM")) {
            columns.addAll(Arrays.asList(headers.getContent()));
        } else {
            String[] requestedDataString = getRequestedDataFromQuery(query).split(", ");
            columns.addAll(Arrays.asList(requestedDataString));
        }
        return columns;
    }

    private Table getRequestedTable(String query) throws MalformedQueryException, FileNotFoundException {
        String requestedTableName = getRequestedTableNameFromQuery(query);
        return tableLoader.getTable(requestedTableName);
    }

    private String getRequestedTableNameFromQuery(String query) throws MalformedQueryException {
        String requestedTableName = null;
        String[] queryKeywords = query.split(" ");
        for (int i = 0; i < queryKeywords.length; i++) {
            String keyWord = queryKeywords[i];
            if (keyWord.equalsIgnoreCase("FROM")) {
                requestedTableName = queryKeywords[i + 1];
                break;
            }
        }

        if (requestedTableName == null) {
            throw new MalformedQueryException("Cannot find table name in query: " + query);
        }

        return requestedTableName;
    }

    String getRequestedDataFromQuery(String query) {
        String start = "SELECT ";
        String end = " FROM";
        return getQueryPartBetween(query, start, end);
    }

    String getLogicalOperationFromQuery(String query) {
        String start = "WHERE ";
        String end = ";";
        return getQueryPartBetween(query, start, end);
    }

    private String getQueryPartBetween(String query, String start, String end) {
        query = query.toUpperCase();
        int startI = query.indexOf(start) + start.length();
        int endI = query.indexOf(end);
        return query.substring(startI, endI);
    }
}