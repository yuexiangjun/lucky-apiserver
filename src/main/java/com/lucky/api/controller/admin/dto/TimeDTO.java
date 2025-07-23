package com.lucky.api.controller.admin.dto;

import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Setter
public class TimeDTO {
	/**
	 * 开始时间
	 */

	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;


	public static LocalDateTime parseLDT(String time) {

		if (Strings.isBlank(time))
			return null;

		var instant = Instant.parse(time);
		return instant.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
	}

	public LocalDateTime getStartTime() {
		return parseLDT(startTime);
	}

	public LocalDateTime getEndTime() {
		return parseLDT(endTime);
	}

}
