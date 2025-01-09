package com.tjx.lew00305.slimstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.tjx.lew00305.slimstore")
@EnableRedisRepositories("none")
public class SlimStoreApplication {
    
    public static void main(
        String[] args
    ) {
        SpringApplication.run(SlimStoreApplication.class, args);
    }

}
