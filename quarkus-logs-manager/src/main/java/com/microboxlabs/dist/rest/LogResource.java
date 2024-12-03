package com.microboxlabs.dist.rest;

import com.microboxlabs.service.LogService;
import com.microboxlabs.service.contract.form.FileUploadForm;
import com.microboxlabs.service.contract.to.LogTO;
import com.microboxlabs.service.contract.to.PaginatedTO;
import com.microboxlabs.service.contract.to.criteria.AdvanceCriteriaTO;
import com.microboxlabs.service.contract.to.criteria.CriteriaTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Tag(name = "Log Management", description = "Endpoints for managing and querying logs")
@Path("/api/logs")
public class LogResource {

    private final LogService logService;
    private static final Logger logger = LoggerFactory.getLogger(LogResource.class);

    public LogResource(LogService logService) {
        this.logService = logService;
    }

    @POST
    @Path("/upload")
    @RolesAllowed("admin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Upload log file", description = "Allows admin users to upload a log file for processing")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Log file uploaded and processed successfully"),
            @APIResponse(responseCode = "400", description = "Bad request, unable to process file"),
            @APIResponse(responseCode = "403", description = "Invalid user credential"),
    })
    public Response create(@MultipartForm FileUploadForm form) {
        try {
            logger.info("Perform api/logs/upload");
            logService.parseAndSaveLogs(form.getFile());
            return Response
                    .ok()
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("")
    @RolesAllowed({"admin", "user"})
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Retrieve logs", description = "Allows users to retrieve logs with pagination")
    @APIResponse(
            responseCode = "200",
            description = "List of logs",
            content = @Content(
                    schema = @Schema(implementation = SearchResponse.class)
            )
    )
    public Response get(
            @QueryParam("page") @DefaultValue("0") @Parameter(
                    description = "Page number for pagination",
                    example = "0",
                    in = ParameterIn.QUERY
            ) int page,
            @QueryParam("size") @DefaultValue("10") @Parameter(
                    description = "Number of records per page",
                    example = "10",
                    in = ParameterIn.QUERY
            ) int size
    ) {
        try {
            logger.info("Perform api/logs/");
            final var result = logService.findAll(new CriteriaTO().withPage(page).withSize(size));
            return Response
                    .ok(result)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/filter")
    @RolesAllowed({"admin", "user"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Filter logs", description = "Allows users to filter logs based on various criteria")

    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Filtered logs",
                    content = @Content(
                            schema = @Schema(implementation = SearchResponse.class),
                            examples = @ExampleObject(
                                    name = "Filter Example",
                                    value = """
                                            {
                                              "data": [
                                                   {
                                                        "id": 23,
                                                        "logLevel": "ERROR",
                                                        "message": "Response time is slow.",
                                                        "serviceName": "Service-A",
                                                        "timestamp": "2024-12-02T09:19:36"
                                                    }
                                              ],
                                              "page": 0,
                                              "size": 0,
                                              "total": 0
                                            }"""
                            )
                    )
            ),
            @APIResponse(responseCode = "400", description = "Invalid filter criteria")
    })

    public Response search(@Parameter(
            description = "JSON object containing filter criteria",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = AdvanceCriteriaTO.class),
                    examples = {
                            @ExampleObject(name = "Filter by log level and service name", value = """
                                    {
                                      "page": 0,
                                      "size": 10,
                                      "fields": {
                                        "logLevel": "ERROR",
                                        "serviceName": "Service-A"
                                      }
                                    }"""),
                            @ExampleObject(name = "Filter by all criteria", value = """
                                    {
                                      "page": 0,
                                      "size": 10,
                                      "fields": {
                                        "logLevel": "ERROR",
                                        "serviceName": "Service-A",
                                        "startDate": "2024-01-01T00:00:00",
                                        "endDate": "2024-12-31T23:59:59"
                                      }
                                    }
                                    """)
                    }
            )
    ) AdvanceCriteriaTO criteria) {
        try {
            logger.info("Perform api/logs/filter");
            final var result = logService.findAll(criteria);
            return Response
                    .ok(result)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    private static class SearchResponse extends PaginatedTO<LogTO> {
        public SearchResponse(List<LogTO> data, int page, int size, long total) {
            super(data, page, size, total);
        }
    }
}
