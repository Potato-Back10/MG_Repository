package com.gamza.study.entity;

import com.gamza.study.entity.enums.Gender;
import com.gamza.study.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login_id")
    private String loginId;

    private String password;

    private String username;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date birthDate;

    private boolean verify;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
}
