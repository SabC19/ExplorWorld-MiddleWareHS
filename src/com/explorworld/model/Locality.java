package com.explorworld.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "locality")

public class Locality extends AbstractValueObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", length = 45)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "province_id")
	private Province province;

	public Locality() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNsame() {
		return name;
	}

	public void setNsame(String nsame) {
		this.name = nsame;
	}
	
	 public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
	
}
