package com.example.movieReco.controller;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Movie;
import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.error.EmptyInputException;
import com.example.movieReco.mapper.MemberDetail;
import com.example.movieReco.mapper.NaverMovieItem;
import com.example.movieReco.mapper.RecoDetail;
import com.example.movieReco.mapper.RecoSaved;
import com.example.movieReco.repository.RecommendRepository;
import com.example.movieReco.service.MovieService;
import com.example.movieReco.service.NaverMovieService;
//import lombok.extern.slf4j.Slf4j;
import com.example.movieReco.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;
    private final RecommendService recommendService;
    private final RecommendRepository recommendRepository;

    @GetMapping(value = "/movies")
    public String home() {
        return "movieHome";
    }

    @GetMapping("/movies/search")
    public String get(Model model, @RequestParam(value="query") String keyword){
       if(!StringUtils.hasText(keyword)){
           throw new EmptyInputException("데이터를 입력해주세요.");
       }

       log.info("Naver Movie Search");
       NaverMovieItem[] movieItems = NaverMovieService.findByKeyword(keyword).getItems();
       model.addAttribute("movieItems", movieItems);
        //return moviesService.findByKeyword(keyword);
        return "movieHome :: #list";
    }

    @PostMapping(value = "/movies/recoNew")
    public String createRecoForm(RecoDetail recoDetail, Model model) {
        log.info("movie title" + recoDetail.getTitle());
        log.info("movie director" + recoDetail.getDirector());
        log.info("movie movieId" + recoDetail.getMovieId());
        log.info("movie imageLink" + recoDetail.getImageLink());

        MovieRecommendForm movieRecommendForm = new MovieRecommendForm(recoDetail);
        model.addAttribute("movieRecommendForm", movieRecommendForm);

        return "recommendMovieForm";
    }

    //예전에 짠 거지같았던 파라미터 넘기기 방법
//    @GetMapping(value = "/movies/recoNew")
//    public String createForm(Model model, @RequestParam("movieTitle") String movieTitle
//                                        ,@RequestParam("movieDirector") String movieDirector
//                                        ,@RequestParam("userRating") float userRating
//                                        ,@RequestParam("movieId") String movieId
//                                        ,@RequestParam("movieImage") String movieImage
//                            ) {
//        log.info("movieRecommendForm");
//        log.info("movie title" + movieTitle);
//        log.info("movie director" + movieDirector);
//
//        MovieRecommendForm movieRecommendForm = new MovieRecommendForm();
//        movieRecommendForm.setTitle(movieTitle);
//        movieRecommendForm.setDirector(movieDirector);
//        movieRecommendForm.setUserRating(userRating);
//        movieRecommendForm.setMovieId(movieId);
//        movieRecommendForm.setImageLink(movieImage);
//        model.addAttribute("movieRecommendForm", movieRecommendForm);
//        return "recommendMovieForm";
//    }

    @PostMapping(value = "movies/recommendMovie")
    public String recommendMovie(@ModelAttribute("movieRecommendForm") MovieRecommendForm movieRecommendForm
                                    ,Authentication authentication){
        Movie movie = Movie.createMovie(movieRecommendForm);
        //Movie 가 이미 저장되어 있으면 저장하지 않는 로직을 추가해야겠다...
        movieService.saveMovie(movie);

        //현재 로그인 된 사용자 정보 가져오기
        MemberDetail memberDetail = (MemberDetail)authentication.getPrincipal();
        Member member = Member.createMember(memberDetail);
        Long recoId = saveRecommendation(movieRecommendForm, movie, member);
        String recommendView = "redirect:/recommendList/";

        return (recommendView + recoId);
    }

    @GetMapping("/recommendList/{recoId}")
    public String recommendList(Model model, @PathVariable Long recoId){
        Recommendation recommendation = recommendService.findRecommendation(recoId);
        RecoSaved recoSaved = new RecoSaved(recommendation);
        model.addAttribute("recoSaved", recoSaved);
        return "recommendList";
    }

    public Long saveRecommendation(MovieRecommendForm mrf, Movie movie, Member member){
        Recommendation recommendation = new Recommendation();
        recommendation.setMember(member);
        recommendation.setRecipientName(mrf.getRecipientName());
        recommendation.setComment(mrf.getComment());
        recommendation.setMovie(movie);
        Long id = recommendService.save(member, recommendation);
        return id;
    }

}
