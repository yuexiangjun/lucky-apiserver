package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Lottery {
	/**
	 * 系列id
	 */
	private Long topicId;
	/**
	 * 场次id
	 */
	private Long sessionId;
	


}
