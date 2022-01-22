package com.vg.ldap;

import com.vg.ldap.controller.LdapApplication;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.Assert;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;


@Testcontainers
@SpringBootTest(classes = LdapApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTestCase {
    private static final Logger logger = LoggerFactory.getLogger(HomeControllerTestCase.class);

    @LocalServerPort private int port;
    private static final int OPENLDAP_EXPOSED_PORT = 10389;



    @Container private static GenericContainer<?> ldapContainer =
        new GenericContainer<>("rroemhild/test-openldap").withNetworkAliases("openldap")
            .withAccessToHost(true).withExposedPorts( 10389, 10636)
            .withLogConsumer(new Slf4jLogConsumer(logger));
    ;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        org.testcontainers.Testcontainers.exposeHostPorts(10389, 10636);
        //ldapContainer.execInContainer("ldapadd", "-x", "-D", "cn=admin,dc=mycompany,dc=com", "-w", "admin", "-H", "ldap://", "-f", "ldap/ldap-mycompany-com.ldif");
    }
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        String openldapUrl =
            String.format("ldap://localhost:%s", ldapContainer.getMappedPort(10389));
        ldapContainer.getMappedPort(10636);
        registry.add("spring.ldap.urls", () -> openldapUrl);
    }

    @Test public void testRetrieveStudentCourse() throws JSONException {
        ResponseEntity<String> response = restTemplate.withBasicAuth("leela", "leela")
            .getForEntity(createURLWithPort("/private"), String.class);

        String expected = "Welcome !!!! You have landed !!!";

        Assertions.assertEquals(expected, response.getBody(), "Response not asserted");
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
