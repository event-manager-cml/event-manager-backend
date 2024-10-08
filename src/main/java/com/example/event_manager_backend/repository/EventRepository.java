package com.example.event_manager_backend.repository;

import com.example.event_manager_backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}