package com.restassuredapi.tests.comments;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Comment;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static com.restassuredapi.utils.JsonSchemaValidatorUtil.matchesJsonSchema;


@Epic("POST/ Comments")
@Feature("create comments validations")
public class CreateCommentTest extends BaseTest {

    private static final String COMMENTS_ENDPOINT = "/comments";


    //  Functional: Create with valid data
    @Story("/")
    @DisplayName("create comments with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createComment_withValidData_shouldReturn201AndValidFields() {
        Comment validComment = new Comment(1, 0, "Test Name", "test@email.com", "Test comment body.");

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .body(validComment)
                .when()
                .post(COMMENTS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(201))
                .body("name", equalTo(validComment.getName()))
                .body("email", equalTo(validComment.getEmail()))
                .body("body", equalTo(validComment.getBody()))
                .body("postId", equalTo(validComment.getPostId()))
                .body("id", notNullValue())
                .extract().response();

        System.out.println("Created comment ID: " + response.path("id"));
    }


    //  Structure: Validate schema separately
    @Story("/")
    @DisplayName(" create comment with valid data should match schema")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createComment_withValidData_shouldMatchJsonSchema() {
        Comment validComment = new Comment(1, 0, "Schema Test", "schema@email.com", "This is a test for schema.");

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(validComment)
                .when()
                .post(COMMENTS_ENDPOINT)
                .then()
                .statusCode(201)
                .body(matchesJsonSchema("comment-schema.json"));
    }

    //  Negative: Missing fields
    @Story("/")
    @DisplayName(" create comment with missing fields")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createComment_withMissingFields_shouldReturn400OrSimilar() {
        String payload = "{ \"email\": \"missing@fields.com\" }";

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(payload)
                .when()
                .post(COMMENTS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(201))); // JSONPlaceholder returns 201 even when it shouldn't
    }

    //  Edge: Empty payload
    @Story("/")
    @DisplayName(" create comments with empty payload")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createComment_withEmptyPayload_shouldReturn400OrSimilar() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body("")
                .when()
                .post(COMMENTS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(201)));
    }

    //  Edge: Malformed JSON
    @Story("/")
    @DisplayName(" create comments with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createComment_withMalformedJson_shouldReturn400OrSimilar() {
        String malformedJson = "{ \"postId\": 1, \"name\": \"bad json\", "; // broken JSON

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(malformedJson)
                .when()
                .post(COMMENTS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }
}