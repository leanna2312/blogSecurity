package com.example.bloggingsec;



//import com.example.bloggingsec.dto.SignupRequestDto;
//import com.example.bloggingsec.entity.User;
//import com.example.bloggingsec.entity.UserRoleEnum;
//import com.example.bloggingsec.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {

   private final PasswordEncoder passwordEncoder;
    private final MemberRepository userRepository;

    // ADMIN_TOKEN
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login-page")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @PostMapping("/signup")
    public String signup(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<Member> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        Member user = new Member(username, password, role);
        userRepository.save(user);

        return "redirect:/api/user/login-page";
    }

    @PostMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("*********************************************************");
        System.out.println("UserController.login");
        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
        System.out.println("*********************************************************");

        return "redirect:/api/user/login-page";
    }

//    @PostMapping("/forbidden")
//    public ModelAndView forbidden() {
//        return new ModelAndView("forbidden");
//    }
}