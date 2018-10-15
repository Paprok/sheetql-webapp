package com.codecool.service.dataManipulation;

import com.codecool.exception.MalformedQueryException;

public interface ILogicalOperationEvaluator {
    boolean isTrue(String operation);

    String prepareQuery(String query) throws MalformedQueryException;
}
