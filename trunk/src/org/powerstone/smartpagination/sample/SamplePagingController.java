package org.powerstone.smartpagination.sample;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.powerstone.smartpagination.common.PageInfo;
import org.powerstone.smartpagination.common.PageResult;
import org.powerstone.smartpagination.hibernate.BaseHbmPagingController;
import org.powerstone.smartpagination.hibernate.HbmPageInfo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class SamplePagingController extends MultiActionController {
	protected ModelAndView test(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseHbmPagingController ctrl = new BaseHbmPagingController() {
			@Override
			public PageResult findByExpressionPaging(
					PageInfo<DetachedCriteria, Order> pageInfo) {
				log.debug(pageInfo);
				PageResult pr = new PageResult();
				pr.setPageAmount(11);
				pr.setTotalRecordsNumber(101);
				pr.setPageData(new ArrayList());
				for (int i = 0; i < 10; i++) {
					pr.getPageData().add(i);
				}
				return pr;
			}

			@Override
			protected PageInfo<DetachedCriteria, Order> makePageInfo(
					HttpServletRequest request) {
				HbmPageInfo pi = new HbmPageInfo();
				pi.setEntityClass(this.getClass());
				pi.setExpression(DetachedCriteria.forClass(this.getClass()));
				pi.addOrderByAsc(request.getParameter("pOrder"));
				return pi;
			}

		};
		ctrl.handleRequest(request, response);
		return new ModelAndView("usersList");
	}
}
