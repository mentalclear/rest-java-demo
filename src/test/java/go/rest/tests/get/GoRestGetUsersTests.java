package go.rest.tests.get;

import go.rest.tests.constants.Constants;
import go.rest.tests.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoRestGetUsersTests {
    private RequestSpecification httpRequest;
    private TestUtils testUtils;

    @BeforeEach
    public void setUp() {
        testUtils = new TestUtils();
        RestAssured.baseURI = Constants.USERS_URL;
        httpRequest = RestAssured.given();
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldReturn200(){
        var response = httpRequest.get();
        assertEquals(response.statusCode(), 200);
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldReturnCorrectStatusLine(){
        var response = httpRequest.get();
        assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldReturnJSON(){
        var response = httpRequest.get();
        assertEquals(response.contentType(),"application/json; charset=utf-8");
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldReturnCorrectHeaders(){
        var response = httpRequest.get();
        var headers = response.headers();
        assertEquals(headers.getValue("Server"), "cloudflare");
        assertEquals(headers.getValue("Content-Encoding"), "gzip");
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldReturnListOf10Users(){
        var response = httpRequest.get().then();
        int responseArraySize = response.extract().jsonPath().getList("$").size();
        assertEquals(responseArraySize, 10);
    }

    @Test
    public void getRequestToUsersEndpoint_ShouldHaveCorrectJSONSchema(){
        var schemaMatcher = testUtils.getSchemaMatcher("users_schema.json");        var response = httpRequest.get().then();

        response.assertThat().body(schemaMatcher);
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldFailIncorrectJSONSchema(){
        var schemaMatcher = testUtils.getSchemaMatcher("users_schema_bad.json");
        var response = httpRequest.get().then();
        response.assertThat().body(not(schemaMatcher));
    }

    @Test
    public void getRequestToUsersEndpoint_ShouldReturnUserData(){
        var expectedUserData = new LinkedHashMap<String, Object>();
        expectedUserData.put("id", 354475);
        expectedUserData.put("name", "Bhadrak Chaturvedi");
        expectedUserData.put("email", "chaturvedi_bhadrak@rosenbaum.net");
        expectedUserData.put("gender", "female");
        expectedUserData.put("status","active");
        var response = httpRequest.get();
        assertEquals(response.getBody().jsonPath().getMap("[0]"), expectedUserData);
    }

    @Test
    public void getRequestToUsersEndpoint_ShouldReturnUserDataForFirstUser(){
        var expectedUserData = "Bhadrak Chaturvedi";
        var response = httpRequest.get();
        assertEquals(response.getBody().jsonPath().getString("[0].name"), expectedUserData);
    }
}
