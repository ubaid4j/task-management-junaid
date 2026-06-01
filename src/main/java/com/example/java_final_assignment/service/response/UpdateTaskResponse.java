package com.example.java_final_assignment.service.response;


import com.example.java_final_assignment.model.TaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateTaskResponse {
    private UUID taskId;
    private LocalDate dueDate;
    private String title;
    private String description;
    private UUID assigneeUuid;
    private TaskStatusEnum status;
}
