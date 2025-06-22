package com.lucky.api.controller.admin.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DropBoxVO {
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;

    public static DropBoxVO getInstance(Long id, String name) {
        return DropBoxVO.builder()
                .id(id)
                .name(name)
                .build();
    }

}
