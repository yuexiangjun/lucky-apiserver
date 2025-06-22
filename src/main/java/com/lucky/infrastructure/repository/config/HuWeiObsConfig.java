package com.lucky.infrastructure.repository.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data

@Component
@ConfigurationProperties("huawei")
@NoArgsConstructor
public class HuWeiObsConfig {

    private  String endpoint;
    private  String ak ;
    private  String sk ;

    private  String bucketname ;

    private  String BASEURL1 ;
}
