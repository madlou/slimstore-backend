package cloud.matthews.slimstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.RedisSerializer;

import cloud.matthews.slimstore.basket.Basket;
import cloud.matthews.slimstore.basket.BasketLine;
import cloud.matthews.slimstore.form.FormElement.FormElementType;
import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.tender.Tender;
import cloud.matthews.slimstore.tender.TenderLine;
import cloud.matthews.slimstore.tender.TenderType;

class SpringConfigRedisSerializerTest {

    @Test
    void roundTripsSessionAttributes() {
        RedisSerializer<Object> serializer = new SpringConfig().springSessionDefaultRedisSerializer();
        Basket basket = new Basket();
        basket.add(new BasketLine("item", "Item", FormElementType.RETURN, 2, new BigDecimal("3.50")));
        Register register = new Register();
        register.setLastTxnTime(Timestamp.valueOf("2026-09-28 13:00:00"));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("scopedTarget.basket", basket);
        attributes.put("scopedTarget.register", register);
        attributes.put("created", LocalDateTime.of(2026, 9, 28, 13, 0));

        Map<?, ?> restored = assertInstanceOf(Map.class, serializer.deserialize(serializer.serialize(attributes)));
        Basket restoredBasket = assertInstanceOf(Basket.class, restored.get("scopedTarget.basket"));
        assertEquals(new ArrayList<>(basket.getArrayList()), restoredBasket.getArrayList());
        Register restoredRegister = assertInstanceOf(Register.class, restored.get("scopedTarget.register"));
        assertEquals(register.getLastTxnTime(), restoredRegister.getLastTxnTime());
        assertEquals(attributes.get("created"), restored.get("created"));

        Basket directBasket = assertInstanceOf(Basket.class, serializer.deserialize(serializer.serialize(basket)));
        assertEquals(basket.getArrayList(), directBasket.getArrayList());
    }

    @Test
    void roundTripsTenderSessionAttribute() {
        RedisSerializer<Object> serializer = new SpringConfig().springSessionDefaultRedisSerializer();
        Tender tender = new Tender();
        tender.add(new TenderLine(TenderType.CASH, "Cash", new BigDecimal("3.50"), "receipt"));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("scopedTarget.tender", tender);
        Map<?, ?> restored = assertInstanceOf(Map.class, serializer.deserialize(serializer.serialize(attributes)));
        Tender restoredTender = assertInstanceOf(Tender.class, restored.get("scopedTarget.tender"));
        assertEquals(tender.getTender(), restoredTender.getTender());
        assertFalse(restoredTender.isComplete());

        tender.setComplete();
        Map<?, ?> completedAttributes = assertInstanceOf(Map.class,
            serializer.deserialize(serializer.serialize(attributes)));
        Tender completedTender = assertInstanceOf(Tender.class, completedAttributes.get("scopedTarget.tender"));
        assertTrue(completedTender.isComplete());
        assertEquals(tender.getTender(), completedTender.getTender());

        byte[] serializedTender = serializer.serialize(tender);
        String tenderJson = new String(serializedTender, StandardCharsets.UTF_8);
        assertTrue(tenderJson.contains("\"isComplete\":true"));
        assertFalse(tenderJson.contains("\"complete\":true"));
        Tender directTender = assertInstanceOf(Tender.class, serializer.deserialize(serializedTender));
        assertTrue(directTender.isComplete());
        directTender.empty();
        assertFalse(directTender.isComplete());
    }
}
