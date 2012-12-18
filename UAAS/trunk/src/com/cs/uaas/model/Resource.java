package com.cs.uaas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cs.uaas.base.BaseHibernateModel;

@Entity
public class Resource extends BaseHibernateModel {
	private static final int LENGTH_OF_NAME_60 = 60;
	private static final int LENGTH_OF_URL_100 = 100;

	/**
	 * 所属子系统
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "FK_SUB_SYSTEM_ID")
	private SubSystem subSystem = new SubSystem();

	/**
	 * 资源名称
	 */
	@NotEmpty
	@Length(max = LENGTH_OF_NAME_60, message = "资源名称长度不能超过" + LENGTH_OF_NAME_60)
	@Column(length = LENGTH_OF_NAME_60, nullable = false, unique = true)
	private String resourceName;

	/**
	 * URL
	 */
	@NotEmpty
	@Length(max = LENGTH_OF_URL_100, message = "url长度不能超过" + LENGTH_OF_URL_100)
	@Column(length = LENGTH_OF_URL_100, nullable = false, unique = true)
	private String url;

	public SubSystem getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(SubSystem subSystem) {
		this.subSystem = subSystem;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
