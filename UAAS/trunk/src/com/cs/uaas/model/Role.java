package com.cs.uaas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cs.uaas.base.BaseHibernateModel;

@Entity
public class Role extends BaseHibernateModel {
	private static final int LENGTH_OF_NAME_60 = 60;
	private static final int LENGTH_OF_DESC_6000 = 6000;

	/**
	 * 资源集合
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinTable(name = "ROLE_RESOURCE", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID") })
	private Set<Resource> resourceSet = new HashSet<Resource>();

	/**
	 * 用户集合
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleSet")
	@Cascade(value = CascadeType.SAVE_UPDATE)
	private Set<User> userSet = new HashSet<User>();

	/**
	 * 用户组集合
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleSet")
	@Cascade(value = CascadeType.SAVE_UPDATE)
	private Set<Organization> organSet = new HashSet<Organization>();

	/**
	 * 角色名称
	 */
	@NotEmpty
	@Length(max = LENGTH_OF_NAME_60, message = "角色名称长度不能超过" + LENGTH_OF_NAME_60 / 3)
	@Column(length = LENGTH_OF_NAME_60, nullable = false, unique = true)
	private String roleName;

	/**
	 * 角色描述
	 */
	@Length(max = LENGTH_OF_DESC_6000, message = "角色描述长度不能超过" + LENGTH_OF_DESC_6000 / 3)
	@Column(length = LENGTH_OF_DESC_6000)
	private String description;

	public Set<Resource> getResourceSet() {
		return resourceSet;
	}

	public void setResourceSet(Set<Resource> resourceSet) {
		this.resourceSet = resourceSet;
	}

	public Set<User> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}

	public Set<Organization> getOrganSet() {
		return organSet;
	}

	public void setOrganSet(Set<Organization> organSet) {
		this.organSet = organSet;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
