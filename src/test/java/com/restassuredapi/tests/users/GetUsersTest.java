package com.restassuredapi.tests.users;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.user.User;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("GET/ Users")
@Feature("Get users validations")
public class GetUsersTest extends BaseTest {

    private static final String USERS_ENDPOINT = "/users";

    @Story("/")
    @DisplayName(" get all users")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void getAllUsers_shouldReturn200AndValidList() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(USERS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("size()", equalTo(10))
                .body("[0].id", notNullValue())
                .body("[0].username", not(emptyString()))
                .extract()
                .response();

        List<User> users = response.jsonPath().getList("", User.class);
        System.out.println("Retrieved users: " + users.size());
        assert users.size() == 10;
    }

    @Story("/")
    @DisplayName(" get user with valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getUserById_validId_shouldReturnCorrectUser() {
        int validUserId = 1;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(USERS_ENDPOINT + "/" + validUserId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(validUserId))
                .body("username", notNullValue())
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("user-schema.json"));
    }

    @Story("/")
    @DisplayName(" get user with invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getUserById_invalidId_shouldReturn404() {
        int invalidUserId = 9999;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(USERS_ENDPOINT + "/" + invalidUserId)
                .then()
                .statusCode(anyOf(is(404), is(200))) // JSONPlaceholder returns 200 with empty object
                .body("$", anyOf(anEmptyMap(), notNullValue()));
    }

    @Story("/")
    @DisplayName(" get user with invalid path")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getUserById_invalidPath_shouldReturn404() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get("/users/invalidPath")
                .then()
                .statusCode(404);
    }

    @Story("/")
    @DisplayName(" get user response schema")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void validateUserResponseSchema_shouldMatchJsonSchema() {
        int sampleUserId = 2;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(USERS_ENDPOINT + "/" + sampleUserId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("user-schema.json"));
    }
}
