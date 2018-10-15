package com.codecool.service.queryValidation;

import com.codecool.exception.MalformedQueryException;
import com.codecool.service.queryValidation.rules.IQueryValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryValidator implements IQueryValidator {

    private final List<IQueryValidationRule> rules;

    @Autowired
    public QueryValidator(List<IQueryValidationRule> rules) {
        this.rules = rules;
    }

    @Override
    public void validateQuery(String query) throws MalformedQueryException {
        for (IQueryValidationRule rule : rules) {
            rule.validateQuery(query);
        }
    }
}
