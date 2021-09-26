package com.example.movieReco.service;

import com.example.movieReco.domain.Movie;
import com.example.movieReco.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public String saveMovie(Movie movie){
        movieRepository.save(movie);
        return movie.getId();
    }


}
