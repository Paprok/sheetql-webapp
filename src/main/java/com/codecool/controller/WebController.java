package com.codecool.controller;

import com.codecool.model.Entry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {
    @GetMapping("/shtql")
    public String display() {
        return "shtql";
    }
}
