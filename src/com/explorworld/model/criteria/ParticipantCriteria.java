package com.explorworld.model.criteria;

import java.util.Date;

import com.explorworld.model.AbstractValueObject;

public class ParticipantCriteria extends AbstractValueObject {

	private Integer id = null;
	private String name = null;
	private String lastName = null;
	private Integer numberPhone = null;
	private String email = null;
	private Date birthDate = null;
	private String password = null;
	
	private Long genderId = null;
	
	public ParticipantCriteria() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getNumberPhone() {
		return numberPhone;
	}

	public void setNumberPhone(Integer numberPhone) {
		this.numberPhone = numberPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
