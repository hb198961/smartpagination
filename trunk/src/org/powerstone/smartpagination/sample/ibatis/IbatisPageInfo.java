package org.powerstone.smartpagination.sample.ibatis;

import java.util.HashMap;
import java.util.Map;

import org.powerstone.smartpagination.common.PageInfo;

@SuppressWarnings("unchecked")
public class IbatisPageInfo extends PageInfo<Map, String> {
	String queryName;

	@SuppressWarnings("unchecked")
	public IbatisPageInfo() {
		super();
		super.expression = new HashMap();
	}

	@Override
	public void addOrderByAsc(String orderBy) {
		super.getOrderByList().add(orderBy + " asc");
	}

	@Override
	public void addOrderByDesc(String orderBy) {
		super.getOrderByList().add(orderBy + " desc");
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

}
