package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.SeriesTopicEntity;
import com.lucky.domain.repository.SeriesTopicRepository;
import com.lucky.infrastructure.repository.mysql.mapper.SeriesTopicMapper;
import com.lucky.infrastructure.repository.mysql.po.SeriesTopicPO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 主题 系列
 */
@Component
public class SeriesTopicRepositoryImpl extends ServiceImpl<SeriesTopicMapper, SeriesTopicPO> implements SeriesTopicRepository {
	private final SeriesTopicMapper seriesTopicMapper;

	public SeriesTopicRepositoryImpl(SeriesTopicMapper seriesTopicMapper) {
		this.seriesTopicMapper = seriesTopicMapper;
	}


	/**
	 * 添加/修改
	 */
	public Long saveOrUpdate(SeriesTopicEntity entity) {

		var gradePO = SeriesTopicPO.getInstance(entity);

		if (Objects.isNull(gradePO))
			return null;

		if (Objects.isNull(gradePO.getId()))
			seriesTopicMapper.insert(gradePO);
		else
			seriesTopicMapper.updateById(gradePO);
		return gradePO.getId();
	}

	/**
	 * 删除
	 */
	public Boolean deleteById(Long id) {
		int i = seriesTopicMapper.deleteById(id);
		if (i > 0)
			return true;
		else
			return false;
	}

	/**
	 * 列表
	 */
	public List<SeriesTopicEntity> findByList() {
		var wrapper = Wrappers.<SeriesTopicPO>lambdaQuery();

		return seriesTopicMapper.selectList(wrapper)
				.stream()
				.map(SeriesTopicPO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public SeriesTopicEntity findById(Long id) {
		var seriesTopicPO = seriesTopicMapper.selectById(id);

		return SeriesTopicPO.toEntity(seriesTopicPO);
	}

	@Override
	public List<SeriesTopicEntity> findByIds(List<Long> topicIds) {
		if (CollectionUtils.isEmpty(topicIds))
			return List.of();
		var wrapper = Wrappers.<SeriesTopicPO>lambdaQuery()
				.in(SeriesTopicPO::getId, topicIds);
		return seriesTopicMapper.selectList(wrapper)
				.stream()
				.map(SeriesTopicPO::toEntity)
				.collect(Collectors.toList());
	}

}
