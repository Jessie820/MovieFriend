package com.example.movieReco.service;

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

    public String saveMovie(NaverMovieItem nm){
        //주문 생성
        Movie movie = Movie.createMovie(nm);
        //주문 저장
        movieRepository.save(movie);
        return movie.getMovieId();
    }
}
