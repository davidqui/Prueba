package com.laamware.ejercito.doc.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;

import com.laamware.ejercito.doc.web.util.HermesRedisObjectSerializer;

@Configuration
public class RedisConfig {

	@Primary
	@Bean
	public RedisTemplate<String, ExpiringSession> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, ExpiringSession> template = new RedisTemplate<String, ExpiringSession>();

		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new HermesRedisObjectSerializer());

		template.setConnectionFactory(connectionFactory);
		return template;
	}

}
