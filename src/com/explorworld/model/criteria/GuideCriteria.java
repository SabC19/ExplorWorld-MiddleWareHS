package com.explorworld.model.criteria;

import java.util.Date;

public class GuideCriteria {

	private Long id = null;
	private String name = null;
	private String lastName = null;
	private String nieDni = null;
	private String email = null;
	private String phoneNumber = null;
	private Date birthDate = null;
	private String password = null;
		
	private Long genderId = null;
		
	public GuideCriteria() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNieDni() {
		return nieDni;
	}

	public void setNieDni(String nieDni) {
		this.nieDni = nieDni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getGenderId() {
		return genderId;
	}

	public void setGenderId(Long genderId) {
		this.genderId = genderId;
	}

	
	
}