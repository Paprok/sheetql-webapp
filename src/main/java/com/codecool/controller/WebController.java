package com.codecool.controller;

import com.codecool.exception.MalformedQueryException;
import com.codecool.model.Entry;
import com.codecool.model.UserQuery;
import com.codecool.service.dataManipulation.SelectData;
import com.codecool.service.queryValidation.QueryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.FileNotFoundException;
import java.util.List;

@Controller
public class WebController {
    private SelectData selectData;
    private QueryValidator queryValidator;

    @Autowired
    public WebController(SelectData selectData, QueryValidator queryValidator) {
        this.selectData = selectData;
        this.queryValidator = queryValidator;
    }

    @GetMapping("/shtql")
    public String serveGetRequest(Model model) {
        model.addAttribute("userQuery", new UserQuery());
        return "shtql";
    }

    @PostMapping("/shtql")
    public String servePostRequest(@ModelAttribute UserQuery userQuery, Model model) {
        try {
            Entry headers = selectData.retriveTableHeaders(userQuery.getUserQuery());
            List<Entry> queryResult = selectData.handleQuery(userQuery.getUserQuery());
            model.addAttribute("entryList", queryResult);
            model.addAttribute("headersEntrys", headers);

        } catch (MalformedQueryException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return "shtql";
    }

}
