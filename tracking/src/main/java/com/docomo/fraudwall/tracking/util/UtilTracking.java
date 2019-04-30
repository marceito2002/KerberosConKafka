package com.docomo.fraudwall.tracking.util;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;

import com.docomo.fraudwall.tracking.bean.FraudInfo;
import com.docomo.fraudwall.tracking.bean.KpiData;
import com.docomo.fraudwall.tracking.bean.KpiUaData;
import com.docomo.fraudwall.tracking.generated.model.FraudLevelResponse;
import com.docomo.fraudwall.tracking.generated.model.Kpis;
import com.docomo.fraudwall.tracking.generated.model.KpisUa;

@Controller
public class UtilTracking {
	private static final Logger logger = LoggerFactory.getLogger(UtilTracking.class);

	public KpiData convertKpisToKpiDataBussiness(Kpis kpis, String partnerName, String sessionId, String eventName) {

		if (kpis == null) {
			logger.info("UtilTracking.convertKpisToKpiDataBussiness. Data is null");
			return null;
		}

		KpiData dataBussiness = new KpiData();

		
		dataBussiness.setSessionId(sessionId);
		dataBussiness.setEventName(eventName);
		dataBussiness.setAffiliate(kpis.getAffiliate());
		dataBussiness.setApp(kpis.getApp());
		dataBussiness.setIp(kpis.getIp());
		dataBussiness.setMediaChannel(kpis.getMediaChannel());
		dataBussiness.setMerchant(kpis.getMerchant());
		dataBussiness.setNetwork(kpis.getNetwork());
		dataBussiness.setOperator(kpis.getOperator());
		dataBussiness.setPartnerName(partnerName);
		dataBussiness.setPartnerTrid(kpis.getPartnerTrid());
		dataBussiness.setService(kpis.getService());
		dataBussiness.setTimestampUtc(kpis.getTimestampUtc());
		dataBussiness.setCountry(kpis.getCountry());

		dataBussiness.setUa(convertKpiUaToDataKpiDataBussiness(kpis.getUa()));

		convertKpiUaToDataKpiDataBussiness(kpis.getUa());
		dataBussiness.setUserId(kpis.getUserId());

		return dataBussiness;

	}

	public FraudLevelResponse convertFraundInfModelToFraudInfBussiness(FraudInfo fraudInfo) {

		if (fraudInfo == null) {
			logger.info("UtilTracking. convertFraundInfModelToFraudInfBussiness:. Data is null");
			return null;
		}

		FraudLevelResponse dataModel = new FraudLevelResponse();

		dataModel.setFraudLevel(fraudInfo.getFraudLevel());
		dataModel.setFraudScore(fraudInfo.getFraudScore());
		dataModel.setTags(fraudInfo.getTags());
		return dataModel;

	}
	
	private KpiUaData convertKpiUaToDataKpiDataBussiness(KpisUa ua) {
		if (ua != null) {
			return new KpiUaData(ua.getUaGroup(),ua.getUaString(), ua.getUaBrand(), ua.getUaModel(), ua.getUaBrowser(),
					ua.getUaBrowserVersion());
		} else {
			logger.info("KpisUa is null");
			return null;
		}
	}

	public boolean isValidDate(String date) {

		try {

			int year = Integer.parseInt(date.substring(0, 4));
			int month = Integer.parseInt(date.substring(4, 6));
			int day = Integer.parseInt(date.substring(6, 8));
			int hour = Integer.parseInt(date.substring(9, 11));
			int minutes = Integer.parseInt(date.substring(12, 14));
			int seconds = Integer.parseInt(date.substring(15, 17));
			int milis = Integer.parseInt(date.substring(18, 21));

			LocalDateTime.of(year, month, day, hour, minutes, seconds, milis);		

		} catch (Exception e) {
			logger.error("isValidDate.: Date is not valid");
			return false;
		}

		return true;
	}		
}
