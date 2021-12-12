package com.example.movieReco.service;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.repository.MemberRepository;
import com.example.movieReco.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;

    public Long save(Member member, Recommendation recommendation){
        consumeCredit(member, recommendation.getUserCredit());
        Long id = recommendRepository.save(recommendation);
        return id;
    }

    private void consumeCredit(Member member, long credit){
        long mCredit = member.getCredit();
        if(mCredit - credit < 0){
            throw new RuntimeException("There is not enough credits left");
        }
        member.setCredit(mCredit-credit);
        memberRepository.save(member);
    }

    public Recommendation find(Long id){
        return recommendRepository.find(id);
    }

    public Recommendation findRecommendation(Long id){
        return recommendRepository.findRecommendation(id);
    }


}
