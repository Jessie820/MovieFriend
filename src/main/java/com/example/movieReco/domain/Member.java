package com.example.movieReco.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    private String birthDate;
    private String gender;

    @Column(nullable = false)
    private String password;
    private String auth;

}
