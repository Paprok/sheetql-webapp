package com.codecool.service.queryValidation.rules;

import com.codecool.exception.MalformedQueryException;
import org.springframework.stereotype.Component;

@Component
public class QueryEndsWithSemicolonRule implements IQueryValidationRule {
    @Override
    public void validateQuery(String query) throws MalformedQueryException {
        if (query.endsWith(";")) {
            return;
        }

        throw new MalformedQueryException("Query should end with semicolon");
    }
}
