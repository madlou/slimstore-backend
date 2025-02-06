package cloud.matthews.slimstore.tender;

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
@JsonDeserialize(as = Tender.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY)
public class Tender implements Serializable {
    
    private static final long serialVersionUID = -5544623814081086407L;
    
    private ArrayList<TenderLine> tender = new ArrayList<TenderLine>();
    
    @JsonIgnore
    private boolean isComplete = false;
    
    public void add(
        TenderLine tenderLine
    ) {
        tender.add(tenderLine);
    }
    
    public void empty() {
        tender = new ArrayList<TenderLine>();
        isComplete = false;
    }
    
    @JsonIgnore
    public TenderLine[] getArray() {
        return tender.toArray(new TenderLine[0]);
    }
    
    @JsonIgnore
    public ArrayList<TenderLine> getArrayList() {
        return tender;
    }
    
    @JsonIgnore
    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (TenderLine line : tender) {
            total = total.add(line.getValue());
        }
        return total;
    }
    
    @JsonIgnore
    public boolean isComplete() {
        return isComplete;
    }
    
    public void setComplete() {
        isComplete = true;
    }

}
