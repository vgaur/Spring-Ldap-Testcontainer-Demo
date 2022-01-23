package com.vg.ldap;

import com.vg.ldap.controller.LdapAuthApplication;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@SpringBootTest(classes = LdapAuthApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTestCase {
    private static final Logger logger = LoggerFactory.getLogger(HomeControllerTestCase.class);

    @LocalServerPort
    private int port;

    private static final int OPENLDAP_EXPOSED_PORT = 10389;


    @Container
    private static GenericContainer<?> ldapContainer =
        new GenericContainer<>("rroemhild/test-openldap")
            .withNetworkAliases("openldap")
            .withExposedPorts(10389, 10636)
            .withLogConsumer(new Slf4jLogConsumer(logger));



    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        String openldapUrl =
            String.format("ldap://localhost:%s", ldapContainer.getMappedPort(10389));
        registry.add("spring.ldap.urls", () -> openldapUrl);
    }

    @Test
    public void testPrivateInfo() {

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.withBasicAuth("leela", "leela")
            .getForEntity("http://localhost:" + port + "/private/info", String.class);

        String expected = "Welcome leela, You have landed !!!";

        Assertions.assertEquals(expected, response.getBody(), "Response not asserted");
    }
}
