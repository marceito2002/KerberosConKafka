package com.docomo.fraudwall.tracking.bean;

import java.io.Serializable;

import lombok.Data;


@Data
public class KpiData implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 8060448904663561693L;

	private String partnerTrid;

	private String partnerName;

	private String userId;

	private String timestampUtc;

	private String service;

	private String mediaChannel;

	private String merchant;

	private String affiliate;

	private String app;

	private String operator;

	private String ip;

	private String network;

	private KpiUaData ua;

	private String sessionId;
	
	private String eventName;
	
	private String eventId;
	
	private String country;
	
	private String fraudwallModel;
	
	public KpiData() {
		super();
	}




}
