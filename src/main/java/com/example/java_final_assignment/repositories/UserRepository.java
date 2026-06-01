package com.example.java_final_assignment.repositories;

import com.example.java_final_assignment.model.RoleEnum;
import com.example.java_final_assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUuidAndRole(UUID uuid, RoleEnum role);

    List<User> findByUuidIn(List<UUID> uuids);
}
