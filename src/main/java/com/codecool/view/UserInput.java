package com.codecool.view;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserInput {

    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
