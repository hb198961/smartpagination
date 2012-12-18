package com.cs.uaas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cs.uaas.base.BaseHibernateModel;

@Entity
public class Organization extends BaseHibernateModel {
	private static final int LENGTH_OF_NAME_60 = 60;
	private static final int LENGTH_OF_DESC_6000 = 6000;

	/**
	 * 组角色集合
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinTable(name = "ORGAN_ROLE", joinColumns = { @JoinColumn(name = "ORGAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	private Set<Role> roleSet = new HashSet<Role>();

	/**
	 * 用户集合
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "organSet")
	@Cascade(value = CascadeType.SAVE_UPDATE)
	private Set<User> userSet = new HashSet<User>();

	/**
	 * 父用户组
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_PARENT_ORGAN_ID")
	private Organization parentOrgan;

	/**
	 * 组名称
	 */
	@NotEmpty
	@Length(max = LENGTH_OF_NAME_60, message = "组名称长度不能超过" + LENGTH_OF_NAME_60 / 3)
	@Column(length = LENGTH_OF_NAME_60, nullable = false, unique = true)
	private String organName;

	/**
	 * 组描述
	 */
	@Length(max = LENGTH_OF_DESC_6000, message = "组描述长度不能超过" + LENGTH_OF_DESC_6000 / 3)
	@Column(length = LENGTH_OF_DESC_6000)
	private String description;

	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	public Set<User> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}

	public Organization getParentOrgan() {
		return parentOrgan;
	}

	public void setParentOrgan(Organization parentOrgan) {
		this.parentOrgan = parentOrgan;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
