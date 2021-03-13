package com.jojoldu.book.springboot.entities;

public class Product {
    private String id;
    private String title;
    private String pagename;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public Product(){

    }
    public Product(String title, String pagename, String url) {

        this.title = title;
        this.pagename = pagename;
        this.url = url;
    }
}