package com.jojoldu.book.springboot.controllers;

import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpSession;

import com.jojoldu.book.springboot.entities.ItemEntity;
import com.jojoldu.book.springboot.entities.ProductEntity;
import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.presentation.MainController;
import com.jojoldu.book.springboot.service.ItemService;
import com.jojoldu.book.springboot.service.ProductService;
import com.jojoldu.book.springboot.service.SpringJpaService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jojoldu.book.springboot.entities.Item;
import com.jojoldu.book.springboot.models.ProductModel;

@Controller
@RequestMapping(value = "/cart")
public class CartController {


    @Autowired
    ProductService productService;
    Connection conn;
    SpringJpaService springJpaService;
    private JdbcTemplate jdbc;

    @Autowired
    ItemService itemService;
    private String email;
    private String role;




    @RequestMapping(value = "/index")
    public String cartindex() {
        /*try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(InterruptedException e){
            System.err.format("IOException : %s%n", e);
        }*/
        //System.out.println("KAKAKAKA");
        return "cart";
    }


    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public String buy(@RequestBody String data, HttpSession session1, HttpSession session2) throws SQLException {

        conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC", "root", "0000");
        java.sql.Statement stmt = conn.createStatement();
        //System.out.println("db 연결 성공");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println("Buy user : "+user);
        this.email = user.getUsername();
        System.out.println("data : " +data);
        Vector<String> idVector = new Vector<String>();
        int strNum = data.length();
        System.out.println("strNum : "+strNum);
        String completeWord = "";
        for(int i = 0 ; i< strNum; i++){
            if(data.charAt(i) == ' '){
                idVector.addElement(completeWord);
                completeWord="";
                continue;
            }
            else if(data.charAt(i) == 'c') {
                this.role = "creator";
                continue;
            }
            else if(data.charAt(i) == 's') {
                this.role = "supporter";
                continue;
            }
            else if(data.charAt(i) == 'p') continue;
            completeWord = completeWord+data.charAt(i);
        }

        System.out.println("role : "+this.role);
        JSONArray sendjson = new JSONArray();
        for(int k = 0; k < idVector.size(); k++){
            String id = idVector.elementAt(k);
            System.out.println("id: "+id);
            id = id.trim();

            if (session1.getAttribute("cart") == null) {
                System.out.println("session nope");
                List<Item> cart = new ArrayList<Item>();
                Optional<ProductEntity> item = productService.findById(Long.parseLong(id));
                Item tempItem = new Item(item.orElse(new ProductEntity("no cart", ".",".")),1);
                cart.add(tempItem);
                ItemEntity tempItemEntity = new ItemEntity(
                        tempItem.getProduct().getTitle(),
                        tempItem.getProduct().getPagename(),
                        tempItem.getProduct().getUrl(),
                        email,
                        this.role);
                System.out.println("title: "+ tempItem.getProduct().getTitle());
                System.out.println("pagename: "+ tempItem.getProduct().getPagename());
                System.out.println("url: "+ tempItem.getProduct().getUrl());
                System.out.println("email: "+ email);
                System.out.println("role: "+ this.role);
                System.out.println("ItemEntity:"+tempItemEntity);

                itemService.save(tempItemEntity);
                System.out.println("저장함");
                session1.setAttribute("cart", cart);

                int length = cart.size();


                JSONObject subjson = new JSONObject();

                subjson.put("id", cart.get(k).getProduct().getId());
                subjson.put("title", cart.get(k).getProduct().getTitle());
                subjson.put("pagename", cart.get(k).getProduct().getPagename());
                subjson.put("url", cart.get(k).getProduct().getUrl());

                sendjson.add(subjson);

                System.out.println("sendjson : "+sendjson);


                session2.setAttribute("Cart", sendjson);

            } else {
                List<Item> cart = (List<Item>) session1.getAttribute("cart");
                System.out.println("session ok");
                int index = this.exists(id, cart);
                Optional<ProductEntity> item = productService.findById(Long.parseLong(id));
                Item tempItem = new Item(item.orElse(new ProductEntity("no cart?", ".",".")),1);
                if (index == -1) {
                    cart.add(tempItem);
                    System.out.println("index : -1");
                    //이전에 장바구니에 추가한 적 없음
                } else {
                    int quantity = cart.get(index).getQuantity() + 1;
                    cart.get(index).setQuantity(quantity);
                    System.out.println("index != -1");
                    //이전에 장바구니에 추가된 적 있는 상품이라 개수만 +1
                }
                //System.out.println(cart.get(0).getProduct().getId());

                ItemEntity tempItemEntity = new ItemEntity(
                        tempItem.getProduct().getTitle(),
                        tempItem.getProduct().getPagename(),
                        tempItem.getProduct().getUrl(),
                        email,
                        this.role);
                itemService.save(tempItemEntity);
                session1.setAttribute("cart", cart);
                int length = cart.size();

                //System.out.println("length : "+length);

                JSONObject subjson = new JSONObject();

                subjson.put("id", cart.get(k).getProduct().getId());
                subjson.put("title", cart.get(k).getProduct().getTitle());
                subjson.put("pagename", cart.get(k).getProduct().getPagename());
                subjson.put("url", cart.get(k).getProduct().getUrl());
                sendjson.add(subjson);
                System.out.println("sendjson : "+sendjson);

                session1.setAttribute("cart", cart);
            }
            System.out.println("title 하나 끝남");
        }

        // cart.mustache(찜 목록 보여주는 페이지)에 보낼 json 작성(해당 유저가 찜한 프로젝트들 다 가져오기)

        StringBuilder sb = new StringBuilder();
        String sql = sb.append("select distinct title, pagename, url from ")
                .append("test.picklist ")
                .append("where email = '")
                .append(email)
                .append("';").toString();

        JSONArray sendtohtml = new JSONArray();

        try {
            ResultSet rs = stmt.executeQuery(sql);

            System.out.print("title");
            System.out.print("\n");
            System.out.print("pagename");
            System.out.print("\n");
            System.out.print("url");
            System.out.print("\n");
            System.out.println("────────────────────────");

            while(rs.next()){
                JSONObject subtohtml = new JSONObject();
                //System.out.print(rs.getString("title"));
                subtohtml.put("title", rs.getString("title"));
                //System.out.print(rs.getString("pagename"));
                subtohtml.put("pagename", rs.getString("pagename"));
                //System.out.print(rs.getString("url"));
                subtohtml.put("url", rs.getString("url"));
                //System.out.println("subtohtml : " + subtohtml);
                sendtohtml.add(subtohtml);
                //System.out.println("sendtohtml : " + sendtohtml);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("html에 보낼 json 넣는데 오류");
            e.printStackTrace();
        }

        //System.out.println("Final sendjson : "+sendjson);
        session2.setAttribute("Cart", sendtohtml);
        System.out.println("json(sending to html) : "+sendtohtml);

        return "redirect:/cart/loading";

    }

    @RequestMapping(value="/loading")
    public String loading(){
        return "loading";
    }


    @RequestMapping(value = "remove/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable("id") String id, HttpSession session1, HttpSession session2) {
        ProductModel productModel = new ProductModel();
        List<Item> cart = (List<Item>) session1.getAttribute("cart");
        int index = this.exists(id, cart);
        cart.remove(index);
        int length = cart.size();

        JSONArray sendjson = null;
        sendjson = new JSONArray();
        JSONObject subjson = new JSONObject();

        for(int i = 0 ; i<length; i++){
            subjson = new JSONObject();
            subjson.put("id", cart.get(i).getProduct().getId());
            subjson.put("title", cart.get(i).getProduct().getTitle());
            subjson.put("pagename", cart.get(i).getProduct().getPagename());
            subjson.put("url", cart.get(i).getProduct().getUrl());
            sendjson.add(subjson);
        }
        session2.setAttribute("Cart", sendjson);
        session1.setAttribute("cart", cart);
        return "redirect:/cart/index";
    }

    private int exists(String id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProduct().getId()== Long.parseLong(id)) {
                return i;
            }
        }
        return -1;
    }

}