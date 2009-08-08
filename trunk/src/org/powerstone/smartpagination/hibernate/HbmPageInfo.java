package org.powerstone.smartpagination.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.powerstone.smartpagination.common.PageInfo;

public class HbmPageInfo extends PageInfo<DetachedCriteria, Order> {

	@Override
	public void addOrderByAsc(String orderBy) {
		super.getOrderBy().add(Order.asc(orderBy));
	}

	@Override
	public void addOrderByDesc(String orderBy) {
		super.getOrderBy().add(Order.desc(orderBy));
	}

}
