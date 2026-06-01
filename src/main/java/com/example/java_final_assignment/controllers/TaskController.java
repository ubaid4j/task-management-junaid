package com.example.java_final_assignment.controllers;


import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.CreateTaskRequest;
import com.example.java_final_assignment.controllers.requests.UpdateTaskRequest;
import com.example.java_final_assignment.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("create-task")
    public AppResponse createTask(@RequestBody CreateTaskRequest request){
        return taskService.createTask(request);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("update-task")
    public AppResponse updateTask(@RequestBody UpdateTaskRequest request){
        return taskService.updateTask(request);
    }
}
