package com.jojoldu.book.springboot.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jojoldu.book.springboot.entities.Item;
import com.jojoldu.book.springboot.models.ProductModel;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String cartindex() {
        return "cart";
    }

    @RequestMapping(value = "buy/{id}", method = RequestMethod.GET)
    public String buy(@PathVariable("id") String id, HttpSession session1, HttpSession session2) {
        ProductModel productModel = new ProductModel();
        if (session1.getAttribute("cart") == null) {
            System.out.println("session nope");
            List<Item> cart = new ArrayList<Item>();
            cart.add(new Item(productModel.find(id), 1));
            session1.setAttribute("cart", cart);

            int length = cart.size();

            JSONArray sendjson = null;
            sendjson = new JSONArray();
            JSONObject subjson = new JSONObject();

            for(int i = 0 ; i<length; i++){
                subjson = new JSONObject();
                subjson.put("Id", cart.get(i).getProduct().getId());
                subjson.put("Name", cart.get(i).getProduct().getName());
                subjson.put("Price", cart.get(i).getProduct().getPrice());
                sendjson.add(subjson);
            }
            System.out.println(sendjson);
            session2.setAttribute("Cart", sendjson);

        } else {
            List<Item> cart = (List<Item>) session1.getAttribute("cart");
            System.out.println("session ok");
            int index = this.exists(id, cart);
            if (index == -1) {
                cart.add(new Item(productModel.find(id), 1));
                System.out.println("index : -1");
            } else {
                int quantity = cart.get(index).getQuantity() + 1;
                cart.get(index).setQuantity(quantity);
                System.out.println("index != -1");
            }
            System.out.println(cart.get(0).getProduct().getId());

            int length = cart.size();

            JSONArray sendjson = null;
            sendjson = new JSONArray();
            JSONObject subjson = new JSONObject();

            for(int i = 0 ; i<length; i++){
                subjson = new JSONObject();
                subjson.put("Id", cart.get(i).getProduct().getId());
                subjson.put("Name", cart.get(i).getProduct().getName());
                subjson.put("Price", cart.get(i).getProduct().getPrice());
                sendjson.add(subjson);
            }
            System.out.println(sendjson);
            session2.setAttribute("Cart", sendjson);
            session1.setAttribute("cart", cart);
        }
        return "redirect:/cart/index";
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
            subjson.put("Id", cart.get(i).getProduct().getId());
            subjson.put("Name", cart.get(i).getProduct().getName());
            subjson.put("Price", cart.get(i).getProduct().getPrice());
            subjson.put("quantity", cart.get(i).getQuantity());
            sendjson.add(subjson);
        }
        session2.setAttribute("Cart", sendjson);
        session1.setAttribute("cart", cart);
        return "redirect:/cart/index";
    }

    private int exists(String id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProduct().getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }

}