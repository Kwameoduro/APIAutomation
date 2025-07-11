package com.restassuredapi.tests.photos;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.anyOf;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@Epic("DELETE/ Photos")
@Feature("Delete Photos validations")
public class DeletePhotoTest extends BaseTest {

    private static final String PHOTOS_ENDPOINT = "/photos";


    @Story("/")
    @DisplayName(" delete photo with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deletePhoto_withValidId_shouldReturn200AndEmptyBody() {
        int photoIdToDelete = 1; // Valid ID
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(PHOTOS_ENDPOINT + "/" + photoIdToDelete)
                .then()
                .statusCode(200)
                .body(anyOf(equalTo(""), equalTo("{}"))); // Accept both "" and "{}" as valid responses
    }

    @Story("/")
    @DisplayName(" delete photo with negative id")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void deletePhoto_withNegativeId_shouldReturn404() {
        int negativeId = -5;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .delete(PHOTOS_ENDPOINT + "/" + negativeId)
                .then()
                .statusCode(404);
    }

    @Story("/")
    @DisplayName(" delete photo with non numeric id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deletePhoto_withNonNumericId_shouldReturn200WithEmptyBody() {
        String nonNumericId = "abc";
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .delete(PHOTOS_ENDPOINT + "/" + nonNumericId)
                .then()
                .statusCode(200) // JSONPlaceholder returns 200 even for invalid ID format
                .extract()
                .response();

        String body = response.getBody().asString();
        System.out.println("Delete response for non-numeric ID: " + body);
        assert body.trim().equals("{}"); // JSONPlaceholder returns empty object
    }

}
