package com.teamapt.offlinesales.testing.domain.model;

import com.teamapt.offlinesales.testing.persistence.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class UserModel {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private User.Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
