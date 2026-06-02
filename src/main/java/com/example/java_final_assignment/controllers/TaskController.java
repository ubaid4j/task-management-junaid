package com.example.java_final_assignment.controllers;


import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.*;
import com.example.java_final_assignment.service.TaskService;
import com.example.java_final_assignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("create-task")
    public AppResponse createTask(@Valid @RequestBody CreateTaskRequest request){
        return taskService.createTask(request);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("update-task")
    public AppResponse updateTask(@Valid @RequestBody UpdateTaskRequest request){
        return taskService.updateTask(request);
    }


    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("view-team-tasks")
    public AppResponse viewAllTeamTasks(@RequestBody ViewTeamTasksRequest request){
        return taskService.viewTeamTasks(request);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("view-team-member-tasks")
    public AppResponse viewTeamMemberTasks(@Valid @RequestBody ViewTeamMemberTasksRequest request){
        return taskService.viewTeamMemberTasks(request);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("get-personal-tasks")
    public AppResponse getPersonalTasks(@RequestBody GetPersonalTasksRequest request){
        return taskService.getPersonalTasks(request);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("get-specific-task")
    public AppResponse getSpecificTask(@Valid @RequestBody GetSpecificTaskRequest request){
        return taskService.getSpecificTask(request);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("update-task-status")
    public AppResponse updateTaskStatus(@Valid @RequestBody UpdateTaskStatusRequest request){
        return taskService.updateTaskStatus(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("get-all-tasks")
    public AppResponse getAllTasks(@RequestBody GetAllTasksRequest request){
        return taskService.getAllTasks(request);
    }


}
