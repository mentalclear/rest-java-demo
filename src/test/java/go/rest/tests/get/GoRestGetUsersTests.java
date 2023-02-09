package go.rest.tests.get;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoRestGetUsersTests {
    private final String BASEURL = "https://gorest.co.in/public/v2";
    private final String USERS_URL = BASEURL + "/users";
    private RequestSpecification httpRequest;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = USERS_URL;
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
        var response = httpRequest.get().then();
        var schemaMatcher = JsonSchemaValidator.matchesJsonSchemaInClasspath("users_schema.json");
        response.assertThat().body((schemaMatcher));
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldFailIncorrectJSONSchema(){
        var response = httpRequest.get().then();
        var schemaMatcher = JsonSchemaValidator
                .matchesJsonSchemaInClasspath("users_schema_bad.json");
        response.assertThat().body(not((schemaMatcher)));
    }

    @Test
    public void getRequestToUsersEndpoint_ShouldReturnUserData(){
        var expectedUserData = new LinkedHashMap<String, Object>();
        expectedUserData.put("id", 347286);
        expectedUserData.put("name", "Kanchan Verma");
        expectedUserData.put("email", "verma_kanchan@lemke.biz");
        expectedUserData.put("gender", "female");
        expectedUserData.put("status","active");
        var response = httpRequest.get();
        assertEquals(response.getBody().jsonPath().getMap("[0]"), expectedUserData);
    }

    @Test
    public void getRequestToUsersEndpoint_ShouldReturnUserDataForFirstUser(){
        var expectedUserData = "Kanchan Verma";
        var response = httpRequest.get();
        assertEquals(response.getBody().jsonPath().getString("[0].name"), expectedUserData);
    }
}
