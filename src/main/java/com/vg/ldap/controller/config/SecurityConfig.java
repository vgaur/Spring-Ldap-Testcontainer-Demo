package com.vg.ldap.controller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(LdapProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ldap.userSearchBase}")
    private String userSearchBase;

    @Value("${ldap.userSearchFilter}")
    private String userSearchFilter;

    private final LdapProperties ldapProperties;

    public SecurityConfig(LdapProperties ldapProperties) {
        this.ldapProperties = ldapProperties;
    }

    @Override protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/private").authenticated()
            .antMatchers(HttpMethod.GET, "/public").permitAll()
            .anyRequest().fullyAuthenticated()
            .and().httpBasic();
        http.cors().and().csrf().disable();


    }
    //
    //    @Override protected void configure(HttpSecurity http) throws Exception {
    //        http.authorizeRequests().anyRequest().fullyAuthenticated().and().httpBasic();
    //        http.cors().and().csrf().disable();
    //    }

    @Override protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String url = String.format("%s/%s", ldapProperties.getUrls()[0], ldapProperties.getBase());

        auth.ldapAuthentication().contextSource().url(url).managerDn(ldapProperties.getUsername())
            .managerPassword(ldapProperties.getPassword()).and().userSearchBase(userSearchBase)
            .userSearchFilter(userSearchFilter).passwordCompare()
            .passwordEncoder(new LdapShaPasswordEncoder()).passwordAttribute("userPassword");
    }
}
