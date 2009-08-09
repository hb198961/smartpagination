package org.powerstone.smartpagination.sample;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.powerstone.smartpagination.common.PageInfo;
import org.powerstone.smartpagination.hibernate.BaseHbmPagingController;
import org.powerstone.smartpagination.hibernate.HbmPageInfo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

public class SamplePagingController extends MultiActionController {
	private BaseHibernateDao baseHibernateDao;

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BaseHbmPagingController ctrl = new BaseHbmPagingController(
				baseHibernateDao) {
			@Override
			protected PageInfo<DetachedCriteria, Order> makePageInfo(
					HttpServletRequest request) {
				HbmPageInfo pi = new HbmPageInfo();
				pi.setCountDistinctProjections("id");
				pi.setExpression(DetachedCriteria.forClass(UserModel.class));
				// pi.addOrderByAsc("id");
				return pi;
			}
		};
		ctrl.handleRequest(request, response);
		return new ModelAndView("userList", "userList", BaseHbmPagingController
				.getPageData(request));
	}

	public ModelAndView initData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List data = baseHibernateDao.findByCriteria(DetachedCriteria
				.forClass(UserModel.class));
		for (Object o : data) {
			baseHibernateDao.delete(UserModel.class, ((UserModel) o).getId());
		}

		UserModel user = null;
		for (int i = 0; i < 17; i++) {
			user = new UserModel();
			user.setBirth(new Date());
			user.setEmail("liyingquan@gmail.com" + i);
			user.setRealName("liyingquan" + i);
			user.setSex("m");
			user.setUserName("admin" + i);
			baseHibernateDao.saveOrUpdate(user);
		}
		return new ModelAndView(new RedirectView(request.getContextPath()
				+ "/list.htm"));
	}

	public void setBaseHibernateDao(BaseHibernateDao baseHibernateDao) {
		this.baseHibernateDao = baseHibernateDao;
	}
}
