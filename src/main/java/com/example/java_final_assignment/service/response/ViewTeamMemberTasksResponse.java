package com.example.java_final_assignment.service.response;

import com.example.java_final_assignment.model.TaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ViewTeamMemberTasksResponse {
    private UUID taskId;
    private String title;
    private String description;
    private TaskStatusEnum status;
    private String createdAt;
    private String dueDate;
}
