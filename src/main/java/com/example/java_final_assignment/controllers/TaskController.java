package com.example.java_final_assignment.controllers;


import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.*;
import com.example.java_final_assignment.service.TaskService;
import com.example.java_final_assignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*
 * RESTful Design Issues in This Controller:
 *
 * 1. SINGULAR RESOURCE NAME — @RequestMapping("task") should be @RequestMapping("/tasks").
 *    REST treats resources as collections; the base path should be the plural noun "/tasks".
 *
 * 2. VERBS IN URLs — REST URLs must be nouns only; HTTP methods convey the action.
 *    - "create-task"            → POST   /tasks
 *    - "update-task"            → PUT    /tasks/{uuid}   (full replace) or PATCH (partial)
 *    - "update-task-status"     → PATCH  /tasks/{uuid}/status
 *    - "view-team-tasks"        → GET    /tasks?scope=team
 *    - "view-team-member-tasks" → GET    /tasks?assignee={uuid}
 *    - "get-personal-tasks"     → GET    /tasks/me
 *    - "get-specific-task"      → GET    /tasks/{uuid}
 *    - "get-all-tasks"          → GET    /tasks          (ADMIN sees all; role filter in service)
 *
 * 3. POST USED FOR READ OPERATIONS — "view-team-tasks", "view-team-member-tasks",
 *    "get-personal-tasks", "get-specific-task", and "get-all-tasks" all fetch data
 *    but use POST. Read operations must use GET so they are safe and cacheable (RFC 9110).
 *
 * 4. ROLE-SCOPED VIEWS ON THE SAME RESOURCE — rather than separate endpoints per role,
 *    a single GET /tasks can return the correct subset based on the caller's role
 *    (all tasks for ADMIN, team tasks for MANAGER, personal tasks for USER).
 *    This keeps the URL space clean and the resource model consistent.
 *
 * 5. UNUSED IMPORT — UserService is imported and injected but never called in this controller.
 */
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
