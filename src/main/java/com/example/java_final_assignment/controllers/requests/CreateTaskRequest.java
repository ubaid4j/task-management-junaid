package com.example.java_final_assignment.controllers.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateTaskRequest {

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title can not be blank")
    private String title;

    private String description;

    @NotNull(message = "Assignee Id can not be null")
    private UUID assigneeUuid;
}
