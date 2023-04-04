package go.rest.tests.post;

import go.rest.tests.constants.Constants;
import go.rest.tests.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostTests {
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
    public void postRequestToUsersEndpoint_ShouldReturn201andCorrectUserInfo(){
        var requestBody = testUtils.getDummyDataForRequestBody();
        var response = httpRequest
                .body(requestBody.toString())
                .post();
        assertEquals(response.statusCode(), 201);
        assertEquals(response.jsonPath().getString("gender"), requestBody.get("gender"));
        assertEquals(response.jsonPath().getString("name"), requestBody.get("name"));
        assertEquals(response.jsonPath().getString("email"), requestBody.get("email"));

        userIdForCleanup.add(response.jsonPath().getString("id"));
    }

    @Test
    public void postRequestToUsersEndpoint_ShouldReturnCorrectSchema() {
        var schemaMatcher = testUtils.getSchemaMatcher("new_user_schema.json");
        var requestBody = testUtils.getDummyDataForRequestBody();
        var response = httpRequest
                .body(requestBody.toString())
                .post();
        response.then().assertThat().body(schemaMatcher);

        userIdForCleanup.add(response.jsonPath().getString("id"));
    }

    @Test
    public void postRequestToUserEndpoint_ShouldFailWhenEmailIsNonUnique() {
        var existingUserEmail = testUtils.getExistingUserEmail();
        var requestBody = testUtils.getDummyDataForRequestBody();
        requestBody.put("email", existingUserEmail);
        var response = httpRequest
                .body(requestBody.toString())
                .post();
        assertEquals(response.statusCode(), 422);
        assertEquals(response.jsonPath().getString("[0].message"), "has already been taken");
    }

    @AfterAll
    public static void cleanUp() {
        userIdForCleanup.forEach(e -> httpRequest.delete("/" + e));
    }
}
