package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.EnabledDTO;
import com.lucky.api.controller.admin.dto.GradeDTO;
import com.lucky.api.controller.admin.vo.DropBoxVO;
import com.lucky.api.controller.admin.vo.GradeVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.GradeServer;

import com.lucky.domain.entity.GradeEntity;
import com.lucky.domain.exception.BusinessException;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 奖等级设置
 *
 * @folder API/后台/奖等级设置
 */
@RestController
@RequestMapping("/api/grade")
public class GradeController {
    private final GradeServer gradeServer;

    public GradeController(GradeServer gradeServer) {
        this.gradeServer = gradeServer;
    }

    /**
     * 添加
     */
    @PostMapping()
    @ResponseFormat
    public void save(@RequestBody GradeDTO gradeDTO) {

        var entity = GradeDTO.toEntity(gradeDTO);

        var id = gradeServer.saveOrUpdate(entity);
        if (Objects.isNull(id))
            throw BusinessException.newInstance("添加失败");

    }


    /**
     * 修改
     */
    @PutMapping()
    @ResponseFormat
    public void update(@RequestBody GradeDTO gradeDTO) {
        var entity = GradeDTO.toEntity(gradeDTO);

        if (Objects.isNull(entity.getId()))
            throw BusinessException.newInstance("缺少id参数");

        var id = gradeServer.saveOrUpdate(entity);

        if (Objects.isNull(id))
            throw BusinessException.newInstance("修改失败");

    }

    /**
     * 启用禁用
     */
    @PutMapping("/enabled")
    @ResponseFormat
    public void enabled(@RequestBody EnabledDTO dto) {
        gradeServer.enabled(dto.getId(), dto.getEnabled());
    }

    /**
     * 列表
     */
    @PostMapping("/list")
    @ResponseFormat
    public List<GradeVO> findByList() {
        return gradeServer.findByList(new GradeEntity())
                .stream()
                .map(GradeVO::getInstance)
                .collect(Collectors.toList());
    }

    /**
     * 删除
     */
    @DeleteMapping("/id")
    @ResponseFormat
    public void deleteById(@RequestParam Long id) {
        if (Objects.isNull(id))
            throw BusinessException.newInstance("缺少id参数");

        var aBoolean = gradeServer.deleteById(id);

        if (!aBoolean)
            throw BusinessException.newInstance("删除失败");
    }

    /**
     * 添加主题时 下拉框
     */
    @GetMapping("/drop-down-box")
    @ResponseFormat
    public List<DropBoxVO> dropBox(@RequestParam(required = false) Integer type) {
        var entity = GradeEntity.builder()
                .type(type)
                .status(true)
                .build();
        return gradeServer.findByList(entity)
                .stream()
                .map(s -> DropBoxVO.getInstance(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }

}
