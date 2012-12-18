package com.cs.uaas.base;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Window;

public class ZkComponentUtil {
	public static final String MAIN_TABPANELS_ID = "#mainTabpanels";
	public static final String MAIN_TABS_ID = "#mainTabs";
	public static final String TOP_WINDOW_ID = "#topWindow";
	private static Logger logger = LoggerUtil.getLogger();

	public enum OverFlowType {
		AUTO("overflow:auto"), SCROLL("overflow:scroll"), HIDDEN("overflow:hidden"), VISIBLE("overflow:visible");
		private final String text;

		private OverFlowType(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	/**
	 * @param tabNew
	 * @param topPage 当前Page
	 * @throws TabDuplicateException 如果已经存在与tabNew的id相同的Tab，会导致该页签被选中，并抛出此异常
	 * 捕获此异常直接返回即可，不要继续打开Tabpanel
	 */
	protected static void newTab(Tab tabNew, Page topPage) throws TabDuplicateException {
		Assert.notNull(tabNew);
		Assert.notNull(tabNew.getId());
		Window topWindow = (Window) Selectors.find(topPage, TOP_WINDOW_ID).get(0);
		Tabs mainTabs = (Tabs) Selectors.find(topWindow, MAIN_TABS_ID).get(0);

		for (Component comp : mainTabs.getChildren()) {
			Tab tab = (Tab) comp;
			logger.debug("已存在的页签【" + tab.getId() + "|" + tab.getLabel() + "】");
			if (tab.getId().equals(tabNew.getId())) {
				String errorMsg = "打开已存在页签【" + tabNew.getId() + "|" + tab.getLabel() + "】！";
				logger.warn(errorMsg);
				tab.setSelected(true);
				throw new TabDuplicateException(errorMsg);
			}
		}
		tabNew.setSelected(true);
		mainTabs.appendChild(tabNew);
	}

	protected static void newTabpanel(Tabpanel tabpanel, Page topPage) {
		Window topWindow = (Window) Selectors.find(topPage, TOP_WINDOW_ID).get(0);
		Tabpanels mainTabpanels = (Tabpanels) Selectors.find(topWindow, MAIN_TABPANELS_ID).get(0);
		mainTabpanels.appendChild(tabpanel);
	}

	/**
	 * 打开一个新页签
	 * @param tabId
	 * @param tabLable
	 * @param closeable
	 * @param overFlowType
	 * @param tabPanelUrl
	 * @param map
	 * @return
	 * @throws TabDuplicateException
	 */
	@SuppressWarnings("rawtypes")
	public static BaseTabpanel newTab(String tabId, String tabLable, boolean closeable, OverFlowType overFlowType,
			String tabPanelUrl, Map map) throws TabDuplicateException {
		Page currentPage = ExecutionsCtrl.getCurrentCtrl().getCurrentPage();
		Tab tabNew = new Tab();
		tabNew.setId("tab_" + tabId);
		tabNew.setClosable(closeable);
		tabNew.setLabel(tabLable);
		newTab(tabNew, currentPage);

		BaseTabpanel tabpanel = new BaseTabpanel(tabPanelUrl, tabNew);
		tabpanel.setTab(tabNew);
		tabpanel.setStyle(overFlowType.getText());
		newTabpanel(tabpanel, currentPage);
		tabpanel.render(map);

		return tabpanel;
	}

	/**
	 * 关闭Tab页签及其Tabpanel
	 * @param tabpanelWindow 要关闭的Tab对应Tabpanel对应的页面的顶层Window
	 * @throws TabCloseFailureException
	 */
	public static void closeTab(Window tabpanelWindow) throws TabCloseFailureException {
		Assert.notNull(tabpanelWindow);
		Component tabpanelComp = tabpanelWindow.getParent();
		if (!BaseTabpanel.class.isAssignableFrom(tabpanelComp.getClass())) {
			String errorMsg = "触发关闭页签的组件所在容器【" + tabpanelComp.getClass() + "】非法，应该为" + BaseTabpanel.class;
			logger.error(errorMsg);
			throw new TabCloseFailureException(errorMsg);
		}
		BaseTabpanel tabpanelToClose = (BaseTabpanel) tabpanelComp;
		Tab tabToClose = tabpanelToClose.getTab();
		logger.debug("正在关闭页签【" + tabToClose.getId() + "|" + tabToClose.getLabel() + "】");
		int tabIndex = tabToClose.getIndex();
		Tabs tabs = (Tabs) tabToClose.getParent();
		Tab nextTab = null;
		if (tabs.getLastChild() != tabToClose) {// 非最后的页签，激活右边下一个页签
			nextTab = (Tab) tabs.getChildren().get(tabIndex + 1);
		} else {
			if (tabs.getChildren().size() > 1) {// 是最后一个页签且左边有页签
				nextTab = (Tab) tabs.getChildren().get(tabIndex - 1);
			}
		}
		if (nextTab != null) {
			nextTab.setSelected(true);
		}
		tabpanelWindow.detach();
		tabToClose.detach();
		tabpanelToClose.detach();
	}
}