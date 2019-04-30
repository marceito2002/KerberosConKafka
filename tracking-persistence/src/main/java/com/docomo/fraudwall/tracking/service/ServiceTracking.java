package com.docomo.fraudwall.tracking.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docomo.fraudwall.tracking.bean.FraudInfo;
import com.docomo.fraudwall.tracking.bean.KpiData;
import com.docomo.fraudwall.tracking.business.ITrackingBusiness;


@Service
public class ServiceTracking implements IServiceTracking {

	@Autowired
	private ITrackingBusiness trackingbusiness;

	@Override
	public String createFwtrid(String partnerName) {
		return trackingbusiness.createFwtrid(partnerName);
	}

	@Override
	public FraudInfo getFraudLevel(String partnerName, String sessionId) {
		return trackingbusiness.getFraudLevel(partnerName, sessionId);
	}


	@Override
	public String sendKpi(KpiData kpi) {
		return trackingbusiness.sendKpi(kpi);
	}

	@Override
	public String getAllDataSession(String partnerName, String sessionId) {
		return trackingbusiness.getAllDataSession(partnerName, sessionId);
	}

}
