package com.explorworld.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "guide")
public class Guide extends AbstractValueObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", length = 100)
	private String name;
	
	@Column(name = "lastname", length = 45)
	private String lastName;
	
	@Column(name = "nieDni", length = 20)
	private String nieDni;
	
	@Column(name = "email", length = 256)
	private String email;
	
	@Column(name = "phone_number", length = 12)
	private String phoneNumber;
	
	@Column(name = "birth_date")
	private Date birthDate;
	
	@Column(name = "password", length = 256)
	private String password;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id")
	private Gender gender;
	
	@ManyToMany(mappedBy = "guides")
	private Set<Activity> activities = new HashSet<>();

	public Guide() {
		
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


	public Gender getGender() {
		return gender;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}
	

	public Set<Activity> getActivities() {
		return activities;
	}


	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}
	
}
