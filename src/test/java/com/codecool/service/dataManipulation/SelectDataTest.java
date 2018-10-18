package com.codecool.service.dataManipulation;

import com.codecool.exception.MalformedQueryException;
import com.codecool.model.Entry;
import com.codecool.service.fileManagement.TableLoader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class SelectDataTest {

    private SelectData testSubject = new SelectData(new TableLoader(), new LogicalOperationEvaluator());

    @Test
    public void Should_CorrectlyExtractRequestedData_When_NotUsedStarSelector() {
        String query = "Select id, name from table where id = 4 and age > 5;";
        String expected = "id, name";
        assertEquals(expected, testSubject.getRequestedDataFromQuery(query).toLowerCase());
    }

    @Test
    public void Should_CorrectlyExtractLogicalOperation() {
        String query = "Select id, name from table where id = 4 and age > 5;";
        String expected = "id = 4 and age > 5";
        assertEquals(expected, testSubject.getLogicalOperationFromQuery(query).toLowerCase());
    }

    @Test
    public void Should_ReturnValidIndexes_When_WorkingOnQuery() {
        String[] headersArray = new String[]{"col1", "col2", "col3", "col4"};
        Entry headers = new Entry(headersArray);
        String query = "Select col1, col4 from table".toUpperCase();
        List<String> expected = new ArrayList<>();
        expected.add("COL1");
        expected.add("COL4");

        assertEquals(expected, testSubject.getColumnsToRetrieve(query, headers));
    }

    @Test
    public void Should_ReturnOnlyRequestedColumns_When_NotUsedStarSelector() throws MalformedQueryException {
        String query = "Select col1, col3 from test1;";
        List<Entry> result = testSubject.handleQuery(query);
        List<Entry> expected = new ArrayList<>();
        expected.add(new Entry(new String[]{"r1c1", "r1c3"}));
        expected.add(new Entry(new String[]{"r2c1", "r2c3"}));
        expected.add(new Entry(new String[]{"r3c1", "r3c3"}));
        expected.add(new Entry(new String[]{"r4c1", "r4c3"}));

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void Should_HandleWhereClause() throws MalformedQueryException {
        String query = "Select * from test1 where col1 = 'r2c1';";
        List<Entry> result = testSubject.handleQuery(query);
        List<Entry> expected = new ArrayList<>();
        expected.add(new Entry(new String[]{"r2c1", "r2c2", "r2c3", "r2c4"}));

        assertEquals(expected.toString(), result.toString());
    }
}