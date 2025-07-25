package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.LogisticsOrderInfoDTO;
import com.lucky.api.controller.admin.dto.UpdateLogisticsOrderDTO;
import com.lucky.api.controller.external.vo.LogisticsOrderInfoVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.LogisticsOrderServer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 物流订单
 *
 * @folder API/后台/物流订单列表
 */
@RequestMapping("/api/admin/logistics-order")
@RestController
public class LogisticsOrderController {
    private final LogisticsOrderServer logisticsOrderServer;

    public LogisticsOrderController(LogisticsOrderServer logisticsOrderServer) {
        this.logisticsOrderServer = logisticsOrderServer;
    }


    /**
     * 获取物流订单列表
     */
    @PostMapping("/list")
    @ResponseFormat
    public List<LogisticsOrderInfoVO> list(@RequestBody LogisticsOrderInfoDTO logisticsOrderInfo) {
        var logisticsOrderEntity = LogisticsOrderInfoDTO.toEntity(logisticsOrderInfo);
        return logisticsOrderServer.getByAdminList(logisticsOrderEntity, logisticsOrderInfo.getPhone())
                .stream()
                .map(LogisticsOrderInfoVO::getInstance)
                .collect(Collectors.toList());
    }

    /**
     * 修改物流订单 包含修改状态
     */
    @PostMapping("/update")
    @ResponseFormat
    public void updateLogisticsOrder(@RequestBody UpdateLogisticsOrderDTO updateLogisticsOrderDTO) {
        var logisticsOrderEntity = UpdateLogisticsOrderDTO.toEntity(updateLogisticsOrderDTO);
        logisticsOrderServer.updateLogisticsOrder(logisticsOrderEntity);
    }


}
