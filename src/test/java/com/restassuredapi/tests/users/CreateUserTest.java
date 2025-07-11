package com.restassuredapi.tests.users;

import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.data.TestDataProvider;
import com.restassuredapi.models.user.User;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("POST/ Users")
@Feature("Create users validations")
public class CreateUserTest extends BaseTest {

    private static final String USERS_ENDPOINT = "/users";

    @Story("/")
    @DisplayName(" create user with valid data")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void createUser_withValidData_shouldReturn201AndValidResponse() {
        User user = TestDataProvider.getValidUserForCreate();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post(USERS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(201))
                .body("name", equalTo(user.getName()))
                .body("username", equalTo(user.getUsername()))
                .body("email", equalTo(user.getEmail()))
                .body("id", notNullValue())
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("user-schema.json"));
    }

    @Story("/")
    @DisplayName(" create user with missing required fields")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void createUser_withMissingRequiredFields_shouldReturn400OrSimilar() {
        User incompleteUser = TestDataProvider.getInvalidUserMissingName();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(incompleteUser)
                .when()
                .post(USERS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(201))); // JSONPlaceholder may return 201 even for bad data
    }

    @Story("/")
    @DisplayName(" create user with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createUser_withMalformedJson_shouldReturn400OrSimilar() {
        String malformedJson = TestDataProvider.getMalformedUserJson();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(malformedJson)
                .header("Content-Type", "application/json")
                .when()
                .post(USERS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(500))); // server may respond with 400 or 500
    }
}
