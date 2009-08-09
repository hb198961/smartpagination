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
	public ModelAndView test(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseHbmPagingController ctrl = new BaseHbmPagingController() {
			@Override
			public PageResult findByPage(
					PageInfo<DetachedCriteria, Order> pageInfo) {
				log.debug(pageInfo);
				HbmPageInfo pi = (HbmPageInfo) pageInfo;
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
				pi.setExpression(DetachedCriteria.forClass(UserModel.class));
				pi.addOrderByAsc(request.getParameter("id"));
				pi.setCountDistinctProjections("id");
				return pi;
			}

		};
		ctrl.handleRequest(request, response);
		return new ModelAndView("usersList");
	}

	public ModelAndView initData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return test(request, response);
	}
}
