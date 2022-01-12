package com.example.movieReco.repository;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.mapper.RecoSaved;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendRepository {

    private final EntityManager em;

    public Long save(Recommendation recommendation) {
        em.persist(recommendation);
        em.flush();
        return recommendation.getId();
    }

    public List<String> findCommentsByMember(Member member){
        TypedQuery<String> query1
                =  em.createQuery("select r.comment from Recommendation r " +
                "join r.member m where m.id = :memberId", String.class);
        query1.setParameter("memberId",1L);
        return query1.getResultList();
    }

    public List<Recommendation> findRecommendationsByMember(Member member){
        TypedQuery<Recommendation> query1
                =  em.createQuery("select r from Recommendation r " +
                "join fetch r.member m where m.id = :memberId", Recommendation.class);
        query1.setParameter("memberId",1L);
        return query1.getResultList();
    }

    public Recommendation find(Long id){
        return em.find(Recommendation.class, id);
    }

    public Recommendation findRecommendation(Long id){
        TypedQuery<Recommendation> query1
                =  em.createQuery("select r from Recommendation r" +
                " join fetch r.movie m" +
                " join fetch r.member me" +
                " where r.id = :rId"
                , Recommendation.class);
        query1.setParameter("rId",id);
        return query1.getSingleResult();
    }

}
