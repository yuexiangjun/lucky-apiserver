package com.lucky.domain.entity.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Builder
public class WechatConfigEntity {

	private String appid;
	private String secret;
	private String accessTokenUrl;
	private String getPhoneNumberUrl;



}