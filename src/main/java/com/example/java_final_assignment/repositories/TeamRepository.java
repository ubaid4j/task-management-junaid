package com.example.java_final_assignment.repositories;

import com.example.java_final_assignment.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t WHERE t.manager.uuid = :uuid")
    Optional<Team> findTeamByManagerUuid(@Param("uuid") UUID uuid);

    Optional<Team> findByUuid(UUID uuid);
}
