package com.example.java_final_assignment.controllers.requests;

import com.example.java_final_assignment.model.TaskStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
public class UpdateTaskRequest {
    private UUID taskId;

    private LocalDate dueDate;
    private String title;
    private String description;
    private UUID assigneeUuid;
    private TaskStatusEnum status;
}
