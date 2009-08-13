package org.powerstone.smartpagination.sample;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.powerstone.smartpagination.common.PageInfo;
import org.powerstone.smartpagination.common.PageQuery;
import org.powerstone.smartpagination.hibernate.HbmPageInfo;

public class UserModelQuery extends UserModel implements PageQuery<DetachedCriteria, Order> {
	private boolean userNameLike;

	public boolean isUserNameLike() {
		return userNameLike;
	}

	public void setUserNameLike(boolean userNameLike) {
		this.userNameLike = userNameLike;
	}

	@Override
	public PageInfo<DetachedCriteria, Order> generatePageInfo() {
		HbmPageInfo pi = new HbmPageInfo();
		DetachedCriteria dc = DetachedCriteria.forClass(UserModel.class);
		if (super.getUserName() != null && super.getUserName().trim().length() > 0) {
			if (userNameLike) {
				dc.add(Restrictions.like("userName", "%" + super.getUserName() + "%"));
			} else {
				dc.add(Restrictions.eq("userName", super.getUserName()));
			}
		}
		if (super.getEmail() != null && super.getEmail().trim().length() > 0) {
			dc.add(Restrictions.like("email", "%" + super.getEmail() + "%"));
		}
		if (super.getRealName() != null && super.getRealName().trim().length() > 0) {
			dc.add(Restrictions.like("realName", "%" + super.getRealName() + "%"));
		}
		if (super.getSex() != null && super.getSex().trim().length() > 0) {
			dc.add(Restrictions.eq("sex", super.getSex()));
		}
		pi.setExpression(dc);
		pi.setCountDistinctProjections("id");

		return pi;
	}

}