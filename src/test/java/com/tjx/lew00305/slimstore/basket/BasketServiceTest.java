package com.tjx.lew00305.slimstore.basket;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjx.lew00305.slimstore.register.form.Form;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasketServiceTest {
    
    @MockitoBean
    Basket basket;
    
    private final ObjectMapper mapper = new ObjectMapper();
    private final BasketService basketService;
    
    private String formJson = """
        {
            "targetView":"HOME",
            "serverProcess":"ADD_TO_BASKET",
            "elements":[
                {"type":"PRODUCT","key":"101","label":"Pepsi","disabled":false,"hidden":false,"required":false,"image":"image/pepsi.webp","price":2,"quantity":1},
                {"type":"PRODUCT","key":"102","label":"Pepsi Max","disabled":false,"hidden":false,"required":false,"image":"image/pepsi-max.webp","price":1.5,"quantity":1},
                {"type":"PRODUCT","key":"103","label":"Coca Cola","disabled":false,"hidden":false,"required":false,"image":"image/coca-cola.webp","price":2,"quantity":1},
                {"type":"RETURN","key":"*","label":"30777364","value":"1","disabled":false,"hidden":false,"required":false,"price":29.99,"quantity":1},
                {"type":"RETURN","key":"*","label":"30791121","value":"1","disabled":false,"hidden":false,"required":false,"price":19.99,"quantity":1}
            ]}
        """;
    
    @Test
    public void addFormToBasket() throws JsonMappingException, JsonProcessingException {
        assertThat(basketService.getBasketArrayList()).hasSize(0);
        basketService.addBasketByForm(getForm());
        assertThat(basketService.getBasketArrayList()).hasSize(5);
        assertThat(basketService.getTotal()).isEqualByComparingTo(new BigDecimal("-44.48").setScale(2, RoundingMode.CEILING));
        assertThat(basketService.getBasketArray().length).isEqualTo(5);
        basketService.empty();
        assertThat(basketService.getBasketArrayList()).hasSize(0);
    }
    
    private Form getForm() throws JsonMappingException, JsonProcessingException {
        return mapper.readValue(formJson, Form.class);
    }
    
}
