package com.hah.excel.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.codec.binary.Hex;

@Entity
public class StudentInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	@Column(nullable = false, unique = true)
	String hashOfNameAndBirth;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String birth;
	@Column
	private String address;
	@Column
	private String parentName;
	@Column
	private String phone1;
	@Column
	private String phone2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHashOfNameAndBirth() {
		return hashOfNameAndBirth;
	}

	public void setHashOfNameAndBirth(String hashOfNameAndBirth) {
		this.hashOfNameAndBirth = hashOfNameAndBirth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public void generateHash() {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		messageDigest.update(("" + name + birth).getBytes());
		this.setHashOfNameAndBirth(new String(Hex.encodeHex(messageDigest.digest())));
	}
}