package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.TimeDTO;
import com.lucky.api.controller.admin.vo.ConsumeRankVO;
import com.lucky.api.controller.admin.vo.MetricsVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.OrderServer;
import com.lucky.domain.valueobject.ConsumeRank;
import com.lucky.domain.valueobject.Metrics;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
/**
 * 后台用户
 *
 * @folder API/后台/首页
 */
@RestController
@RequestMapping("/api/admin/home")
public class AdminHomeController {
    private final OrderServer orderServer;

    public AdminHomeController(OrderServer orderServer) {
        this.orderServer = orderServer;
    }

    /**
     * 指标
     */
    @RequestMapping("/metrics")
    @ResponseFormat
    public MetricsVO metrics(@RequestBody TimeDTO timeDTO) {

        Metrics metrics = orderServer.metrics(timeDTO.getStartTime(), timeDTO.getEndTime());
        return MetricsVO.getInstance(metrics);
    }

    /**
     * 消费榜单
     */
    @RequestMapping("/consume-rank")
    @ResponseFormat
    public List<ConsumeRankVO> consumeRank(@RequestBody TimeDTO timeDTO) {
        List<ConsumeRank> consumeRanks = orderServer.consumeRank(timeDTO.getStartTime(), timeDTO.getEndTime());
        return consumeRanks.stream()
                .map(ConsumeRankVO::getInstance)
                .collect(Collectors.toList());

    }

}
