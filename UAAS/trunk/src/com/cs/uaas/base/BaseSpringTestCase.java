package com.cs.uaas.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.cs.uaas.security.AuthenticationUtil;

@ContextConfiguration(locations = { "classpath:/spring-xml/uaas-db.xml", "classpath:/spring-xml/uaas-common.xml" })
public class BaseSpringTestCase extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	protected BaseDao baseDao;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public BaseSpringTestCase() {

	}

	protected void setCurrentUser(String userName) {
		AuthenticationUtil.setCurrentUser(userName);
	}
}