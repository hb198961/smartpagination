package com.cs.uaas.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.uaas.base.BaseSpringTestCase;
import com.cs.uaas.model.Resource;
import com.cs.uaas.model.SubSystem;

public class ResourceServiceTest extends BaseSpringTestCase {
	@Autowired
	ResourceService resourceService;

	@Before
	public void beforeEveryTest() {
		super.deleteFromTables("RESOURCE", "SUB_SYSTEM");
		super.setCurrentUser("admin");
	}

	@Test
	public void testGetAllSubSystem_全部子系统() {
		for (int i = 0; i < 10; i++) {
			SubSystem subSystem = new SubSystem();
			subSystem.setSubSystemName("subSystemName" + i);
			baseDao.saveOrUpdate(subSystem);
		}
		baseDao.flush();

		int sysNum = baseDao.getJdbcTemplate().queryForInt("select count(*) from SUB_SYSTEM where enable_flg = 1");
		int resultNum = resourceService.getAllSubSystem().size();
		Assert.assertEquals("初始化子系统名称", sysNum, resultNum);
		Assert.assertEquals("初始化子系统名称", 10, sysNum);
	}

	@Test
	public void testFindResource_按条件查询资源列表() {
		SubSystem subSystem = new SubSystem();
		subSystem.setSubSystemName("UAAS");
		for (int i = 0; i < 100; i++) {
			Resource resource = new Resource();
			resource.setResourceName("resourceName" + i);
			resource.setUrl("/admin/resourceForm" + i + ".zul");
			resource.setSubSystem(subSystem);
			baseDao.saveOrUpdate(resource);
		}
		baseDao.flush();

		Resource resource = new Resource();
		resource.setSubSystem(subSystem);
		List<Resource> result = resourceService.findResource(resource);
		Assert.assertEquals("查询UAAS系统资源", 100, result.size());

		resource.setResourceName("resourceNameR");
		Assert.assertEquals("查询UAAS系统资源resourceNameR", 0, resourceService.findResource(resource).size());

		resource.setResourceName("resourceName9");
		Assert.assertEquals("查询UAAS系统资源resourceNameR", 11, resourceService.findResource(resource).size());
	}
}
