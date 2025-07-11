package com.restassuredapi.tests.todos;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Todo;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("GET/ Todos")
@Feature("Get todos validations")
public class GetTodosTest extends BaseTest {

    private static final String TODOS_ENDPOINT = "/todos";

    //  Positive: Get all todos
    @Story("/")
    @DisplayName(" get all todo")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAllTodos_shouldReturn200AndExpectedSize() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(TODOS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("size()", equalTo(200))
                .body("[0].id", notNullValue())
                .body("[0].title", not(emptyOrNullString()))
                .extract().response();

        // Deserialize into List<Todo>
        List<Todo> todos = response.jsonPath().getList("", Todo.class);
        System.out.println("Total todos retrieved: " + todos.size());

        // Example assertion using deserialized object
        assert todos.get(0).getUserId() > 0;
    }

    // Schema validation (JSON schema)
    @Story("/")
    @DisplayName(" get all todo to match with schema")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAllTodos_shouldMatchJsonSchema() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(TODOS_ENDPOINT)
                .then()
                .statusCode(200)
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("todo-schema.json"));
    }

    // Positive: Get single todo by ID
    @Story("/")
    @DisplayName(" get all todo by valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getTodoByValidId_shouldReturnTodo() {
        int validTodoId = 1;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(TODOS_ENDPOINT + "/" + validTodoId)
                .then()
                .statusCode(200)
                .body("id", equalTo(validTodoId))
                .body("userId", notNullValue())
                .body("title", not(emptyOrNullString()))
                .body("completed", notNullValue());
    }

    //  Schema validation for single todo
    @Story("/")
    @DisplayName(" get all todo by valid id to mactch the schema")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getTodoByValidId_shouldMatchSchema() {
        int validTodoId = 1;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(TODOS_ENDPOINT + "/" + validTodoId)
                .then()
                .statusCode(200)
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("todo-schema.json"));
    }

    //  Negative: Non-existent ID
    @Story("/")
    @DisplayName(" get todo by non existent id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getTodoByNonExistentId_shouldReturn404() {
        int nonExistentId = 9999;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(TODOS_ENDPOINT + "/" + nonExistentId)
                .then()
                .statusCode(404); // Real APIs return 404 for not found resources
    }

    //  Negative: Invalid ID format
    @Story("/")
    @DisplayName(" get all todo by valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getTodoByInvalidIdFormat_shouldReturn404() {
        String invalidId = "abc";
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(TODOS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(404);
    }
}