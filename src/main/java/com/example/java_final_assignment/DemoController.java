package com.example.java_final_assignment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    //Just for the sake of checking JWT Authentication
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

}
