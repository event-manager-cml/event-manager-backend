package com.example.event_manager_backend.service;

import com.example.event_manager_backend.dto.BatchPayloadDto;
import com.example.event_manager_backend.dto.RecordDto;
import com.example.event_manager_backend.exception.BatchProcessingException;
import com.example.event_manager_backend.exception.EventNotFoundException;
import com.example.event_manager_backend.entity.Event;
import com.example.event_manager_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Service
public class EventService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        try {
            return eventRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching events", e);
        }
    }

    public Event getEventById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

    public Event createEvent(Event event) {
        try {
            return eventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error creating event", e);
        }
    }

    public Event updateEvent(UUID eventId, Event eventDetails) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
        try {
            event.setTransId(eventDetails.getTransId());
            event.setTransTms(eventDetails.getTransTms());
            event.setRcNum(eventDetails.getRcNum());
            event.setClientId(eventDetails.getClientId());
            event.setEventCnt(eventDetails.getEventCnt());
            event.setLocationCd(eventDetails.getLocationCd());
            event.setLocationId1(eventDetails.getLocationId1());
            event.setLocationId2(eventDetails.getLocationId2());
            event.setAddrNbr(eventDetails.getAddrNbr());
            return eventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error updating event", e);
        }
    }

    public void deleteEvent(UUID eventId) {
        try {
            eventRepository.deleteById(eventId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting event", e);
        }
    }

    public boolean processBatch(BatchPayloadDto batchPayload) {
        try {
            List<CompletableFuture<Void>> futures = batchPayload.getRecords().stream()
                    .map(record -> CompletableFuture.runAsync(() -> processRecord(record), executorService))
                    .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            return true;
        } catch (Exception e) {
            throw new BatchProcessingException("Error processing batch", e);
        }
    }

    private void processRecord(RecordDto record) {
        record.getEvent().parallelStream().forEach(eventDetail -> {
            try {
                Event event = new Event();
                event.setTransId(record.getTransId());
                event.setTransTms(record.getTransTms());
                event.setRcNum(record.getRcNum());
                event.setClientId(record.getClientId());
                event.setEventCnt(eventDetail.getEventCnt());
                event.setLocationCd(eventDetail.getLocationCd());
                event.setLocationId1(eventDetail.getLocationId1());
                event.setLocationId2(eventDetail.getLocationId2());
                event.setAddrNbr(eventDetail.getAddrNbr());

                eventRepository.save(event);
            } catch (Exception e) {
                throw new RuntimeException("Error processing record: " + eventDetail, e);
            }
        });
    }

    @PreDestroy
    public void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
