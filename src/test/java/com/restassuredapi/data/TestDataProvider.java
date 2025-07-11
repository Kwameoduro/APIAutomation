package com.restassuredapi.data;


import com.restassuredapi.models.*;
import org.junit.jupiter.params.provider.Arguments;
import com.restassuredapi.models.user.Address;
import com.restassuredapi.models.user.Company;
import com.restassuredapi.models.user.Geo;
import com.restassuredapi.models.user.User;
import java.util.stream.Stream;


import java.util.stream.Stream;

public class TestDataProvider {

    // Provides test data for Post

    public static Stream<Arguments> providePostData() {
        return Stream.of(
                Arguments.of(new Post(1, 0, "First test post", "This is the body of the first post")),
                Arguments.of(new Post(2, 0, "Second test post", "Another body content here")),
                Arguments.of(new Post(3, 0, "Edge case title", "")),
                Arguments.of(new Post(4, 0, "", "Body without a title"))
        );
    }

    public static Stream<Arguments> provideUpdatePostData() {
        return Stream.of(
                Arguments.of(1, new Post(10, 1, "Updated Title 1", "Updated Body 1")),
                Arguments.of(2, new Post(20, 2, "Updated Title 2", "Updated Body 2")),
                Arguments.of(3, new Post(30, 3, "Another Title", "Another Body")),
                Arguments.of(4, new Post(40, 4, "", ""))  // edge case: empty title/body
        );
    }

    public static Stream<Arguments> provideDeletePostIds() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3),
                Arguments.of(99) // testing edge case: high ID
        );
    }

    public static Stream<Arguments> provideInvalidPostData() {
        return Stream.of(
                Arguments.of(new Post(null, "Body present", 1)),        // Missing title
                Arguments.of(new Post("Title present", null, 1)),       // Missing body
                Arguments.of(new Post("Valid Title", "Valid Body", null)), // Missing userId
                Arguments.of(new Post("", "Body with empty title", 1)), // Empty title
                Arguments.of(new Post("Title with empty body", "", 1))  // Empty body
        );
    }



    // Provides Data for Comment

    public static Stream<Comment> provideCommentData() {
        return Stream.of(
                new Comment(1, "Commenter One", "one@example.com", "This is the first comment."),
                new Comment(2, "Commenter Two", "two@example.com", "This is another comment.")
        );
    }

    public static Stream<Arguments> provideUpdateCommentData() {
        return Stream.of(
                Arguments.of(1, new Comment(1, "Updated Name", "updated@example.com", "Updated comment body")),
                Arguments.of(5, new Comment(2, "Changed Name", "change@example.com", "Changed comment body"))
        );
    }

    public static Stream<Integer> provideInvalidCommentIds() {
        return Stream.of(0, -1, 99999); // unlikely to exist
    }

    public static Comment getValidUpdatedComment(int commentId) {
        return new Comment(1, commentId, "Updated Name", "updated@email.com", "Updated comment body");
    }

    public static Comment getNonExistentComment(int commentId) {
        return new Comment(1, commentId, "Ghost", "ghost@nowhere.com", "Non-existent comment update");
    }

    public static String getMalformedCommentJson() {
        return "{ \"postId\": 1, \"name\": \"broken\", ";
    }


    //    Provides Data for Todo

    public static Todo getValidTodoForCreate() {
        return new Todo(1, 0, "Learn REST Assured", false);
    }

    public static Todo getValidTodoForUpdate(int id) {
        return new Todo(1, id, "Updated Todo Task", true);
    }

    public static Todo getInvalidTodoMissingTitle(int id) {
        return new Todo(1, id, null, false);
    }

    public static String getMalformedTodoJson() {
        return "{ \"userId\": 1, \"title\": \"Broken JSON\" ";
    }


    // Provide Data for Album

    public static Album getValidAlbumForCreate() {
        return new Album(1, 0, "New Album Title");
    }

    public static Album getValidAlbumForUpdate(int id) {
        return new Album(1, id, "Updated Album Title");
    }

    public static Album getInvalidAlbumWithMissingTitle(int id) {
        return new Album(1, id, null);
    }

    public static String getMalformedAlbumJson() {
        return "{ \"userId\": 1, \"title\": \"Broken Album JSON\" "; // deliberately broken
    }


    // Provide Data for Photos

    public static Photo getValidPhotoForCreate() {
        return new Photo(
                1,
                0, // id will be generated
                "Sample Photo Title",
                "https://via.placeholder.com/600/sample",
                "https://via.placeholder.com/150/sample"
        );
    }

    public static Photo getValidPhotoForUpdate(int id) {
        return new Photo(
                1,
                id,
                "Updated Photo Title",
                "https://via.placeholder.com/600/updated",
                "https://via.placeholder.com/150/updated"
        );
    }

    public static Photo getInvalidPhotoMissingTitle(int id) {
        return new Photo(
                1,
                id,
                null, // Missing title
                "https://via.placeholder.com/600/missing-title",
                "https://via.placeholder.com/150/missing-title"
        );
    }

    public static String getMalformedPhotoJson() {
        return "{ \"albumId\": 1, \"title\": \"Broken Photo JSON\" "; // malformed
    }


    // Provide Data for Users

    public static Stream<User> provideValidUserData() {
        return Stream.of(
                new User(
                        0, // ID will be generated
                        "John Doe",
                        "johndoe",
                        "john@example.com",
                        new Address("123 Main St", "Apt. 1", "New York", "10001", new Geo("40.7128", "-74.0060")),
                        "123-456-7890",
                        "johndoe.com",
                        new Company("Example Inc", "We build things", "build-markets")
                ),
                new User(
                        0,
                        "Jane Smith",
                        "jsmith",
                        "jane@example.com",
                        new Address("456 Elm St", "Suite 202", "Los Angeles", "90001", new Geo("34.0522", "-118.2437")),
                        "987-654-3210",
                        "janesmith.io",
                        new Company("Smith LLC", "Innovation meets reality", "innovate-tech")
                )
        );
    }


    public static User getValidUserForCreate() {
        Geo geo = new Geo("12.34", "56.78");
        Address address = new Address("123 Main St", "Suite 100", "Springfield", "12345", geo);
        Company company = new Company("OpenAI Inc.", "Language models & more", "AI-ML");

        return new User(0, "John Doe", "johnd", "john.doe@example.com", address, "123-456-7890", "johndoe.dev", company);
    }

    public static User getInvalidUserMissingName() {
        Geo geo = new Geo("12.34", "56.78");
        Address address = new Address("123 Main St", "Suite 100", "Springfield", "12345", geo);
        Company company = new Company("OpenAI Inc.", "Language models & more", "AI-ML");

        return new User(0, null, "nousername", "no.name@example.com", address, "000-000-0000", "no.name.dev", company);
    }

    public static String getMalformedUserJson() {
        return "{ \"username\": \"brokenuser\", \"email\": \"broken@email.com\" "; // missing closing brace
    }


    public static User getValidUserForUpdate(int id) {
        return new User(
                id,
                "Updated User",
                "updateduser",
                "updated@example.com",
                new Address("321 Update St", "Suite 404", "Updateville", "40404", new Geo("99.9999", "-99.9999")),
                "000-000-0000",
                "updatedsite.com",
                new Company("Updated Corp", "We innovate", "innovate-now")
        );
    }


    public static User getUserMissingName(int id) {
        return new User(
                id,
                null,
                "noNameUser",
                "missingname@example.com",
                new Address("X St", "Suite 0", "Nowhere", "00000", new Geo("0.0", "0.0")),
                "000-000-0000",
                "noname.com",
                new Company("NoName Inc", "Nothing to see", "none")
        );
    }


    public static User getUserWithLongName(int id) {
        String longName = "A".repeat(1000);
        return new User(
                id,
                longName,
                "longnameuser",
                "longname@example.com",
                new Address("Long Ave", "Apt 999", "Big City", "99999", new Geo("99.999", "-99.999")),
                "999-999-9999",
                "longname.com",
                new Company("LongName Co", "Stretch it out", "long-markets")
        );
    }



}





