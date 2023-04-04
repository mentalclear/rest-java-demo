package go.rest.tests.patch;

import go.rest.tests.constants.Constants;
import go.rest.tests.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutPatchTests {
    private static RequestSpecification httpRequest;
    private TestUtils testUtils;

    private static List<String> userIdForCleanup = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        testUtils = new TestUtils();
        RestAssured.baseURI = Constants.USERS_URL;
        httpRequest = RestAssured.given().headers(
                "Authorization",
                "Bearer " + Constants.TOKEN,
                "Content-type", "application/json"
        );
    }
    @Test
    public void patchRequestToUsersEndpoint_ShouldUpdateUserNameAndEmail(){
        var existingUser = testUtils.getExistingUserID();
        var newUserData = new JSONObject();
        newUserData.put("name", "Luke Skywalker");
        newUserData.put("email", "luke.skywalker@starwarsonylgalaxy.com");
        var response = httpRequest.body(newUserData.toString())
                .patch("/" + existingUser);
        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("name"), newUserData.get("name"));
        assertEquals(response.jsonPath().getString("email"), newUserData.get("email"));

        userIdForCleanup.add(response.jsonPath().getString("id"));
    }

    @Test
    public void putRequestToUsersEndpoint_ShouldUpdateUser() {
        var existingUser = testUtils.getExistingUserID();
        var requestBody = testUtils.getDummyDataForRequestBody();
        var response = httpRequest
                .body(requestBody.toString())
                .put("/" + existingUser);
        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getString("gender"), requestBody.get("gender"));
        assertEquals(response.jsonPath().getString("name"), requestBody.get("name"));
        assertEquals(response.jsonPath().getString("email"), requestBody.get("email"));

        userIdForCleanup.add(response.jsonPath().getString("id"));
    }

    @Test
    public void patchRequestToUsersEndpoint_ShouldFailForNonExistentUser(){
        var nonExistingUser = "123dfsjsf9126151237hgdfsjfhgjsdhf";
        var response = httpRequest.patch("/" + nonExistingUser);
        assertEquals(response.statusCode(), 404);
        assertEquals(response.jsonPath().getString("message"), "Resource not found");
    }

    @Test
    public void putRequestToUsersEndpoint_ShouldFailForNonExistentUser(){
        var nonExistingUser = "123dfsjsf9126151237hgdfsjfhgjsdhf";
        var response = httpRequest.put("/" + nonExistingUser);
        assertEquals(response.statusCode(), 404);
        assertEquals(response.jsonPath().getString("message"), "Resource not found");
    }

    @AfterAll
    public static void cleanUp() {
        userIdForCleanup.forEach(e -> httpRequest.delete("/" + e));
    }
}
