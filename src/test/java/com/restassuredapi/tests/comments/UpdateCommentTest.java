package com.restassuredapi.tests.comments;

import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.data.TestDataProvider;
import com.restassuredapi.models.Comment;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("PUT/ Comments")
@Feature("Update comments validations")
public class UpdateCommentTest extends BaseTest {

    private static final String COMMENTS_ENDPOINT = "/comments";

    //  Positive: Update valid comment
    @Story("/")
    @DisplayName(" update comments with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateComment_withValidData_shouldReturn200AndUpdatedValues() {
        int commentId = 1;
        Comment updatedComment = TestDataProvider.getValidUpdatedComment(commentId);

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .body(updatedComment)
                .when()
                .put(COMMENTS_ENDPOINT + "/" + commentId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(commentId))
                .body("name", equalTo(updatedComment.getName()))
                .body("email", equalTo(updatedComment.getEmail()))
                .body("body", equalTo(updatedComment.getBody()))
                .extract().response();

        System.out.println("Updated comment ID: " + response.path("id"));
    }

    //  Schema validation

    @Story("/")
    @DisplayName(" update comment should match response schema")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateComment_shouldMatchResponseSchema() {
        int commentId = 2;
        Comment updatedComment = TestDataProvider.getValidUpdatedComment(commentId);

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedComment)
                .when()
                .put(COMMENTS_ENDPOINT + "/" + commentId)
                .then()
                .statusCode(200)
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("comment-schema.json"));
    }

    //  Negative: Empty payload

    @Story("/")
    @DisplayName(" update comment with empty payload")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateComment_withEmptyPayload_shouldReturn400OrSimilar() {
        int commentId = 3;

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body("")
                .when()
                .put(COMMENTS_ENDPOINT + "/" + commentId)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }

    // This test documents what SHOULD happen in a production-grade API.
    // JSONPlaceholder does not currently return 400 for this case, so this will fail

    @Story("/")
    @DisplayName(" update comments with not existent id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateComment_withNonExistentId_shouldReturn200_butNotActuallyUpdateAnything() {
        int invalidCommentId = 9999;
        Comment comment = TestDataProvider.getNonExistentComment(invalidCommentId);

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(comment)
                .when()
                .put(COMMENTS_ENDPOINT + "/" + invalidCommentId)
                .then()
                .statusCode(200); // JSONPlaceholder behavior — in real APIs, expects 404
    }

    //  Edge: Malformed JSON

    @Story("/")
    @DisplayName(" update comments with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateComment_withMalformedJson_shouldReturn400OrSimilar() {
        int commentId = 4;
        String badJson = TestDataProvider.getMalformedCommentJson();

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(badJson)
                .when()
                .put(COMMENTS_ENDPOINT + "/" + commentId)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }
}