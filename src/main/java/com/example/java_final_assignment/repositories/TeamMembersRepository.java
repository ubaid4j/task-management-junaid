package com.example.java_final_assignment.repositories;

import com.example.java_final_assignment.controllers.requests.GetAssignedUsersResponse;
import com.example.java_final_assignment.model.TeamMember;
import com.example.java_final_assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TeamMembersRepository extends JpaRepository<TeamMember, Long> {

    @Query("SELECT tm.user.uuid FROM TeamMember tm WHERE tm.team.id = :teamId")
    List<UUID> findUserUuidsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tm.user FROM TeamMember tm WHERE tm.team.id = :teamId")
    List<User> findUsersByTeamId(@Param("teamId") Long teamId);

    @Query("""
        SELECT new com.example.java_final_assignment.controllers.requests.GetAssignedUsersResponse(
            tm.user.uuid,
            tm.user.username,
            tm.user.email,
            tm.user.status
        )
        FROM TeamMember tm
        WHERE tm.team.id = :teamId
    """)
    List<GetAssignedUsersResponse> findAssignedUsersByTeamId(
            @Param("teamId") Long teamId
    );
}
