package cloud.matthews.slimstore.broadcast;

import java.util.Map;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.register.RegisterRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BroadcastSubscriptionGuard {

    private static final String TOPIC_PREFIX = "/topic/";
    private static final String DISPLAY_TOKEN_HEADER = "token";

    private final RegisterRepository registerRepository;

    public void check(
        StompHeaderAccessor accessor
    ) {
        String destination = accessor.getDestination();
        if (isPublicTopic(destination)) {
            return;
        }

        TopicLocation location = parseRegisterTopic(destination);
        if (location == null) {
            throw new IllegalArgumentException("Subscription destination is not allowed: " + destination);
        }

        String token = accessor.getFirstNativeHeader(DISPLAY_TOKEN_HEADER);
        Register register = registerRepository.findByStoreNumberAndNumber(location.store(), location.register());
        if (isDisplayTokenValid(register, token)) {
            return;
        }

        String httpSessionId = httpSessionId(accessor.getSessionAttributes());
        if (isCurrentRegisterSession(register, httpSessionId)) {
            return;
        }

        throw new IllegalArgumentException("Not allowed to subscribe to " + destination);
    }

    private boolean isPublicTopic(
        String destination
    ) {
        return "/topic/connected".equals(destination) ||
            "/topic/disconnected".equals(destination);
    }

    private TopicLocation parseRegisterTopic(
        String destination
    ) {
        if ((destination == null) ||
            !destination.startsWith(TOPIC_PREFIX)) {
            return null;
        }
        String[] parts = destination.substring(TOPIC_PREFIX.length()).split("/");
        if (parts.length != 2) {
            return null;
        }
        try {
            return new TopicLocation(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String httpSessionId(
        Map<String, Object> sessionAttributes
    ) {
        if (sessionAttributes == null) {
            return null;
        }
        Object sessionId = sessionAttributes.get(BroadcastHandshakeInterceptor.HTTP_SESSION_ID);
        return sessionId instanceof String ? (String)sessionId : null;
    }

    private boolean isDisplayTokenValid(
        Register register,
        String token
    ) {
        return (register != null) &&
            (token != null) &&
            !token.isBlank() &&
            token.equals(register.getCustomerDisplayToken());
    }

    private boolean isCurrentRegisterSession(
        Register register,
        String sessionId
    ) {
        return (register != null) &&
            (sessionId != null) &&
            sessionId.equals(register.getSessionId());
    }

    private record TopicLocation(Integer store, Integer register) {}

}
