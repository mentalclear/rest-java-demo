package stg.challenges;

import kong.unirest.Unirest;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.path.json.JsonPath.from;

public class Challenge9Unirest {
    // Unirest library example. Benefit here, Unirest doesn't need browser cookies for this endpoint.

    @Test(priority = 1)
    public void getValueTypeForEachKey() {
        String endpointURL = "https://www.copart.com/public/lots/search";
        String searchQuery = "Ford Mustang GT";

        String response = collectResponseBody(endpointURL, searchQuery);
        mapValuesFromResponse(response);
    }

    private void mapValuesFromResponse(String response) {
        Map<Object, Object> allItems = from(response).getMap("data.results.content[0]");
        for (Map.Entry<Object,Object> item : allItems.entrySet())
            System.out.println("Key: " + item.getKey() +
                    ", Value is a: " + item.getValue().getClass().getSimpleName());
    }

    public String collectResponseBody(String endpoint, String searchQuery){
        return Unirest.post(endpoint)
                .header("accept", "application/json")
                .queryString("query", searchQuery)
                .asString()
                .getBody();
    }

    @AfterSuite
    public void stopSuite() {
       Unirest.shutDown();
    }
}
