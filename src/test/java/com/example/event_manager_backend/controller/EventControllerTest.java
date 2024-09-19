package com.example.event_manager_backend.controller;

import com.example.event_manager_backend.dto.BatchPayloadDto;
import com.example.event_manager_backend.dto.EventDetailDto;
import com.example.event_manager_backend.dto.RecordDto;
import com.example.event_manager_backend.entity.Event;
import com.example.event_manager_backend.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    // Test case for retrieving all events
    @Test
    void testGetAllEvents() throws Exception {
        List<Event> events = new ArrayList<>();
        events.add(new Event());

        when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(events.size()));
    }

    // Test case for retrieving an event by ID
    @Test
    void testGetEventById() throws Exception {
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        event.setEventId(eventId);
        event.setTransId("trans123");
        event.setTransTms("2024-09-18T10:00:00");
        event.setRcNum("RC12345");
        event.setClientId("client123");

        when(eventService.getEventById(eventId)).thenReturn(event);

        mockMvc.perform(get("/api/events/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventId.toString()));
    }

    // Not Found Test case for retrieving an event by ID
    @Test
    void testGetEventById_NotFound() throws Exception {
        UUID eventId = UUID.randomUUID();

        when(eventService.getEventById(eventId)).thenReturn(null);

        mockMvc.perform(get("/api/events/{eventId}", eventId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Event not found with ID: " + eventId)));
    }

    // Test case for creating an event
    @Test
    void testCreateEvent() throws Exception {
        Event event = new Event();
        event.setTransId("trans123");
        event.setTransTms("2024-09-18T10:00:00");
        event.setRcNum("RC12345");
        event.setClientId("client123");
        event.setEventCnt(1);
        event.setLocationCd("DESTINATION");
        event.setLocationId1("T8C");

        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(event)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transId").value(event.getTransId()));
    }


    // Test case for updating an event
    @Test
    void testUpdateEvent() throws Exception {
        UUID eventId = UUID.randomUUID();
        Event eventDetails = new Event();
        eventDetails.setTransId("trans123");
        eventDetails.setTransTms("2024-09-18T10:00:00");
        eventDetails.setRcNum("RC12345");
        eventDetails.setClientId("client123");
        eventDetails.setEventCnt(1);
        eventDetails.setLocationCd("DESTINATION");
        eventDetails.setLocationId1("T8C");

        when(eventService.updateEvent(eventId, eventDetails)).thenReturn(eventDetails);

        mockMvc.perform(put("/api/events/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transId").value(eventDetails.getTransId()));
    }

    // Not Found Test case for updating an event
    @Test
    void testUpdateEvent_NotFound() throws Exception {
        UUID eventId = UUID.randomUUID();
        Event eventDetails = new Event();
        eventDetails.setTransId("trans123");
        eventDetails.setTransTms("2024-09-18T10:00:00");
        eventDetails.setRcNum("RC12345");
        eventDetails.setClientId("client123");
        eventDetails.setEventCnt(1);
        eventDetails.setLocationCd("DESTINATION");
        eventDetails.setLocationId1("T8C");

        when(eventService.updateEvent(eventId, eventDetails)).thenReturn(null);

        mockMvc.perform(put("/api/events/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDetails)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Cannot update. Event not found with ID: " + eventId)));
    }


    // Test case for deleting an event
    @Test
    void testDeleteEvent() throws Exception {
        UUID eventId = UUID.randomUUID();

        doNothing().when(eventService).deleteEvent(eventId);

        mockMvc.perform(delete("/api/events/{eventId}", eventId))
                .andExpect(status().isNoContent());
    }

    // Test case for processing a batch payload
    @Test
    void testProcessBatch() throws Exception {
        BatchPayloadDto batchPayload = new BatchPayloadDto();
        batchPayload.setBatchId(UUID.randomUUID());

        RecordDto recordDto = new RecordDto();
        recordDto.setTransId("trans123");
        recordDto.setTransTms("2024-09-18T10:00:00");
        recordDto.setRcNum("RC12345");
        recordDto.setClientId("client123");

        EventDetailDto eventDetailDto = new EventDetailDto();
        eventDetailDto.setEventCnt(1);
        eventDetailDto.setLocationCd("loc1");
        eventDetailDto.setLocationId1("locId1");
        eventDetailDto.setLocationId2("locId2");
        eventDetailDto.setAddrNbr("addr123");

        recordDto.setEvent(Collections.singletonList(eventDetailDto));
        batchPayload.setRecords(Collections.singletonList(recordDto));

        when(eventService.processBatch(batchPayload)).thenReturn(true);

        mockMvc.perform(post("/api/events/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(batchPayload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Batch processed successfully"));
    }

    // Utility function to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}