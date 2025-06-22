package com.lucky.domain.valueobject;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息
 *
 * @param <T> 分页数据类型
 */
@Data
@NoArgsConstructor
public class BaseDataPage<T> {
	/**
	 * 总条数
	 */
	private Long total;
	/**
	 * 总页数
	 */
	private Long pages;

	/**
	 * 分页数据
	 */
	private List<T> dataList = new ArrayList<>();

	/**
	 * 分页信息
	 *
	 * @param dataCount 数量
	 */
	public BaseDataPage(Long dataCount) {
		this.total = dataCount;
	}


	/**
	 * 返回一个 新实体对象
	 */
	public static <T> BaseDataPage<T> newInstance(Long total, Long pages, List<T> dataList) {
		BaseDataPage<T> tBaseDataPage = new BaseDataPage<>();
		tBaseDataPage.setTotal(total);
		tBaseDataPage.setPages(pages);
		tBaseDataPage.setDataList(dataList);
		return tBaseDataPage;
	}
}

