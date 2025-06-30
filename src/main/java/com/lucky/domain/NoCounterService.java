package com.lucky.domain;

import com.lucky.domain.entity.NoCounterEntity;
import com.lucky.domain.enumes.NoCounterType;
import com.lucky.domain.repository.NoCounterRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
@Slf4j
@Component
public class NoCounterService {
	private final NoCounterRepository noCounterRepository;

	public NoCounterService(NoCounterRepository noCounterRepository) {
		this.noCounterRepository = noCounterRepository;
	}

	public String nextNo(NoCounterType type ) {
		var now = LocalDate.now();
		var lock = new ReentrantLock();
		try {
			lock.lock();
			var entity = noCounterRepository.getNoCounterPo(type);
			if (Objects.isNull(entity)) {
				entity = entity.builder()
						.value(0)
						.belongDay(now)
						.type(Strings.isBlank(type.getPrefix()) ? type.name() : type.name().concat("-").concat(type.getPrefix()))
						.build();
			}
			var result = getNextNo(entity, type);
			this.noCounterRepository.saveOrUpdate(entity);
			return result;
		} finally {
			if (Objects.nonNull(lock) && lock.isLocked()) {
				try {
					lock.unlock();
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
	}


	public String getNextNo(NoCounterEntity noCounterPo, NoCounterType type) {

		var belongDay = noCounterPo.getBelongDay();

		var formatter = DateTimeFormatter.ofPattern("yyyy-MMdd"); // 定义格式
		var formattedDate = belongDay.format(formatter); // 格式化日期

		return type.getPrefix()
				.concat(
						formattedDate.concat("-")
								.concat(noCounterPo.getValue().toString()
								)
				);
	}
}
