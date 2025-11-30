package com.line7studio.boulderside.infrastructure.cache.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisProvider {
	private final RedisTemplate<String, Object> redisTemplate;

	public void set(String key, Object value, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	public <T> Optional<T> get(String key, Class<T> clazz) {
		Object value = redisTemplate.opsForValue().get(key);
		return Optional.ofNullable(clazz.cast(value));
	}

	public void hSet(String key, String field, Object value) {
		redisTemplate.opsForHash().put(key, field, value);
	}

	public <T> Optional<T> hGet(String key, String field, Class<T> clazz) {
		Object value = redisTemplate.opsForHash().get(key, field);
		return Optional.ofNullable(clazz.cast(value));
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}
}
