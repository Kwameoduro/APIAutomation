package com.restassuredapi.tests.photos;



import io.qameta.allure.*;
import com.restassuredapi.base.BaseTest;
import com.restassuredapi.base.Specifications;
import com.restassuredapi.models.Photo;
import com.restassuredapi.utils.JsonSchemaValidatorUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Epic("GET/ Photos")
@Feature("Get Photos validations")
public class GetPhotosTest extends BaseTest {

    private static final String PHOTOS_ENDPOINT = "/photos";

    @Story("/")
    @DisplayName(" get all photos")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getAllPhotos_shouldReturn200AndListOfPhotos() {
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .when()
                .get(PHOTOS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("size()", equalTo(5000)) // JSONPlaceholder has 5000 photos
                .body("[0].id", notNullValue())
                .body("[0].albumId", notNullValue())
                .body("[0].title", not(empty()))
                .extract()
                .response();

        List<Photo> photoList = response.jsonPath().getList("", Photo.class);
        System.out.println("Total photos fetched: " + photoList.size());

        assert photoList.size() == 5000;
    }

    @Story("/")
    @DisplayName(" get photo by id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getPhotoById_shouldReturnValidPhoto() {
        int photoId = 1;

        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .when()
                .get(PHOTOS_ENDPOINT + "/" + photoId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("id", equalTo(photoId))
                .body("title", not(empty()))
                .body("albumId", greaterThan(0))
                .body("url", startsWith("https://"))
                .body("thumbnailUrl", startsWith("https://"))
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("photo-schema.json"));
    }

    @Story("/")
    @DisplayName(" get photo by invalid id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getPhotoByInvalidId_shouldReturn404() {
        int invalidId = 999999;

        given()
                .spec(Specifications.getRequestSpec(BASE_URI))
                .when()
                .get(PHOTOS_ENDPOINT + "/" + invalidId)
                .then()
                .statusCode(404);
    }


    @Story("/")
    @DisplayName(" get photo by negative id")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getPhotoByNegativeId_shouldReturnEmptyObject() {
        int negativeId = -1;

        given()
                .spec(Specifications.getRequestSpec(BASE_URI))
                .when()
                .get(PHOTOS_ENDPOINT + "/" + negativeId)
                .then()
                .spec(Specifications.getResponseSpec(200))
                .body("", anEmptyMap()); // also returns an empty object
    }

}
