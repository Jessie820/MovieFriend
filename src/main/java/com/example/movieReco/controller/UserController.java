package com.example.movieReco.controller;

import com.example.movieReco.constant.Method;
import com.example.movieReco.domain.Member;
import com.example.movieReco.error.DuplicateException;
import com.example.movieReco.error.NoResultException;
import com.example.movieReco.mapper.MemberDetail;
import com.example.movieReco.service.UserService;
import com.example.movieReco.utils.UiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController extends UiUtils {
    private final UserService userService;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @GetMapping(value = "/join")
    public String signUpForm(Model model){
        MemberDetail memberDetial = new MemberDetail();
        memberDetial.setBirthDate(LocalDate.now());
        model.addAttribute("memberDetail", memberDetial);
        return "join";
    }

    @GetMapping(value = "/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping(value = "/join")
    public String signUp(MemberDetail memberDetail, Model model, BindingResult result){
        try {
            Member member = new Member();
            member.setBirthDate(memberDetail.getBirthDate());
            member.setGender(memberDetail.getGender());
            member.setEmail(memberDetail.getUsername());
            member.setPassword(memberDetail.getPassword());
            member.setAuth("USER");
            member.setHeart(100L);
            userService.joinUser(member);
        }catch(Exception e){
            MemberDetail memberDetial = new MemberDetail();
            memberDetial.setBirthDate(LocalDate.now());
            model.addAttribute("memberDetail", memberDetial);
            return showMessageWithRedirect("??????????????? ??????????????????. ?????? ??????????????????.", "/signup", Method.GET, null, model);
        }
        return showMessageWithRedirect("??????????????? ??????????????????. ????????? ???????????? ???????????????.", "/login", Method.GET, null, model);
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

    @GetMapping(value = "/myInfo")
    public String myInfo(Model model, Authentication authentication){
        MemberDetail curMemberDetail = (MemberDetail) authentication.getPrincipal();
        model.addAttribute("memberDetail", curMemberDetail);
        return "myInfo";
    }

    @PostMapping(value = "/myInfo")
    public String updateMyInfo(MemberDetail newDetail, RedirectAttributes redirectAttributes, Authentication authentication){
        try {
            MemberDetail curMemberDetail = (MemberDetail) authentication.getPrincipal();
            Member member = userService.find(curMemberDetail.getMemberId());
            member.setBirthDate(newDetail.getBirthDate());
            member.setGender(newDetail.getGender());
            userService.saveUser(member);

            Authentication newAuth = new UsernamePasswordAuthenticationToken(new MemberDetail(member), curMemberDetail.getPassword(), curMemberDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            redirectAttributes.addFlashAttribute("memberDetail", new MemberDetail(member));
            redirectAttributes.addFlashAttribute("message", "??????????????? ??????????????????.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        }catch(Exception e){
            redirectAttributes.addFlashAttribute("message", "??????????????? ??????????????????.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/myInfo";
    }

    @GetMapping(value = "/resetPwd")
    public String resetPwd(Model model){
        model.addAttribute("memberDetail", new MemberDetail());
        return "resetPwd";
    }

    @GetMapping(value = "/resetPwdByEmailForm")
    public String resetPwdByEmailForm(){
        return "resetPwdByEmail";
    }

    @PostMapping(value = "/resetPwd")
    public String resetPwdForm(MemberDetail memberDetail, Authentication authentication, Model model){
        try {
            MemberDetail curMemberDetail = (MemberDetail) authentication.getPrincipal();
            Member member = userService.find(curMemberDetail.getMemberId());
            member.setPassword(memberDetail.getPassword());
            //userService.joinUser(member);
        }catch(Exception e){
            model.addAttribute("memberDetail", new MemberDetail());
            return showMessageWithRedirect("???????????? ???????????? ?????????????????????.", "/resetPwd", Method.GET, null, model);
        }
        return showMessageWithRedirect("???????????? ???????????? ??????????????????. ?????? ?????????????????????.", "/login", Method.GET, null, model);
    }

    @GetMapping(value = "/resetPwdByEmail")
    public String resetPwdByEmail(Model model, @RequestParam(value="query") String email){
        String subject = "???????????? ?????????";
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
                body.append("??????????????? ").append(member.getEmail()).append("???");
                body.append("<br/>???????????? ???????????? ?????? ???????????? ????????? ?????? ????????? ???????????????.");
                body.append("<br/>???????????? ?????? ??????????????? ").append(tmpPassword).append("?????????.");
                body.append("<br/>????????? ????????? ????????? ??????????????? ??????????????? ?????????.");

                mimeMessageHelper.setText(body.toString(), true);
                javaMailSender.send(mimeMessage);
            }catch(MessagingException e){

            }
        }else{
            model.addAttribute("msg", "???????????? ?????? ??????????????????.");
            return "resetPwdByEmail :: #resultDiv";
        }
        model.addAttribute("msg", "???????????? ?????? ??????????????? ?????????????????????.");
        return "resetPwdByEmail :: #resultDiv";
    }

    @GetMapping(value = "/findFriend")
    public String view(Model model) throws Exception {
        return "popup/findFriend";
    }
    @GetMapping("/findEmail")
    public String findEmail(Model model, @RequestParam(value="query") String email){
        if(!StringUtils.hasText(email)){
            model.addAttribute("message", "????????? ???????????? ??????????????????.");
            model.addAttribute("alertClass", "alert-danger");
            return "popup/findFriend :: #resultDiv";
        }

        Optional<Member> opMember = userService.findByEmail(email);
        if(!opMember.isPresent()){
            model.addAttribute("message", "????????? ?????? ????????? ????????????.");
            model.addAttribute("alertClass", "alert-danger");
        }else{
            model.addAttribute("message", "???????????? ???????????????.");
            model.addAttribute("alertClass", "alert-success");
        }
        return "popup/findFriend :: #resultDiv";
    }
}
