package com.hah.excel.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.powerstone.smartpagination.sample.hibernate.BaseHibernateDao;

import com.hah.excel.model.StudentInfo;

public class ExcelProcessor extends BaseHibernateDao {
	/**
	 * 从excel输入流读取StudentInfo存入数据库，去除重复数据
	 * 
	 * @param inputStream
	 * @return 重复数据
	 */
	@SuppressWarnings("unchecked")
	public List<StudentInfo> storeExcelWithoutDuplicate(InputStream inputStream) {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(inputStream);
			Sheet st = wb.getSheet(0);// 得到工作薄中的第一个工作表
			// 得到当前工作表的行数
			int rowNum = st.getRows();
			List<StudentInfo> result = new ArrayList<StudentInfo>();
			for (int j = 0; j < rowNum; j++) {
				// 得到当前行的所有单元格
				Cell[] cells = st.getRow(j);
				StudentInfo si = new StudentInfo();
				si.setName(cells[0].getContents());
				si.setBirth(cells[1].getContents());
				si.setAddress(cells[2].getContents());
				si.setPhone1(cells[3].getContents());
				si.generateHash();
				List<StudentInfo> list = findByCriteria(DetachedCriteria.forClass(
						StudentInfo.class)
						.add(
								Restrictions.eq("hashOfNameAndBirth", si
										.getHashOfNameAndBirth())));
				if (list.size() > 0) {
					result.add(si);
					logger.warn("StudentInfo Duplicate[name:" + si.getName() + "|birth:"
							+ si.getBirth() + "]========");
				} else {
					this.saveOrUpdate(si);
					this.flush();
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
	}

	public void renderExcel(List<StudentInfo> data, OutputStream outputStream) {

	}
}