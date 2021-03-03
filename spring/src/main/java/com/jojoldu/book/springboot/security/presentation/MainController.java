package com.jojoldu.book.springboot.security.presentation;

import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signUp")
    public String singUp(){

        return "signUp";
        /*UserEntity user = UserEntity.builder()
                .name("username")
                .password(passwordEncoder.encode("1234"))
                .role("user")
                .build();
        userRepository.save(user);
        return "redirect:/login";
         */
    }

    @PostMapping("/signUp")
    public String signUp(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        String id = request.getParameter("email");
        String pw = request.getParameter("pw");

        System.out.println(id);
        System.out.println(pw);
        UserEntity user = UserEntity.builder()
                .name(id)
                .password(passwordEncoder.encode(pw))
                .role("supporter")
                .build();
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/index2")
    public String getIndex(){
        return "index2";
    }

    @GetMapping("/main")
    public String getMain(){
        return "main";
    }

    @GetMapping("/login")
    public String getLoginForm(){
        return "loginPage";
    }
}
