package com.lucky.application.tripartite;


import com.lucky.domain.tripartite.HuaWeiOssService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class HuaWeiOssServer {
	private final HuaWeiOssService huaWeiOssService;

	public HuaWeiOssServer(HuaWeiOssService huaWeiOssService) {
		this.huaWeiOssService = huaWeiOssService;
	}


	/**
	 * 上传图片
	 *
	 * @param file
	 * @return
	 */
	public String upload(MultipartFile file) {
		return huaWeiOssService.upload(file);
	}

}
