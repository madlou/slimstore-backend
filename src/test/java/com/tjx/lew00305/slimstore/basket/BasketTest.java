package com.tjx.lew00305.slimstore.basket;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tjx.lew00305.slimstore.register.form.FormElement.FormElementType;

@SpringBootTest
public class BasketTest {
    
    @Autowired
    private Basket basket;
    
    private void addToBasket() {
        BasketLine line1 = new BasketLine();
        line1.setCode("mycode");
        line1.setName("myname");
        line1.setQuantity(10);
        line1.setType(FormElementType.PRODUCT);
        line1.setUnitValue(new BigDecimal("9.99"));
        basket.add(line1);
        BasketLine line2 = new BasketLine();
        line2.setCode("mycode");
        line2.setName("myname");
        line2.setQuantity(2);
        line2.setType(FormElementType.PRODUCT);
        line2.setUnitValue(new BigDecimal("19.99"));
        basket.add(line2);
        BasketLine line3 = new BasketLine();
        line3.setCode("mycode");
        line3.setName("myname");
        line3.setQuantity(1);
        line3.setType(FormElementType.RETURN);
        line3.setUnitValue(new BigDecimal("10.00"));
        basket.add(line3);
    }

    @Test
    public void canCreateBasket() {
        assertThat(basket.getBasket()).hasSize(0);
        assertThat(basket.getTotal()).isEqualByComparingTo(new BigDecimal("0.00").setScale(2, RoundingMode.CEILING));
        addToBasket();
        assertThat(basket.getBasket()).hasSize(3);
        assertThat(basket.getTotal()).isEqualByComparingTo(new BigDecimal("129.88").setScale(2, RoundingMode.CEILING));
        assertThat(basket.getBasket().getFirst().getCode()).isEqualTo("mycode");
        assertThat(basket.getBasket().getFirst().getName()).isEqualTo("myname");
        assertThat(basket.getBasket().getFirst().getQuantity()).isEqualTo(10);
        assertThat(basket.getBasket().getFirst().getType()).isEqualTo(FormElementType.PRODUCT);
        assertThat(basket.getBasket().getFirst().getUnitValue()).isEqualByComparingTo(new BigDecimal("9.99").setScale(2, RoundingMode.CEILING));
        assertThat(basket.getBasket().getFirst().getLineValue()).isEqualByComparingTo(new BigDecimal("99.90").setScale(2, RoundingMode.CEILING));
        assertThat(basket.getBasket().getFirst().getSignedQuantity()).isEqualTo(10);
        assertThat(basket.getBasket().getLast().getType()).isEqualTo(FormElementType.RETURN);
        assertThat(basket.getBasket().getLast().getLineValue()).isEqualByComparingTo(new BigDecimal("-10.00").setScale(2, RoundingMode.CEILING));
        assertThat(basket.getBasket().getLast().getSignedQuantity()).isEqualTo(-1);
        assertThat(basket.getArray().length).isEqualTo(3);
        assertThat(basket.getArrayList()).hasSize(3);
        basket.empty();
        assertThat(basket.getBasket()).hasSize(0);
        assertThat(basket.getTotal()).isEqualByComparingTo(new BigDecimal("0.00").setScale(2, RoundingMode.CEILING));
    }

}
