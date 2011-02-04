package org.powerstone.smartpagination.ibatis;

import java.util.List;

import org.powerstone.smartpagination.common.PageResult;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class BaseIbatisDao extends SqlMapClientDaoSupport {
	@SuppressWarnings("unchecked")
	public PageResult findByPage(IbatisPageInfo pageInfo) {
		int recordsNumber = countRecordsNumber(pageInfo);

		if (pageInfo.getPageSize() <= 0) {// PageSize==0相当于取全部
			List all = findByPi(pageInfo);
			return new PageResult(all, recordsNumber, 1);// 取全部，所以页数为1
		}

		int pageAmount = (recordsNumber % pageInfo.getPageSize() > 0) ? (recordsNumber
				/ pageInfo.getPageSize() + 1) : (recordsNumber / pageInfo.getPageSize());
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
		List pageData = findByPi(pageInfo, firstResult, pageInfo.getPageSize());

		return new PageResult(pageData, recordsNumber, pageAmount);
	}

	private int countRecordsNumber(IbatisPageInfo pi) {
		return (Integer) getSqlMapClientTemplate().queryForObject(pi.getCountQueryName(),
				pi.getExpression());
	}

	@SuppressWarnings("unchecked")
	private List findByPi(IbatisPageInfo pi, int... firstResultAndMaxResults) {
		//set orderby to
		if (pi.getOrderByList() != null && pi.getOrderByList().size() > 0) {
			String orderBy = " ";
			for (String order : pi.getOrderByList()) {
				orderBy += (order + " ,");
			}
			orderBy = orderBy.substring(0, orderBy.length() - 1);
			pi.getExpression().setOrderByStr(orderBy);
		}
		//set start and end index
		if (firstResultAndMaxResults != null && firstResultAndMaxResults.length == 2) {
			pi.getExpression().setPaginationStart(
					new Integer(firstResultAndMaxResults[0]).toString());
			pi.getExpression().setPaginationEnd(
					new Integer(firstResultAndMaxResults[0] + firstResultAndMaxResults[1])
							.toString());
		}

		return getSqlMapClientTemplate().queryForList(pi.getPageQueryName(), pi.getExpression());
	}
}
