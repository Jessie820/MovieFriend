package com.example.movieReco.repository;


import com.example.movieReco.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public Optional<Member> findByEmail(String email) {
        Optional<Member>  member =  em.createQuery("select m from Member m where m.email = :email",
                Member.class)
                .setParameter("email", email)
               .getResultList().stream().findFirst();

       return member;
    }

    public Long getRecommendCnt(Long id, LocalDate localDate){
        TypedQuery<Long> cnt = em.createQuery("select count(r) from Recommendation r "+
                "join r.member m where m.id = :memberId and r.createdDate = :localDate",Long.class);
        cnt.setParameter("memberId",id).setParameter("localDate", localDate);
        return cnt.getSingleResult();
    }

}
