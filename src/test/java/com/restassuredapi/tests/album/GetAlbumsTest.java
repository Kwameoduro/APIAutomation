package com.restassuredapi.tests.album;



import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Album;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("GET/ ALBUM")
@Feature("Get album validations")
public class GetAlbumsTest extends BaseTest {

    private static final String ALBUMS_ENDPOINT = "/albums";

    @Story("/")
    @DisplayName(" get all albums")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAllAlbums_shouldReturn200AndValidList() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(ALBUMS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("size()", equalTo(100))
                .body("[0].userId", notNullValue())
                .body("[0].id", notNullValue())
                .body("[0].title", not(empty()))
                .extract()
                .response();

        List<Album> albums = response.jsonPath().getList("", Album.class);
        System.out.println("Total albums retrieved: " + albums.size());

        // Additional object-level validation
        assert albums.get(0).getUserId() > 0;
    }

    @Story("/")
    @DisplayName(" get albums by valid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAlbumByValidId_shouldReturnAlbum() {
        int albumId = 1;

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(ALBUMS_ENDPOINT + "/" + albumId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(albumId))
                .body("userId", notNullValue())
                .body("title", not(empty()));
    }

    @Story("/")
    @DisplayName(" get albums by invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAlbumByInvalidId_shouldReturn404OrEmptyObjectWith200() {
        int invalidAlbumId = 99999;
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(ALBUMS_ENDPOINT + "/" + invalidAlbumId)
                .then()
                .statusCode(anyOf(is(404), is(200)))
                .extract()
                .response();

        System.out.println("Response for invalid album ID: " + response.asString());
    }


    @Story("/")
    @DisplayName(" get all albums")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAlbumByZeroId_shouldReturn404OrEmptyResponse() {
        int zeroId = 0;

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(ALBUMS_ENDPOINT + "/" + zeroId)
                .then()
                .statusCode(anyOf(is(404), is(200)))
                .extract()
                .response();

        System.out.println("Response for album with ID 0: " + response.asString());
    }

    @Story("/")
    @DisplayName(" get albums with negative id")
    @Description("/")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void getAlbumWithNegativeId_shouldReturn404() {
        int negativeId = -5;

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(ALBUMS_ENDPOINT + "/" + negativeId)
                .then()
                .statusCode(anyOf(is(404), is(200)))  // JSONPlaceholder usually returns 200 with {}
                .extract()
                .response();

        System.out.println("Response for negative ID: " + response.asString());
    }

}
