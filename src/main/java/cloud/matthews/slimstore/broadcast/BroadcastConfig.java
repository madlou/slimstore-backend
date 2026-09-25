package cloud.matthews.slimstore.broadcast;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class BroadcastConfig implements WebSocketMessageBrokerConfigurer {

    private final BroadcastSubscriptionGuard subscriptionGuard;
    
    @SuppressWarnings("null")
    @Override
    public void configureMessageBroker(
        MessageBrokerRegistry config
    ) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @SuppressWarnings("null")
    @Override
    public void registerStompEndpoints(
        StompEndpointRegistry registry
    ) {
        BroadcastHandshakeInterceptor handshakeInterceptor = new BroadcastHandshakeInterceptor();
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("*").addInterceptors(handshakeInterceptor).withSockJS();
        registry.addEndpoint("/websocket-native").setAllowedOriginPatterns("*").addInterceptors(handshakeInterceptor);
//        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:3002");
    }

    @Override
    public void configureClientInboundChannel(
        ChannelRegistration registration
    ) {
        registration.interceptors(new ChannelInterceptor() {
            @SuppressWarnings("null")
            @Override
            public Message<?> preSend(
                Message<?> message,
                MessageChannel channel
            ) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if ((accessor != null) &&
                    (accessor.getCommand() == StompCommand.SUBSCRIBE)) {
                    subscriptionGuard.check(accessor);
                }
                return message;
            }
        });
    }
}
