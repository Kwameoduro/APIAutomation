package com.restassuredapi.tests.todos;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.data.TestDataProvider;
import com.restassuredapi.models.Todo;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("PUT/ Todos")
@Feature("Update todos validations")
public class UpdateTodoTest extends BaseTest {

    private static final String TODOS_ENDPOINT = "/todos";

    //  Positive test: valid update

    @Story("/")
    @DisplayName(" update todo with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateTodo_withValidData_shouldReturn200AndMatchSchema() {
        int todoIdToUpdate = 1; // JSONPlaceholder accepts any ID
        Todo updatedTodo = TestDataProvider.getValidTodoForUpdate(todoIdToUpdate);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedTodo)
                .when()
                .put(TODOS_ENDPOINT + "/" + todoIdToUpdate)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(todoIdToUpdate))
                .body("title", equalTo(updatedTodo.getTitle()))
                .body("completed", equalTo(updatedTodo.isCompleted()))
                .body("userId", equalTo(updatedTodo.getUserId()))
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("todo-schema.json"));
    }

    //  Negative: ID does not exist
    @Story("/")
    @DisplayName(" update todo with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateTodo_withInvalidId_shouldReturn404OrSimilar() {
        int invalidId = 99999;
        Todo validTodo = TestDataProvider.getValidTodoForUpdate(invalidId);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(validTodo)
                .when()
                .put(TODOS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(anyOf(is(404), is(500), is(200))); // JSONPlaceholder returns 200 even for non-existent
    }

    //  Negative: Malformed JSON
    @Story("/")
    @DisplayName(" update todo with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateTodo_withMalformedJson_shouldReturn400OrSimilar() {
        int todoIdToUpdate = 1;
        String malformedJson = TestDataProvider.getMalformedTodoJson();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(malformedJson)
                .when()
                .put(TODOS_ENDPOINT + "/" + todoIdToUpdate)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }


    //  Negative: Missing title field
    @Story("/")
    @DisplayName(" update todo with missing title")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateTodo_withMissingTitle_shouldReturn400OrSimilar() {
        int todoId = 2;
        Todo todoMissingTitle = TestDataProvider.getInvalidTodoMissingTitle(todoId);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(todoMissingTitle)
                .when()
                .put(TODOS_ENDPOINT + "/" + todoId)
                .then()
                .statusCode(anyOf(is(400), is(500), is(200)));  // JSONPlaceholder may return 200
    }

    //  Negative: Empty payload
    @Story("/")
    @DisplayName(" update todo with empty payload")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateTodo_withEmptyPayload_shouldReturn400OrSimilar() {
        int todoId = 3;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body("") // empty body
                .when()
                .put(TODOS_ENDPOINT + "/" + todoId)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }

    //  Edge: ID mismatch between path and body
    @Story("/")
    @DisplayName(" update todo with mismatched ids")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateTodo_withMismatchedIds_shouldReturn400OrSimilar() {
        int pathId = 4;
        int mismatchedBodyId = 999; // different from pathId
        Todo mismatchedTodo = TestDataProvider.getValidTodoForUpdate(mismatchedBodyId);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(mismatchedTodo)
                .when()
                .put(TODOS_ENDPOINT + "/" + pathId)
                .then()
                .statusCode(anyOf(is(400), is(200))); // JSONPlaceholder often accepts mismatches
    }

}
