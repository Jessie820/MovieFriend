package com.example.movieReco.service;

import com.example.movieReco.domain.Member;
import com.example.movieReco.mapper.MemberDetail;
import com.example.movieReco.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public void joinUser(Member member){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    public String checkEmail(String email){
        // 기존에 가입되어 있는 고객인지 확인
        Optional<Member> searchMember = memberRepository.findByEmail(email);
        if(searchMember.isPresent()){
            return "emailDuplicate";
        }else{
            return "success";
        }
    }

    public Member find(Long id){
        return memberRepository.find(id);
    }

    /**
     * Spring Security 필수 메소드 구현
     *
     * @param email 이메일
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     */
    @Override // 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 UserInfo로 반환 타입 지정 (자동으로 다운 캐스팅됨)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
       System.out.println("email=" + email);
        Optional<Member> member = memberRepository.findByEmail(email);
       // List<GrantedAuthority> authorities = new ArrayList<>();
       // authorities.add(new SimpleGrantedAuthority("USER"));
       // return new User(member.getEmail(), member.getPassword(), authorities);
        return new MemberDetail(member.get());
    }
}