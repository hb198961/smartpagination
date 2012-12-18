package com.cs.uaas.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.model.Resource;
import com.cs.uaas.model.SubSystem;
import com.cs.uaas.service.ResourceService;

@SuppressWarnings("serial")
@VariableResolver(DelegatingVariableResolver.class)
public class ResourceController extends SelectorComposer<Window> {
	Logger logger = LoggerUtil.getLogger();

	@WireVariable
	ResourceService resourceService;

	@Wire
	Listbox subSystem;

	@Wire
	Grid dataGrid;

	// data binding to query form
	private final Resource resource = new Resource();

	public Resource getResource() {
		return resource;
	}

	public ListModelList<SubSystem> getSubsystemList() {
		List<SubSystem> allSubSystem = new ArrayList<SubSystem>();
		SubSystem emptyItm = new SubSystem();
		emptyItm.setSubSystemName("请选择");
		allSubSystem.add(emptyItm);
		allSubSystem.addAll(resourceService.getAllSubSystem());
		return new ListModelList<SubSystem>(allSubSystem);
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		List<SubSystem> allSubSystem = resourceService.getAllSubSystem();
		for (SubSystem ss : allSubSystem) {
			subSystem.appendItem(ss.getSubSystemName(), ss.getId().toString());
		}
		// subSystem.sel;
		// 页面就不需绑定了：<listbox id="subSystem"
		// selectedItem="@{$composer.resource.subSystem, save-when='cmdBtn_resourceQuery.onClick'}"
		// subSystem.setModel(new
		// ListModelList<SubSystem>(resourceService.getAllSubSystem(), false));
		doQuery();
	}

	@Listen("onClick=#cmdBtn_resourceQuery")
	public void onClickQueryBtn(Event event) {
		doQuery();
	}

	public void doQuery() {
		logger.debug("页面提交得到的resourceName：" + resource.getResourceName());
		Listitem item = subSystem.getSelectedItem();
		if (item != null && item.getValue() != null) {
			logger.debug("页面提交得到的resource.subSystem.id:" + item.getValue());
			SubSystem subSystemSelected = new SubSystem();
			subSystemSelected.setId(Long.valueOf(item.getValue().toString()));
			resource.setSubSystem(subSystemSelected);
		} else {
			resource.setSubSystem(null);
		}
		List<Resource> resourceTempList = resourceService.findResource(resource);
		logger.debug("查询到资源的个数为：" + resourceTempList.size());
		dataGrid.setModel(new ListModelList<Resource>(resourceTempList));
	}

}