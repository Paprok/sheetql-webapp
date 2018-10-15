package com.codecool.service.queryValidation.rules;

import com.codecool.exception.MalformedQueryException;

public interface IQueryValidationRule {

    void validateQuery(String query) throws MalformedQueryException;
}
