package com.example.event_manager_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class BatchPayloadDto {
    @NotNull(message = "batchId is mandatory")
    private UUID batchId;

    @NotNull(message = "Records list cannot be null")
    private List<@Valid RecordDto> records;
}

