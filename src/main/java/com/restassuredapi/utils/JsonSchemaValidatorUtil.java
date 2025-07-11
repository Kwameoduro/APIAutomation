package com.restassuredapi.utils;



import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matcher;

import java.io.InputStream;

public class JsonSchemaValidatorUtil {

    private static final String SCHEMA_FOLDER = "/schemas/";

    /**
     * Loads and returns a JSON schema matcher for the given schema file.
     *
     * @param schemaFileName The name of the schema file (e.g., "post-schema.json")
     * @return A Hamcrest matcher for JSON schema validation
     */
    public static Matcher<?> matchesJsonSchema(String schemaFileName) {
        InputStream schemaStream = JsonSchemaValidatorUtil.class
                .getResourceAsStream(SCHEMA_FOLDER + schemaFileName);

        if (schemaStream == null) {
            throw new RuntimeException("Schema file not found: " + SCHEMA_FOLDER + schemaFileName);
        }

        return JsonSchemaValidator.matchesJsonSchema(schemaStream);
    }
}
