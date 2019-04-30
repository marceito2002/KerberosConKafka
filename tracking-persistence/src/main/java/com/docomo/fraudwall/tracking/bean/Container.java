package com.docomo.fraudwall.tracking.bean;

import java.io.Serializable;


import lombok.Data;

@Data
public class Container  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private FraudInfo fraudInfo;

	public Container(FraudInfo fraudInfo) {
		super();
		this.fraudInfo = fraudInfo;
	}

	public Container() {
		super();
	}
	
	
	
	
}
