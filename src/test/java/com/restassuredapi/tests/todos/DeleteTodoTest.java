package com.restassuredapi.tests.todos;



import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DELETE/ Todos")
@Feature("Delete todos validations")
public class DeleteTodoTest extends BaseTest {

    private static final String TODOS_ENDPOINT = "/todos";

    //  Positive: valid ID
    @Story("/")
    @DisplayName(" delete todo with valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteTodo_withValidId_shouldReturn200AndEmptyBody() {
        int validId = 1;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .delete(TODOS_ENDPOINT + "/" + validId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body(anyOf(equalTo(""), equalTo("{}"))) // Accept either "" or {}
                .extract()
                .response();

        System.out.println("Deleted Todo ID: " + validId);
    }

    //  Negative: ID does not exist
    @Story("/")
    @DisplayName(" delete todo with invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteTodo_withInvalidId_shouldReturn200Or404() {
        int invalidId = 99999;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(TODOS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(anyOf(is(404), is(200))); // JSONPlaceholder fakes 200
    }

    /**
     * Edge Case: Deleting a Todo with a negative ID should ideally return 404/400,
     * but JSONPlaceholder returns 200 — so we keep the test failing to document that.
     */
    @Story("/")
    @DisplayName(" delete todo with zero or negative id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteTodo_withZeroOrNegativeId_shouldReturn400OrSimilar() {
        int invalidId = -10;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(TODOS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(anyOf(is(400), is(404), is(500))); // This will FAIL because placeholder returns 200
    }
}
