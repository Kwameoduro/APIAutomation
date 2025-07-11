package com.restassuredapi.tests.users;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.user.User;
import com.restassuredapi.data.TestDataProvider;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@Epic("PUT/ Users")
@Feature("Update users validations")
public class UpdateUserTest extends BaseTest {

    private static final String USERS_ENDPOINT = "/users";

    @Story("/")
    @DisplayName(" update user with valid data")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void updateUser_withValidData_shouldReturn200AndUpdatedFields() {
        int userId = 1; // Updating user with ID 1 (static placeholder)

        User updatedUser = TestDataProvider.getValidUserForUpdate(userId);

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .body(updatedUser)
                .when()
                .put(USERS_ENDPOINT + "/" + userId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(userId))
                .body("name", equalTo(updatedUser.getName()))
                .body("email", equalTo(updatedUser.getEmail()))
                .body("username", equalTo(updatedUser.getUsername()))
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("user-schema.json"))
                .extract()
                .response();

        System.out.println("Updated user ID: " + response.path("id"));
    }

    @Story("/")
    @DisplayName(" update user with valid data returns 200")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void updateUser_withValidData_shouldReturn200() {
        int userId = 1;
        User updatedUser = TestDataProvider.getValidUserForUpdate(userId);

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedUser)
                .when()
                .put(USERS_ENDPOINT + "/" + userId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(userId));
    }

    // Fails in JSONPlaceholder, but kept for realistic validation in real-world APIs
    @Story("/")
    @DisplayName(" update user with missing name")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void updateUser_withMissingName_shouldReturn400OrSimilar() {
        int userId = 1;
        User invalidUser = TestDataProvider.getUserMissingName(userId);

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(invalidUser)
                .when()
                .put(USERS_ENDPOINT + "/" + userId)
                .then()
                .statusCode(400); //
    }

    @Story("/")
    @DisplayName(" update user with malformed json")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void updateUser_withMalformedJson_shouldReturn400() {
        int userId = 1;
        String malformedJson = TestDataProvider.getMalformedUserJson();

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(malformedJson)
                .when()
                .put(USERS_ENDPOINT + "/" + userId)
                .then()
                .statusCode(400);
    }

    // Fails in JSONPlaceholder, but correct for real APIs
    @Story("/")
    @DisplayName(" update user with non existent id")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void updateUser_withNonExistentId_shouldReturn404OrSimilar() {
        int nonExistentUserId = 99999;
        User validUser = TestDataProvider.getValidUserForUpdate(nonExistentUserId);

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(validUser)
                .when()
                .put(USERS_ENDPOINT + "/" + nonExistentUserId)
                .then()
                .statusCode(404); // JSONPlaceholder may still return 200, but a real API should return 404
    }

    // Expected to fail with JSONPlaceholder since it doesn't validate inputs,
    // but should pass in real-world APIs that reject overly long fields.
    @Story("/")
    @DisplayName(" update user with excessively long name")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void updateUser_withExcessivelyLongName_shouldReturn400OrSimilar() {
        int userId = 1;
        User longNameUser = TestDataProvider.getUserWithLongName(userId);

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(longNameUser)
                .when()
                .put(USERS_ENDPOINT + "/" + userId)
                .then()
                .statusCode(400); // Or 200, depending on backend validation
    }

}
