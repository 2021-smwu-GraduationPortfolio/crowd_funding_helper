package com.jojoldu.book.springboot.security.presentation;

import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signUp")
    public String singUp(){
        UserEntity user = UserEntity.builder()
                .name("username")
                .password(passwordEncoder.encode("1234"))
                .role("user")
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
