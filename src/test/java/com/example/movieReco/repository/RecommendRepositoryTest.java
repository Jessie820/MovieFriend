package com.example.movieReco.repository;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Movie;
import com.example.movieReco.domain.Recommendation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(false)
class RecommendRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    private RecommendRepository recommendRepository;

    @Test
    void save(){
        Member member = new Member();
        member.setBirthDate("19900620");
        member.setGender("FEMALE");
        member.setEmail("FEMALE@gmail.com");
        member.setPassword("1234");
        member.setAuth("USER");
        memberRepository.save(member);

        Movie movie = new Movie();
        movie.setId("123456");
        movie.setTitle("행복한 영화");
        movie.setDirector("냥냥이");
        movieRepository.save(movie);

        Recommendation reco1 = new Recommendation();
        reco1.setMember(member);
        reco1.setMovie(movie);
        reco1.setComment("너가 좋아할 만한 영화야");
        reco1.setRecipientName("집사");
        recommendRepository.save(reco1);

        List<String> myComments = recommendRepository.findCommentsByMember(member);

        for(String c : myComments){
            assertEquals("너가 좋아할 만한 영화야", c);
        }
    }
}