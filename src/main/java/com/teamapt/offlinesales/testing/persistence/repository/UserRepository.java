package com.teamapt.offlinesales.testing.persistence.repository;

import com.teamapt.offlinesales.testing.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
}
