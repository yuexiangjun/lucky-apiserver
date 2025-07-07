package com.lucky.domain;

import com.lucky.domain.entity.SeriesTopicEntity;
import com.lucky.domain.repository.SeriesTopicRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 主题 系列
 */
@Component
public class SeriesTopicService {
	private final SeriesTopicRepository seriesTopicRepository;

	public SeriesTopicService(SeriesTopicRepository seriesTopicRepository) {
		this.seriesTopicRepository = seriesTopicRepository;
	}

	/**
	 * 添加/修改
	 */
	public Long saveOrUpdate(SeriesTopicEntity entity) {
		return seriesTopicRepository.saveOrUpdate(entity);
	}

	/**
	 * 删除
	 */
	public Boolean deleteById(Long id) {
		return seriesTopicRepository.deleteById(id);
	}

	/**
	 * 列表
	 */
	public List<SeriesTopicEntity> findByList(SeriesTopicEntity seriesTopicEntity) {
		return seriesTopicRepository.findByList(seriesTopicEntity);
	}

	public SeriesTopicEntity findById(Long id) {
		return seriesTopicRepository.findById(id);
	}

	public List<SeriesTopicEntity> findByIds(List<Long> topicIds) {
		return seriesTopicRepository.findByIds(topicIds);
	}

	public List<SeriesTopicEntity> getIdByName(String seriesName) {

		List<SeriesTopicEntity> byName = seriesTopicRepository.getByName(seriesName);
		if (Strings.isNotBlank(seriesName) && CollectionUtils.isEmpty(byName)) {
			SeriesTopicEntity seriesTopicEntity = new SeriesTopicEntity();
			seriesTopicEntity.setId(-1l);

			return List.of(seriesTopicEntity);
		}
		return byName;

	}
}
