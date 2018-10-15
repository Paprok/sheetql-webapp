package com.codecool.service.queryValidation.rules;

import com.codecool.exception.MalformedQueryException;
import org.springframework.stereotype.Component;

@Component
public class QueryIsNotNullOrEmpty implements IQueryValidationRule {
    @Override
    public void validateQuery(String query) throws MalformedQueryException {
        if (query == null) {
            throw new MalformedQueryException("Received query is NULL.");
        }

        if (query.equals("") || query.equals(";")) {
            throw new MalformedQueryException("Received query is empty.");
        }

    }
}
