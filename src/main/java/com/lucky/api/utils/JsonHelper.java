package com.lucky.api.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.math.BigInteger;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Json Helper
 */
public class JsonHelper {
    /**
     * 序列化配置
     */
    public static final ObjectMapper mapper = new ObjectMapper();

    /*
     * 静态加载
     */
    static {
        //允许json属性名不使用双引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //日期序列化
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        //忽略不存在字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //忽略为空的对象
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //序列化配置
        var simpleModule = new SimpleModule();

        //时间
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        //Long转为String，防止前端精度丢失
        simpleModule.addSerializer(Long.class, new LongSerializer());
        //BigInteger转为String，防止前端精度丢失
        simpleModule.addSerializer(BigInteger.class, new BigIntegerSerializer());

        //注册模型
        mapper.registerModule(simpleModule);
    }


}