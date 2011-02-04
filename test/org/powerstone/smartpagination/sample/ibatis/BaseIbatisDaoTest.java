package org.powerstone.smartpagination.sample.ibatis;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.powerstone.smartpagination.common.PageResult;
import org.powerstone.smartpagination.hibernate.BaseHibernateDao;
import org.powerstone.smartpagination.ibatis.BaseIbatisDao;
import org.powerstone.smartpagination.ibatis.IbatisPageInfo;
import org.powerstone.smartpagination.ibatis.UserModelIbatisQuery;
import org.powerstone.smartpagination.sample.UserModel;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public class BaseIbatisDaoTest extends AbstractTransactionalDataSourceSpringContextTests {
	Logger log = Logger.getLogger(getClass());
	private BaseHibernateDao baseHibernateDao;
	private BaseIbatisDao baseIbatisDao;

	public void setBaseIbatisDao(BaseIbatisDao baseIbatisDao) {
		this.baseIbatisDao = baseIbatisDao;
	}

	public void setBaseHibernateDao(BaseHibernateDao baseHibernateDao) {
		this.baseHibernateDao = baseHibernateDao;
	}

	private UserModel user;

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath:spring-common.xml" };
	}

	@Override
	protected void onSetUpInTransaction() throws Exception {
		logger.debug(super.applicationContext.getBean("baseHibernateDao"));
		for (int i = 0; i < 17; i++) {
			user = new UserModel();
			user.setBirth(new Date());
			user.setEmail(i + "liyingquan@gmail.com");
			user.setRealName(i + "liyingquan");
			user.setSex("m");
			user.setUserName(i + "admin");
			baseHibernateDao.saveOrUpdate(user);
		}
		baseHibernateDao.flush();
	}

	@SuppressWarnings("unchecked")
	public void testFindByPage() {
		UserModelIbatisQuery query = new UserModelIbatisQuery();

		query.setEmail("liyingquan@gmail.com");
		query.setUserName("admin");
		query.setUserNameLike(true);

		IbatisPageInfo pi = (IbatisPageInfo) query.generatePageInfo();
		pi.addOrderByAsc("email");
		pi.addOrderByDesc("user_Name");
		pi.setPageNo(1);
		pi.setPageSize(10);

		PageResult pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first result", "0liyingquan@gmail.com", ((UserModel) pageResult
				.getPageData().get(0)).getEmail());

		pi.setOrderByList(new ArrayList());
		pi.addOrderByDesc("email");// OrderBy EXCEPTIONCODE desc
		pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第1页", 10, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
		Assert.assertEquals("first user", "9liyingquan@gmail.com", ((UserModel) pageResult
				.getPageData().get(0)).getEmail());

		pi.setPageNo(2);
		pi.setPageSize(10);
		pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第2页", 7, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
	}

	public void testFindByPage_NoOrderBy() {
		UserModelIbatisQuery query = new UserModelIbatisQuery();
		IbatisPageInfo pi = (IbatisPageInfo) query.generatePageInfo();
		pi.setPageNo(2);
		pi.setPageSize(10);

		PageResult pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第2页", 7, pageResult.getPageData().size());
		Assert.assertEquals("页数", 2, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
	}

	public void testFindByPageQuery_LessThan1Page() {
		UserModelIbatisQuery query = new UserModelIbatisQuery();
		query.setUserName("liyingquan@gmail.com");
		query.setUserName("admin");
		query.setUserNameLike(true);

		IbatisPageInfo pi = (IbatisPageInfo) query.generatePageInfo();
		pi.addOrderByAsc("email");
		pi.setPageNo(1);
		pi.setPageSize(50);

		PageResult pageResult = baseIbatisDao.findByPage(pi);
		Assert.assertEquals("第1页", 17, pageResult.getPageData().size());
		Assert.assertEquals("页数", 1, pageResult.getPageAmount());
		Assert.assertEquals("总记录数", 17, pageResult.getTotalRecordsNumber());
	}
}
