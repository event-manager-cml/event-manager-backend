package com.example.event_manager_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailDto {

    @NotNull(message = "eventCnt is mandatory")
    private Integer eventCnt;

    @NotBlank(message = "locationCd is mandatory")
    private String locationCd;

    @NotBlank(message = "locationId1 is mandatory")
    private String locationId1;

    private String locationId2;
    private String addrNbr;
}
