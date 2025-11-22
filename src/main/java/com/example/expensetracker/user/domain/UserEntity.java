package com.example.expensetracker.user.domain;

import com.example.expensetracker.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction(value = "deleted is null")
@Entity
@Table(name = "users", schema = "expense_tracker")
public class UserEntity extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence")
    private Long id;

    @Column(name = "mobile", length = 11, nullable = false)
    private String mobile;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name = "verified", columnDefinition = "boolean default false")
    private boolean verified;

    public static UserEntity createNewUser(String mobile, String encodedPassword, String name) {
        return UserEntity.builder()
                .mobile(mobile)
                .password(encodedPassword)
                .name(name)
                .verified(false) //user will be verified after OTP
                .build();
    }


    public void verified() {
        this.verified = true;
        this.updated = LocalDateTime.now();
    }
}
