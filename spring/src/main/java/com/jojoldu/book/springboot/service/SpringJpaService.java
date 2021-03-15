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
