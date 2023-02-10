package go.rest.tests.utils;

import io.restassured.module.jsv.JsonSchemaValidator;

public class TestUtils {
    public JsonSchemaValidator getSchemaMatcher(String schemaFile) {
        return JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaFile);
    }
}
