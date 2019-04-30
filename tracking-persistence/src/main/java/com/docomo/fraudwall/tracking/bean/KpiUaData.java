package com.docomo.fraudwall.tracking.bean;

import java.io.Serializable;


import lombok.Data;

/**
 * KpisUaData
 */

@Data
public class KpiUaData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1595680384595087553L;
	private String uaGroup;
	private String uaString;
	private String uaBrand;
	private String uaModel;
	private String uaBrowser;
	private String uaBrowserVersion;

	
	
	public KpiUaData(String uaGroup,String uaString, String uaBrand, String uaModel, String uaBrowser, String uaBrowserVersion) {
		super();
		this.uaGroup = uaGroup;
		this.uaString = uaString;
		this.uaBrand = uaBrand;
		this.uaModel = uaModel;
		this.uaBrowser = uaBrowser;
		this.uaBrowserVersion = uaBrowserVersion;
	}

	
}
