package com.jojoldu.book.springboot.security.presentation;

import com.jojoldu.book.springboot.entities.ProductEntity;
import com.jojoldu.book.springboot.entities.ProductRepository;
import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.domain.UserRepository;
import com.jojoldu.book.springboot.service.SpringJpaService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

@Controller
public class MainController {

    private UserEntity user;
    @Autowired
    private SpringJpaService springJpaService;
    private String id;
    private String pw;

    @Autowired
    private ProductRepository productRepository;


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


    @RequestMapping(value = "/categoryResult", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void makePostEcho(@RequestBody String data) {
        ModelMap model = new ModelMap();

        System.out.println("Post category result");

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


    }

    @RequestMapping(value="/categoryResult", method = RequestMethod.GET,produces = "text/html; charset=UTF8")
    public String getCategoryResult(Model model){

        try{
            Thread.sleep(1050);
        }catch(InterruptedException e){}

        System.out.println("getCategory");
        Vector<String> category = springJpaService.getCategory(id);
        JSONArray sendjson = new JSONArray();


        ArrayList<String[]> list = new ArrayList<String[]>();
        int categoryNum = category.size();
        System.out.println("categoryNum : "+ categoryNum);
        for(int i = 0 ; i<categoryNum; i++){
            JSONObject subjson = new JSONObject();
            subjson.put("category", category.get(i));
            list.add(productRepository.findTitleTop10(category.get(i)));
            System.out.println("test : "+category.get(i));
            System.out.println("length : "+list.get(i).length);
            for(int j=0; j<list.get(i).length; j++) System.out.println("list  : "+list.get(i)[j]);
            //for(int j = 0 ; j<list.indexOf(i);j++)
            sendjson.add(subjson);
        }


        model.addAttribute("categories", sendjson);

        return "categoryResult";
    }


}
