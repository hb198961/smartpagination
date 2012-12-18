package com.cs.uaas.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.operation.PagingAgent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Paging;

import com.cs.uaas.base.BaseZatsSpringTestCase;
import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.model.User;
import com.cs.uaas.service.UserService;

public class UserQueryControllerTest extends BaseZatsSpringTestCase {
	Logger logger = LoggerUtil.getLogger();
	private final User user = new User();

	@Resource
	UserService userService;

	@Before
	public void beforeEveryTest() {
		baseDao.getJdbcTemplate().update("delete from user");
	}

	private void initUser() {
		user.setUserName("userNameInit");
		user.getContactInfo().setEmail("add@asd.com");
		user.setRealName("姓名Init");
		user.setGentle(User.USER_GENTLE.male);
		user.setPassword("password");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -10);
		user.setBirthday(cal.getTime());
		user.setCrtUser("UT");
		user.setLastUpdateUser("UT");
		userService.saveOrUpdate(user);
		baseDao.flush();
	}

	@Test
	public void test分页查询用户信息() {
		userService.generateUserData(48);// 默认pageSize 0

		Client client = env.newClient();
		DesktopAgent desktop = client.connect("/pages/admin/user/userQuery.zul");

		Listbox dataListbox = desktop.query("#dataListbox").as(Listbox.class);
		ComponentAgent pagingAgent = desktop.query("#userPaging");
		Paging userPaging = pagingAgent.as(Paging.class);

		logger.debug("刚进入页面时，总记录数：" + userPaging.getTotalSize());
		Assert.assertEquals(48, userPaging.getTotalSize());

		ComponentAgent userQueryBtn = desktop.query("#cmdBtn_userQuery");
		userQueryBtn.click();
		logger.debug("无查询条件时，总记录数：" + userPaging.getTotalSize());
		Assert.assertEquals("无查询条件时，总记录数", 48, userPaging.getTotalSize());

		pagingAgent.as(PagingAgent.class).moveTo(4);
		Assert.assertEquals(48, userPaging.getTotalSize());
		Assert.assertEquals(4, userPaging.getActivePage());
		Assert.assertEquals(5, userPaging.getPageCount());
		Assert.assertEquals(8, dataListbox.getModel().getSize());

		desktop.query("#userNameTextbox").type("anyThing");
		userQueryBtn.click();
		logger.debug("带查询条件时，总记录数：" + userPaging.getTotalSize());
		Assert.assertEquals(0, userPaging.getTotalSize());
	}

	@Test
	public void test打开一个用户编辑页面() {
		initUser();

		Client client = env.newClient();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getId());
		logger.debug("用户编号【" + user.getId() + "】");
		User userModel = (User) baseDao.find("from User u where u.id=?", user.getId()).get(0);
		logger.debug(userModel);

		DesktopAgent desktop = client.connectAsIncluded("/pages/admin/user/userForm.zul", map);
		Longbox longbox = desktop.query("#userId").as(Longbox.class);
		Assert.assertEquals("用户编号：", user.getId(), longbox.getValue());
		Label userNameLabel = desktop.query("#userNameLabel").as(Label.class);
		Assert.assertEquals("用户名：", "userNameInit", userNameLabel.getValue());
	}
}