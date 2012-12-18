package com.cs.uaas.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.ListModelExt;
import org.zkoss.zul.event.ListDataEvent;

@SuppressWarnings({ "rawtypes", "deprecation", "serial" })
public abstract class BasePagingListModel<T, C> extends AbstractListModel implements ListModelExt {
	private static Logger logger = LoggerUtil.getLogger();

	private final int _pageSize;
	private final int _itemStartNumber;

	private List<T> _items = new ArrayList<T>();

	protected String orderBy;
	protected Boolean orderByAsc;
	private C criteria;

	public BasePagingListModel(int startPageNumber, int pageSize, C criteria, String orderBy, Boolean orderByAsc) {
		super();
		this._pageSize = pageSize;
		this._itemStartNumber = startPageNumber * pageSize;

		this.criteria = criteria;
		this.orderBy = orderBy;
		this.orderByAsc = orderByAsc;
		_items = getPageData(_itemStartNumber, _pageSize, criteria, orderBy, orderByAsc);
	}

	protected abstract List<T> getPageData(int itemStartNumber, int pageSize, C criteria, String orderBy,
			Boolean orderByAsc);

	@Override
	public Object getElementAt(int index) {
		return _items.get(index);
	}

	@Override
	public int getSize() {
		return _items.size();
	}

	public final int getTotalSize() {
		return getTotalSize(criteria);
	}

	protected abstract int getTotalSize(C criteria);

	@Override
	public void sort(Comparator comparator, boolean ascending) {
		logger.debug("comparator:" + comparator.getClass());
		logger.debug("ascending?" + ascending);
		if (comparator instanceof FieldComparator) {
			this.orderBy = ((FieldComparator) comparator).getRawOrderBy();
			this.orderByAsc = ascending;
			_items = getPageData(_itemStartNumber, _pageSize, criteria, orderBy, orderByAsc);
			fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
		}
	}

	@Override
	public String getSortDirection(Comparator comparator) {
		String rawOrderBy = ((FieldComparator) comparator).getRawOrderBy();
		logger.debug(rawOrderBy);
		return rawOrderBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public Boolean getOrderByAsc() {
		return orderByAsc;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setOrderByAsc(Boolean orderByAsc) {
		this.orderByAsc = orderByAsc;
	}

	public C getCriteria() {
		return criteria;
	}

	public void setCriteria(C criteria) {
		this.criteria = criteria;
	}
}