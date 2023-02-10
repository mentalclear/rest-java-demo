package go.rest.tests.get;

import go.rest.tests.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.not;

public class GoRestGetPostsTests {
    private RequestSpecification httpRequest;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = Constants.POSTS_URL;
        httpRequest = RestAssured.given();
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldHaveCorrectJSONSchema(){
        var response = httpRequest.get().then();
        var schemaMatcher = JsonSchemaValidator.matchesJsonSchemaInClasspath("posts_schema.json");
        response.assertThat().body((schemaMatcher));
    }
    @Test
    public void getRequestToUsersEndpoint_ShouldFailIncorrectJSONSchema(){
        var response = httpRequest.get().then();
        var schemaMatcher = JsonSchemaValidator
                .matchesJsonSchemaInClasspath("posts_schema_bad.json");
        response.assertThat().body(not((schemaMatcher)));
    }
}
