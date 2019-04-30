package com.docomo.fraudwall.tracking.controller;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.docomo.fraudwall.tracking.controller.validator.KpiValidator;
import com.docomo.fraudwall.tracking.generated.api.PartnerApi;
import com.docomo.fraudwall.tracking.generated.model.FraudLevelResponse;
import com.docomo.fraudwall.tracking.generated.model.Kpis;
import com.docomo.fraudwall.tracking.generated.model.SessionIdResponse;

import com.docomo.fraudwall.tracking.service.IServiceTracking;
import com.docomo.fraudwall.tracking.util.UtilTracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class TrackingController implements PartnerApi {

	private static final Logger logger = LoggerFactory.getLogger(TrackingController.class);

	@Autowired
	private IServiceTracking serviceTracking;

	@Value("${msg.partnerNameNoExist:NotExist}")
	private String msgPartnerNoExist;

	@Value("${msg.ssesionSuccess:Ok}")
	private String msgSessionSuccess;

	@Value("${msg.msgDataInvalid:date invalid}")
	private String msgDataInvalid;

	@Autowired
	private UtilTracking utilTracking;

	@Autowired
	private KpiValidator kpiValidators;

	/**
	 *
	 * @param binder Se añade el validador para los datos de entrada Kpis
	 */
	@InitBinder("Kpis")
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(kpiValidators);
	}

	@Override
	public ResponseEntity<SessionIdResponse> createSessionId(@PathVariable String partnerName) {

		logger.info("Controller: addKpi.:{}", partnerName);

		String sessionId = serviceTracking.createFwtrid(partnerName);

		SessionIdResponse sessionIdResponse = new SessionIdResponse();
		if (sessionId != null) {
			sessionIdResponse.setResponseMessage(msgSessionSuccess);
			sessionIdResponse.setSessionId(sessionId);
			return new ResponseEntity<>(sessionIdResponse, HttpStatus.OK);

		} else {
			sessionIdResponse.setResponseMessage(msgPartnerNoExist);
			sessionIdResponse.setSessionId(null);
			return new ResponseEntity<>(sessionIdResponse, HttpStatus.NOT_FOUND);
		}
	}



	@Override
	public ResponseEntity<SessionIdResponse> sendKpi(@PathVariable String partnerName,
			@PathVariable @DecimalMin("1") String sessionId, @PathVariable String eventName,
			@RequestBody @Valid Kpis kpiParameters) {

		SessionIdResponse sessionIdResponse = new SessionIdResponse();

		if (utilTracking.isValidDate(kpiParameters.getTimestampUtc())) {
			String resp = serviceTracking
					.sendKpi(utilTracking.convertKpisToKpiDataBussiness(kpiParameters, partnerName, sessionId, eventName));

			sessionIdResponse.setResponseMessage(resp);
			sessionIdResponse.setSessionId(sessionId);
			if (resp != null) {
				return new ResponseEntity<>(sessionIdResponse, HttpStatus.NOT_FOUND);
			} else {
				sessionIdResponse.setResponseMessage("");
				return new ResponseEntity<>(sessionIdResponse, HttpStatus.OK);
			}			
		}
		else {
			sessionIdResponse.setResponseMessage(msgDataInvalid.replace("\\", ""));

			return new ResponseEntity<>(sessionIdResponse, HttpStatus.BAD_REQUEST);
		}
			


	}

	
	@Override
	public ResponseEntity<FraudLevelResponse> getFraudLevel(@PathVariable String partnerName,
			@DecimalMin("1") @PathVariable String sessionId) {

		logger.info("Controller: getFraudLevel.:{}", partnerName);  
		FraudLevelResponse fraudLevelResponse = utilTracking.convertFraundInfModelToFraudInfBussiness (serviceTracking.getFraudLevel(partnerName, sessionId));
		
		if (fraudLevelResponse != null) {
			return new ResponseEntity<>(fraudLevelResponse, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(fraudLevelResponse, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Override
	public ResponseEntity<String> getSessionIdInfo( @PathVariable String partnerName, @PathVariable @DecimalMin("1") String sessionId) {
		logger.info("Controller: getFraudLevel.:{}", partnerName);  
		String fraudLevelResponse = serviceTracking.getAllDataSession(partnerName, sessionId);
		
		if (fraudLevelResponse != null) {
			return new ResponseEntity<>(fraudLevelResponse, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(fraudLevelResponse, HttpStatus.NOT_FOUND);
		}
	}
}
