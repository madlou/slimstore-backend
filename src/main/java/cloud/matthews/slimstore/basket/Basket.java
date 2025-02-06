package cloud.matthews.slimstore.basket;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@SessionScope
@JsonSerialize
@JsonDeserialize(as = Basket.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY)
public class Basket implements Serializable {
    
    private static final long serialVersionUID = -233420559502189484L;
    private ArrayList<BasketLine> basket = new ArrayList<BasketLine>();
    
    public void add(
        BasketLine basketLine
    ) {
        basket.add(basketLine);
    }
    
    public void empty() {
        basket = new ArrayList<BasketLine>();
    }
    
    @JsonIgnore
    public BasketLine[] getArray() {
        return basket.toArray(new BasketLine[0]);
    }
    
    @JsonIgnore
    public ArrayList<BasketLine> getArrayList() {
        return basket;
    }
    
    @JsonIgnore
    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (BasketLine line : basket) {
            total = total.add(line.getLineValue());
        }
        return total;
    }
    
}
