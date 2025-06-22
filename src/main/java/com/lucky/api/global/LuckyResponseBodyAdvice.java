package com.lucky.api.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucky.api.utils.R;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.domain.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author 岳相军
 * @description 格式化出参
 */
@Slf4j
@ControllerAdvice
public class LuckyResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private ObjectMapper objectMapper;

    public LuckyResponseBodyAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return Objects.nonNull(
                methodParameter
                        .getAnnotatedElement()
                        .getAnnotation(ResponseFormat.class)
        );
    }

    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (o instanceof String)
            try {
                var headers = serverHttpResponse.getHeaders();
                headers.add("Content-Type", "application/json");
                return this.objectMapper.writeValueAsString(R.ok(o));
            } catch (JsonProcessingException e) {
                log.error("响应参数序列化失败", e);
                throw BusinessException.newInstance("响应参数序列化失败");
            }
        return R.ok(o);
    }
}
