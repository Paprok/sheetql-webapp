package com.codecool.service.queryValidation;

import com.codecool.exception.MalformedQueryException;

public interface IQueryValidator {

    void validateQuery(String query) throws MalformedQueryException;
}
