package com.codecool.service.dataManipulation;

import com.codecool.exception.MalformedQueryException;
import com.codecool.model.Entry;

import java.io.FileNotFoundException;
import java.util.List;

public interface ISelectData {

    List<Entry> handleQuery(String query) throws MalformedQueryException, FileNotFoundException;
}
