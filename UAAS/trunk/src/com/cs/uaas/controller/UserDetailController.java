package com.cs.uaas.controller;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.model.User;
import com.cs.uaas.service.UserService;

@SuppressWarnings("serial")
@VariableResolver(DelegatingVariableResolver.class)
public class UserDetailController extends SelectorComposer<Window> {
	Logger logger = LoggerUtil.getLogger();

	@WireVariable
	UserService userService;

	private User user;

	public User getUser() {
		Long userId = (Long) Executions.getCurrent().getArg().get("userId");
		if (userId != null) {
			user = userService.getUserById(userId);
		}
		if (user == null) {
			logger.warn("user[" + userId + "]不存在！");
			user = new User();
		}
		return user;
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
	}
}