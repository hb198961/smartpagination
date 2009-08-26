package org.powerstone.smartpagination.sample.ibatis;

import org.powerstone.smartpagination.common.PageInfo;
import org.powerstone.smartpagination.common.PageQuery;
import org.powerstone.smartpagination.sample.UserModel;

public class UserModelIbatisQuery extends UserModel implements PageQuery<IbatisOrderable, String>,
		IbatisOrderable {
	private boolean userNameLike;

	private String orderByStr;

	public boolean isUserNameLike() {
		return userNameLike;
	}

	public void setUserNameLike(boolean userNameLike) {
		this.userNameLike = userNameLike;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public PageInfo<IbatisOrderable, String> generatePageInfo() {
		IbatisPageInfo pi = new IbatisPageInfo();
		pi.setCountQueryName("countUser");
		pi.setPageQueryName("findUsers");
		UserModelIbatisQuery exampleModel = new UserModelIbatisQuery();
		exampleModel.setBirth(this.getBirth());
		if (getEmail() != null && getEmail().trim().length() > 0) {
			exampleModel.setEmail("%" + getEmail().toLowerCase() + "%");
		}
		exampleModel.setId(getId());
		if (getRealName() != null && getRealName().trim().length() > 0) {
			exampleModel.setRealName("%" + getRealName().toLowerCase() + "%");
		}
		if (getSex() != null && getSex().trim().length() > 0) {
			exampleModel.setSex(getSex());
		}
		exampleModel.setUserNameLike(isUserNameLike());
		if (isUserNameLike()) {
			exampleModel.setUserName("%" + getUserName().toLowerCase() + "%");
		} else {
			exampleModel.setUserName(getUserName());
		}

		pi.setExpression(exampleModel);
		return pi;
	}

}