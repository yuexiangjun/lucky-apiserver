package com.lucky.api.controller.external;

import com.lucky.api.controller.common.BaseController;
import com.lucky.api.controller.external.vo.LogisticsOrderInfoVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.LogisticsOrderServer;

import com.lucky.domain.entity.LogisticsOrderEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客服信息
 *
 * @folder API/小程序/我的订单
 */
@RequestMapping("/api/wechat/logistics-order")
@RestController
public class WechatLogisticsOrderController extends BaseController {
    private final LogisticsOrderServer logisticsOrderServer;

    public WechatLogisticsOrderController(LogisticsOrderServer logisticsOrderServer) {
        this.logisticsOrderServer = logisticsOrderServer;
    }


    /**
     * 获取物流订单列表
     */
    @GetMapping("/list")
    @ResponseFormat
    public List<LogisticsOrderInfoVO> list(@RequestParam(value = "wechatUserId", required = false) Long wechatUserId, @RequestParam(value = "status", required = false) Integer status) {

        if (Objects.isNull(wechatUserId))
            wechatUserId = this.getWechatUserId();

        var logisticsOrderEntity = LogisticsOrderEntity.builder()
                .wechatUserId(wechatUserId)
                .status(status)
                .build();
        return logisticsOrderServer.getByAdminList(logisticsOrderEntity)
                .stream()
                .map(LogisticsOrderInfoVO::getInstance)
                .collect(Collectors.toList());
    }
}
