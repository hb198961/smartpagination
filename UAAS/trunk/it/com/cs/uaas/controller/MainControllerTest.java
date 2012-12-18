package com.cs.uaas.controller;

import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;
import org.zkoss.zats.mimic.Client;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import com.cs.uaas.base.BaseZatsSpringTestCase;

public class MainControllerTest extends BaseZatsSpringTestCase {

	@Test
	public void test点击菜单项打开页签() {
		Client client = env.newClient();
		DesktopAgent desktop = client.connect("/pages/main.zul");

		ComponentAgent menuUserManagement = desktop.query("#treemenu_userManagement");
		logger.debug("menuAgent:" + menuUserManagement);
		logger.debug(desktop.query("#north"));

		ComponentAgent tabs = desktop.query("#mainTabs");
		ComponentAgent tabpanels = desktop.query("#mainTabpanels");
		Assert.assertEquals("最开始有一个欢迎tab", 1, tabs.getChildren().size());
		menuUserManagement.click();
		Assert.assertEquals("点击open菜单，打开一个新的tab", 2, tabs.getChildren().size());
		Assert.assertEquals("点击open菜单，打开一个新的tabpanel", 2, tabpanels.getChildren().size());

		menuUserManagement.click();
		Assert.assertEquals("重复点击open菜单，不会打开一个新的tab，只会选中之前已打开的tab", 2, tabs.getChildren().size());
		Assert.assertEquals("重复点击open菜单，不会打开一个新的tabpanel", 2, tabpanels.getChildren().size());
		Tab newTab = (Tab) tabs.as(Tabs.class).getLastChild();
		assertTrue(newTab.isSelected());
		Assert.assertEquals("重复点击open菜单，不会打开一个新的tab，只会选中之前已打开的tab", "tab_treemenu_userManagement", newTab.getId());
	}

	@Test
	public void test关闭页签() {
		Client client = env.newClient();
		DesktopAgent desktop = client.connect("/pages/main.zul");
		ComponentAgent menuUserManagement = desktop.query("#treemenu_userManagement");
		menuUserManagement.click();
		ComponentAgent tabpanelsAgent = desktop.query("#mainTabpanels");
		Assert.assertEquals("点击open菜单，打开一个新的tabpanel", 2, tabpanelsAgent.getChildren().size());
		ComponentAgent tabpanelAgent = tabpanelsAgent.getChildren().get(1);
		ComponentAgent btnCloseTabAgent = tabpanelAgent.getChild(0).query("#btn_CloseTab");
		logger.debug("btnCloseTab:" + btnCloseTabAgent);
		btnCloseTabAgent.click();// 关闭页签

		Tabpanels tabpanels = tabpanelsAgent.as(Tabpanels.class);
		Assert.assertEquals("点击关闭页签按钮后，tabpanel数量", 1, tabpanels.getChildren().size());
	}
}
