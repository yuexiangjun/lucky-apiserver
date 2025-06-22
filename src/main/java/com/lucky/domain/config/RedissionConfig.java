package com.lucky.domain.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("spring.redis")
@NoArgsConstructor
@Slf4j
@Data
public class RedissionConfig {
	/**
	 * 地址
	 */
	private String host;

	/**
	 * 端口
	 */
	private String port;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * @return
	 */

	@Bean
	public RedissonClient redissonClient() {
		// 配置
		var config = new Config();
		var address = new StringBuilder("redis://")
				.append(this.getHost())
				.append(":")
				.append(this.getPort())
				.toString();
		config.useSingleServer().setAddress(address);
		if (Strings.isNotBlank(this.getPassword()))
			config.useSingleServer().setPassword(this.getPassword());
		// 创建RedissonClient对象
		return Redisson.create(config);
	}

}
