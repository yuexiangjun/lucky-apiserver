package com.lucky.api.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Long 转 String 序列化器
 */
public class LongSerializer extends JsonSerializer<Long> {

	/**
	 * 序列化
	 *
	 * @param value              Int64
	 * @param jsonGenerator      json生成器
	 * @param serializerProvider 序列化器
	 * @throws IOException IO异常
	 */
	@Override
	public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		if (value == null) {
			jsonGenerator.writeNull();
			return;
		}
		jsonGenerator.writeObject(value.toString());
	}
}