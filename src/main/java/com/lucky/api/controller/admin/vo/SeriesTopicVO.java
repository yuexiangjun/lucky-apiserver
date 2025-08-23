package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.SeriesTopicEntity;
import com.lucky.domain.valueobject.SeriesTopicDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 主题 系列
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeriesTopicVO {
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 主题颜色
     */
    private String topicColor;
    /**
     * 主题图片
     */
    private String topicUrl;

    /**
     * 场次(一共多少场)
     */
    private Integer session;
    /**
     * 价格（多少钱一抽）
     */
    private BigDecimal price;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private Boolean status;

    /**
     * 奖项详情
     */
    private List<DropBoxVO> gradeDetail;


    /**
     * @param entity
     * @return
     */

    public static SeriesTopicVO getInstance(SeriesTopicDetail entity) {

        if (Objects.isNull(entity))
            return null;
        return BeanUtil.toBean(entity, SeriesTopicVO.class);


    }

    /**
     * @param entity
     * @return
     */

    public static SeriesTopicVO getInstance(SeriesTopicEntity entity) {

        if (Objects.isNull(entity))
            return null;
        return BeanUtil.toBean(entity, SeriesTopicVO.class);


    }


}
