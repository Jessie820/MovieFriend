package com.example.movieReco.mapper;

import com.example.movieReco.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class MemberDetail implements UserDetails {
    private Long memberId;
    private String email;
    private String password;
    private String auth;
    private String birthDate;
    private String gender;

    private Long heart;

    private LocalDate today;
    private Long todayRecommendCnt;

    public MemberDetail(){

    }

    public MemberDetail(Member member){
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.auth = "ROLE_" + member.getAuth();
        this.birthDate = member.getBirthDate();
        this.gender = member.getGender();
        this.heart = member.getHeart();
        this.today = LocalDate.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.auth));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
