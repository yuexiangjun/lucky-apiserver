package com.lucky.infrastructure.repository.mysql.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "prize_info")
public class PrizeInfoPO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 类别
     * 1:隐藏
     * 2：普通级别
     */
    private Integer type;
    /**
     * 主题id
     */
    private Long topicId;
    /**
     * 奖品等级
     */
    private Long gradeId;
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
     * 替换商品id
     */
    private Long replacePrizeId;
    /**
     * 是否删除
     */
    private Boolean isDelete;
    public static PrizeInfoPO getInstance(PrizeInfoEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return PrizeInfoPO.builder()
                .id(entity.getId())
                .type(entity.getType())
                .price(entity.getPrice())
                .topicId(entity.getTopicId())
                .gradeId(entity.getGradeId())
                .prizeName(entity.getPrizeName())
                .prizeUrl(entity.getPrizeUrl())
                .inventory(entity.getInventory())
                .replacePrizeId(entity.getReplacePrizeId())
                .isDelete(entity.getIsDelete())
                .build();

    }

    public static PrizeInfoEntity toEntity(PrizeInfoPO po) {
        if (Objects.isNull(po))

            return null;
        return PrizeInfoEntity.builder()
                .id(po.getId())
                .type(po.getType())
                .topicId(po.getTopicId())
                .price(po.getPrice())
                .gradeId(po.getGradeId())
                .prizeName(po.getPrizeName())
                .prizeUrl(po.getPrizeUrl())
                .inventory(po.getInventory())
                .replacePrizeId(po.getReplacePrizeId())
                 .isDelete(po.getIsDelete())
                .build();

    }
}
