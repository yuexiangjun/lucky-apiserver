package com.lucky.domain.valueobject;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.SeriesTopicEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 主题 系列
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeriesTopicDetail {
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
     * 是否启用
     */
    private Boolean status;

    /**
     * 奖项详情
     */
    private List<DropBox> gradeDetail;

    public static SeriesTopicDetail getInstance(SeriesTopicEntity entity, Map<Long, String> gradeMapName) {
        if (Objects.isNull(entity))
            return null;
        SeriesTopicDetail bean = BeanUtil.toBean(entity, SeriesTopicDetail.class);
        List<Long> gradeIds = entity.getGradeIds();
        if (Objects.nonNull(gradeIds)) {
            bean.setGradeDetail(
                    gradeIds
                            .stream()
                            .map(id -> {
                                String name1 = gradeMapName.get(id);
                                if (Objects.isNull(name1))
                                    return null;
                                return DropBox.builder()
                                        .id(id)
                                        .name(name1)
                                        .build();
                            }).filter(Objects::nonNull)
                            .collect(Collectors.toList())
            );
        }
        return bean;

    }


}
