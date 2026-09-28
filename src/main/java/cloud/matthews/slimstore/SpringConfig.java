package cloud.matthews.slimstore;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration(proxyBeanMethods = false)
@EnableCaching
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
@EnableJpaRepositories("cloud.matthews.slimstore")
@EnableRedisRepositories("none")
public class SpringConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate(
        RestTemplateBuilder builder
    ) {
        return builder.build();
    }
    
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        var typeValidator = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType("cloud.matthews.slimstore.")
            .allowIfSubType("java.util.")
            .allowIfSubType("java.time.")
            .allowIfSubType(BigDecimal.class)
            .allowIfSubType(Timestamp.class)
            .build();
        return GenericJacksonJsonRedisSerializer.builder()
            .enableDefaultTyping(typeValidator)
            .customize(mapper -> mapper
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .changeDefaultVisibility(visibility -> visibility.withFieldVisibility(Visibility.ANY)))
            .build();
    }
    
}
