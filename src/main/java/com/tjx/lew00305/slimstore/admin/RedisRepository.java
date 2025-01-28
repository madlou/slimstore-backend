package com.tjx.lew00305.slimstore.admin;

import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisSessionRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class RedisRepository {
    
    private final RedisSessionRepository redisRepository;
    private final HttpServletRequest request;
    
    public Object getAttribute(
        String attribute
    ) {
        return getAttribute(request.getRequestedSessionId(), attribute);
    }

    public Object getAttribute(
        String attribute,
        String sessionId
    ) {
        Session session = redisRepository.findById(sessionId);
        if (session == null) {
            return null;
        }
        Object obj = session.getAttribute("scopedTarget." + attribute);
        if (obj == null) {
            return null;
        }
        return obj;
    }
}
