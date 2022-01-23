package com.vg.ldap.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.vg"})
public class LdapAuthApplication {

    public static void main(String[] args){
        SpringApplication.run(LdapAuthApplication.class, args);
    }
}
