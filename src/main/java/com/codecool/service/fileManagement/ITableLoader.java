package com.codecool.service.fileManagement;

import com.codecool.model.Table;

import java.io.FileNotFoundException;

public interface ITableLoader {

    Table getTable(String tableName) throws FileNotFoundException;
}
