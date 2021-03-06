package com.jojoldu.book.springboot.security.presentation;

import com.jojoldu.book.springboot.entities.ProductEntity;
import com.jojoldu.book.springboot.entities.ProductRepository;
import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.domain.UserRepository;
import com.jojoldu.book.springboot.service.SpringJpaService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Controller
public class MainController {

    private JdbcTemplate jdbc;
    private UserEntity user;
    @Autowired
    private SpringJpaService springJpaService;
    private String id;
    private String pw;


    public String getUserId(){
        return id;
    }

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


    @RequestMapping(value = "/categoryResult", method = RequestMethod.POST)
    @ResponseBody
    public String makePostEcho(@RequestBody String data, Model model) throws SQLException {


        System.out.println("id : "+id);
        UserEntity user = new UserEntity(id);

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
        Character onlinegameValue = user.getOnlinegame();
        Character offlinegameValue = user.getOfflinegame();
        Character publicationValue = user.getPublication();
        Character concertValue = user.getConcert();
        Character techValue = user.getTech();
        Character eduValue = user.getEdu();
        Character socialValue = user.getSocial();
        Character donateValue = user.getDonate();
        Character sportsValue = user.getSports();
        Character travelValue = user.getTravel();
        Character hobbyValue = user.getHobby();
        Character designValue = user.getDesign();
        Character homelivingValue = user.getHomeliving();
        Character petValue = user.getPet();
        Character beautyValue = user.getBeauty();
        Character festivalValue = user.getFestival();
        Character webtoonValue = user.getWebtoon();
        Character photoValue = user.getPhoto();
        Character movieValue = user.getMovie();
        Character musicValue = user.getMusic();
        Character artValue = user.getArt();
        Character foodValue = user.getFood();
        Character fashionValue = user.getFashion();

        for(int i = 0; i < categoryVector.size(); i++){
            String categoryValue = categoryVector.elementAt(i);
            switch (categoryValue) {
                case "온라인게임": onlinegameValue = 'Y';break;
                case "오프라인게임": offlinegameValue = 'Y';break;
                case "출판": publicationValue = 'Y';break;
                case "공연": concertValue = 'Y';break;
                case "테크": techValue = 'Y';break;
                case "교육·키즈": eduValue = 'Y';break;
                case "소셜·캠페인": socialValue = 'Y';break;
                case "기부·후원": donateValue = 'Y';break;
                case "스포츠·모빌리티": sportsValue = 'Y';break;
                case "여행·레저": travelValue = 'Y';break;
                case "취미": hobbyValue = 'Y';break;
                case "디자인": designValue = 'Y';break;
                case "홈리빙": homelivingValue = 'Y';break;
                case "반려동물": petValue = 'Y';break;
                case "뷰티": beautyValue = 'Y';break;
                case "페스티벌": festivalValue = 'Y';break;
                case "만화": webtoonValue = 'Y';break;
                case "사진": photoValue = 'Y';break;
                case "음악": musicValue = 'Y';break;
                case "영화·비디오": movieValue = 'Y';break;
                case "예술": artValue = 'Y';break;
                case "푸드": foodValue = 'Y';break;
                case "패션": fashionValue = 'Y';break;
            }
            System.out.println(categoryVector.elementAt(i));
        }
        //product에서 적합한 프로젝트들 가져와서 json으로 다시 만들기...
        //userdb에 카테고리 categoryVector 내용 옮기기

        Connection conn;
        conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC", "root", "0000");
        java.sql.Statement stmt = conn.createStatement();
        System.out.println("db 연결 성공");

        //springJpaService.updateUser(id, categoryVector);


        System.out.println("user:"+user);
        StringBuilder sb1 = new StringBuilder();

        /*sb.append("update test.user set onlinegame = ?, offlinegame = ?," +
                "publication = ?, tech =?, edu = ? , social =?, donate = ? , sports = ?, travel = ?, hobby =?, design = ?" +
                ", homeliving =?, pet = ?, beauty =?, festival = ?, webtoon =?, photo = ?, movie =? , music = ?, art = ?" +
                ", food = ?, fashion = ?, concert = ? where name = ?;");



        jdbc.update(sb.toString(),onlinegameValue,offlinegameValue,publicationValue,techValue,eduValue,socialValue,donateValue,
                    sportsValue,travelValue,hobbyValue,homelivingValue,petValue,beautyValue,festivalValue,webtoonValue,photoValue,movieValue,musicValue,artValue,
                    foodValue,fashionValue,concertValue,id);
*/
        String sql1 = sb1.append("update test.user set"+
                " onlinegame = '")
                .append(onlinegameValue)
                .append("', offlinegame = '")
                .append(offlinegameValue)
                .append("', publication = '")
                .append(publicationValue)
                .append("', tech = '")
                .append(techValue)
                .append("', edu = '")
                .append(eduValue)
                .append("', social = '")
                .append(socialValue)
                .append("', donate = '")
                .append(donateValue)
                .append("', sports = '")
                .append(sportsValue)
                .append("', travel = '")
                .append(travelValue)
                .append("', hobby = '")
                .append(hobbyValue)
                .append("', design = '")
                .append(designValue)
                .append("', homeliving = '")
                .append(homelivingValue)
                .append("', pet = '")
                .append(petValue)
                .append("', beauty = '")
                .append(beautyValue)
                .append("', festival = '")
                .append(festivalValue)
                .append("', webtoon = '")
                .append(webtoonValue)
                .append("', photo = '")
                .append(photoValue)
                .append("', movie = '")
                .append(movieValue)
                .append("', music = '")
                .append(musicValue)
                .append("', art = '")
                .append(artValue)
                .append("', food = '")
                .append(foodValue)
                .append("', fashion = '")
                .append(fashionValue)
                .append("', concert = '")
                .append(concertValue)
                .append("' where name = '")
                .append(id)
                .append("';").toString();
        System.out.println("sql : "+sql1);
        try {
            stmt.executeUpdate(sql1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //출처: https://sime.tistory.com/83 [심이 블로그]
        // userdb 내용 읽어오기 0
        // userdb 내용 수정하기 0
        //userdb에 카테고리 categoryVector 내용 옮기기 0


        //crawldb 내용 읽어오기
        //categoryVector에 들어 있는 카테고리의 프로젝트 출력하기
        //crawldb achieve순대로 10개 추려오기

        // categoryVector에 따른 project 데이터들 json 형태로 가져오기
        // model에 데이터 붙인 뒤 categoryResult에 뿌리기기

        //categoryVector 초기화
        //return data + "hello";
        //
        //categoryVector.clear();
        return "redirect:/categorySelect";

    }
    @RequestMapping(value="/loading", method = RequestMethod.GET)
    public String loading(Model model){
        return "loading2";
    }

    @RequestMapping(value="/categoryResult", method = RequestMethod.GET,produces = "text/html; charset=UTF8")
    public String getCategoryResult(Model model) throws SQLException {

        Connection conn;
        try{
            Thread.sleep(5000);
        }catch(InterruptedException e){}

        conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC", "root", "0000");
        java.sql.Statement stmt = conn.createStatement();
        System.out.println("db 연결 성공");

        System.out.println("getCategory");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User)principal;


        UserEntity user = new UserEntity(((User) principal).getUsername());
        StringBuilder sb1 = new StringBuilder();

        String sql1 = sb1.append("select * from test.user where name = '")
                .append(user.getName())
                .append("';").toString();
        try{
            ResultSet rs = stmt.executeQuery(sql1);
            while(rs.next()){
                user.setPassword(((User) principal).getPassword());
                user.setPassword(rs.getString("role"));
                user.setOfflinegame(rs.getString("offlinegame"));
                user.setOnlinegame(rs.getString("onlinegame"));
                user.setPublication(rs.getString("publication"));
                user.setConcert(rs.getString("concert"));
                user.setTech(rs.getString("tech"));
                user.setEdu(rs.getString("edu"));
                user.setSocial(rs.getString("social"));
                user.setDonate(rs.getString("donate"));
                user.setSports(rs.getString("sports"));
                user.setTravel(rs.getString("travel"));
                user.setHobby(rs.getString("hobby"));
                user.setDesign(rs.getString("design"));
                user.setHomeliving(rs.getString("homeliving"));
                user.setPet(rs.getString("pet"));
                user.setBeauty(rs.getString("beauty"));
                user.setFestival(rs.getString("festival"));
                user.setWebtoon(rs.getString("webtoon"));
                user.setPhoto(rs.getString("photo"));
                user.setMovie(rs.getString("movie"));
                user.setMusic(rs.getString("music"));
                user.setArt(rs.getString("art"));
                user.setFood(rs.getString("food"));
                user.setFashion(rs.getString("fashion"));

            }
        }catch (SQLException e){
            System.out.println("유저 정보 가져오기 실패");
            e.printStackTrace();
        }

        JSONArray sendjson = new JSONArray();
        Vector<String> category = new Vector<>();



        if(user.getOnlinegame()=='Y') category.addElement("온라인게임");
        if(user.getOfflinegame()=='Y') category.addElement("오프라인게임");
        if(user.getPublication()=='Y') category.addElement("출판");
        if(user.getConcert()=='Y') category.addElement("공연");
        if(user.getTech()=='Y') category.addElement("테크");
        if(user.getEdu()=='Y') category.addElement("교육·키즈");
        if(user.getSocial()=='Y') category.addElement("소셜·캠페인");
        if(user.getDonate()=='Y') category.addElement("기부·후원");
        if(user.getSports()=='Y') category.addElement("스포츠·모빌리티");
        if(user.getTravel()=='Y') category.addElement("여행·레저");
        if(user.getHobby()=='Y') category.addElement("취미");
        if(user.getDesign()=='Y') category.addElement("디자인");
        if(user.getHomeliving()=='Y') category.addElement("홈리빙");
        if(user.getPet()=='Y') category.addElement("반려동물");
        if(user.getBeauty()=='Y') category.addElement("뷰티");
        if(user.getFestival()=='Y') category.addElement("페스티벌");
        if(user.getWebtoon()=='Y') category.addElement("만화");
        if(user.getPhoto()=='Y') category.addElement("사진");
        if(user.getMovie()=='Y') category.addElement("영화·비디오");
        if(user.getMusic()=='Y') category.addElement("음악");
        if(user.getArt()=='Y') category.addElement("예술");
        if(user.getFood()=='Y') category.addElement("푸드");
        if(user.getFashion()=='Y') category.addElement("패션");



        int categoryNum = category.size();
        System.out.println("categoryNum : "+ categoryNum);
        for(int i = 0 ; i<categoryNum; i++){
            StringBuilder sb = new StringBuilder();

            String sql = sb.append("select distinct id, pagename, title, url from product where product.category ='")
                    .append(category.get(i))
                    .append("' order by product.achieve desc limit 10;").toString();
            System.out.println("sql : "+sql);
            try{
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    JSONObject subjson = new JSONObject();
                    System.out.println("title : "+rs.getString("title"));
                    subjson.put("category", category.get(i));
                    subjson.put("title", rs.getString("title"));
                    subjson.put("pagename", rs.getString("pagename"));
                    subjson.put("id", rs.getString("id"));
                    subjson.put("url", rs.getString("url"));
                    sendjson.add(subjson);
                    //System.out.println("sendtohtml : " + sendtohtml);
                }
            }catch(SQLException e){
                System.out.println("html에 보낼 json 넣는데 오류");
                e.printStackTrace();
            }
            //for(int j = 0 ; j<list.indexOf(i);j++)

        }

        for(int i=0; i<sendjson.size();i++){
            System.out.println("sendjson : "+sendjson.get(i).toString());
        }

        model.addAttribute("categories", sendjson);

        return "categoryResult";
    }


}