package com.hah.excel.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import com.hah.excel.model.StudentInfo;

@SuppressWarnings("deprecation")
public class ExcelProcessorTest extends AbstractTransactionalDataSourceSpringContextTests {
	protected ExcelProcessor excelProcessor;

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath:spring-common.xml" };
	}

	public void testCrud() {
		StudentInfo si = new StudentInfo();
		si.setName("李英权");
		si.setBirth("20011002");
		si.setAddress("曹杨路623弄");
		si.setParentName("李刚");
		si.setPhone1("13817709037");
		si.generateHash();
		excelProcessor.saveOrUpdate(si);
		excelProcessor.flush();

		logger.debug("StudentInfo[id:" + si.getId() + "|HashOfNameAndBirth:"
				+ si.getHashOfNameAndBirth() + "]");
		assertNotNull(si.getId());
		assertEquals("select count(*) from student_info", 1, this.jdbcTemplate
				.queryForInt("select count(*) from student_info"));
	}

	@SuppressWarnings("unchecked")
	public void testStoreExcelWithoutDuplicate() throws Exception {
		InputStream is = new FileInputStream(new ClassPathResource("/hahTest.xls")
				.getFile());
		List<StudentInfo> result = excelProcessor.storeExcelWithoutDuplicate(is);
		assertTrue(result.size() == 0);
		is.close();

		is = new FileInputStream(new ClassPathResource("/hahTest.xls").getFile());
		result = excelProcessor.storeExcelWithoutDuplicate(is);
		assertTrue(result.size() == 2);

		List<StudentInfo> list = excelProcessor.findByCriteria(DetachedCriteria
				.forClass(StudentInfo.class));
		assertTrue(list.size() == 2);
		for (StudentInfo si : list) {
			logger.debug("StudentInfo[name:" + si.getName() + "|HashOfNameAndBirth:"
					+ si.getHashOfNameAndBirth() + "]");
		}
	}

	// public void testRenderExcel() {
	// fail("Not yet implemented");
	// }

	public void setExcelProcessor(ExcelProcessor excelProcessor) {
		this.excelProcessor = excelProcessor;
	}
}
