package com.example.java_final_assignment.service;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.CreateTaskRequest;
import com.example.java_final_assignment.controllers.requests.UpdateTaskRequest;
import com.example.java_final_assignment.exceptions.GlobalException;
import com.example.java_final_assignment.model.*;
import com.example.java_final_assignment.repositories.TaskRepository;
import com.example.java_final_assignment.repositories.TeamMembersRepository;
import com.example.java_final_assignment.repositories.TeamRepository;
import com.example.java_final_assignment.repositories.UserRepository;
import com.example.java_final_assignment.service.response.CreateTaskResponse;
import com.example.java_final_assignment.service.response.UpdateTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;

    public AppResponse createTask(CreateTaskRequest request){
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
}
