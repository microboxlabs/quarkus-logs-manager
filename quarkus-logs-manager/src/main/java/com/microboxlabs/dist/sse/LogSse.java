package com.microboxlabs.dist.sse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microboxlabs.service.contract.to.EventTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.microboxlabs.util.DateUtil.instantFormatter;

@ApplicationScoped
@Tag(name = "Log SSE", description = "Endpoints to manage log stream")
@Path("/stream")
public class LogSse {
    private static final Logger logger = LoggerFactory.getLogger(LogSse.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, SseBroadcaster> broadcastersMap = new ConcurrentHashMap<>();
    private Sse sse;
    private OutboundSseEvent.Builder eventBuilder;

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
        this.eventBuilder = sse.newEventBuilder();
    }

    @POST
    @Path("/{eventCode}/subscribe")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void subscribe(@Context SseEventSink sseEventSink, @PathParam("eventCode") String eventCode) {
        logger.info("Subscription received for event {}", eventCode);
        var broadcaster = broadcastersMap.computeIfAbsent(eventCode, newBroadcaster -> sse.newBroadcaster());
        broadcaster.register(sseEventSink);
    }

    @POST
    @Path("/publish")
    public EventTO publish(EventTO eventToPublish) {
        if (eventToPublish.getId() == null)
            eventToPublish.setId(UUID.randomUUID().toString());

        eventToPublish.setEventTime(instantFormatter.format(Instant.now()));
        logger.info("Publication for event {} published: {}", eventToPublish.getEventCode(), eventToPublish.getPayload().toString());
        this.performEventPublish(eventToPublish);
        return createPublishWithBandwidthPreservation(eventToPublish);
    }

    private static EventTO createPublishWithBandwidthPreservation(EventTO event) {
        var publish = new EventTO(event);
        publish.setPayload(null);
        return publish;
    }

    private void performEventPublish(EventTO eventToPublish) {
        final var broadcaster = broadcastersMap.get(eventToPublish.getEventCode());
        if (null == broadcaster) {
            logger.info("SseBroadcaster NOT found for event {}", eventToPublish.getEventCode());
            return;
        }
        this.broadCastEvent(eventToPublish, broadcaster);
    }

    private void broadCastEvent(EventTO eventToPublish, SseBroadcaster broadcaster) {
        logger.info("SseBroadcaster found for event {}", eventToPublish.getEventCode());
        final var jsonEvent = GSON.toJson(eventToPublish);
        final var sseEvent = this.eventBuilder
                .name("message")
                .id(eventToPublish.getId())
                .mediaType(MediaType.TEXT_PLAIN_TYPE)
                .data(jsonEvent)
                .reconnectDelay(3000)
                .comment("Event type is " + eventToPublish.getEventCode())
                .build();
        broadcaster.broadcast(sseEvent);
    }
}
