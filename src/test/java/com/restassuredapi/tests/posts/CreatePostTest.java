package com.restassuredapi.tests.posts;

import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Post;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("POST/ Posts")
@Feature("Create posts validations")
public class CreatePostTest extends BaseTest {

    private static final String POSTS_ENDPOINT = "/posts";

    @Story("/")
    @DisplayName(" create post")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest
    @MethodSource("com.restassuredapi.data.TestDataProvider#providePostData")
    public void createPost_shouldReturn201Status(Post postData) {
        given()
                .spec(Specifications.getRequestSpec(BASE_URI))
                .body(postData)
                .when()
                .post(POSTS_ENDPOINT)
                .then()
                .statusCode(201);
    }

    @Story("/")
    @DisplayName(" create post should return correct fields")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest
    @MethodSource("com.restassuredapi.data.TestDataProvider#providePostData")
    public void createPost_shouldReturnCorrectFields(Post postData) {
        given()
                .spec(Specifications.getRequestSpec(BASE_URI))
                .body(postData)
                .when()
                .post(POSTS_ENDPOINT)
                .then()
                .body("title", equalTo(postData.getTitle()))
                .body("body", equalTo(postData.getBody()))
                .body("userId", equalTo(postData.getUserId()))
                .body("id", notNullValue());
    }

    @Story("/")
    @DisplayName(" create post should match schema")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest
    @MethodSource("com.restassuredapi.data.TestDataProvider#providePostData")
    public void createPost_shouldMatchSchema(Post postData) {
        given()
                .spec(Specifications.getRequestSpec(BASE_URI))
                .body(postData)
                .when()
                .post(POSTS_ENDPOINT)
                .then()
                .assertThat()
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("post-schema.json"));
    }

    // This test is expected to FAIL because JSONPlaceholder accepts invalid data
    @Story("/")
    @DisplayName(" create post with empty fields")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest
    @MethodSource("com.restassuredapi.data.TestDataProvider#provideInvalidPostData")
    public void createPost_withMissingOrEmptyFields_shouldReturn400OrSimilar(Post invalidPost) {
        given()
                .spec(Specifications.getRequestSpec(BASE_URI))
                .body(invalidPost)
                .when()
                .post(POSTS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(422), is(500))); // JSONPlaceholder returns 201 but a real API shouldn't
    }

    // This test is expected to FAIL with JSONPlaceholder.
    // Real APIs should return 400 or 422 for empty payloads.
    @Story("/")
    @DisplayName(" create post with empty payload")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createPost_withEmptyPayload_shouldReturn400OrSimilar() {
        given()
                .spec(Specifications.getRequestSpec(BASE_URI))
                .body("{}") // Completely empty JSON
                .when()
                .post(POSTS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(422), is(500)));
    }

    @Story("/")
    @DisplayName(" create post with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createPost_withMalformedJson_shouldReturn400OrSimilar() {
        given()
                .spec(Specifications.getRequestSpec(BASE_URI))
                .body("{ title: 'no-quotes' }") // Invalid JSON format
                .when()
                .post(POSTS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(422), is(500)));
    }
}