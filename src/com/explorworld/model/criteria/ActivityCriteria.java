package com.explorworld.model.criteria;

import java.util.Date;

import com.explorworld.model.AbstractValueObject;

public class ActivityCriteria extends AbstractValueObject {
	
	private Long id = null;
	private String name = null;
	private String description = null;
	private Date startDate = null;
	private Date endDate = null;
	private Integer capacity = null;
	private Double priceMin = null;
	private Double priceMax = null;
	private Long activityTypeId = null;
	private Long startPlaceId = null;
	private Long endPlaceId = null;
	
	public ActivityCriteria() {
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

	public Long getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Long activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	public Long getStartPlaceId() {
		return startPlaceId;
	}

	public void setStartPlaceId(Long startPlaceId) {
		this.startPlaceId = startPlaceId;
	}

	public Long getEndPlaceId() {
		return endPlaceId;
	}

	public void setEndPlaceId(Long endPlaceId) {
		this.endPlaceId = endPlaceId;
	}
	
	
	
	
}
