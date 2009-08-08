package org.powerstone.smartpagination.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.powerstone.smartpagination.common.BasePagingController;

abstract public class BaseHbmPagingController extends
		BasePagingController<DetachedCriteria, Order> {

}
