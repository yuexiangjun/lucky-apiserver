package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.NoCounterEntity;
import com.lucky.domain.enumes.NoCounterType;
import com.lucky.domain.repository.NoCounterRepository;
import com.lucky.infrastructure.repository.mysql.mapper.NoCounterMapper;
import com.lucky.infrastructure.repository.mysql.po.NoCounterPO;
import com.lucky.infrastructure.repository.mysql.po.OrderPrizePO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author qiuys
 * @description 针对表【no_counter(公共表 - CRM-编号计数器)】的数据库操作Service实现
 * @createDate 2025-06-06 16:35:38
 */
@Component
public class NoCounterRepositoryImpl extends ServiceImpl<NoCounterMapper, NoCounterPO> implements NoCounterRepository {
	@Override
	public NoCounterEntity getNoCounterPo(NoCounterType type) {
		var localDate = LocalDate.now();
		var eq =  Wrappers.lambdaQuery(NoCounterPO.class)
				.eq(NoCounterPO::getBelongDay, localDate)

				.eq(NoCounterPO::getType, type.name().concat("-").concat(type.getPrefix()));
		NoCounterPO one = this.getOne(eq);
		return	NoCounterPO.toEntity( one);

	}

	@Override
	public void saveOrUpdate(NoCounterEntity entity) {

		var po = NoCounterPO.getInstance(entity);
		this.saveOrUpdate(po);
	}
}




