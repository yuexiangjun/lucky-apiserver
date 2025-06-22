package com.lucky.domain.repository.tripartite;


import org.springframework.web.multipart.MultipartFile;

public interface HuaWeiOssRepository {
	 String upload(MultipartFile file) ;
}
