package com.cs.uaas.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cs.uaas.base.BaseHibernateModel;

@Entity
public class SubSystem extends BaseHibernateModel {

	private static final int LENGTH_OF_NAME_60 = 60;
	/**
	 * 子系统名称
	 */
	@NotEmpty
	@Length(max = LENGTH_OF_NAME_60, message = "子系统名称长度不能超过" + LENGTH_OF_NAME_60)
	@Column(length = LENGTH_OF_NAME_60, nullable = false)
	private String subSystemName;

	public String getSubSystemName() {
		return subSystemName;
	}

	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}

}
