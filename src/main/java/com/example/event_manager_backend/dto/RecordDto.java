package com.example.event_manager_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class RecordDto {
    @NotBlank(message = "transId is mandatory")
    private String transId;

    @NotBlank(message = "transTms is mandatory")
    private String transTms;

    @NotBlank(message = "rcNum is mandatory")
    private String rcNum;

    @NotBlank(message = "clientId is mandatory")
    private String clientId;

    @NotNull(message = "Event list cannot be null")
    private List<@Valid EventDetailDto> event;
}

