package com.example.movieReco.service;

import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecommendService {

    private RecommendRepository recommendRepository;

    public void save(Recommendation recommendation){
        recommendRepository.save(recommendation);
    }




}
