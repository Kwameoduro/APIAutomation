package com.restassuredapi.tests.album;


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
import io.qameta.allure.*;


import static org.hamcrest.Matchers.*;


@Epic("POST/ ALBUM")
@Feature("Create album validations")
public class CreateAlbumTest extends BaseTest {

    private static final String ALBUMS_ENDPOINT = "/albums";

    @Story("/")
    @DisplayName("Album with Valid Data")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createAlbum_withValidData_shouldReturn201AndMatchSchema() {
        Album validAlbum = TestDataProvider.getValidAlbumForCreate();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .body(validAlbum)
                .when()
                .post(ALBUMS_ENDPOINT)
                .then()
                .spec(Specifications.getResponseSpec(201)) // 201 expected here
                .body("title", equalTo(validAlbum.getTitle()))
                .body("userId", equalTo(validAlbum.getUserId()))
                .body("id", notNullValue())
                .body(JsonSchemaValidatorUtil.matchesJsonSchema("album-schema.json"))
                .extract()
                .response();

        System.out.println("Created album ID: " + response.path("id"));
    }


    @Story("/")
    @DisplayName("Album with Missing title")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createAlbum_withMissingTitle_shouldBeHandledGracefullyEvenIfAPIDoesNotValidate() {
        Album invalidAlbum = TestDataProvider.getInvalidAlbumWithMissingTitle(0);
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        Response response = given()
                .spec(requestSpec)
                .body(invalidAlbum)
                .when()
                .post(ALBUMS_ENDPOINT)
                .then()
                .extract().response();

        System.out.println("Actual status code: " + response.statusCode());
        assert response.statusCode() == 201 : "Expected validation failure, but got success.";
    }

    @Story("/")
    @DisplayName("Album with Malformed Json")
    @Description("/")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void createAlbum_withMalformedJson_shouldReturn400OrSimilar() {
        String malformedJson = TestDataProvider.getMalformedAlbumJson();
        RequestSpecification requestSpec = Specifications.getRequestSpec(BASE_URI);

        given()
                .spec(requestSpec)
                .body(malformedJson)
                .when()
                .post(ALBUMS_ENDPOINT)
                .then()
                .statusCode(anyOf(is(400), is(500))); // Acceptable for malformed bodies
    }
}
