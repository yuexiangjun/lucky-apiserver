package com.lucky.api.controller.admin.dto;

import com.lucky.domain.entity.PrizeInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 奖品
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePrizeInfoDTO {
    /**
     * id
     */
    private Long id;
    /**
     * 类别
     * 1:隐藏
     * 2：普通级别
     */
    private Integer type;
    /**
     * 奖品等级
     */
    private Long gradeId;
    /**
     * 主题id
     */
    private Long topicId;
    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 奖品图片
     */
    private String prizeUrl;
    /**
     * 库存
     */
    private Integer inventory;
    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * @param dto
     * @return
     */
    public static PrizeInfoEntity toEntity(UpdatePrizeInfoDTO dto, Long topicId) {
        if (Objects.isNull(dto))
            return null;
        return PrizeInfoEntity.builder()
                .id(dto.getId())
                .type(dto.getType())
                .gradeId(dto.getGradeId())
                .topicId(topicId)
                .price(dto.getPrice())
                .prizeName(dto.getPrizeName())
                .prizeUrl(dto.getPrizeUrl())
                .inventory(dto.getInventory())
                .build();
    }
}