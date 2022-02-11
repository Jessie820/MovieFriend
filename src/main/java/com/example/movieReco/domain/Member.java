package com.example.movieReco.domain;

import com.example.movieReco.mapper.MemberDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    private LocalDate birthDate;
    private String gender;

    private long heart;

    @Column(nullable = false)
    private String password;
    private String auth;

    public static Member createMember(MemberDetail memberDetail){
        Member member = new Member();
        member.setId(memberDetail.getMemberId());
        member.setEmail(memberDetail.getEmail());
        member.setBirthDate(memberDetail.getBirthDate());
        member.setGender(memberDetail.getGender());
        member.setPassword(memberDetail.getPassword());
        member.setAuth(memberDetail.getAuth());
        return member;
    }
}
