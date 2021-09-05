package com.example.movieReco.service;

import com.example.movieReco.controller.MovieRecommendForm;
import com.example.movieReco.domain.Movie;
import com.example.movieReco.mapper.NaverMovie;
import com.example.movieReco.mapper.NaverMovieItem;
import com.example.movieReco.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public String saveMovie(Movie movie){
        movieRepository.save(movie);
        return movie.getMovieId();
    }


}
