package com.codecool.service.dataManipulation;

import com.codecool.exception.MalformedQueryException;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Service
public class LogicalOperationEvaluator implements ILogicalOperationEvaluator {

    @Override
    public boolean isTrue(String operation) {
        try {
            operation = operation.replace(".*'", ".*").replace("'.*", ".*");
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            return (Boolean) engine.eval(operation.toLowerCase());
        } catch (ScriptException e) {
            return false;
        }
    }

    @Override
    public String prepareQuery(String query) throws MalformedQueryException {
        query = query.toUpperCase();
        query = convertBasicLogicalOperators(query);
        query = handleLikeOperator(query);
        return query;
    }

    private String convertBasicLogicalOperators(String query) {
        query = query
                .replace(" NOT ", " ! ")
                .replace(" AND ", " && ")
                .replace(" OR ", " || ")
                .replace(" = ", " == ")
                .replace(" <> ", " != ");
        return query;
    }

    private String handleLikeOperator(String query) throws MalformedQueryException {
        if (!query.contains(" LIKE ")) {
            return query;
        }

        while (query.contains(" LIKE ")) {
            String likeOperation = getLikeOperationFrom(query);
            String[] operands = likeOperation.split(" ");
            String convertedOperation = operands[0] + ".match(/.*" + operands[2].replace("'", "") + ".*/) != null";
            query = query.replace(likeOperation, convertedOperation);
        }

        return query;
    }

    private String getLikeOperationFrom(String query) throws MalformedQueryException {
        String[] words = query.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equalsIgnoreCase("LIKE")) {
                return words[i - 1] + " " + words[i] + " " + words[i + 1];
            }
        }
        throw new MalformedQueryException("Invalid LIKE operation syntax");
    }
}
