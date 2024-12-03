package com.microboxlabs;

import com.microboxlabs.service.LogService;
import com.microboxlabs.service.contract.form.FileUploadForm;
import com.microboxlabs.service.contract.to.LogTO;
import com.microboxlabs.service.contract.to.PaginatedTO;
import com.microboxlabs.service.contract.to.criteria.AdvanceCriteriaTO;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static com.microboxlabs.mock.LogTOMock.createMockList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class LogResourceTest {
    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void testUploadLog() {
        FileUploadForm form = new FileUploadForm();
        form.setFile(new ByteArrayInputStream("test-log-file-content".getBytes())); // Mocked file content

        given()
                .multiPart("file", "test.log", form.getFile())
                .when()
                .post("/api/logs/upload")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin", "user"})
    void testGetLogs() {
        PaginatedTO<LogTO> mockResponse = new PaginatedTO<>(createMockList(), 0, 2, 2);

        LogService mockLogService = Mockito.mock(LogService.class);
        Mockito.when(mockLogService.findAll(any())).thenReturn(mockResponse);

        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/logs")
                .then()
                .statusCode(200)
                .body("data.size()", is(10));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin", "user"})
    void testFilterLogs() {
        PaginatedTO<LogTO> mockResponse = new PaginatedTO<>(
                createMockList(),
                0, 10, 1);

        LogService mockLogService = Mockito.mock(LogService.class);
        Mockito.when(mockLogService.findAll(any(AdvanceCriteriaTO.class))).thenReturn(mockResponse);

        AdvanceCriteriaTO criteria = new AdvanceCriteriaTO();
        criteria.setPage(0);
        criteria.setSize(10);
        criteria.setFields(Map.of("logLevel", "ERROR", "serviceName", "Service-A"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(criteria)
                .when()
                .post("/api/logs/filter")
                .then()
                .statusCode(200)
                .body("data.size()", is(10));
    }

    @Test
    @TestSecurity(user = "user", roles = {"user"})
    void testUnauthorizedUpload() {
        FileUploadForm form = new FileUploadForm();
        form.setFile(new ByteArrayInputStream("test-log-file-content".getBytes())); // Mocked file content

        given()
                .multiPart("file", "test.log", form.getFile())
                .when()
                .post("/api/logs/upload")
                .then()
                .statusCode(403); // Forbidden for non-admin users
    }
}