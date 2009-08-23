package org.powerstone.smartpagination.sample.ibatis;

import java.util.List;

import org.powerstone.smartpagination.common.PageResult;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class BaseIbatisDao extends SqlMapClientDaoSupport {
	@SuppressWarnings("unchecked")
	public PageResult findByPage(IbatisPageInfo pageInfo) {
		int recordsNumber = countRecordsNumber(pageInfo);

		if (pageInfo.getPageSize() <= 0) {// PageSize==0相当于取全部
			List all = findByMap(pageInfo);
			return new PageResult(all, recordsNumber, 1);// 取全部，所以页数为1
		}

		int pageAmount = (recordsNumber % pageInfo.getPageSize() > 0) ? (recordsNumber
				/ pageInfo.getPageSize() + 1)
				: (recordsNumber / pageInfo.getPageSize());
		int pageNo = pageInfo.getPageNo() > 0 ? pageInfo.getPageNo() : 1;

		if (pageNo > pageAmount) {
			pageNo = pageAmount;
		}

		if (pageInfo.getEnd() != null && pageInfo.getEnd().booleanValue()) {// end=true:首页
			pageNo = 1;
		}
		if (pageInfo.getEnd() != null && !pageInfo.getEnd().booleanValue()) {// end=false:尾页
			pageNo = pageAmount;
		}

		int firstResult = (pageNo - 1) * pageInfo.getPageSize();
		List pageData = findByMap(pageInfo, firstResult, pageInfo.getPageSize());

		return new PageResult(pageData, recordsNumber, pageAmount);
	}

	public int countRecordsNumber(IbatisPageInfo pi) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				pi.getQueryName(), pi.getExpression());
	}

	@SuppressWarnings("unchecked")
	public List findByMap(IbatisPageInfo pi, int... firstResultAndMaxResults) {
		if (firstResultAndMaxResults != null
				&& firstResultAndMaxResults.length == 2) {
			return getSqlMapClientTemplate().queryForList(pi.getQueryName(),
					pi.getExpression(), firstResultAndMaxResults[0],
					firstResultAndMaxResults[1]);
		}

		return getSqlMapClientTemplate().queryForList(pi.getQueryName(),
				pi.getExpression());
	}
}
