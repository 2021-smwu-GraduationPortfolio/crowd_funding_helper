package com.jojoldu.book.springboot.security.presentation;

import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Vector;

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
        return "redirect:/categorySelect";
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
        return "index";
    }

    @GetMapping("/categorySelect")
    public String getCategorySelect(){
        return "categorySelect";
    }

    @RequestMapping(value = "/categoryResult", method = RequestMethod.POST)
    @ResponseBody
    public String makePostEcho(@RequestBody String data, Model model) {
        Vector<String> categoryVector = new Vector<String>();
        int strNum = data.length();
        String completeWord = "";
        for(int i = 0; i < strNum; i++){
            if(data.charAt(i) == ',' || i == strNum-1){
                categoryVector.addElement(completeWord);
                completeWord = "";
                continue;
            }
            completeWord = completeWord + data.charAt(i);
            if(i == strNum-2){
                completeWord = completeWord + data.charAt(strNum-1);
            }
        }
        for(int i = 0; i < categoryVector.size(); i++){
            System.out.println(categoryVector.elementAt(i));
        }
        //product에서 적합한 프로젝트들 가져와서 json으로 다시 만들기...
        //userdb에 카테고리 categoryVector 내용 옮기기
        //categoryVector 초기화
        return data + "hello";
        //
    }

}
