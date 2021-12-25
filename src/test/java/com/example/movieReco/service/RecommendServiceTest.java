package com.example.movieReco.service;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.repository.MemberRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(false)
class RecommendServiceTest {
    private Member member;
    private Recommendation recommendation;

    @Autowired
    RecommendService recommendService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach(){
        member = new Member();
        //필수값 설정
        member.setEmail("test@test.com");
        member.setPassword("1212");
        recommendation = new Recommendation();

    }

    @Test
    void saveRecommendation_pass() throws Exception{
        member.setCredit(100);
        Long memberId = memberRepository.save(member);

        recommendation.setUserCredit(100);
        Long recommendId = recommendService.save(member, recommendation);

        Member aftMember = memberRepository.find(memberId);
        Recommendation aftRecommendation = recommendService.find(recommendId);

        //db에서 각각 조회한 내용을 비교
        assertEquals(0L, aftMember.getCredit());
        assertEquals(100, aftRecommendation.getUserCredit());
    }

    @Test
    void saveRecommendation_error() throws Exception{
        member.setCredit(100);
        Long memberId = memberRepository.save(member);

        recommendation.setUserCredit(101);
        RuntimeException re = assertThrows(RuntimeException.class, () -> recommendService.save(member, recommendation));

        assertThat(re.getMessage()).isEqualTo("There is not enough credits left");
    }
}