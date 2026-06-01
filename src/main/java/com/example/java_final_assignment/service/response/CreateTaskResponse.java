package com.example.java_final_assignment.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateTaskResponse {
    private UUID taskId;
    private String title;
}
