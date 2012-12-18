package com.cs.uaas.controller;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.base.MyThemeProvider;
import com.cs.uaas.base.TabDuplicateException;
import com.cs.uaas.base.ZkComponentUtil;
import com.cs.uaas.base.ZkComponentUtil.OverFlowType;
import com.cs.uaas.security.AuthenticationUtil;

@SuppressWarnings("serial")
@VariableResolver(DelegatingVariableResolver.class)
public class MainController extends SelectorComposer<Window> {
	Logger logger = LoggerUtil.getLogger();

	@Wire
	Window topWindow;

	@Wire
	private West west;

	@Wire
	Listbox themeSelector;

	@Wire("#btn_Test")
	Button btnTest;

	@Override
	public void doAfterCompose(Window window) throws Exception {
		super.doAfterCompose(window);
		Assert.notNull(window);
		Assert.notNull(west);
		Assert.notNull(btnTest);

		// west.setOpen(false);

		// 选中用户习惯的theme
		for (Component comp : themeSelector.getChildren()) {
			Listitem listitem = (Listitem) comp;
			if (listitem.getValue().equals(MyThemeProvider.getTheme(Executions.getCurrent()))) {
				themeSelector.selectItem(listitem);
			}
		}
	}

	/**
	 * zul文件使用${$composer.currentUser}表达式显示用户名
	 */
	public String getCurrentUser() {
		return AuthenticationUtil.getCurrentUser();
	}

	@Listen("onSelect = #themeSelector")
	public void onSelectThemeSelector(Event event) {
		Object themeName = ((Listbox) event.getTarget()).getSelectedItem().getValue();
		logger.debug("选择的theme主题为【" + themeName + "】");
		MyThemeProvider.setTheme(Executions.getCurrent(), (String) themeName);
		Executions.sendRedirect(null);
	}

	/**
	 * 监听所有树形菜单点击动作
	 */
	@Listen("onClick = #tree treechildren treeitem treechildren treeitem treerow treecell")
	public void onClickTreecellNew(Event event) {
		logger.debug(event.getName() + "|" + event.getTarget());
		Treecell treecellSelected = (Treecell) event.getTarget();
		String treecellSelectedUrl = (String) treecellSelected.getAttribute("url");
		try {
			ZkComponentUtil.newTab(event.getTarget().getId(), treecellSelected.getLabel(), true, OverFlowType.AUTO,
					treecellSelectedUrl, null);
		} catch (TabDuplicateException e) {
			Messagebox.show(e.getMessage(), "WARN", Messagebox.OK, Messagebox.INFORMATION);
		}
		logger.debug("正在新页签中打开页面【" + treecellSelectedUrl + "】");
	}

	@Listen("onClick = #btn_Test")
	public void onClickBtnTest(Event e) {
		logger.debug(e.getTarget().getId());
		Messagebox.show("Information is pressed", "Information", Messagebox.OK, Messagebox.INFORMATION);

	}
}