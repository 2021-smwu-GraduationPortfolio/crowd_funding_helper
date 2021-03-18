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

        if(user.getOnlinegame()=='Y') category.add("onlinegame");
        if(user.getOfflinegame()=='Y') category.add("offlinegame");
        if(user.getPublication()=='Y') category.add("publication");
        if(user.getConcert()=='Y') category.add("concert");
        if(user.getTech()=='Y') category.add("tech");
        if(user.getEdu()=='Y') category.add("edu");
        if(user.getSocial()=='Y') category.add("social");
        if(user.getDonate()=='Y') category.add("donate");
        if(user.getSports()=='Y') category.add("sports");
        if(user.getTravel()=='Y') category.add("travel");
        if(user.getHobby()=='Y') category.add("hobby");
        if(user.getDesign()=='Y') category.add("design");
        if(user.getHomeliving()=='Y') category.add("homeliving");
        if(user.getPet()=='Y') category.add("pet");
        if(user.getBeauty()=='Y') category.add("beauty");
        if(user.getFestival()=='Y') category.add("festival");
        if(user.getWebtoon()=='Y') category.add("webtoon");
        if(user.getPhoto()=='Y') category.add("photo");
        if(user.getMovie()=='Y') category.add("movie");
        if(user.getMusic()=='Y') category.add("music");
        if(user.getArt()=='Y') category.add("art");
        if(user.getFood()=='Y') category.add("food");
        if(user.getFashion()=='Y') category.add("fashion");

        return category;
    }

    public void updateUser(String id, Vector<String> categoryVector) {
        UserEntity user = getUserOne(id);
        System.out.println("update user : "+user.getName());
        for(int i = 0; i < categoryVector.size(); i++){
            String categoryValue = categoryVector.elementAt(i);
            switch (categoryValue) {
                case "onlinegame": user.setOnlinegame('Y');break;
                case "offlinegame": user.setOfflinegame('Y');break;
                case "publication": user.setPublication('Y');break;
                case "show": user.setConcert('Y');break;
                case "tech": user.setTech('Y');break;
                case "edu": user.setEdu('Y');break;
                case "social": user.setSocial('Y');break;
                case "donate": user.setDonate('Y');break;
                case "sports": user.setSports('Y');break;
                case "travel": user.setTravel('Y');break;
                case "hobby": user.setHobby('Y');break;
                case "design": user.setDesign('Y');break;
                case "homeliving": user.setHomeliving('Y');break;
                case "pet": user.setPet('Y');break;
                case "beauty": user.setBeauty('Y');break;
                case "festival": user.setFestival('Y');break;
                case "webtoon": user.setWebtoon('Y');break;
                case "photo": user.setPhoto('Y');break;
                case "music": user.setMusic('Y');break;
                case "art": user.setArt('Y');break;
                case "food": user.setFood('Y');break;
                case "fashion": user.setFashion('Y');break;

            }
            System.out.println(categoryVector.elementAt(i));
        }
        userRepository.save(user);
    }
}
