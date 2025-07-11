package com.restassuredapi.tests.posts;

import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Post;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("GET/ Posts")
@Feature("Get posts validations")
public class GetPostsTest extends BaseTest {

    private static final String POSTS_ENDPOINT = "/posts";

    @Story("/")
    @DisplayName(" get all post")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAllPosts_shouldReturn200AndListOf100() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(POSTS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("size()", equalTo(100))
                .extract()
                .response();

        List<Post> posts = response.jsonPath().getList("", Post.class);
        System.out.println("Total posts retrieved: " + posts.size());

        assert posts.get(0).getId() > 0;
    }

    @Story("/")
    @DisplayName(" get single post with valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getSinglePost_withValidId_shouldReturnValidPost() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(POSTS_ENDPOINT + "/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", notNullValue())
                .body("title", not(empty()));
    }

    @Story("/")
    @DisplayName(" get post with query param")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getPost_withQueryParam_shouldReturnFilteredPosts() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .queryParam("userId", 1)
                .when()
                .get(POSTS_ENDPOINT)
                .then()
                .statusCode(200)
                .body("userId", everyItem(equalTo(1)));
    }

    @Story("/")
    @DisplayName(" get post with non existent id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getPost_withNonExistentId_shouldReturn404() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(POSTS_ENDPOINT + "/99999")
                .then()
                .statusCode(404);
    }


    @Story("/")
    @DisplayName(" get post with invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getPost_withInvalidId_shouldReturn404() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(POSTS_ENDPOINT + "/abc")
                .then()
                .statusCode(404);
    }
    @Story("/")
    @DisplayName(" get post with negative id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getPost_withNegativeId_shouldReturn200Or404() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(POSTS_ENDPOINT + "/-1")
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Story("/")
    @DisplayName(" get post with trailing slash")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getPost_withTrailingSlash_shouldReturn404() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(POSTS_ENDPOINT + "//")
                .then()
                .statusCode(404);
    }
}
