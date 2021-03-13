package com.jojoldu.book.springboot.entities;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "picklist")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemEntity {
    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmail(String email){
        this.email=email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String email;
    private String pagename;
    private String url;

    @Builder
    public ItemEntity(String title, String pagename, String url, String email){
        this.title = title;
        this.pagename = pagename;
        this.url = url;
        this.email = email;
    }

}
