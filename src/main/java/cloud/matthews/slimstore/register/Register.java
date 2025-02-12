package cloud.matthews.slimstore.register;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cloud.matthews.slimstore.store.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Component
@NoArgsConstructor
@AllArgsConstructor
@SessionScope
@JsonSerialize
@JsonDeserialize(as = Register.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY)
@Table(name = "store_register")
public class Register implements Serializable {
    
    public enum RegisterStatus {
        OPEN,
        CLOSED
    }
    
    private static final long serialVersionUID = 972644074L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;
    private Integer number;
    @Enumerated(EnumType.STRING)
    private RegisterStatus status;
    private Integer lastTxnNumber;
    private String sessionId;
    private String userName;
    private Timestamp lastTxnTime;
    private String customerDisplayToken;
    private Integer customerDisplayPin;
    
    @JsonIgnore
    public Boolean isSet() {
        return number == null ? false : true;
    }

}
