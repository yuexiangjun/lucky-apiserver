package com.lucky.infrastructure.repository.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("wechat")
@NoArgsConstructor

@Data
public class WechatConfig {

	private String appid;
	private String secret;
	private String accessTokenUrl;
	private String phoneNumberUrl;
	private String code2SessionUrl;


}