package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.EnabledDTO;
import com.lucky.api.controller.admin.dto.SeriesTopicDTO;
import com.lucky.api.controller.admin.vo.SeriesTopicVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.SeriesTopicServer;

import com.lucky.domain.entity.SeriesTopicEntity;
import com.lucky.domain.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 主题 系列
 *
 * @folder API/后台/主题 系列
 */
@Slf4j
@RestController
@RequestMapping("/api/series-topic")
public class SeriesTopicController {
    private final SeriesTopicServer topicServer;

    public SeriesTopicController(SeriesTopicServer topicServer) {
        this.topicServer = topicServer;
    }

    /**
     * 添加
     */
    @PostMapping
    @ResponseFormat
    public void save(@RequestBody SeriesTopicDTO dto) {
        var entity = SeriesTopicDTO.toEntity(dto);
        entity.setStatus(false);
        var id = topicServer.saveOrUpdate(entity);
        if (Objects.isNull(id))
            throw BusinessException.newInstance("修改失败");

    }

    /**
     * 修改
     */
    @PutMapping
    @ResponseFormat
    public void update(@RequestBody SeriesTopicDTO dto) {
        var entity = SeriesTopicDTO.toEntity(dto);

        if (Objects.isNull(entity.getId()))
            throw BusinessException.newInstance("缺少id参数");

        var id = topicServer.saveOrUpdate(entity);
        if (Objects.isNull(id))
            throw BusinessException.newInstance("修改失败");

    }

    /**
     * 修改状态
     */
    @PutMapping("/enabled")
    @ResponseFormat
    public void updateStatus(@RequestBody EnabledDTO enabledDTO) {

        topicServer.updateStatus(enabledDTO.getId(), enabledDTO.getEnabled());
    }

    /**
     * 删除
     */
    @DeleteMapping
    @ResponseFormat
    public void deleteById(@RequestParam Long id) {
        var aBoolean = topicServer.deleteById(id);
        if (!aBoolean)
            throw BusinessException.newInstance("删除失败");
    }

    /**
     * 列表
     */
    @PostMapping("/list")
    @ResponseFormat
    public List<SeriesTopicVO> findByList() {
        log.info("后台系列列表");
         var  seriesTopicEntity= SeriesTopicEntity
                .builder().build();
        return topicServer.findByList(seriesTopicEntity)
                .stream()
                .map(SeriesTopicVO::getInstance)
                .collect(Collectors.toList());
    }

    /**
     * 设置场次
     */
    @PostMapping("/fields-number")
    @ResponseFormat
    public void setFieldsNumber(@RequestParam Long topicId, @RequestParam Integer number) {
        var aBoolean = topicServer.setFieldsNumber(topicId, number);
        if (!aBoolean)
            throw BusinessException.newInstance("设置场次失败");
    }
    /**
     * 修改排序
     */
    @PostMapping("/sort")
    @ResponseFormat
    public void updateSort(@RequestParam Long topicId, @RequestParam Integer sort) {
        var aBoolean = topicServer.updateSort(topicId, sort);
        if (!aBoolean)
            throw BusinessException.newInstance("修改排序失败");
    }



}
