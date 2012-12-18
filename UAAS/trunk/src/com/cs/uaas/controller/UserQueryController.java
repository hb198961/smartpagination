package com.cs.uaas.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.cs.uaas.base.BasePagingListModel;
import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.base.TabCloseFailureException;
import com.cs.uaas.base.TabDuplicateException;
import com.cs.uaas.base.ZkComponentUtil;
import com.cs.uaas.base.ZkComponentUtil.OverFlowType;
import com.cs.uaas.model.User;
import com.cs.uaas.service.UserService;

@SuppressWarnings("serial")
@VariableResolver(DelegatingVariableResolver.class)
public class UserQueryController extends SelectorComposer<Window> {
	Logger logger = LoggerUtil.getLogger();
	private Window window;

	private final int _pageSize = 10;
	@Wire
	private Textbox userNameTextbox;
	@Wire
	private Textbox emailTextbox;
	@Wire
	private Textbox realNameTextbox;
	@Wire
	private Listbox gentleListbox;
	// TODO: MVVM Data Binding and Validation

	@Wire
	Paging userPaging;
	@Wire
	private Listbox dataListbox;

	@Wire("#btn_Reset")
	private Button btnReset;

	@WireVariable
	UserService userService;

	private BasePagingListModel<User, User> modelCache;

	private User queryModel = new User();

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		Assert.notNull(userService);
		Assert.notNull(btnReset);

		window = comp;
		btnReset.setDisabled(true);
		logger.debug("分页模式：" + dataListbox.getPagingChild().getMold());

		dataListbox.setPageSize(_pageSize);
		userPaging.setPageSize(_pageSize);
		queryPageData(0);// 第一次访问，查询第一页

		gentleListbox.appendItem("女", User.USER_GENTLE.female.toString());
		gentleListbox.appendItem("男", User.USER_GENTLE.male.toString());
	}

	@Listen("onDetailUser=#userQueryWindow")
	public void onDetailUser(ForwardEvent event) {
		logger.debug("event data:" + event.getData());
		Long userId = (Long) event.getData();
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("userId", userId);
		Window window = (Window) Executions.createComponents("/pages/admin/user/userDetailDialog.zul", null, map);
		window.doModal();
	}

	@Listen("onEditUser=#userQueryWindow")
	public void onEditUser(ForwardEvent event) throws TabDuplicateException {
		logger.debug("event data:" + event.getData());
		Long userId = (Long) event.getData();
		User user = userService.getUserById(userId);

		String url = ((Button) event.getOrigin().getTarget()).getAttribute("newUserUrl").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getId());
		map.put("userQueryWindow", window);//创建tab时把当前window传进去，以便关闭页签时刷新
		ZkComponentUtil.newTab("userEdit" + user.getId(), "编辑[" + user.getUserName() + "]", true, OverFlowType.AUTO,
				url, map);
	}

	@Listen("onNewUser=#userQueryWindow")
	public void onClickBtnNewUser(ForwardEvent event) {
		try {
			String url = ((Button) event.getOrigin().getTarget()).getAttribute("newUserUrl").toString();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userQueryWindow", window);//创建tab时把当前window传进去，以便关闭页签时刷新
			ZkComponentUtil.newTab("newUser", "新建用户", true, OverFlowType.AUTO, url, map);
		} catch (TabDuplicateException e) {
			Messagebox.show("不能重复打开页签【tab_newUser|】！", "WARN", Messagebox.OK, Messagebox.INFORMATION);
		}
	}

	@Listen("onQuery=#userQueryWindow")
	public void onClickQueryBtn(Event event) {
		bindQueryParameter();
		if (modelCache != null) {
			modelCache.setOrderBy(null);
		}
		queryPageData(0);
		userPaging.setActivePage(0);
	}

	@Listen("onChange=textbox")
	public void onChangeCriteria(Event event) {
		btnReset.setDisabled(false);
	}

	@Listen("onReset=#userQueryWindow")
	public void onClickBtnReset(Event event) {
		for (Component comp : Selectors.find(window, "textbox")) {
			Textbox tb = (Textbox) comp;
			tb.setValue(null);
		}
		btnReset.setDisabled(true);
		if (modelCache != null) {
			modelCache.setOrderBy(null);
		}
		dataListbox.getPaginal().setActivePage(0);
		userPaging.setActivePage(0);
		queryPageData(0);
	}

	@Listen("onPaging=#userPaging")
	public void onPaging(PagingEvent pe) {
		logger.debug("goto page " + pe.getActivePage());
		queryPageData(pe.getActivePage());
	}

	@Listen("onClose=#userQueryWindow")
	public void onClickBtnCloseTab(Event event) throws TabCloseFailureException {
		ZkComponentUtil.closeTab(window);
	}

	@Listen("onRefresh=#userQueryWindow")
	public void onClickBtnRefreshData(Event event) throws TabCloseFailureException {
		queryPageData(userPaging.getActivePage());
	}

	private void queryPageData(int activePage) {
		String orderBy = null;
		Boolean orderByAsc = null;
		if (modelCache != null) {
			orderBy = modelCache.getOrderBy();
			orderByAsc = modelCache.getOrderByAsc();
		}
		BasePagingListModel<User, User> model = userService.findPagingUser(activePage, _pageSize, queryModel, orderBy,
				orderByAsc);
		modelCache = model;
		model.setMultiple(true);
		dataListbox.setModel(model);
		userPaging.setTotalSize(model.getTotalSize());
	}

	private void bindQueryParameter() {
		queryModel = new User();
		queryModel.setUserName(userNameTextbox.getValue());
		queryModel.setRealName(realNameTextbox.getValue());
		queryModel.getContactInfo().setEmail(emailTextbox.getValue());
		String gentleValue = (String) (gentleListbox.getSelectedItem() == null ? null : gentleListbox.getSelectedItem()
				.getValue());
		queryModel.setGentle(gentleValue == null ? null : User.USER_GENTLE.valueOf(gentleValue));
	}
}