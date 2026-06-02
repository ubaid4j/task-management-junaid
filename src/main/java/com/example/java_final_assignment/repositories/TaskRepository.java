package com.example.java_final_assignment.repositories;

import com.example.java_final_assignment.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUuid(UUID uuid);

    @Query("""
        SELECT t
        FROM Task t
        WHERE t.assignee.id IN :userIds
    """)
    Page<Task> findTasksByUserIds(@Param("userIds") List<Long> userIds, Pageable pageable);


    @Query("""
        SELECT t
        FROM Task t
        WHERE t.assignee.uuid = :userUuid
    """)
    Page<Task> findTasksByUserUuid(@Param("userUuid") UUID userUuid, Pageable pageable);

    Optional<Task> findByUuidAndAssignee_Id(UUID taskId, Long userId);
}
