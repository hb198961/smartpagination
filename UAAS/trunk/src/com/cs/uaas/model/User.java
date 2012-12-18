package com.cs.uaas.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cs.uaas.base.BaseHibernateModel;
import com.cs.uaas.validator.DistinctUserName;

@Entity
public class User extends BaseHibernateModel {
	public enum USER_GENTLE {
		male("男"), female("女"), unknown("未知");
		String text;

		private USER_GENTLE(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	@NotEmpty
	@Column(unique = true, nullable = false, updatable = false)
	private String userName;

	/**
	 * 定制的Validation constraint，
	 * 因为判断userName是否重复时，需要根据id是否为空做不同判断，因此不能简单加在userName字段上
	 */
	@DistinctUserName
	public User getSelf() {
		return this;
	}

	@Valid
	@Embedded
	private UserContactInfo contactInfo = new UserContactInfo(this);

	@NotEmpty
	@Length(min = 6, max = 50)
	@Column(nullable = false, length = 50)
	private String password;

	@NotEmpty
	@Length(min = 6, max = 50)
	@Transient
	private String confirmPassword;

	@NotEmpty
	@Column(nullable = false, length = 50)
	private String realName;

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private USER_GENTLE gentle;

	@Past(message = "出生日期不应晚于当前时间！")
	@Temporal(TemporalType.DATE)
	@Column
	private Date birthday;

	@Length(max = 6000)
	@Column(length = 6000)
	private String description;

	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	private Set<Role> roleSet = new HashSet<Role>();

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = CascadeType.SAVE_UPDATE)
	@JoinTable(name = "USER_ORGAN", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ORGAN_ID") })
	private Set<Organization> organSet = new HashSet<Organization>();

	public void addRole(Role role) {
		if (role != null) {
			roleSet.add(role);
		}
	}

	public void addGroup(Organization organization) {
		if (organization != null) {
			organSet.add(organization);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String memo) {
		this.description = memo;
	}

	public Date getBirthday() {
		if (birthday != null) {
			birthday = new Date(birthday.getTime());
		}
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(UserContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	@AssertTrue(message = "两次输入的密码必须一致！")
	public Boolean getConfirmPasswordValidation() {
		return StringUtils.equals(password, confirmPassword);
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public USER_GENTLE getGentle() {
		return gentle;
	}

	public String getGentleText() {
		return gentle != null ? gentle.getText() : "";
	}

	public void setGentle(USER_GENTLE gentle) {
		this.gentle = gentle;
	}

	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	public Set<Organization> getOrganSet() {
		return organSet;
	}

	public void setOrganSet(Set<Organization> organSet) {
		this.organSet = organSet;
	}
}