package com.restassuredapi.base;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import io.qameta.allure.*;


import static io.restassured.RestAssured.requestSpecification;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    protected static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    @BeforeAll
    public void setup() {
        // Set base URI for all requests
        RestAssured.baseURI = BASE_URI;

        // Default request specification
        RequestSpecification requestSpec = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .log().all();

        // Apply only request specification globally
        requestSpecification = requestSpec;

    }
}
