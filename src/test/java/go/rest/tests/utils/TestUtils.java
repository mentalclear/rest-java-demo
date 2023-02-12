package go.rest.tests.utils;

import go.rest.tests.constants.Constants;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import java.util.Map;

public class TestUtils {
    private Map<String, Object> existingUser;
    private RequestSpecification httpRequest;

    public TestUtils() {
        RestAssured.baseURI = Constants.USERS_URL;
        httpRequest = RestAssured.given();
        this.existingUser = httpRequest.get().getBody().jsonPath().getMap("[0]");
    }

    public JsonSchemaValidator getSchemaMatcher(String schemaFile) {
        return JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaFile);
    }

    public JSONObject getDummyDataForRequestBody() {
        int randomNumber = (int)(Math.random() * 100);
        var obj = new JSONObject();
        obj.put( "gender", "female");
        obj.put("name", "Alliando Peddando");
        obj.put("email", "alliando.peddando-" + randomNumber + "@15ce.com");
        obj.put( "status", "active");
        return obj;
    }
    public String getExistingUserID(){
        return existingUser.get("id").toString();
    }
    public String getExistingUserName(){
        return existingUser.get("name").toString();
    }
    public String getExistingUserEmail(){
        return existingUser.get("email").toString();
    }
    public String getExistingUserStatus(){
        return existingUser.get("status").toString();
    }
}
