package com.restassuredapi.tests.album;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Album;
import com.restassuredapi.data.TestDataProvider;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("PUT/ ALBUM")
@Feature("Update album validations")
public class UpdateAlbumTest extends BaseTest {

    private static final String ALBUMS_ENDPOINT = "/albums";

    @Story("/")
    @DisplayName(" update albums with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateAlbum_withValidData_shouldReturn200AndMatchSchema() {
        int albumIdToUpdate = 1;
        Album updatedAlbum = TestDataProvider.getValidAlbumForUpdate(albumIdToUpdate);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .body(updatedAlbum)
                .when()
                .put(ALBUMS_ENDPOINT + "/" + albumIdToUpdate)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(albumIdToUpdate))
                .body("title", equalTo(updatedAlbum.getTitle()))
                .body("userId", equalTo(updatedAlbum.getUserId()))
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("album-schema.json"))
                .extract()
                .response();

        System.out.println("Updated album ID: " + response.path("id"));
    }


    // This test is expected to fail against JSONPlaceholder
    // In a real-world API, this should return 400 or 500.
    @Story("/")
    @DisplayName(" update albums with missing title")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateAlbum_withMissingTitle_shouldReturn400OrSimilar() {
        int id = 2;
        Album invalidAlbum = TestDataProvider.getInvalidAlbumWithMissingTitle(id);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(invalidAlbum)
                .when()
                .put(ALBUMS_ENDPOINT + "/" + id)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }

    @Story("/")
    @DisplayName(" update albums with invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updateAlbum_withInvalidId_shouldReturn404OrEmptyResponse() {
        int invalidId = 9999;
        Album validAlbum = TestDataProvider.getValidAlbumForUpdate(invalidId);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .body(validAlbum)
                .when()
                .put(ALBUMS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(404) // JSONPlaceholder is returning 404 now
                .extract()
                .response();

        System.out.println("Update attempt with invalid ID response: " + response.asString());
    }

}

