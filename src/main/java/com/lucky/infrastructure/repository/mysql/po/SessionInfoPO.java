package com.lucky.infrastructure.repository.mysql.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.SessionInfoEntity;
import com.lucky.domain.valueobject.Inventory;
import com.lucky.infrastructure.repository.mysql.InventoryListTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * 场次
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "session_info",autoResultMap = true)
public class SessionInfoPO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
    @TableField(typeHandler = InventoryListTypeHandler.class)
    private List<Inventory> prizeInventory;

    public static SessionInfoPO getInstance(SessionInfoEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return SessionInfoPO.builder()
                .id(entity.getId())
                .sessionNumber(entity.getSessionNumber())
                .prizeInventory(entity.getPrizeInventory())
                .topicId(entity.getTopicId())
                .status(entity.getStatus())
                .build();

    }

    public static SessionInfoEntity toEntity(SessionInfoPO po) {
        if (Objects.isNull(po))
            return null;
        return SessionInfoEntity.builder()
                .id(po.getId())
                .sessionNumber(po.getSessionNumber())
                .prizeInventory(po.getPrizeInventory())
                .topicId(po.getTopicId())
                .status(po.getStatus())
                .build();

    }

}
