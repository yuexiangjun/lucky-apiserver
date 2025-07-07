package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.OrderDTO;
import com.lucky.api.controller.admin.dto.StatusDTO;
import com.lucky.api.controller.admin.vo.OrderVO;
import com.lucky.api.controller.admin.vo.SalesVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.OrderServer;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台用户
 *
 * @folder API/后台/订单记录
 */
@RestController
@RequestMapping("/api/admin/order")
public class OrderController {
    private final OrderServer orderServer;

    public OrderController(OrderServer orderServer) {
        this.orderServer = orderServer;
    }


    /**
     * 场次的抽奖记录
     * 列表
     */
    @PostMapping("/list")
    @ResponseFormat
    public List<OrderVO> list(@RequestBody OrderDTO dto) {

        var entity = OrderDTO.toEntity(dto);

        return orderServer.list(entity,dto.getUserNameOrPhone(),dto.getSeriesName())
                .stream()
                .map(OrderVO::getInstance)
                .collect(Collectors.toList());
    }


    /**
     * 修改订单状态
     */
    @PutMapping("/status")
    @ResponseFormat
    public void updateStatus(@RequestBody StatusDTO dto) {

        orderServer.updateStatus(dto.getId(), dto.getStatus());
    }

    /**
     * 某一系列利润（更据主题系列查看销售情况）
     * 销售情况
     */
    @GetMapping("/sales")
    @ResponseFormat
    public SalesVO sales(@RequestParam Long topicId) {
        var sales = orderServer.sales(topicId);
        return SalesVO.getInstance(sales);

    }
}
