package go.rest.tests.delete;

import go.rest.tests.constants.Constants;
import go.rest.tests.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteTests {
    private static RequestSpecification httpRequest;
    private TestUtils testUtils;

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
    public void deleteRequestToUsersEndpoint_ShouldDeleteSpecificUser(){
        var existingUser = testUtils.getExistingUserID();
        var response = httpRequest.delete("/" + existingUser);
        assertEquals(response.statusCode(), 204);
    }

    @Test
    public void deleteRequestToUsersEndpoint_ShouldFailForNonExistentUser(){
        var response = httpRequest.delete("/123dfsjsf9126151237hgdfsjfhgjsdhf");
        assertEquals(response.statusCode(), 404);
        assertEquals(response.jsonPath().getString("message"), "Resource not found");
    }
}
