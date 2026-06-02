package com.example.java_final_assignment.controllers.requests;

import com.example.java_final_assignment.model.TaskStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateTaskStatusRequest {

    @NotNull(message = "TaskId can not be null")
    private UUID taskId;

    @NotNull(message = "Status can not be null")
    private TaskStatusEnum status;
}
