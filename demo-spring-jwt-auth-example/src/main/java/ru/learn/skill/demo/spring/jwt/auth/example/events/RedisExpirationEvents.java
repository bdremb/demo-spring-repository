package ru.learn.skill.demo.spring.jwt.auth.example.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import ru.learn.skill.demo.spring.jwt.auth.example.entity.RefreshToken;

@Slf4j
@Component
public class RedisExpirationEvents {

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event) {
        RefreshToken expiredRefreshToken = (RefreshToken) event.getValue();

        if (expiredRefreshToken == null) {
            throw new RuntimeException(("Refresh token is null in handleRedisKeyExpiredEvent method"));
        }
        log.info("Refresh token with key {} has expired! Refresh token is: {}", expiredRefreshToken.getId(), expiredRefreshToken.getToken());
    }

}
