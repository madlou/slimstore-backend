package com.tjx.lew00305.slimstore.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tjx.lew00305.slimstore.dto.TjxComSearchDTO;
import com.tjx.lew00305.slimstore.model.FormElement;

@Service
public class ProductService {

    @Value("${tjx.online.search}")
    private String onlineSearchUrl;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public FormElement[] onlineSearch(String query) {
        TjxComSearchDTO search = restTemplate.getForObject(onlineSearchUrl + query, TjxComSearchDTO.class);
        int productCount = search.response.docs.length;
        FormElement[] products = new FormElement[productCount];
        for(int i=0; i< productCount; i++) {
            TjxComSearchDTO.Response.Doc doc = search.response.docs[i];
            products[i] = new FormElement("image", doc.pid, doc.title, doc.thumbImage, doc.price, null);
        }
        return products;
    }

}
