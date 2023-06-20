package com.teamapt.offlinesales.testing.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private boolean deleted = Boolean.FALSE;
    @CreatedDate
    @Setter
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Setter
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void setDeleted(boolean deleted) {
        if (deleted) {
            deletedAt = LocalDateTime.now();
        }
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHERS
    }
}
