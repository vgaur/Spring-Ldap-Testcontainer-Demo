package com.vg.ldap.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.vg"})
public class LdapApplication {
    public static void main(String[] args){
        SpringApplication.run(LdapApplication.class, args);
    }
}
