package com.jojoldu.book.springboot.controllers;

import com.jojoldu.book.springboot.entities.Product;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jojoldu.book.springboot.models.ProductModel;
import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        ProductModel productModel = new ProductModel();
        //modelMap.put("products", productModel.findAll());
        List<Product> products = productModel.findAll();
        int length = products.size();
        //System.out.println(products.get(0).getId());

        JSONArray sendjson = null;
        sendjson = new JSONArray();
        JSONObject subjson = new JSONObject();


        int variableNum =0;
        //List<fundingList> sendList = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            subjson = new JSONObject();
            subjson.put("id", products.get(i).getId());
            subjson.put("name", products.get(i).getName());
            subjson.put("price", products.get(i).getPrice());
            sendjson.add(subjson);
        }
        System.out.println(sendjson);
        model.addAttribute("products",sendjson);
        return "product";
    }

}
