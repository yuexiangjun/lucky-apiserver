package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.PrizeInfoDTO;
import com.lucky.api.controller.admin.vo.PrizeInfoVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.PrizeInfoServer;

import com.lucky.domain.entity.PrizeInfoEntity;
import com.lucky.domain.exception.BusinessException;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 奖品
 *
 * @folder API/后台/奖品设置
 */
@RestController
@RequestMapping("/api/prize-info")
public class PrizeInfoController {
    private final PrizeInfoServer prizeInfoServer;

    public PrizeInfoController(PrizeInfoServer prizeInfoServer) {
        this.prizeInfoServer = prizeInfoServer;
    }


    /**
     * 添加
     */
    @PostMapping
    @ResponseFormat
    public void save(@RequestBody PrizeInfoDTO dto) {
        var entity = PrizeInfoDTO.toEntity(dto, dto.getTopicId());

        var id = prizeInfoServer.saveOrUpdate(entity);
        if (Objects.isNull(id))
            throw BusinessException.newInstance("添加失败");

    }

    /**
     * 批量添加
     */
    @PostMapping("/save-batch")
    @ResponseFormat
    public Boolean saveList(@RequestBody List<PrizeInfoDTO> dtos, @RequestParam Long topicId) {
        List<PrizeInfoEntity> entity = List.of();
        if (!CollectionUtils.isEmpty(dtos)) {
            entity = dtos.stream()
                    .map(s -> PrizeInfoDTO.toEntity(s, topicId))
                    .collect(Collectors.toList());
        }
        return prizeInfoServer.saveOrUpdateList(entity, topicId);
    }

    /**
     * 批量修改
     */
    @PostMapping("/update-batch")
    @ResponseFormat
    public Boolean updateList(@RequestBody List<PrizeInfoDTO> dtos, @RequestParam Long topicId) {
        List<PrizeInfoEntity> entity = List.of();
        if (!CollectionUtils.isEmpty(dtos)) {
            entity = dtos.stream()
                    .map(s -> PrizeInfoDTO.toEntity(s, topicId))
                    .collect(Collectors.toList());
        }
        return prizeInfoServer.saveOrUpdateList(entity, topicId);
    }

    /**
     * 修改
     */
    @PutMapping
    @ResponseFormat
    public void update(@RequestBody PrizeInfoDTO dto) {
        var entity = PrizeInfoDTO.toEntity(dto, dto.getTopicId());

        if (Objects.isNull(entity.getId()))
            throw BusinessException.newInstance("缺少id参数");

        var id = prizeInfoServer.saveOrUpdate(entity);
        if (Objects.isNull(id))
            throw BusinessException.newInstance("修改失败");

    }

    /**
     * 根据主题系列查看商品
     */
    @GetMapping("/list")
    @ResponseFormat
    public List<PrizeInfoVO> findByTopicId(@RequestParam Long topicId) {
        var list = prizeInfoServer.findByTopicId(topicId);
        return
                list.stream()
                        .map(PrizeInfoVO::getInstance)
                        .collect(Collectors.toList());
    }

    /**
     * 删除
     */
    @ResponseFormat
    @DeleteMapping
    public void deleteById(@RequestParam Long id) {
        var aBoolean = prizeInfoServer.deleteById(id);
        if (!aBoolean)
            throw BusinessException.newInstance("删除失败");
    }
}
