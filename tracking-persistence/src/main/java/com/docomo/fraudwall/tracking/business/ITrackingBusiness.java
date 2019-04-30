package com.docomo.fraudwall.tracking.business;

import com.docomo.fraudwall.tracking.bean.FraudInfo;
import com.docomo.fraudwall.tracking.bean.KpiData;


public interface ITrackingBusiness {
	String createFwtrid(String instanceName);
	FraudInfo getFraudLevel(String partnerName, String sessionId);
	String sendKpi( KpiData kpi);	
	String getAllDataSession(String partnerName, String sessionId);
}
