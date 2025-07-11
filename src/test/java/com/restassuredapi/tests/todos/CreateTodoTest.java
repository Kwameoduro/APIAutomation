package com.restassuredapi.tests.todos;



import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.data.TestDataProvider;
import com.restassuredapi.models.Todo;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("POST/ Todos")
@Feature("Create todos validations")
public class CreateTodoTest extends BaseTest {

    private static final String TODOS_ENDPOINT = "/todos";

    //  Positive: Create a valid todo
    @Story("/")
    @DisplayName(" create todo with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createTodo_withValidData_shouldReturn201AndMatchSchema() {
        Todo newTodo = TestDataProvider.getValidTodoForCreate();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(newTodo)
                .when()
                .post(TODOS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(201))
                .body("title", equalTo(newTodo.getTitle()))
                .body("completed", equalTo(newTodo.isCompleted()))
                .body("userId", equalTo(newTodo.getUserId()))
                .body("id", notNullValue())
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("todo-schema.json"));
    }


    @Story("/")
    @DisplayName(" create todo with missing title")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createTodo_missingTitle_shouldReturn400OrSimilar() {
        Todo invalidTodo = TestDataProvider.getInvalidTodoMissingTitle(0);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(invalidTodo)
                .when()
                .post(TODOS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(500))); // Expected in real-world APIs
    }

    //  Negative: Malformed JSON

    @Story("/")
    @DisplayName(" create todo with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createTodo_withMalformedJson_shouldReturn400OrSimilar() {
        String malformedJson = TestDataProvider.getMalformedTodoJson();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(malformedJson)
                .when()
                .post(TODOS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }
}
