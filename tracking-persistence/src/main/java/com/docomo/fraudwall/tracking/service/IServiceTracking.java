package com.docomo.fraudwall.tracking.service;



import com.docomo.fraudwall.tracking.bean.FraudInfo;
import com.docomo.fraudwall.tracking.bean.KpiData;


public interface IServiceTracking {
	String createFwtrid(String partnerId);
	FraudInfo getFraudLevel(String partnerName, String sessionId);
	String getAllDataSession(String partnerName, String sessionId);
	String sendKpi(KpiData kpi);


}
