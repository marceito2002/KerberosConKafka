package com.docomo.fraudwall.tracking.bean;

import java.io.Serializable;
import java.util.List;

import com.docomo.fraudwall.domain.PartnerDomain;

import lombok.Data;

@Data
public class FraudInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer fraudLevel;
	private Float fraudScore;
	private List<String> tags;
	

	public FraudInfo(Integer fraudLevel, Float fraudScore) {
		super();
		this.fraudLevel = fraudLevel;
		this.fraudScore = fraudScore;
	}


	public FraudInfo(Integer fraudLevel, Float fraudScore, List<String> tags) {
		super();
		this.fraudLevel = fraudLevel;
		this.fraudScore = fraudScore;
		this.tags = tags;
	}
	
	

}
