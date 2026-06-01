package com.example.java_final_assignment.controllers.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateTaskRequest {
    private LocalDate dueDate;
    private String title;
    private String description;
    private UUID assigneeUuid;
}
