package com.example.movieReco.service;

import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;

    public Long save(Recommendation recommendation){
        Long id = recommendRepository.save(recommendation);
        return id;
    }

    public Recommendation find(Long id){
        return recommendRepository.find(id);
    }




}
