package com.example.movieReco.repository;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendJpaRepository extends JpaRepository<Recommendation, Long> {

    Page<Recommendation> findAllByMember(Member member, Pageable pageable);

    Page<Recommendation> findByIdLessThanAndMemberOrderByIdDesc(Long lastRecommendationId, Member member, Pageable pageable);

    Page<Recommendation> findByRecipientEmail(String email, Pageable pageable);

    //Slice<Recommendation> findAllByMember(Member member);

}
