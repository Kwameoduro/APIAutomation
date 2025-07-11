package com.restassuredapi.tests.posts;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Post;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("PUT/ Posts")
@Feature("Update posts validations")
public class UpdatePostTest extends BaseTest {

    private static final String POSTS_ENDPOINT = "/posts";

    @Story("/")
    @DisplayName(" update post with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePost_withValidData_shouldReturn200AndUpdatedPost() {
        Post updatedPost = new Post("Updated Title", "Updated Body", 1);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedPost)
                .when()
                .put(POSTS_ENDPOINT + "/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo(updatedPost.getTitle()))
                .body("body", equalTo(updatedPost.getBody()))
                .body("userId", equalTo(updatedPost.getUserId()));
    }

    @Story("/")
    @DisplayName(" update post with non existent id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePost_withNonExistentId_shouldReturn200AndReflectUpdate() {
        Post updatedPost = new Post("Fake Title", "Fake Body", 99);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedPost)
                .when()
                .put(POSTS_ENDPOINT + "/99999")
                .then()
                .statusCode(200)
                .body("id", equalTo(99999))
                .body("title", equalTo(updatedPost.getTitle()));
    }

    @Story("/")
    @DisplayName(" update post with invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePost_withInvalidId_shouldReturnError() {
        Post updatedPost = new Post("Invalid ID", "Body", 1);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedPost)
                .when()
                .put(POSTS_ENDPOINT + "/abc")
                .then()
                .statusCode(500); // JSONPlaceholder actually returns 500 here
    }

    @Story("/")
    @DisplayName(" update post without id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePost_withoutId_shouldReturn404() {
        Post updatedPost = new Post("Missing ID", "Body", 1);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedPost)
                .when()
                .put(POSTS_ENDPOINT)
                .then()
                .statusCode(404);
    }

    @Story("/")
    @DisplayName(" update post with empty body")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePost_withEmptyBody_shouldReturn200AndDefaultResponse() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body("{}")
                .when()
                .put(POSTS_ENDPOINT + "/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1)); // Only 'id' will exist in response
    }

    @Story("/")
    @DisplayName(" update post with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePost_withMalformedJson_shouldReturn400OrSimilar() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body("{ title: 'Missing quotes' }") // Invalid JSON
                .when()
                .put(POSTS_ENDPOINT + "/1")
                .then()
                .statusCode(anyOf(is(400), is(422), is(500)));
    }
}
