package com.docomo.fraudwall.tracking.business;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.docomo.fraudwall.domain.EventDomain;
import com.docomo.fraudwall.domain.PartnerDomain;
import com.docomo.fraudwall.domain.SessionDomain;
import com.docomo.fraudwall.tracking.bean.FraudInfo;
import com.docomo.fraudwall.tracking.bean.KpiData;
import com.docomo.fraudwall.tracking.bean.Container;
import com.docomo.fraudwall.tracking.business.hbase.IReadHbase;
import com.docomo.fraudwall.tracking.business.kafka.IKafkaBusiness;
import com.docomo.fraudwall.tracking.persistencia.mongodb.ISessionRepository;
import com.docomo.fraudwall.tracking.util.TokenGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service
public class TrackingBusiness implements ITrackingBusiness {

	private static final Logger logger = LoggerFactory.getLogger(TrackingBusiness.class);

	@Autowired
	private IKafkaBusiness serviceKafka;

	@Autowired
	private ISessionRepository sessionRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IReadHbase readHbase;

	@Value("${msg.sessionOrPartnerNoExist}")
	private String msgSessionOrPartnerNoExist;

	@Value("${msg.sessionBadPartnerName}")
	private String sessionBadPartnerName;

	@Value("${kafka.topic:HDP_FW_TRACK}")
	private String kafkaTopic;

	@Value("${call.partner.endpoint:http://localhost:2222/partner/getByName/}")
	private String endpointPartner;

	@Override
	public String createFwtrid(String partnerName) {

		List<PartnerDomain> lstPartner = findPartnerByName(partnerName);
		if (lstPartner == null) {
			logger.info("TrackingBusiness: createFwtrid.. Instance not exist.: {}", partnerName);
			return null;
		}

		if (lstPartner.size() != 1) {
			logger.info("TrackingBusiness: createFwtrid.: {} -- {}", partnerName, sessionBadPartnerName);
			return null;
		}

		String sessionID = generateNewToken();
		sessionRepo.save(new SessionDomain(sessionID, partnerName, new Date()));
		logger.info("TrackingBusiness: Create new token .: {} to partname.: {} ", sessionID, partnerName);

		return sessionID;

	}

	@Override
	public String sendKpi(KpiData kpi) {

		if (existSessionnId(kpi.getPartnerName(), kpi.getSessionId())) {

			List<PartnerDomain> lstPartner = findPartnerByName(kpi.getPartnerName());

			if (lstPartner != null && lstPartner.size() == 1) {
				kpi.setEventId(recoverEventId(kpi.getEventName(), lstPartner.get(0).getEvents()));
				kpi.setFraudwallModel(lstPartner.get(0).getFraudwallModel());
				serviceKafka.sendMsgKafka(convertObjectToJson(kpi).toString());
				return null;
			} else {
				logger.info("TrackingBusiness: sendKpi.: {}", sessionBadPartnerName);
				return sessionBadPartnerName;
			}

		} else {
			logger.info("TrackingBusiness: sendKpi.: {}", msgSessionOrPartnerNoExist);
			return msgSessionOrPartnerNoExist;
		}
	}

	@Override
	public FraudInfo getFraudLevel(String partnerName, String sessionId) {
		return foundFraulInfo(getDataHBase(partnerName, sessionId));
	}

	@Override
	public String getAllDataSession(String partnerName, String sessionId) {
		return getDataHBase(partnerName, sessionId);
	}

	
	// public for test??
	public  List<PartnerDomain> findPartnerByName(String partnerName) {
		List<PartnerDomain> data = null;
		try {

			logger.info("TrackingBusiness: calling to partner: {}{} ", endpointPartner, partnerName);

			ResponseEntity<PartnerDomain[]> response = restTemplate.getForEntity(endpointPartner + partnerName,
					PartnerDomain[].class);
			data = Arrays.asList(response.getBody());

		} catch (RestClientException e) {
			logger.info("TrackingBusiness: Error to call partner {}", e.getMessage());
		}
		return data;

	}

	public  boolean existSessionnId(String partnerName, String sesionid) {
		Optional<SessionDomain> findById = sessionRepo.findById(sesionid);

		return (findById.isPresent() && findById.get().getPartnerName().compareTo(partnerName) == 0);

	}

	private String getDataHBase(String partnerName, String sessionId) {
		return readHbase.readDataHdfs(sessionId);
	}

	private String generateNewToken() {
		TokenGenerator tokenGenerator = new TokenGenerator();
		return tokenGenerator.generate();
	}

	private JsonNode convertObjectToJson(Object obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.valueToTree(obj);

	}

	private FraudInfo foundFraulInfo(String buffer) {
		if (buffer == null)
			return null;

		Gson gson = new Gson();
		Container data = gson.fromJson(buffer, Container.class);

		if (data != null)
			return data.getFraudInfo();
		else
			return null;

	}

	private String recoverEventId(String eventName, List<EventDomain> lstEvent) {

		for (int i = 0; i < lstEvent.size(); i++) {
			if (lstEvent.get(i).getEventName().equals(eventName)) {
				return lstEvent.get(i).getEventId();
			}
		}

		return null;

	}
}
