package com.jojoldu.book.springboot.security.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    public String getName() {
        return name;
    }

    private String name;
    private String password;

    private String role;


    @Builder
    public UserEntity(String name, String password, String role){
        this.name = name;
        this.password = password;
        this.role = role;

    }

}
