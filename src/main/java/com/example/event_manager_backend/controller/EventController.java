package com.example.event_manager_backend.controller;

import com.example.event_manager_backend.dto.BatchPayloadDto;
import com.example.event_manager_backend.exception.BatchProcessingException;
import com.example.event_manager_backend.exception.EventNotFoundException;
import com.example.event_manager_backend.entity.Event;
import com.example.event_manager_backend.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@Validated
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable UUID eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            throw new EventNotFoundException("Event not found with ID: " + eventId);
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody @Valid Event event) {
        try {
            Event createdEvent = eventService.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID eventId, @RequestBody @Valid Event eventDetails) {
        Event updatedEvent = eventService.updateEvent(eventId, eventDetails);
        if (updatedEvent == null) {
            throw new EventNotFoundException("Cannot update. Event not found with ID: " + eventId);
        }
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<String> createEventsFromBatch(@RequestBody @Valid BatchPayloadDto batchPayload) {
        try {
            boolean success = eventService.processBatch(batchPayload);
            if (!success) {
                throw new BatchProcessingException("Failed to process batch.");
            }
            return ResponseEntity.ok("Batch processed successfully");
        } catch (BatchProcessingException ex) {
            throw ex;
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing batch");
        }
    }
}
