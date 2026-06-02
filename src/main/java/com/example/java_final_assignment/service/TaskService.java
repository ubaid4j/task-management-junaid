package com.example.java_final_assignment.service;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.*;
import com.example.java_final_assignment.exceptions.GlobalException;
import com.example.java_final_assignment.model.*;
import com.example.java_final_assignment.repositories.TaskRepository;
import com.example.java_final_assignment.repositories.TeamMembersRepository;
import com.example.java_final_assignment.repositories.TeamRepository;
import com.example.java_final_assignment.repositories.UserRepository;
import com.example.java_final_assignment.service.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;

    public AppResponse createTask(CreateTaskRequest request){

        User manager = getLoggedInUser();

        Optional<Team> teamEntity = teamRepository.findTeamByManagerUuid(manager.getUuid());

        if(teamEntity.isEmpty())
            throw new GlobalException("Manager is not linked to a team");

        Team team = teamEntity.get();

        Optional<User> assigneeUserEntity = userRepository.findByUuid(request.getAssigneeUuid());

        if(assigneeUserEntity.isEmpty())
            throw new GlobalException("Assignee not found");

        User assignee = assigneeUserEntity.get();

        if(assignee.getStatus().equals(StatusEnum.INACTIVE))
            throw new GlobalException("This assignee is INACTIVE");

        List<User> teamMembers =  teamMembersRepository.findUsersByTeamId(team.getId());


        boolean isTeamMember = teamMembers.stream()
                .anyMatch(user -> user.getUuid().equals(assignee.getUuid()));

        if (!isTeamMember)
            throw new GlobalException("Assignee is not a member of this team");

        Task task = new Task();

        task.setAssignee(assignee);
        task.setCreator(manager);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatusEnum.PENDING);
        task.setDueDate(request.getDueDate());

        Task savedTask = taskRepository.save(task);

        return new AppResponse(new CreateTaskResponse(
                savedTask.getUuid(),
                savedTask.getTitle()
        ));

    }

    public AppResponse updateTask(UpdateTaskRequest request){

        Optional<Task> taskEntity = taskRepository.findByUuid(request.getTaskId());

        if(taskEntity.isEmpty())
            throw new GlobalException("Task not found");

        Task task = taskEntity.get();



        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Optional<User> managerEntity = userRepository.findByEmail(email);

        if(managerEntity.isEmpty())
            throw new GlobalException("Manager not found");

        User manager = managerEntity.get();

        Optional<Team> teamEntity = teamRepository.findTeamByManagerUuid(manager.getUuid());

        if(teamEntity.isEmpty())
            throw new GlobalException("Manager is not linked to a team");

        Team team = teamEntity.get();


       if(request.getAssigneeUuid() != null){
           Optional<User> assigneeUserEntity = userRepository.findByUuid(request.getAssigneeUuid());

           if(assigneeUserEntity.isEmpty())
               throw new GlobalException("Assignee not found");

           User assignee = assigneeUserEntity.get();

           if(assignee.getStatus().equals(StatusEnum.INACTIVE))
               throw new GlobalException("This assignee is INACTIVE");

           List<User> teamMembers =  teamMembersRepository.findUsersByTeamId(team.getId());


           boolean isTeamMember = teamMembers.stream()
                   .anyMatch(user -> user.getUuid().equals(assignee.getUuid()));

           if (!isTeamMember)
               throw new GlobalException("Assignee is not a member of this team");

           task.setAssignee(
                   request.getAssigneeUuid()!=null
                           ? assignee
                           : task.getAssignee()
           );
       }




        task.setDueDate(
                request.getDueDate() != null
                        ? request.getDueDate()
                        : task.getDueDate()
        );




        task.setTitle(
                request.getTitle() !=null
                ? request.getTitle()
                : task.getTitle()
        );

        task.setDescription(
                request.getDescription()!=null
                ? request.getDescription()
                : task.getDescription()
        );

        task.setStatus(
                request.getStatus() != null
                ? request.getStatus()
                : task.getStatus()
        );

        Task savedTask = taskRepository.save(task);

        return new AppResponse(new UpdateTaskResponse(
                savedTask.getUuid(),
                savedTask.getDueDate(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getAssignee().getUuid(),
                savedTask.getStatus()
        ));
    }

    public AppResponse viewTeamTasks(ViewTeamTasksRequest request){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Optional<User> managerEntity = userRepository.findByEmail(email);

        if(managerEntity.isEmpty())
            throw new GlobalException("Manager not found");

        User manager = managerEntity.get();


        Optional<Team> teamEntity = teamRepository.findTeamByManagerUuid(manager.getUuid());

        if(teamEntity.isEmpty())
            throw new GlobalException("Manager is not linked to a team.");

        Team team = teamEntity.get();

        List<Long> userIds = teamMembersRepository.findUserIdsByTeamId(team.getId());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Task> tasks = taskRepository.findTasksByUserIds(userIds, pageable);

        List<GetTeamTasksResponse> response = new ArrayList<>();



        for(Task task: tasks){
            GetTeamTasksResponse res = new GetTeamTasksResponse();

            res.setTaskId(task.getUuid());
            res.setAssigneeId(task.getAssignee().getUuid());
            res.setCreatedAt(task.getCreatedAt() != null ? returnFormattedDate(task.getCreatedAt()) : null);
            res.setDueDate(task.getDueDate() != null ? returnFormattedDate(task.getDueDate()) : null);
            res.setTitle(task.getTitle());
            res.setDescription(task.getDescription());
            res.setStatus(task.getStatus());

            response.add(res);
        }

        return new AppResponse(response);
    }

    private String returnFormattedDate(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");
        return formatter.format(date);
    }

    public AppResponse viewTeamMemberTasks(ViewTeamMemberTasksRequest request){

        User manager = getLoggedInUser();


        Optional<Team> teamEntity = teamRepository.findTeamByManagerUuid(manager.getUuid());

        if(teamEntity.isEmpty())
            throw new GlobalException("Manager is not linked to a team.");

        Team team = teamEntity.get();

        Optional<User> assigneeEntity = userRepository.findByUuid(request.getAssigneeId());
        if(assigneeEntity.isEmpty())
            throw new GlobalException("User not found");

        List<UUID> userUuids = teamMembersRepository.findUserUuidsByTeamId(team.getId());

        boolean isTeamMember = userUuids.stream()
                .anyMatch(uuid -> uuid.equals(request.getAssigneeId()));

        if (!isTeamMember)
            throw new GlobalException("Assignee is not a member of this team");


        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Task> tasks = taskRepository.findTasksByUserUuid(request.getAssigneeId(), pageable);

        List<ViewTeamMemberTasksResponse> response = new ArrayList<>();



        for(Task task: tasks){
            ViewTeamMemberTasksResponse res = new ViewTeamMemberTasksResponse();

            res.setTaskId(task.getUuid());
            res.setCreatedAt(task.getCreatedAt() != null ? returnFormattedDate(task.getCreatedAt()) : null);
            res.setDueDate(task.getDueDate() != null ? returnFormattedDate(task.getDueDate()) : null);
            res.setTitle(task.getTitle());
            res.setDescription(task.getDescription());
            res.setStatus(task.getStatus());

            response.add(res);
        }

        return new AppResponse(response);
    }

    private User getLoggedInUser(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Optional<User> managerEntity = userRepository.findByEmail(email);

        if(managerEntity.isEmpty())
            throw new GlobalException("Manager not found");

        return managerEntity.get();
    }

    public AppResponse getPersonalTasks(GetPersonalTasksRequest request){
        User user = getLoggedInUser();

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Task> tasks = taskRepository.findTasksByUserUuid(user.getUuid(), pageable);

        List<ViewTeamMemberTasksResponse> response = new ArrayList<>();


        for(Task task: tasks){
            ViewTeamMemberTasksResponse res = new ViewTeamMemberTasksResponse();

            res.setTaskId(task.getUuid());
            res.setCreatedAt(task.getCreatedAt() != null ? returnFormattedDate(task.getCreatedAt()) : null);
            res.setDueDate(task.getDueDate() != null ? returnFormattedDate(task.getDueDate()) : null);
            res.setTitle(task.getTitle());
            res.setDescription(task.getDescription());
            res.setStatus(task.getStatus());

            response.add(res);
        }

        return new AppResponse(response);
    }

    public AppResponse getSpecificTask(GetSpecificTaskRequest request){

        User user = getLoggedInUser();

        Optional<Task> taskEntity = taskRepository.findByUuidAndAssignee_Id(request.getTaskId(), user.getId());

        if(taskEntity.isEmpty())
            throw new GlobalException("Task not found");

        Task task = taskEntity.get();

        ViewTeamMemberTasksResponse response = new ViewTeamMemberTasksResponse();
        response.setTaskId(task.getUuid());
        response.setCreatedAt(task.getCreatedAt() != null ? returnFormattedDate(task.getCreatedAt()) : null);
        response.setDueDate(task.getDueDate() != null ? returnFormattedDate(task.getDueDate()) : null);
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());

        return new AppResponse(response);
    }

    public AppResponse updateTaskStatus(UpdateTaskStatusRequest request){
        User user = getLoggedInUser();

        Optional<Task> taskEntity = taskRepository.findByUuidAndAssignee_Id(request.getTaskId(), user.getId());

        if(taskEntity.isEmpty())
            throw new GlobalException("Task not found");

        Task task = taskEntity.get();

        task.setStatus(request.getStatus());

        taskRepository.save(task);

        UpdateTaskStatusResponse response = new UpdateTaskStatusResponse();

        response.setTaskId(task.getUuid());
        response.setStatus(task.getStatus());

        return new AppResponse(response);
    }

    public AppResponse getAllTasks(GetAllTasksRequest request){

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Task> allTasks = taskRepository.findAll(pageable);

        List<ViewAllTasksResponse> response = new ArrayList<>();


        for(Task task: allTasks){
            ViewAllTasksResponse res = new ViewAllTasksResponse();

            res.setTaskId(task.getUuid());
            res.setAssigneeId(task.getAssignee().getUuid());
            res.setCreatedAt(task.getCreatedAt() != null ? returnFormattedDate(task.getCreatedAt()) : null);
            res.setDueDate(task.getDueDate() != null ? returnFormattedDate(task.getDueDate()) : null);
            res.setTitle(task.getTitle());
            res.setDescription(task.getDescription());
            res.setStatus(task.getStatus());

            response.add(res);
        }

        return new AppResponse(response);
    }
}
