package com.lucky.domain.tripartite;


import com.lucky.domain.repository.tripartite.HuaWeiOssRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class HuaWeiOssService {
	private final HuaWeiOssRepository huaWeiOssRepository;

	public HuaWeiOssService(HuaWeiOssRepository huaWeiOssRepository) {
		this.huaWeiOssRepository = huaWeiOssRepository;
	}

	/**
	 * 上传图片
	 *
	 * @param file
	 * @return
	 */
	public String upload(MultipartFile file) {
		return huaWeiOssRepository.upload(file);
	}

}
