package com.tjx.lew00305.slimstore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.entity.Product;
import com.tjx.lew00305.slimstore.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository associateRepository;

    public void add(Product associate){
        if(associate == null){ return; }
        associateRepository.save(associate);
    }

    public Iterable<Product> all(){
        return associateRepository.findAll();
    }

    public Product get(int id){
        Optional<Product> optional = associateRepository.findById(id);
        return optional.get();
    }

    public void delete(int id){
        associateRepository.deleteById(id);
    }

}
