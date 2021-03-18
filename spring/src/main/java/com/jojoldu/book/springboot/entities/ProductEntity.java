package com.jojoldu.book.springboot.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String pagename;
    private String url;
    private String category;
    private int achieve;

    @Builder
    public ProductEntity(String title, String pagename, String url){
        this.title = title;
        this.pagename = pagename;
        this.url = url;
    }


}
