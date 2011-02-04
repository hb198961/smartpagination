package org.powerstone.smartpagination.ibatis;

public interface IbatisPagerable {
	public String getOrderByStr();

	public void setOrderByStr(String orderByStr);

	public String getPaginationStart();

	public void setPaginationStart(String paginationStart);

	public String getPaginationEnd();

	public void setPaginationEnd(String paginationEnd);
}
