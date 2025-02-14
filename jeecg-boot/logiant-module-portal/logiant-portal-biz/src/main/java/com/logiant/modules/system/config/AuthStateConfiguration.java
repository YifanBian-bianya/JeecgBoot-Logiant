package com.logiant.modules.system.config;

import me.zhyd.oauth.cache.AuthStateCache;
import com.logiant.modules.system.cache.AuthStateRedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthStateConfiguration {

    @Bean
    public AuthStateCache authStateCache() {
        return new AuthStateRedisCache();
    }
}
