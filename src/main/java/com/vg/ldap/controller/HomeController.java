package com.vg.ldap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/private")
    public String index() {
        return "Welcome !!!! You have landed !!!";
    }

    @GetMapping("/public")
    public String getPublicString() {
        return "It is public.\n";
    }
}
