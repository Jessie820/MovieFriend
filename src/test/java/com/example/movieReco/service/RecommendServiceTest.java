package com.example.movieReco.service;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Recommendation;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RecommendServiceTest {

    static private Member member;
    static private Recommendation recommendation_pass;
    static private Recommendation recommendation_fail;

    @Autowired
    UserService userService;
    @Autowired
    RecommendService recommendService;

    @BeforeAll
    static void beforeAll(@Autowired UserService userService){
        member = new Member();
        //필수값 설정
        member.setEmail("test@test.com");
        member.setPassword("1212");
        member.setHeart(100);
        userService.joinUser(member);
        recommendation_pass = new Recommendation();
        recommendation_fail = new Recommendation();
    }

    @Test
    @DisplayName("영화 추천 하트 배분 성공")
    void saveRecommendation_pass() throws Exception{
        recommendation_pass.setUserHeart(100);
        Long recommendId = recommendService.save(member, recommendation_pass);

        //db에서 다시 조회한 내역을 비교
        Member aftMember = userService.find(member.getId());
        Recommendation aftRecommendation = recommendService.find(recommendId);
        
        assertEquals(0L, aftMember.getHeart());
        assertEquals(100, aftRecommendation.getUserHeart());
    }

    @Test
    @DisplayName("영화 추천 하트 배분 실패")
    void saveRecommendation_error() throws Exception{
        recommendation_fail.setUserHeart(101);
        RuntimeException re = assertThrows(RuntimeException.class, () -> recommendService.save(member, recommendation_fail));

        assertThat(re.getMessage()).isEqualTo("Not enough hearts left");
    }
}