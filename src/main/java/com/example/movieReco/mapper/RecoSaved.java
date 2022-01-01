package com.example.movieReco.mapper;

import com.example.movieReco.domain.Member;
import com.example.movieReco.domain.Movie;
import com.example.movieReco.domain.Recommendation;
import lombok.Data;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class RecoSaved {
    private MovieDto movie;
    private String comment;
    private MemberDto recommender; // 추천한사람
    private Long recommendId; //영화추천내역 아이디
    private Long recipientHeart;//추천받은 사람이 주는 점수

    public RecoSaved(){

    }

    public RecoSaved(Recommendation recommendation){
        movie = new MovieDto(recommendation.getMovie());
        comment = recommendation.getComment();
        recommender = new MemberDto(recommendation.getMember());
        recommendId = recommendation.getId();
    }

    @Data
    static class MemberDto{
        private String email;
        private String birthDate;
        private String gender;

        public MemberDto(Member member){
            email = getMaskedEmail(member.getEmail());//개인정보 보호를 위해 마스킹된 이메일 주소 넣기
            birthDate = member.getBirthDate();
            gender = member.getGender();
        }
    }

    @Data
    static class MovieDto{
        private String title;
        private String director;
        private String imageLink;

        public MovieDto(Movie movie){
            title = movie.getTitle();
            director = movie.getDirector();
            imageLink = movie.getImageLink();
        }
    }

    /**
     * 이메일 주소 마스킹 처리
     * @param email
     * @return maskedEmailAddress
     */
    private static String getMaskedEmail(String email) {
        /*
         * 요구되는 메일 포맷
         * {userId}@domain.com
         * */
        String regex = "\\b(\\S+)+@(\\S+.\\S+)";
        Matcher matcher = Pattern.compile(regex).matcher(email);
        if (matcher.find()) {
            String id = matcher.group(1); // 마스킹 처리할 부분인 userId
            /*
             * userId의 길이를 기준으로 세글자 초과인 경우 뒤 세자리를 마스킹 처리하고,
             * 세글자인 경우 뒤 두글자만 마스킹,
             * 세글자 미만인 경우 모두 마스킹 처리
             */
            int length = id.length();
            if (length < 4) {
                char[] c = new char[length];
                Arrays.fill(c, '*');
                return email.replace(id, String.valueOf(c));
            } else if (length == 3) {
                return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
            } else {
                return email.replaceAll("\\b(\\S+)[^@][^@][^@][^@]+@(\\S+)", "$1****@$2");
            }
        }
        return email;
    }

}
