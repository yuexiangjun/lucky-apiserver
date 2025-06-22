package com.lucky.infrastructure.repository.mysql;


import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.lucky.domain.valueobject.Inventory;
import com.fasterxml.jackson.core.type.TypeReference;


import java.io.IOException;
import java.util.List;

public class InventoryListTypeHandler extends JacksonTypeHandler {
    public InventoryListTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    protected Object parse(String json) {
        try {
            // 明确指定 List 元素类型为 Inventory
            return getObjectMapper().readValue(json,new TypeReference<List<Inventory>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}