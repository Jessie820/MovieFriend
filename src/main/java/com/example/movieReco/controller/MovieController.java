package com.example.movieReco.controller;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Movie;
import com.example.movieReco.domain.Recommendation;
import com.example.movieReco.error.EmptyInputException;
import com.example.movieReco.error.NoResultException;
import com.example.movieReco.mapper.MemberDetail;
import com.example.movieReco.mapper.NaverMovieItem;
import com.example.movieReco.mapper.RecoDetail;
import com.example.movieReco.mapper.RecoSaved;
import com.example.movieReco.service.MovieService;
import com.example.movieReco.service.NaverMovieService;
//import lombok.extern.slf4j.Slf4j;
import com.example.movieReco.service.RecommendService;
import com.example.movieReco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;
    private final RecommendService recommendService;
    private final UserService userService;

    @GetMapping(value = "/movies")
    public String home() {
        return "movieHome";
    }

    @GetMapping("/movies/search")
    public String searchMovie(Model model, @RequestParam(value="query") String keyword){
       if(!StringUtils.hasText(keyword)){
           throw new EmptyInputException("데이터를 입력해주세요.");
       }

       log.info("Naver Movie Search");
       NaverMovieItem[] movieItems = NaverMovieService.findByKeyword(keyword).getItems();
       if(movieItems.length == 0){
           throw new NoResultException("영화 검색결과가 없습니다.");
       }
       model.addAttribute("movieItems", movieItems);
        //return moviesService.findByKeyword(keyword);
        return "movieHome :: #list";
    }

    //영화 추천하는 페이지 호출(Post로 넘기면서 redirect 안해도 되나..?)
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

    //추천받은 영화에 하트주기
    @PostMapping(value = "/movies/scoreReco")
    public String scoreReco(RecoSaved recoSaved, Model model) {
        log.info("recipient Heart" + recoSaved.getRecipientHeart());
        log.info("recommendId" + recoSaved.getRecommendId());
        recommendService.scoreReco(recoSaved);
        model.addAttribute("memberDetail", new MemberDetail());
        return "redirect:/signup";
    }

    /**
     * 영화 추천하기
     *  1) 추천한 영화정보 저장하기
     *  2) 추천내역 저장하기
     *  - 시용자 하트 차감 및 보상하트 추가
     * */
    @PostMapping(value = "movies/recommendMovie")
    public String recommendMovie(@ModelAttribute("movieRecommendForm") MovieRecommendForm movieRecommendForm
                                    ,Authentication authentication){
        Movie movie = Movie.createMovie(movieRecommendForm);
        movieService.saveMovie(movie);

        MemberDetail memberDetail = (MemberDetail) authentication.getPrincipal();
        Member member = Member.createMember(memberDetail);
        Long recoId = saveRecommendation(movieRecommendForm, movie, member);

        //오늘 추천한 내역 업데이트
        long updatedHeart = memberDetail.getHeart() - movieRecommendForm.getUserHeart();
        memberDetail.setHeart(updatedHeart);
        long updatedRecommendCnt = memberDetail.getTodayRecommendCnt()+1;
        memberDetail.setTodayRecommendCnt(updatedRecommendCnt);

        //로그인한 날짜가 오늘과 다르면 날짜 및 오늘 추천수 update
        if(memberDetail.getToday().compareTo(LocalDate.now())!=0){
            memberDetail.setToday(LocalDate.now());
            memberDetail.setTodayRecommendCnt(0L);
        }

        //오늘 추천수가 3이면 100하트를 추가로 주기
        if(updatedRecommendCnt == 3){
            memberDetail.setHeart(memberDetail.getHeart()+100);
        }
        getMemberCurInfo(memberDetail);

        String recommendView = "redirect:/recommendation/";
        return (recommendView + recoId);
    }

    public Long saveRecommendation(MovieRecommendForm mrf, Movie movie, Member member){
        Recommendation recommendation = new Recommendation();
        recommendation.setMember(member);
        recommendation.setRecipientName(mrf.getRecipientName());
        recommendation.setComment(mrf.getComment());
        recommendation.setMovie(movie);
        recommendation.setUserHeart(mrf.getUserHeart());
        Long id = recommendService.save(member, recommendation);
        return id;
    }

    //사용자 현재 정보 update
    @NotNull
    private void getMemberCurInfo(MemberDetail memberDetail) {
        Authentication newAuth = new UsernamePasswordAuthenticationToken(memberDetail, memberDetail.getPassword(), memberDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    //사용자가 추천한 내역 보여주기
    @GetMapping("/recommendation/{recoId}")
    public String recommendation(Model model, @PathVariable Long recoId){
        Recommendation recommendation = recommendService.findRecommendation(recoId);
        RecoSaved recoSaved = new RecoSaved(recommendation);
        model.addAttribute("recoSaved", recoSaved);
        return "recommendation";
    }
    
    //추천받은 내역 보기
    @GetMapping("/recommendation/{recoId}/appreciate")
    public String recommendAppreciate(Model model, @PathVariable Long recoId){
        Recommendation recommendation = recommendService.findRecommendation(recoId);
        RecoSaved recoSaved = new RecoSaved(recommendation);
        model.addAttribute("recoSaved", recoSaved);
        return "recommendAppreciate";
    }

    //내가 추천한 내역 보여주기
    @GetMapping("/myMovieList")
    public String getMovieList(Model model, Authentication authentication){
        //현재 로그인 된 사용자 정보 가져오기
//        MemberDetail memberDetail = (MemberDetail)authentication.getPrincipal();
//        Member member = Member.createMember(memberDetail);
//        getMemberCurInfo(memberDetail);
//        List<Recommendation> recommendItems = recommendService.findMyRecommendations(member);
//        model.addAttribute("recommendItems", recommendItems);
        return "myMovieList";
    }

    //내가 추천한 내역 보여주기
    @GetMapping("/myRecommendList")
    public String getMyRecommendList(Model model, Authentication authentication){
        //현재 로그인 된 사용자 정보 가져오기
        MemberDetail memberDetail = (MemberDetail)authentication.getPrincipal();
        Member member = Member.createMember(memberDetail);
        getMemberCurInfo(memberDetail);
        List<Recommendation> recommendItems = recommendService.findMyRecommendations(member);
        model.addAttribute("recommendItems", recommendItems);
        return "myRecommendList";
    }

    //내가 추천한 내역 보여주기
    @GetMapping("/recommendedList")
    public String getRecommendedList(Model model, Authentication authentication){

        return "recommendedList";
    }
}
