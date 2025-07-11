package com.restassuredapi.tests.users;



import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;


@Epic("DELETE/ Users")
@Feature("Delete users validations")
public class DeleteUserTest extends BaseTest {

    private static final String USERS_ENDPOINT = "/users";

    @Story("/")
    @DisplayName(" delete user with valid data")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void deleteUser_withValidId_shouldReturn200AndEmptyBody() {
        int userIdToDelete = 1; // ID that exists in JSONPlaceholder

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(USERS_ENDPOINT + "/" + userIdToDelete)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body(anyOf(equalTo(""), equalTo("{}"), emptyString())); // Accepts "" or {} or an empty string
    }

    @Story("/")
    @DisplayName(" delete user with non existent id")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void deleteUser_withNonExistentId_shouldStillReturn200OrSimilar() {
        int nonExistentUserId = 99999;

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(USERS_ENDPOINT + "/" + nonExistentUserId)
                .then()
                .statusCode(200); // JSONPlaceholder fakes deletion, real APIs should return 404
    }

    @Story("/")
    @DisplayName(" delete user with invalid id")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void deleteUser_withInvalidId_shouldReturn404Or400() {
        String invalidUserId = "abc";

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(USERS_ENDPOINT + "/" + invalidUserId)
                .then()
                .statusCode(404); // or 400 depending on real API
    }

    @Story("/")
    @DisplayName(" delete user without providing id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteUser_withoutProvidingId_shouldReturn404() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(USERS_ENDPOINT + "/")
                .then()
                .statusCode(404);
    }
}
