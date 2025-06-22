package com.lucky.domain.entity;

import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.valueobject.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 场次
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionInfoEntity {
    /**
     * id
     */
    private Long id;
    /**
     * 主题id
     */
    private Long topicId;
    /**
     * 状态 0：禁用 1：启用 2：结束
     */
    private Integer status;
    /**
     * 场次编号
     */
    private Integer sessionNumber;
    /**
     * 奖品库存
     */
    private List<Inventory> prizeInventory;

    /**
     * 获取总库存
     */
    public Integer totalInventory() {
        if (CollectionUtils.isEmpty(this.getPrizeInventory()))
            return 0;
        return this.getPrizeInventory().stream()
                .map(Inventory::getInventory)
                .reduce(0, Integer::sum);
    }

    /**
     * 获取
     */
    public static SessionInfoEntity getInstance(Long topicId, List<PrizeInfoEntity> prizeInfoEntityList) {

        if (CollectionUtils.isEmpty(prizeInfoEntityList))
            throw BusinessException.newInstance("没有奖品，请设置奖品");

        var prizeInfoEntities = prizeInfoEntityList.stream()
                .filter(s -> Objects.equals(s.getType(), 2))
                .map(Inventory::getInstance)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(prizeInfoEntityList))
            throw BusinessException.newInstance("没有普通奖品，请设置普通奖品");

        return
                SessionInfoEntity.builder()
                        .prizeInventory(prizeInfoEntities)
                        .status(1)
                        .topicId(topicId)
                        .build();

    }

}
