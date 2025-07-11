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

@Epic("PUT/ Photos")
@Feature("Update Photos validations")
public class UpdatePhotoTest extends BaseTest {

    private static final String PHOTOS_ENDPOINT = "/photos";

    @Story("/")
    @DisplayName(" update photo with valid data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePhoto_withValidData_shouldReturn200AndUpdatedFields() {
        int photoId = 1; // any existing ID in JSONPlaceholder
        Photo updatedPhoto = TestDataProvider.getValidPhotoForUpdate(photoId);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedPhoto)
                .when()
                .put(PHOTOS_ENDPOINT + "/" + photoId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(photoId))
                .body("title", equalTo(updatedPhoto.getTitle()))
                .body("url", equalTo(updatedPhoto.getUrl()))
                .body("thumbnailUrl", equalTo(updatedPhoto.getThumbnailUrl()))
                .body("albumId", equalTo(updatedPhoto.getAlbumId()));
    }

    @Story("/")
    @DisplayName(" update photo with missing title")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePhoto_withMissingTitle_shouldReturn200DespiteMissingField() {
        int photoId = 2;
        Photo invalidPhoto = TestDataProvider.getInvalidPhotoMissingTitle(photoId);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(invalidPhoto)
                .when()
                .put(PHOTOS_ENDPOINT + "/" + photoId)
                .then()
                .statusCode(200); // Actual behavior in JSONPlaceholder
    }

    @Story("/")
    @DisplayName(" update photo with malformed json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePhoto_withMalformedJson_shouldReturn400OrSimilar() {
        int photoId = 3;
        String malformedJson = TestDataProvider.getMalformedPhotoJson();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(malformedJson)
                .when()
                .put(PHOTOS_ENDPOINT + "/" + photoId)
                .then()
                .statusCode(anyOf(is(400), is(500)));
    }

    @Story("/")
    @DisplayName(" update photo response match with schema")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void updatePhoto_responseShouldMatchSchema() {
        int photoId = 4;
        Photo updatedPhoto = TestDataProvider.getValidPhotoForUpdate(photoId);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(updatedPhoto)
                .when()
                .put(PHOTOS_ENDPOINT + "/" + photoId)
                .then()
                .statusCode(200)
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("photo-schema.json"));
    }
}
