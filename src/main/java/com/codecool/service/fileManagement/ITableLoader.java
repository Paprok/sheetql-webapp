package com.codecool.service.fileManagement;

import com.codecool.exception.MalformedQueryException;
import com.codecool.model.Table;

public interface ITableLoader {

    Table getTable(String tableName) throws MalformedQueryException;
}
