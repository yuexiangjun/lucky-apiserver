package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.AdminUserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("admin_user")
public class AdminUserPO {
	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 是否启用
	 */
	private Boolean enabled;
	/**
	 * 最后一次登录时间
	 */
	private LocalDateTime lastLoginTime;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	public static AdminUserPO getInstance(AdminUserEntity entity){
		if (Objects.isNull(entity))
			return null;
		return BeanUtil.toBean(entity,AdminUserPO.class);
	}
	public static AdminUserEntity toEntity(AdminUserPO po){
		if (Objects.isNull(po))
			return null;
		return BeanUtil.toBean(po,AdminUserEntity.class);
	}


}
