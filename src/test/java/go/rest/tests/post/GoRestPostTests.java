package go.rest.tests.post;

import go.rest.tests.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoRestPostTests {
    private static RequestSpecification httpRequest;
    private String bearerToken = "a1128ac3ab51160c1d15155a3a95c3c80686f527e446c147e3cc782863355377";
    private static String userIdForCleanup;


    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = Constants.USERS_URL;
        httpRequest = RestAssured.given().headers(
                "Authorization",
                "Bearer " + bearerToken,
                "Content-type", "application/json"
        );
    }

    @Test
    public void postRequestToUsersEndpoint_ShouldReturn201(){
        var requestBody = getRequestBodyData();
        var response = httpRequest
                .body(requestBody.toString())
                .post();
        assertEquals(response.statusCode(), 201);
        userIdForCleanup = response.jsonPath().getString("id");
    }

    private JSONObject getRequestBodyData() {
        JSONObject obj = new JSONObject();
        obj.put( "gender", "female");
        obj.put("name", "Allia5 Pedda5");
        obj.put("email", "allia5.pedda5@15ce.com");
        obj.put( "status", "active");
        return obj;
    }

    @AfterAll
    public static void cleanUp() {
        httpRequest.delete("/" + userIdForCleanup);
    }
}
