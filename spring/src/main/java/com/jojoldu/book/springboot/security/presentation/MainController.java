package com.jojoldu.book.springboot.security.presentation;

import com.jojoldu.book.springboot.entities.ProductEntity;
import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.domain.UserRepository;
import com.jojoldu.book.springboot.service.SpringJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

@Controller
public class MainController {

    private UserEntity user;
    @Autowired
    private SpringJpaService springJpaService;
    private String id;
    private String pw;


    public String getId(Principal principal) {
        return principal.getName();
    }

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
        id = request.getParameter("email");
        pw = request.getParameter("pw");
        char onlinegameValue = 'N';
        char offlinegameValue = 'N';
        char publicationValue = 'N';
        char showValue = 'N';
        char techValue = 'N';
        char eduValue = 'N';
        char socialValue = 'N';
        char donateValue = 'N';
        char sportsValue = 'N';
        char travelValue = 'N';
        char hobbyValue = 'N';
        char designValue = 'N';
        char homelivingValue = 'N';
        char petValue = 'N';
        char beautyValue = 'N';
        char festivalValue = 'N';
        char webtoonValue = 'N';
        char photoValue = 'N';
        char movieValue = 'N';
        char musicValue = 'N';
        char artValue = 'N';
        char foodValue = 'N';
        char fashionValue = 'N';

        System.out.println(id);
        System.out.println(pw);
        user = UserEntity.builder()
                .name(id)
                .password(passwordEncoder.encode(pw))
                .role("supporter")
                .onlinegame(onlinegameValue)
                .offlinegame(offlinegameValue)
                .publication(publicationValue)
                .concert(showValue)
                .tech(techValue)
                .edu(eduValue)
                .social(socialValue)
                .donate(donateValue)
                .sports(sportsValue)
                .travel(travelValue)
                .hobby(hobbyValue)
                .design(designValue)
                .homeliving(homelivingValue)
                .pet(petValue)
                .beauty(beautyValue)
                .festival(festivalValue)
                .webtoon(webtoonValue)
                .photo(photoValue)
                .movie(movieValue)
                .music(musicValue)
                .art(artValue)
                .food(foodValue)
                .fashion(fashionValue)
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
    @GetMapping("/categoryResult")
    public String getCategoryResult(){return "categoryResult";}

    @RequestMapping(value = "/categoryResult", method = RequestMethod.POST)
    @ResponseBody
    public String makePostEcho(@RequestBody String data, Model model) {

        UserEntity user;
        Vector<String> categoryVector = new Vector<String>();
        System.out.println("data: " + data);
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
/*
        Character onlinegameValue = 'N';
        Character offlinegameValue = 'N';
        Character publicationValue = 'N';
        Character concertValue = 'N';
        Character techValue = 'N';
        Character eduValue = 'N';
        Character socialValue = 'N';
        Character donateValue = 'N';
        Character sportsValue = 'N';
        Character travelValue = 'N';
        Character hobbyValue = 'N';
        Character designValue = 'N';
        Character homelivingValue = 'N';
        Character petValue = 'N';
        Character beautyValue = 'N';
        Character festivalValue = 'N';
        Character webtoonValue = 'N';
        Character photoValue = 'N';
        Character movieValue = 'N';
        Character musicValue = 'N';
        Character artValue = 'N';
        Character foodValue = 'N';
        Character fashionValue = 'N';

        for(int i = 0; i < categoryVector.size(); i++){
            String categoryValue = categoryVector.elementAt(i);
            switch (categoryValue) {
                case "onlinegame": onlinegameValue = 'Y';break;
                case "offlinegame": offlinegameValue = 'Y';break;
                case "publication": publicationValue = 'Y';break;
                case "show": concertValue = 'Y';break;
                case "tech": techValue = 'Y';break;
                case "edu": eduValue = 'Y';break;
                case "social": socialValue = 'Y';break;
                case "donate": donateValue = 'Y';break;
                case "sports": sportsValue = 'Y';break;
                case "travel": travelValue = 'Y';break;
                case "hobby": hobbyValue = 'Y';break;
                case "design": designValue = 'Y';break;
                case "homeliving": homelivingValue = 'Y';break;
                case "pet": petValue = 'Y';break;
                case "beauty": beautyValue = 'Y';break;
                case "festival": festivalValue = 'Y';break;
                case "webtoon": webtoonValue = 'Y';break;
                case "photo": photoValue = 'Y';break;
                case "music": musicValue = 'Y';break;
                case "art": artValue = 'Y';break;
                case "food": foodValue = 'Y';break;
                case "fashion": fashionValue = 'Y';break;

            }
            //System.out.println(categoryVector.elementAt(i));
        }
*/


        springJpaService.updateUser(id, categoryVector);


        // userdb 내용 읽어오기 0
        // userdb 내용 수정하기 0
        //userdb에 카테고리 categoryVector 내용 옮기기 0


        //crawldb 내용 읽어오기
        //categoryVector에 들어 있는 카테고리의 프로젝트 출력하기
        //crawldb achieve순대로 10개 추려오기

        // categoryVector에 따른 project 데이터들 json 형태로 가져오기
        // model에 데이터 붙인 뒤 categoryResult에 뿌리기기

        //categoryVector 초기화
        //categoryVector.clear();
        return "redirect:/categorySelect";
    }


}
