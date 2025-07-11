package com.restassuredapi.tests.comments;

import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Comment;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.restassuredapi.utils.JsonSchemaValidatorUtil.matchesJsonSchema;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;




@Epic("GET/ Comments")
@Feature("get comments validations")
public class GetCommentsTest extends BaseTest {

    private static final String COMMENTS_ENDPOINT = "/comments";

    @Story("/")
    @DisplayName(" get all comments")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAllComments_shouldReturn200AndValidList() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(COMMENTS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("size()", equalTo(500))
                .body("[0].id", notNullValue())
                .body("[0].email", not(empty()))
                .extract()
                .response();

        List<Comment> comments = response.jsonPath().getList("", Comment.class);
        System.out.println("Total comments retrieved: " + comments.size());

        assert comments.get(0).getPostId() > 0;
    }

    @Story("/")
    @DisplayName(" get single comment with valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getSingleComment_withValidId_shouldReturnCorrectComment() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(COMMENTS_ENDPOINT + "/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("postId", greaterThan(0))
                .body("name", not(empty()))
                .body("email", containsString("@"))
                .body("body", not(empty()));
    }

    @Story("/")
    @DisplayName(" get comments by post id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getCommentsByPostId_shouldReturnFilteredComments() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .queryParam("postId", 1)
                .when()
                .get(COMMENTS_ENDPOINT)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract()
                .response();

        List<Comment> comments = response.jsonPath().getList("", Comment.class);

        for (Comment comment : comments) {
            assert comment.getPostId() == 1;
        }
    }

    @Story("/")
    @DisplayName(" get single comments with wrong id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getSingleComment_withNonExistentId_shouldReturn404() {
        int nonExistentId = 9999; // Assuming this ID does not exist
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(COMMENTS_ENDPOINT + "/" + nonExistentId)
                .then()
                .statusCode(404); // JSONPlaceholder returns 404 for truly missing resources
    }


    @Story("/")
    @DisplayName(" get single comments with valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest
    @ValueSource(strings = { "-1", "0", "abc", "1.5", "@$%" })
    public void getSingleComment_withInvalidId_shouldHandleGracefully(String invalidId) {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(COMMENTS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(anyOf(is(200), is(404)))
                .extract()
                .response();

        System.out.println("Tried invalid ID: " + invalidId + " | Status: " + response.statusCode());
    }

    @Story("/")
    @DisplayName(" get comments with invalid post id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getComments_withInvalidPostIdQuery_shouldReturnEmptyListOr400() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .queryParam("postId", "invalid")
                .when()
                .get(COMMENTS_ENDPOINT)
                .then()
                .statusCode(200) // JSONPlaceholder will return 200 with empty array
                .extract()
                .response();

        List<?> comments = response.jsonPath().getList("");
        assert comments.isEmpty();
    }

    @Story("/")
    @DisplayName(" get comments with unused post id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getComments_withUnusedPostId_shouldReturnEmptyArray() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .queryParam("postId", 99999)
                .when()
                .get(COMMENTS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<?> comments = response.jsonPath().getList("");
        assert comments.isEmpty();
    }

    @Story("/")
    @DisplayName(" get single comment should match json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getSingleComment_shouldMatchJsonSchema() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(COMMENTS_ENDPOINT + "/1")
                .then()
                .statusCode(200)
                .body(matchesJsonSchema("comment-schema.json"));
    }

}