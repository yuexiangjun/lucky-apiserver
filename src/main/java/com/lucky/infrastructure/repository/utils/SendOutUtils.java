package com.lucky.infrastructure.repository.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Objects;

/**
 * ClassName: SendOutUtils

 * @author 岳相军
 * @since JDK 1.8
 */
@Slf4j
public class SendOutUtils {
    /**
     * 发送post 请求
     */
    private static HttpRequest getHttpPostRequest(String url, String toJSONString) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(3000))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(toJSONString))
                .build();
    }


    /**
     * 发送get请求
     */
    private static HttpRequest getHttpGetRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(3000))
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }

     public  static HttpResponse.BodyHandler<String> getStringBodyHandler() {
        return HttpResponse.BodyHandlers
                .ofString(Charset.forName("utf-8"));
    }


    @SneakyThrows
    public static String sendGet(String url) {
        var response = HttpClient.newHttpClient().send(
                SendOutUtils.getHttpGetRequest(url),
                SendOutUtils.getStringBodyHandler()
        );
        log.info("get请求结果：{}", response);
        return response.body();
    }


    @SneakyThrows
    public static String sendPost(String url, String toJosnString) {
        var response = HttpClient.newHttpClient().send(
                SendOutUtils.getHttpPostRequest(url, toJosnString),
                SendOutUtils.getStringBodyHandler()
        );
        return response.body();

    }

    @SneakyThrows
    public static String sendGet2(String url) {
        var response = HttpClient.newHttpClient().send(
                SendOutUtils.getHttpGetRequest(url),
                SendOutUtils.getStringBodyHandler()
        );
        log.info("get请求结果：{}", response);
        String  url1=null;
        String[] s1 = response.toString().split(" ");
        if (Objects.equals(s1[2], "200")) {
            String s = s1[1];
              url1 = s.substring(0, s.length() - 1);
            log.info("地址：{}", url1);
        }
        return url1;
    }

}
