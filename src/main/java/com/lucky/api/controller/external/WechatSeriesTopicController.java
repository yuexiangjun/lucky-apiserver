package com.lucky.api.controller.external;

import com.lucky.api.controller.admin.vo.SeriesTopicVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.SeriesTopicServer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 主题 系列
 *
 * @folder API/小程序/主题系列
 */
@RestController
@RequestMapping("/api/wechat/series-topic")
public class WechatSeriesTopicController {
	private final SeriesTopicServer topicServer;

	public WechatSeriesTopicController(SeriesTopicServer topicServer) {
		this.topicServer = topicServer;
	}


	/**
	 * 系列列表
	 */
	@PostMapping("/list")
	@ResponseFormat
	public List<SeriesTopicVO> findByList() {

		return topicServer.findByList()
				.stream()
				.map(SeriesTopicVO::getInstance)
				.collect(Collectors.toList());
	}
    /**
     * 系列详情
     */
    @PostMapping("/detail")
    @ResponseFormat
    public SeriesTopicVO findById(@RequestParam Long id) {
        return SeriesTopicVO.getInstance(topicServer.findById(id));
    }





}
