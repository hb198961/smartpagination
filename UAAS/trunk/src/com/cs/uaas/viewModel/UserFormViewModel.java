package com.cs.uaas.viewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;

import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.model.User;

public class UserFormViewModel {
	Logger logger = LoggerUtil.getLogger();
	private User user = new User();
	private String retypedPassword;

	public String getRetypedPassword() {
		return retypedPassword;
	}

	public void setRetypedPassword(String retypedPassword) {
		this.retypedPassword = retypedPassword;
	}

	public User getUser() {
		User userTemp = new User();
		userTemp.setUserName("Bill");
		userTemp.setBirthday(new Date());
		return userTemp;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Command
	public void submit() {
		logger.debug("MVVM模式下获取页面用户名【" + user.getUserName() + "】");
		logger.debug("MVVM模式下获取页面birthday【" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getBirthday())
				+ "】");
	}
}
