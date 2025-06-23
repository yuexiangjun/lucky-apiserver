package com.lucky.api.controller.external;

import com.lucky.api.controller.common.BaseController;
import com.lucky.api.controller.external.dto.LogisticsOrderDTO;
import com.lucky.api.controller.external.vo.WechatPrizeInfoVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.OrderServer;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 盒柜
 *
 * @folder API/小程序/盒柜
 */
@RequestMapping("/api/wechat/box-cabinets")
@RestController
public class BoxCabinetsController extends BaseController {
    private final OrderServer orderServer;

    public BoxCabinetsController(OrderServer orderServer) {
        this.orderServer = orderServer;
    }

    /**
     * 列表
     */
    @ResponseFormat
    @GetMapping("/list")
    public List<WechatPrizeInfoVO> list(@RequestParam(value = "wechatUserId", required = false) Long wechatUserId) {

        if (Objects.isNull(wechatUserId))
            wechatUserId = this.getWechatUserId();

        return orderServer.boxCabinets(wechatUserId).stream()
                .map(WechatPrizeInfoVO::getInstance)
                .collect(Collectors.toList());
    }

    /**
     * 生成物流定单
     */
    @ResponseFormat
    @PostMapping("/generate-logistics-order")
    public void generateLogisticsOrder(@RequestBody LogisticsOrderDTO dto) {

        if (Objects.isNull(dto.getWechatUserId()))
            dto.setWechatUserId(this.getWechatUserId());

        var logisticsOrder = LogisticsOrderDTO.toLogisticsOrder(dto);
        orderServer.generateLogisticsOrder(logisticsOrder);
    }

}
