package com.lucky.infrastructure.repository.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@ConfigurationProperties("spring.redis")
@Data
public class RedisConfig {

	private String host;
	private int port;
	private String password;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();

		config.setHostName(host);
		config.setPort(port);
		config.setPassword(password);
		return new LettuceConnectionFactory(config);
	}
}