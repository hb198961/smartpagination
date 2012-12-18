import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.cs.uaas.base.BaseSpringTestCase;
import com.cs.uaas.model.Resource;
import com.cs.uaas.model.SubSystem;
import com.cs.uaas.service.ResourceService;

public class DataGenerator extends BaseSpringTestCase {
	@Autowired
	ResourceService resourceService;

	@Before
	public void beforeEveryTest() {
		super.setCurrentUser("admin");
	}

	@Test
	@Rollback(false)
	public void initSubSystem() {
		baseDao.getJdbcTemplate().update("delete from RESOURCE");
		baseDao.getJdbcTemplate().update("delete from SUB_SYSTEM");
		SubSystem subSystem = new SubSystem();
		subSystem.setSubSystemName("UAAS");
		baseDao.saveOrUpdate(subSystem);

		SubSystem subSystemCc = new SubSystem();
		subSystemCc.setSubSystemName("CC");
		baseDao.saveOrUpdate(subSystemCc);

		baseDao.flush();

		int sysNum = baseDao.getJdbcTemplate().queryForInt("select count(*) from SUB_SYSTEM where enable_flg = 1");

		Assert.assertEquals("初始化子系统名称", 2, sysNum);
	}

	@SuppressWarnings("unchecked")
	@Test
	@Rollback(false)
	public void initResource() {
		List<SubSystem> subSystemList = baseDao
				.find("from SubSystem where enableFlg = 1 and subSystemName = ?", "UAAS");
		SubSystem subSystem = new SubSystem();
		if (subSystemList != null) {
			subSystem = subSystemList.get(0);
		}
		Resource resource = new Resource();
		resource.setSubSystem(subSystem);
		resource.setResourceName("用户管理");
		resource.setUrl("/pages/admin/user/userQuery.zul");
		baseDao.saveOrUpdate(resource);

		List<SubSystem> subSystemCCList = baseDao
				.find("from SubSystem where enableFlg = 1 and subSystemName = ?", "CC");
		SubSystem subSystemCC = new SubSystem();
		if (subSystemCCList != null) {
			subSystemCC = subSystemCCList.get(0);
		}
		Resource resourceCC = new Resource();
		resourceCC.setSubSystem(subSystemCC);
		resourceCC.setResourceName("话术管理");
		resourceCC.setUrl("/pages/sysManager/callScript/callScriptQuery.zul");
		baseDao.saveOrUpdate(resourceCC);

		baseDao.flush();

		int sysNum = baseDao.getJdbcTemplate().queryForInt("select count(*) from RESOURCE where enable_flg = 1");

		Assert.assertEquals("初始化资源", 2, sysNum);
	}

}
