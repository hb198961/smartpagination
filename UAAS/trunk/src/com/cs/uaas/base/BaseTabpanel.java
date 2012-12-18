package com.cs.uaas.base;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;

@SuppressWarnings("serial")
public class BaseTabpanel extends Tabpanel {
	private String url;
	private Tab tab;

	public BaseTabpanel(String url, Tab tab) {
		super();
		this.url = url;
		this.tab = tab;
	}

	public void render(Map map) {
		Executions.createComponents(url, this, map);
	}

	public void render() {
		Executions.createComponents(url, this, null);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

}
