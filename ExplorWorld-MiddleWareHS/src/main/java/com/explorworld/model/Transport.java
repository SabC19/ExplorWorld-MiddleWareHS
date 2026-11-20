package com.explorworld.model;

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
@Table(name = "transport")
public class Transport extends AbstractValueObject{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "plate_number", length = 20)
	private String plateNumber;
	
	@Column(name = "seat_number", length = 45)
	private String seatNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transport_type_id")
	private TransportType transportType;
	
	@ManyToMany
	@JoinTable(name = "transport_activity",
	    joinColumns = @JoinColumn(name = "transport_id"),
	    inverseJoinColumns = @JoinColumn(name = "activity_id"))
	private Set<Activity> activities = new HashSet<>();	
	
	public Transport() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public TransportType getTransportType() {
		return transportType;
	}

	public void setTransportType(TransportType transportType) {
		this.transportType = transportType;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}
	
	
}
