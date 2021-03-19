package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Vector;

@Service
public class SpringJpaService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity getUserOne(String id) {
        return userRepository.findByName(id)
                .orElseThrow(() -> new NoSuchElementException("없는 ID입니다."));
    }

    public Vector<String> getCategory(String id){
        UserEntity user = getUserOne(id);
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

        return category;
    }

    public void updateUser(String id, Vector<String> categoryVector) {
        UserEntity user = getUserOne(id);
        System.out.println("update user : "+user.getName());
        for(int i = 0; i < categoryVector.size(); i++){
            String categoryValue = categoryVector.elementAt(i);
            switch (categoryValue) {
                case "온라인게임" :  user.setOnlinegame('Y');break;
                case "오프라인게임": user.setOfflinegame('Y');break;
                case "출판": user.setPublication('Y');break;
                case "테크": user.setTech('Y');break;
                case "교육·키즈": user.setEdu('Y');break;
                case "소셜·캠페인": user.setSocial('Y');break;
                case "기부·후원": user.setDonate('Y');break;
                case "스포츠·모빌리티": user.setSports('Y');break;
                case "여행·레저": user.setTravel('Y');break;
                case "취미": user.setHobby('Y');break;
                case "디자인": user.setDesign('Y');break;
                case "홈리빙": user.setHomeliving('Y');break;
                case "반려동물": user.setPet('Y');break;
                case "뷰티": user.setBeauty('Y');break;
                case "페스티벌": user.setFestival('Y');break;
                case "만화": user.setWebtoon('Y');break;
                case "사진": user.setPhoto('Y');break;
                case "영화·비디오": user.setMovie('Y');break;
                case "음악": user.setMusic('Y');break;
                case "예술": user.setArt('Y');break;
                case "푸드": user.setFood('Y');break;
                case "패션": user.setFashion('Y');break;
                case "공연": user.setConcert('Y');break;
            }
            System.out.println(categoryVector.elementAt(i));
        }
        userRepository.save(user);
    }
}
