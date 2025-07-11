package com.restassuredapi.tests.album;

import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("DELETE/ ALBUM")
@Feature("Delete album validations")
public class DeleteAlbumTest extends BaseTest {

    private static final String ALBUMS_ENDPOINT = "/albums";

    @Story("/")
    @DisplayName("Delete album with Valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteAlbum_withValidId_shouldReturn200AndEmptyJsonBody() {
        int albumIdToDelete = 1;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .delete(ALBUMS_ENDPOINT + "/" + albumIdToDelete)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body(equalTo("{}"))
                .extract()
                .response();

        System.out.println("Deleted album ID: " + albumIdToDelete);
    }


    @Story("/")
    @DisplayName("Delete album with Invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void deleteAlbum_withInvalidId_shouldReturn200WithEmptyJsonBody() {
        int invalidId = 9999; // Typically doesn't exist
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .delete(ALBUMS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(200) // JSONPlaceholder returns 200 even if the ID is fake
                .body(equalTo("{}")) // Correct body expectation
                .extract()
                .response();

        System.out.println("Attempted to delete non-existent album ID: " + invalidId);
    }

}
