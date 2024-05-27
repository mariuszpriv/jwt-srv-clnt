package org.example.demojwt.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloWorldController {

    @Value("${simulator.option.one}")
    String firstOption;
    @Value("${simulator.option.two}")
    String secondOption;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, world!";
    }

    @GetMapping({"/greet", "/abc"})
    public String greet(@RequestParam(name = "optparam", required = false, defaultValue = "123") String name) {
        log.info("Request content after auth to return string!");
        if (name.equals(firstOption)) {
            return "Hello, " + " First ever Mariusz" + "!";
        } else if (name.equals(secondOption)) {
            return "Hello, " + "someone else, maybe Mariusz" + "!";
        }
        return "Hello, anonymous!";
    }
}
