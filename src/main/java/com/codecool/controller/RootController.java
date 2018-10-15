package com.codecool.controller;

import com.codecool.exception.MalformedQueryException;
import com.codecool.model.Entry;
import com.codecool.service.dataManipulation.ISelectData;
import com.codecool.service.queryValidation.IQueryValidator;
import com.codecool.view.ConsoleLogger;
import com.codecool.view.QueryResultPrinter;
import com.codecool.view.UserInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class RootController implements CommandLineRunner {

    private final UserInput userInput;
    private final ISelectData selectData;
    private final IQueryValidator queryValidator;
    private final QueryResultPrinter resultPrinter;
    private boolean isRunning = true;

    @Autowired
    public RootController(UserInput userInput, ISelectData selectData, IQueryValidator queryValidator, QueryResultPrinter resultPrinter) {
        this.userInput = userInput;
        this.selectData = selectData;
        this.queryValidator = queryValidator;
        this.resultPrinter = resultPrinter;
    }

    @Override
    public void run(String... args) {
        try {
            while (isRunning) {
                handleUserAction();
            }
        } catch (FileNotFoundException | NullPointerException e) {
            ConsoleLogger.error("File not found");
        }
    }

    private void handleUserAction() throws FileNotFoundException {
        String input = userInput.getInput();
        if (input.equalsIgnoreCase("\\q")) {
            isRunning = false;
        } else {
            try {
                queryValidator.validateQuery(input);
                List<Entry> queryResult = selectData.handleQuery(input);
                resultPrinter.print(queryResult);
            } catch (MalformedQueryException e) {
                ConsoleLogger.warn(e.getMessage());
            }
        }
    }
}
