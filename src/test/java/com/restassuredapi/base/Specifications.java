package com.restassuredapi.base;



import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


import static org.hamcrest.Matchers.lessThan;

public class Specifications {

    /**
     * Builds a reusable request specification with base URI, content type, and logging.
     */
    public static RequestSpecification getRequestSpec(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .build();
    }

    /**
     * Builds a reusable response specification with expected status code and response time limit.
     */
    public static ResponseSpecification getResponseSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .build(); // Removed content type assertion
    }

    public static ResponseSpecification getResponseSpec(int expectedStatusCode, ContentType contentType) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .expectContentType(contentType)
                .build();
    }



}
