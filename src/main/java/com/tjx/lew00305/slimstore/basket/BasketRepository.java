package com.tjx.lew00305.slimstore.basket;

import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisSessionRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasketRepository {
    
    private final RedisSessionRepository redisRepository;
    private final HttpServletRequest request;
    
    public Object getAttribute(
        String attribute // e.g. basket
    ) {
        return getAttribute(request.getRequestedSessionId(), attribute);
    }

    public Object getAttribute(
        String sessionId,
        String attribute
    ) {
        Session session = redisRepository.findById(sessionId);
        if (session == null) {
            return null;
        }
        System.out.println(session.getAttributeNames());
        Object obj = session.getAttribute("scopedTarget." + attribute);
        if (obj == null) {
            return null;
        }
        return obj;
    }
}
