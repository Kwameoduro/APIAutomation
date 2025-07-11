package com.restassuredapi.tests.photos;


import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.data.TestDataProvider;
import com.restassuredapi.models.Photo;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("POST/ Photos")
@Feature("Create Photos validations")
public class CreatePhotoTest extends BaseTest {

    private static final String PHOTOS_ENDPOINT = "/photos";

    @Story("/")
    @DisplayName(" create photo with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createPhoto_withValidData_shouldReturn201AndCorrectFields() {
        Photo photo = TestDataProvider.getValidPhotoForCreate();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(photo)
                .when()
                .post(PHOTOS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(201))
                .body("title", equalTo(photo.getTitle()))
                .body("albumId", equalTo(photo.getAlbumId()))
                .body("url", equalTo(photo.getUrl()))
                .body("thumbnailUrl", equalTo(photo.getThumbnailUrl()))
                .body("id", notNullValue());
    }

    // This test is expected to fail against JSONPlaceholder because it lacks validation.
    @Story("/")
    @DisplayName(" create photos with missing title")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createPhoto_withMissingTitle_shouldReturn400OrSimilar() {
        Photo invalidPhoto = TestDataProvider.getInvalidPhotoMissingTitle(1);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(invalidPhoto)
                .when()
                .post(PHOTOS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }

    @Story("/")
    @DisplayName(" create photos with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createPhoto_withMalformedJson_shouldReturn400OrSimilar() {
        String malformedJson = TestDataProvider.getMalformedPhotoJson();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(malformedJson)
                .when()
                .post(PHOTOS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }


    @Story("/")
    @DisplayName(" create photo should match schema")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createPhoto_responseShouldMatchSchema() {
        Photo photo = TestDataProvider.getValidPhotoForCreate();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(photo)
                .when()
                .post(PHOTOS_ENDPOINT)
                .then()
                .statusCode(201)
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("photo-schema.json"));
    }
}
