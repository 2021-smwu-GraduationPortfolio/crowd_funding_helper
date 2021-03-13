package com.jojoldu.book.springboot.controllers;

import com.jojoldu.book.springboot.entities.Product;
import com.jojoldu.book.springboot.entities.ProductEntity;
import com.jojoldu.book.springboot.service.ProductService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jojoldu.book.springboot.models.ProductModel;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {


        //modelMap.put("products", productModel.findAll());
        List<ProductEntity> products = productService.findAll();
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
            subjson.put("title", products.get(i).getTitle());
            subjson.put("pagename", products.get(i).getPagename());
            subjson.put("url", products.get(i).getUrl());
            sendjson.add(subjson);
        }
        //System.out.println(sendjson);
        model.addAttribute("products",sendjson);
        return "product";
    }

}
