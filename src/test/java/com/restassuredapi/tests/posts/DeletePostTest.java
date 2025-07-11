package com.restassuredapi.tests.posts;

import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("DELETE/ Posts")
@Feature("Delete posts validations")
public class DeletePostTest extends BaseTest {

    private static final String POSTS_ENDPOINT = "/posts";

    @Story("/")
    @DisplayName(" delete post with valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deletePost_withValidId_shouldReturn200AndEmptyBody() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(POSTS_ENDPOINT + "/1")
                .then()
                .statusCode(200)
                .body(equalTo("{}")); //  JSONPlaceholder returns "{}"
    }


    @Story("/")
    @DisplayName(" delete post with non existent id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deletePost_withNonExistentId_shouldReturn200AndEmptyBody() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(POSTS_ENDPOINT + "/99999")
                .then()
                .statusCode(200)
                .body(equalTo("{}")); //  JSONPlaceholder returns empty JSON object
    }


    @Story("/")
    @DisplayName(" delete post with invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deletePost_withInvalidId_shouldReturn200AndEmptyObject() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(POSTS_ENDPOINT + "/abc")
                .then()
                .statusCode(200)
                .body("", anEmptyMap()); // Matches `{}` in the response
    }


    @Story("/")
    @DisplayName(" delete post with negative id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deletePost_withNegativeId_shouldReturn200Or404() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(POSTS_ENDPOINT + "/-5")
                .then()
                .statusCode(anyOf(is(200), is(404))); // JSONPlaceholder returns 200
    }

    @Story("/")
    @DisplayName(" delete post without id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deletePost_withoutId_shouldReturn404() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(POSTS_ENDPOINT) // No /{id} at the end
                .then()
                .statusCode(404);
    }
}
