package com.example.movieReco.controller;

import com.example.movieReco.domain.Member;
import com.example.movieReco.error.DuplicateException;
import com.example.movieReco.mapper.MemberDetail;
import com.example.movieReco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @GetMapping(value = "/signup")
    public String signUpForm(Model model){
        model.addAttribute("memberDetail", new MemberDetail());
        return "signup";
    }

    @GetMapping(value = "/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping(value = "/signup")
    public String signUp(MemberDetail memberDetail, BindingResult result){
        Member member = new Member();
        member.setBirthDate(memberDetail.getBirthDate());
        member.setGender(memberDetail.getGender());
        member.setEmail(memberDetail.getUsername());
        member.setPassword(memberDetail.getPassword());
        member.setAuth("USER");
        member.setHeart(100L);
        userService.joinUser(member);
        return "redirect:/login";
    }

    @GetMapping(value = "/")
    public String userAccess(Model model, Authentication authentication){
        System.out.println("user access");
        MemberDetail memberDetail = (MemberDetail)authentication.getPrincipal();
        model.addAttribute("info", memberDetail.getUsername());
        return "movieHome";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping(value = "/resetPwdByEmailForm")
    public String resetPwdByEmailForm(){
        return "resetPwdByEmail";
    }

    @GetMapping(value = "/resetPwdByEmail")
    public String resetPwdByEmail(Model model, @RequestParam(value="query") String email){
        String subject = "비밀번호 재설정";
        String to = email;

        Optional<Member> opMember = userService.findByEmail(to);
        if(opMember.isPresent()){
            Member member = opMember.get();
            String tmpPassword = userService.getTempPassword();
            member.setPassword(tmpPassword);
            userService.joinUser(member);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            try{
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setFrom(from);
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(subject);

                StringBuilder body = new StringBuilder();
                body.append("안녕하세요 ").append(member.getEmail()).append("님");
                body.append("\n귀하께서 요청하신 임시 비밀번호 수신을 위해 발송된 메일입니다.");
                body.append("\n고객님의 임시 비밀번호는 ").append(tmpPassword).append("입니다.");
                body.append("로그인 후에는 새로운 비밀번호로 변경하셔야 합니다.");

                mimeMessageHelper.setText(body.toString(), true);
                javaMailSender.send(mimeMessage);
            }catch(MessagingException e){

            }
        }else{
            model.addAttribute("msg", "존재하지 않는 이메일입니다.");
            return "resetPwdByEmail :: #resultDiv";
        }
        model.addAttribute("msg", "이메일로 임시 비밀번호가 발송되었습니다.");
        return "resetPwdByEmail :: #resultDiv";
    }
}
