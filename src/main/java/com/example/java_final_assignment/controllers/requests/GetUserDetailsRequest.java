package com.example.java_final_assignment.controllers.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GetUserDetailsRequest {
    private UUID userId;
}
