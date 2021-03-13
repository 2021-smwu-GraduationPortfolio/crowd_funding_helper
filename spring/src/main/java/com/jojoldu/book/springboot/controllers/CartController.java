package com.jojoldu.book.springboot.controllers;

import java.security.Principal;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    ItemService itemService;
    private String email;



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
    public String buy(@RequestBody String data, HttpSession session1, HttpSession session2) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        this.email = user.getUsername();

        System.out.println("data : " +data);
        Vector<String> idVector = new Vector<String>();
        int strNum = data.length();
        System.out.println("strNum : "+strNum);
        String completeWord = "";
        for(int i = 0 ; i< strNum; i++){
            if(data.charAt(i) == ' ' || i == strNum-1){
                idVector.addElement(completeWord);
                completeWord="";
                continue;
            }
            completeWord = completeWord+data.charAt(i);
            if(i==strNum-2){
                completeWord=completeWord+data.charAt(strNum-1);
            }
        }
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
                    email);
            System.out.println("title: "+ tempItem.getProduct().getTitle());
            System.out.println("pagename: "+ tempItem.getProduct().getPagename());
            System.out.println("url: "+ tempItem.getProduct().getUrl());
            System.out.println("email: "+ email);
            System.out.println("ItemEntity:"+tempItemEntity);

            itemService.save(tempItemEntity);
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
                    email);
            itemService.save(tempItemEntity);
            session1.setAttribute("cart", cart);
            int length = cart.size();

            System.out.println("length : "+length);



            JSONObject subjson = new JSONObject();

            subjson.put("id", cart.get(k).getProduct().getId());
            subjson.put("title", cart.get(k).getProduct().getTitle());
            subjson.put("pagename", cart.get(k).getProduct().getPagename());
            subjson.put("url", cart.get(k).getProduct().getUrl());
            sendjson.add(subjson);
            System.out.println("sendjson : "+sendjson);

            session1.setAttribute("cart", cart);
        }
        }
        System.out.println("Final sendjson : "+sendjson);
        session2.setAttribute("Cart", sendjson);
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