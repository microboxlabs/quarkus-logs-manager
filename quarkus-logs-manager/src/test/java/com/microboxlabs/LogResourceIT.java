package com.microboxlabs;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusIntegrationTest
class LogResourceIT extends LogResourceTest {
    @Test
    void testUploadLogFileAsAdmin() {
        // Assuming basic authentication credentials for "admin" user
        given()
                .auth().basic("admin", "logAdmin") // Replace with actual credentials
                .multiPart("file", "dummy-log-content")
                .when()
                .post("/api/logs/upload")
                .then()
                .statusCode(200);
    }

    @Test
    void testRetrieveLogsAsUser() {
        // Assuming basic authentication credentials for "user" role
        given()
                .auth().basic("user", "logUser") // Replace with actual credentials
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/logs")
                .then()
                .statusCode(200)
                .body("data.size()", greaterThanOrEqualTo(0));
    }

    @Test
    void testUnauthorizedAccess() {
        RestAssured
                .get("/api/logs")
                .then()
                .statusCode(401);
    }
}
