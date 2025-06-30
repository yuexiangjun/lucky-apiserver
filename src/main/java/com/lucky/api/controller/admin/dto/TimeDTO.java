package com.lucky.api.controller.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TimeDTO {
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     *  结束时间
     */
    private LocalDateTime endTime;
}
