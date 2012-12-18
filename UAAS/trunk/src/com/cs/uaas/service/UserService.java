package com.cs.uaas.service;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.cs.uaas.base.BaseDao;
import com.cs.uaas.base.BasePagingListModel;
import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.model.User;

@Service
public class UserService {

	Logger logger = LoggerUtil.getLogger();
	@Resource
	BaseDao baseDao;

	public Long saveOrUpdate(User userModel) {
		baseDao.saveOrUpdate(userModel);
		return userModel.getId();
	}

	public User getUserById(Long id) {
		return baseDao.get(User.class, id);
	}

	/**
	 * 启动spring容器后，初始化一些User数据
	 */
	@PostConstruct
	public void initUserData() {
		baseDao.getJdbcTemplate().update("delete from user");
		generateUserData(105);
	}

	public int generateUserData(int count) {
		for (int j = 1; j <= count; j++) {
			User userModel = new User();
			userModel.setUserName("userName" + j);
			userModel.getContactInfo().setEmail("add" + j + "@asd.com");
			userModel.setRealName("姓名" + j);
			userModel.setGentle(User.USER_GENTLE.male);
			userModel.setPassword("password");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -10);
			userModel.setBirthday(cal.getTime());
			userModel.setCrtUser("UT");
			userModel.setLastUpdateUser("UT");
			baseDao.saveOrUpdate(userModel);
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public boolean validateEmailExisting(String email, Long id) {
		DetachedCriteria dc = DetachedCriteria.forClass(User.class);
		dc.add(Restrictions.eq("contactInfo.email", email));

		List<User> resultList = baseDao.findByCriteria(dc);
		int size = resultList.size();
		logger.debug("从DBload出【" + size + "】");
		if (size == 0) {
			return true;
		} else if (size == 1) {
			return resultList.get(0).getId().equals(id);
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean validateUserNameExisting(String userName, Long id) {
		DetachedCriteria dc = DetachedCriteria.forClass(User.class);
		dc.add(Restrictions.eq("userName", userName));

		List<User> resultList = baseDao.findByCriteria(dc);
		int size = resultList.size();
		logger.debug("从DBload出【" + size + "】");
		if (size == 0) {
			return true;
		} else if (size == 1) {
			return resultList.get(0).getId().equals(id);
		} else {
			return false;
		}
	}

	@SuppressWarnings("serial")
	public BasePagingListModel<User, User> findPagingUser(int activePage, int _pageSize, User queryModel,
			String orderBy, Boolean orderByAsc) {
		return new BasePagingListModel<User, User>(activePage, _pageSize, queryModel, orderBy, orderByAsc) {
			@Override
			public int getTotalSize(User criteria) {
				logger.debug(criteria.getUserName());
				DetachedCriteria dc = constructCriteria(criteria);
				return baseDao.countRecordsNumber(dc, "id");
			}

			@SuppressWarnings("unchecked")
			@Override
			protected List<User> getPageData(int itemStartNumber, int pageSize, User criteria, String orderBy,
					Boolean orderByAsc) {
				logger.debug("Criteria:" + super.getCriteria());
				logger.debug("Order by [" + orderBy + "] orderByAsc[" + orderByAsc + "]");
				DetachedCriteria dc = constructCriteria(criteria);
				if (StringUtils.isNotEmpty(orderBy) && orderByAsc != null) {
					if (orderByAsc) {
						dc.addOrder(Order.asc(orderBy));
					} else {
						dc.addOrder(Order.desc(orderBy));
					}
				} else {
					dc.addOrder(Order.desc("id"));
				}
				return baseDao.findByCriteria(dc, itemStartNumber, pageSize);
			}

			private DetachedCriteria constructCriteria(User criteria) {
				DetachedCriteria dc = DetachedCriteria.forClass(User.class);
				if (StringUtils.isNotEmpty(criteria.getUserName())) {
					dc.add(Restrictions.ilike("userName", "%" + criteria.getUserName() + "%"));
				}
				if (StringUtils.isNotEmpty(criteria.getRealName())) {
					dc.add(Restrictions.ilike("realName", "%" + criteria.getRealName() + "%"));
				}
				if (StringUtils.isNotEmpty(criteria.getContactInfo().getEmail())) {
					dc.add(Restrictions.eq("contactInfo.email", criteria.getContactInfo().getEmail()));
				}
				if (criteria.getGentle() != null) {
					dc.add(Restrictions.eq("gentle", criteria.getGentle()));
				}
				return dc;
			}
		};
	}
}