package com.lucky.domain;

import com.lucky.domain.entity.DeliveryAddressEntity;
import com.lucky.domain.repository.DeliveryAddressRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 收货地址信息
 */
@Component
public class DeliveryAddressService {
	private final DeliveryAddressRepository deliveryAddressRepository;

	public DeliveryAddressService(DeliveryAddressRepository deliveryAddressRepository) {
		this.deliveryAddressRepository = deliveryAddressRepository;
	}


	/**
	 * 添加修改
	 *
	 * @param entity
	 * @return
	 */
	public Long saveOrUpdate(DeliveryAddressEntity entity) {
		return deliveryAddressRepository.saveOrUpdate(entity);
	}


	/**
	 * 根据id查询
	 */
	public DeliveryAddressEntity getById(Long id) {
		return deliveryAddressRepository.getById(id);
	}


	/**
	 * 更据id删除
	 */
	public Boolean deleteById(Long id) {
		return deliveryAddressRepository.deleteById(id);
	}

	/**
	 * 更据用户查询
	 */
	public List<DeliveryAddressEntity> getByWechatUserId(Long wechatUserId) {
		return deliveryAddressRepository.getByWechatUserId(wechatUserId);
	}

	/**
	 * 修改默认地址
	 */
	public Boolean updateDefault(Long id, Long wechatUserId) {
		var deliveryAddressEntities = deliveryAddressRepository.getByWechatUserId(wechatUserId);

		var addressEntities = deliveryAddressEntities.stream()

				.filter(s -> Objects.equals(s.getId(), id) || s.getIsDefault())
				.peek(s -> {
					if (Objects.equals(s.getId(), id))
						s.setIsDefault(true);
					else
						s.setIsDefault(false);

				}).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(addressEntities))
			return true;
		return deliveryAddressRepository.saveOrUpdateBatch(addressEntities);

	}

}
