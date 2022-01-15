package com.example.movieReco.repository;

import com.example.movieReco.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MovieRepository {
    private final EntityManager em;

    public void save(Movie movie) {
        em.persist(movie);
    }

    public Movie find(String id) {
        return em.find(Movie.class, id);
    }
}
