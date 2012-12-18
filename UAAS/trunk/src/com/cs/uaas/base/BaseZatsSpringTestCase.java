package com.cs.uaas.base;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.zkoss.zats.mimic.DefaultZatsEnvironment;
import org.zkoss.zats.mimic.Zats;
import org.zkoss.zats.mimic.ZatsEnvironment;

import com.cs.uaas.security.AuthenticationUtil;

@ContextConfiguration(locations = { "classpath:/uaas-zats-db.xml", "classpath:/spring-xml/uaas-common.xml" })
public class BaseZatsSpringTestCase extends AbstractJUnit4SpringContextTests {
	@Autowired
	protected BaseDao baseDao;

	protected static Logger logger = LoggerUtil.getLogger();
	protected static ZatsEnvironment env;

	@BeforeClass
	public static void init() {
		env = new DefaultZatsEnvironment("./it");
		env.init("./web");
	}

	@AfterClass
	public static void end() {
		Zats.end();
	}

	@After
	public void after() {
		Zats.cleanup();
	}

	// @Before
	// public void beforeEveryTest() {
	// setCurrentUser("admin");
	// }

	protected void setCurrentUser(String userName) {
		AuthenticationUtil.setCurrentUser(userName);
	}
}
