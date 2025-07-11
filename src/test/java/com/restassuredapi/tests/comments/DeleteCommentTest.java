package com.restassuredapi.tests.comments;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DElETE/ Comments")
@Feature("delete comments validations")
public class DeleteCommentTest extends BaseTest {

    private static final String COMMENTS_ENDPOINT = "/comments";

    //  Positive: Delete an existing comment
    @Story("/")
    @DisplayName(" delete comments with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteComment_withValidId_shouldReturnSuccessStatus() {
        int commentIdToDelete = 1; // Assuming it exists

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .delete(COMMENTS_ENDPOINT + "/" + commentIdToDelete)
                .then()
                .statusCode(anyOf(is(200), is(204))) // Accept common DELETE codes
                .body(anyOf(equalTo(""), equalTo("{}"), nullValue())) // Accept "", null, or {}
                .extract()
                .response();

        System.out.println("Deleted comment ID: " + commentIdToDelete);
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.asString());
    }



    //  Edge: Delete a non-existent comment (still returns 200 in JSONPlaceholder)
    @Story("/")
    @DisplayName(" delete comment with wrong id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteComment_withNonExistentId_shouldStillReturn200OrSimilar() {
        int nonExistentId = 9999;

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(COMMENTS_ENDPOINT + "/" + nonExistentId)
                .then()
                .statusCode(200) // JSONPlaceholder always returns 200
                .body(anyOf(equalTo("{}"), equalTo(""), nullValue())); // Accept all possible mock responses
    }


    //  Negative: Delete with invalid ID format. This is not realistic:
    @Story("/")
    @DisplayName(" delete comment with valid format")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteComment_withInvalidIdFormat_shouldReturn200OnMockAPI() {
        String invalidId = "abc"; // Invalid format

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(COMMENTS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(200); // JSONPlaceholder returns 200 even for invalid paths
    }

    // Realistic case:
    // @Test
// public void deleteComment_withInvalidIdFormat_shouldReturn404Or400InRealAPI() {
//     String invalidId = "abc";
//     RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);
//     given()
//         .spec(requestSpec)
//         .when()
//         .delete(COMMENTS_ENDPOINT + "/" + invalidId)
//         .then()
//         .statusCode(anyOf(is(400), is(404)));
// }

}

