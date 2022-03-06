package com.example.movieReco.controller;

import com.example.movieReco.error.DuplicateException;
import com.example.movieReco.error.EmptyInputException;
import com.example.movieReco.error.NoResultException;
import com.example.movieReco.mapper.NaverMovieItem;
import com.example.movieReco.mapper.RecoDetail;
import com.example.movieReco.service.NaverMovieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
public class MovieRestController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    @GetMapping(value = "/movies/searchMore")
    public ResponseEntity searchMoreMovie(@RequestParam(value="title") String title
                                                 ,@RequestParam(value="genre") String genre
                                                 ,@RequestParam(value="start") String start){

        if(!StringUtils.hasText(title)){
            throw new EmptyInputException("데이터를 입력해주세요.");
        }

        log.info("Naver Movie Search more");
        NaverMovieItem[] movieItems = NaverMovieService.findByKeyword(title, genre, start).getItems();
        if(movieItems.length == 0){
            throw new NoResultException("더 이상 영화 검색결과가 없습니다.");
        }
        return ResponseEntity.ok().body(movieItems);
    }

    //영화 추천하는 페이지 호출
    @GetMapping(value = "/movies/redirect")
    public ResponseEntity redirectTo() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
