package com.example.movieReco.repository;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Recommendation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(false)
class RecommendJpaRepositoryTest {

    @Autowired RecommendJpaRepository recommendJpaRepository;

    @Test
    void findByRecipientEmail() {
        Member member = new Member();
        member.setId(2l);

        PageRequest pageRequest = PageRequest.of(0,3);
        Page<Recommendation> page = recommendJpaRepository.findByRecipientEmail("냥냥이", pageRequest);
        List<Recommendation> content = page.getContent();

        assertEquals(content.size(), 3);
    }

    @Test
    void findAllByMember() {
        Member member = new Member();
        member.setId(2l);

        PageRequest pageRequest = PageRequest.of(0,5);
        Page<Recommendation> page = recommendJpaRepository.findAllByMember(member, pageRequest);
        List<Recommendation> content = page.getContent();

        assertEquals(content.size(), 4);
    }

    @Test
    void findByIdLessThanAndMemberOrderByIdDesc() {
        Member member = new Member();
        member.setId(2l);

        PageRequest pageRequest = PageRequest.of(0,3);
        Page<Recommendation> page = recommendJpaRepository.findByIdLessThanAndMemberOrderByIdDesc(2l,member, pageRequest);
        List<Recommendation> content = page.getContent();

        for(Recommendation r : content){
            System.out.println(r.getMovie().getTitle());
        }

        //assertEquals(content.size(), 4);
    }
}