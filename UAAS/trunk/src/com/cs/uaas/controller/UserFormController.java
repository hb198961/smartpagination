package com.cs.uaas.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.databind.BindingValidateEvent;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.cs.uaas.base.CommonValidator;
import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.base.ZkComponentUtil;
import com.cs.uaas.model.User;
import com.cs.uaas.model.User.USER_GENTLE;
import com.cs.uaas.service.UserService;

@SuppressWarnings("serial")
@VariableResolver(DelegatingVariableResolver.class)
public class UserFormController extends SelectorComposer<Window> {

	Logger logger = LoggerUtil.getLogger();

	Window curWindow;

	@WireVariable
	CommonValidator commonValidator;

	@WireVariable
	UserService userService;

	private User user;

	Window userQueryWindow;

	/**
	 * zul中判断是新建用户还是编辑用户：if="${$composer.newUserFlag}"
	 */
	public boolean getNewUserFlag() {
		Object userId = Executions.getCurrent().getArg().get("userId");
		return userId == null;
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		curWindow = comp;
		//创建这个tab时把当时所在window传进来，以便关闭页签时刷新
		userQueryWindow = (Window) Executions.getCurrent().getArg().get("userQueryWindow");

		Long userId = (Long) Executions.getCurrent().getArg().get("userId");
		logger.debug("打开userForm后用户编号【" + userId + "】");
		if (userId != null) {
			user = userService.getUserById(userId);
		} else {
			user = new User();
		}
	}

	@Listen("onBindingValidate=#btn_submit")
	public void onBindingValidateSubmit(BindingValidateEvent event) {
		WrongValuesException userNameError = commonValidator.validateModelProperty(event, User.class, "self",
				"userName");
		WrongValuesException emailError = commonValidator.validateModelProperty(event, User.class, "contactInfo.self",
				"contactInfo.email");
		WrongValuesException mobileOrTelephoneError1 = commonValidator.validateModelProperty(event, User.class,
				"contactInfo.mobileOrTelephone", "contactInfo.mobile");
		WrongValuesException mobileOrTelephoneError2 = commonValidator.validateModelProperty(event, User.class,
				"contactInfo.mobileOrTelephone", "contactInfo.telephone");
		WrongValuesException passwordError = commonValidator.validateModelProperty(event, User.class,
				"confirmPasswordValidation", "confirmPassword");
		WrongValuesException otherError = commonValidator.validate(event);
		commonValidator.throwWrongValues(userNameError, emailError, mobileOrTelephoneError1, mobileOrTelephoneError2,
				passwordError, otherError);
	}

	@Listen("onClick=#btn_submit")
	public void onClickSubmit(Event event) {
		logger.debug("用户手机【" + user.getContactInfo().getMobile() + "】");
		Button b = null;
		if (user.getId() != null) {//edit user
			b = (Button) Selectors.find(userQueryWindow, "#btn_RefreshData").get(0);
		} else {//add new user
			b = (Button) Selectors.find(userQueryWindow, "#cmdBtn_userQuery").get(0);
		}
		logger.debug(b);
		userService.saveOrUpdate(user);
		Messagebox.show("操作成功！", "Information", Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				ZkComponentUtil.closeTab(curWindow);
			}
		});
		//Window topWindow = (Window) Selectors.find(this.getPage(), ZkComponentUtil.TOP_WINDOW_ID).get(0);
		//Window queryWindow = (Window) Selectors.find(topWindow, "#userQueryWindow").get(0);
		//Button b = (Button) Selectors.find(queryWindow, "#btn_RefreshData").get(0);
		Events.sendEvent(new Event("onClick", b));
	}

	@Listen("onClick=#btn_cancel")
	public void onCancel(Event event) {
		ZkComponentUtil.closeTab(curWindow);
	}

	public User getUser() {
		return user;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getSexList() {
		return new ListModelList(USER_GENTLE.values());
	}

}