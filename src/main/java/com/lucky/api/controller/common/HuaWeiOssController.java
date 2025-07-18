package com.lucky.api.controller.common;

import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.tripartite.HuaWeiOssServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传
 *
 * @folder API/公共/图片上传
 */
@RestController
@RequestMapping("/api/oss")
@Slf4j
public class HuaWeiOssController {
	private final HuaWeiOssServer HuaWeiOssServer;

	public HuaWeiOssController(com.lucky.application.tripartite.HuaWeiOssServer huaWeiOssServer) {
		HuaWeiOssServer = huaWeiOssServer;
	}


	/**
	 * 上传图片
	 *
	 * @param file
	 * @return
	 */
	@PostMapping("/upload")
	@ResponseFormat
	public String upload(@RequestPart("file") MultipartFile file) {
		log.info("上传图片");

		return HuaWeiOssServer.upload(file);
	}

}
