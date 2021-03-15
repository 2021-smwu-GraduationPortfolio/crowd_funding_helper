



package com.jojoldu.book.springboot.security.domain;

        import lombok.AccessLevel;
        import lombok.Builder;
        import lombok.Getter;
        import lombok.NoArgsConstructor;

        import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    private String name;
    private String password;
    public String getName() {
        return name;
    }

    private String role;


    private char onlinegame='N';
    private char offlinegame='N';
    private char publication='N';
    private char concert='N';
    private char tech='N';
    private char edu='N';
    private char social='N';
    private char donate='N';
    private char sports='N';
    private char travel='N';
    private char hobby='N';
    private char design='N';
    private char homeliving='N';
    private char pet='N';
    private char beauty='N';

    public void setOnlinegame(char onlinegame) {
        this.onlinegame = onlinegame;
    }

    public void setOfflinegame(char offlinegame) {
        this.offlinegame = offlinegame;
    }

    public void setPublication(char publication) {
        this.publication = publication;
    }

    public void setConcert(char concert) {
        this.concert = concert;
    }

    public void setTech(char tech) {
        this.tech = tech;
    }

    public void setEdu(char edu) {
        this.edu = edu;
    }

    public void setSocial(char social) {
        this.social = social;
    }

    public void setDonate(char donate) {
        this.donate = donate;
    }

    public void setSports(char sports) {
        this.sports = sports;
    }

    public void setTravel(char travel) {
        this.travel = travel;
    }

    public void setHobby(char hobby) {
        this.hobby = hobby;
    }

    public void setDesign(char design) {
        this.design = design;
    }

    public void setHomeliving(char homeliving) {
        this.homeliving = homeliving;
    }

    public void setPet(char pet) {
        this.pet = pet;
    }

    public void setBeauty(char beauty) {
        this.beauty = beauty;
    }

    public void setFestival(char festival) {
        this.festival = festival;
    }

    public void setWebtoon(char webtoon) {
        this.webtoon = webtoon;
    }

    public void setPhoto(char photo) {
        this.photo = photo;
    }

    public void setMovie(char movie) {
        this.movie = movie;
    }

    public void setMusic(char music) {
        this.music = music;
    }

    public void setArt(char art) {
        this.art = art;
    }

    public void setFood(char food) {
        this.food = food;
    }

    public void setFashion(char fashion) {
        this.fashion = fashion;
    }

    private char festival='N';
    private char webtoon='N';
    private char photo='N';
    private char movie='N';
    private char music='N';
    private char art='N';
    private char food='N';
    private char fashion='N';
/*
    @Builder
    public UserEntity(String name, String password, String role){
        this.name = name;
        this.password = password;
        this.role = role;

    }
*/
    @Builder
    public UserEntity(String name, String password, String role,
                      Character onlinegame, Character offlinegame, Character publication, Character concert,
                      Character tech, Character edu, Character social, Character donate, Character sports,
                      Character travel, Character hobby, Character design, Character homeliving, Character pet,
                      Character beauty, Character festival, Character webtoon, Character photo, Character movie,
                      Character music, Character art, Character food, Character fashion){
        this.name = name;
        this.password = password;
        this.role = role;

        this.onlinegame = onlinegame;
        this.offlinegame = offlinegame;
        this.publication = publication;
        this.concert = concert;
        this.tech = tech;
        this.edu = edu;
        this.social = social;
        this.donate = donate;
        this.sports = sports;
        this.travel = travel;
        this.hobby = hobby;
        this.design = design;
        this.homeliving = homeliving;
        this.pet = pet;
        this.beauty = beauty;
        this.festival = festival;
        this.webtoon = webtoon;
        this.photo = photo;
        this.movie = movie;
        this.music = music;
        this.art = art;
        this.food = food;
        this.fashion = fashion;


    }

}
