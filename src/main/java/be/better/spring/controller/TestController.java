package be.better.spring.controller;


import be.better.spring.core.beans.AutoWired;
import be.better.spring.core.web.servlet.mvc.Controller;
import be.better.spring.core.web.servlet.mvc.RequestMapping;
import be.better.spring.core.web.servlet.mvc.RequestParam;
import be.better.spring.service.TestService;

@Controller
public class TestController {

    @AutoWired
    private TestService salaryService;

    @RequestMapping("/test")
    public Integer testMethod(@RequestParam("num") String num) {
        return salaryService.testMethod(Integer.valueOf(num));
    }
}
