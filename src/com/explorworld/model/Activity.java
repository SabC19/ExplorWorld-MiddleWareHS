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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "activity")
public class Activity extends AbstractValueObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", length = 100)
	private String name;
	
	@Column(name = "description", length = 256)
	private String description;
	
	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "capacity")
	private Integer capacity;
	
	@Column(name = "pricemin", precision = 10, scale = 2)
	private Double priceMin;
	
	@Column(name = "pricemax", precision = 10, scale = 2)
	private Double priceMax;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activity_type_id")
	private ActivityType activityType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "start_place_id")
	private Place startPlace;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "end_place_id")
	private Place endPlace;
	
	@ManyToMany
	@JoinTable(name = "activity_guide",
	joinColumns = @JoinColumn(name = "activity_id"),
	inverseJoinColumns = @JoinColumn(name = "guide_id"))
	private Set<Guide> guides = new HashSet<>();
	
	@ManyToMany(mappedBy = "activities")
	private Set<Transport> transports = new HashSet<>();
	
	public Activity() {
		
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Double getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}

	public Double getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public Place getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(Place startPlace) {
		this.startPlace = startPlace;
	}

	public Place getEndPlace() {
		return endPlace;
	}

	public void setEndPlace(Place endPlace) {
		this.endPlace = endPlace;
	}

	public Set<Guide> getGuides() {
		return guides;
	}

	public void setGuides(Set<Guide> guides) {
		this.guides = guides;
	}
	
	
	
}
