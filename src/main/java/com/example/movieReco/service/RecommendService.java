package com.example.movieReco.service;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.mapper.RecoSaved;
import com.example.movieReco.repository.MemberRepository;
import com.example.movieReco.repository.RecommendJpaRepository;
import com.example.movieReco.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final RecommendJpaRepository recommendJpaRepository;
    private final MemberRepository memberRepository;

    public Long save(Member member, Recommendation recommendation){
        Member findMember = memberRepository.find(member.getId());
        consumeHeart(findMember, recommendation.getUserHeart());
        Long id = recommendRepository.save(recommendation);
        return id;
    }

    //추천받은 영화에 하트주기
    public void scoreReco(RecoSaved recoSaved){
        Recommendation recommendation = recommendRepository.find(recoSaved.getRecommendId());
        recommendation.setRecipientHeart(recoSaved.getRecipientHeart());

        Member member = recommendation.getMember();
        long userHeart = recommendation.getUserHeart();
        long recipientHeart = recommendation.getRecipientHeart();
        long oriHeart = member.getHeart();

        if(userHeart <= recipientHeart){
            long rewardHeart = (recipientHeart - userHeart)*2;
            member.setHeart(oriHeart+userHeart+rewardHeart);
            recommendation.setRewardHeart(rewardHeart);
        }else{
            member.setHeart(oriHeart+10L);
            recommendation.setRewardHeart(10);
        }
        recommendRepository.save(recommendation);
    }

    private void consumeHeart(Member member, long heart){
        long mHeart = member.getHeart();
        if(mHeart - heart < 0){
            throw new RuntimeException("Not enough hearts left");
        }
        member.setHeart(mHeart-heart);
    }

    public Recommendation find(Long id){
        return recommendRepository.find(id);
    }

    public Recommendation findRecommendation(Long id){
        return recommendRepository.findRecommendation(id);
    }

//    public List<Recommendation> findMyRecommendations(int no, Member member){
//        PageRequest pageRequest = PageRequest.of(no,5);
//        Page<Recommendation> page= recommendJpaRepository.findAllByMember(member, pageRequest);
//
//        List<Recommendation> recommendations = page.getContent();
//        return recommendations;
//    }

    public List<Recommendation> findMyRecommendations(Member member){
        return recommendRepository.findRecommendationsByMember(member);
    }

    public List<Recommendation> findRecommendationsForMember(Member member){
        return recommendRepository.findRecommendationsForMember(member);
    }
}
