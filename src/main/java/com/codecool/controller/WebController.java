package com.codecool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/shtql")
    public String display() {
        return "shtql";
    }
}
