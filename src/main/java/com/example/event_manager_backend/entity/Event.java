package com.example.event_manager_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID eventId;

    @NotBlank(message = "transId is mandatory")
    @Column(nullable = false)
    private String transId;

    @NotBlank(message = "transTms is mandatory")
    @Column(nullable = false)
    private String transTms;

    @NotBlank(message = "rcNum is mandatory")
    @Column(nullable = false)
    private String rcNum;

    @NotBlank(message = "clientId is mandatory")
    @Column(nullable = false)
    private String clientId;

    @NotNull(message = "eventCnt is mandatory")
    @Column(nullable = false)
    private Integer eventCnt;

    @NotBlank(message = "locationCd is mandatory")
    @Column(nullable = false)
    private String locationCd;

    @NotBlank(message = "locationId1 is mandatory")
    @Column(nullable = false)
    private String locationId1;

    private String locationId2;
    private String addrNbr;
}


