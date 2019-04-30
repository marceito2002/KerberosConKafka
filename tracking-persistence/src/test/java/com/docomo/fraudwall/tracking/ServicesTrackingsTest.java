package com.docomo.fraudwall.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;

import com.docomo.fraudwall.domain.PartnerDomain;
import com.docomo.fraudwall.domain.SessionDomain;
import com.docomo.fraudwall.tracking.bean.FraudInfo;
import com.docomo.fraudwall.tracking.bean.KpiData;
import com.docomo.fraudwall.tracking.bean.KpiUaData;
import com.docomo.fraudwall.tracking.business.ITrackingBusiness;
import com.docomo.fraudwall.tracking.business.TrackingBusiness;
import com.docomo.fraudwall.tracking.business.kafka.KafkaBusiness;
import com.docomo.fraudwall.tracking.persistencia.mongodb.IPartnerRespository;
import com.docomo.fraudwall.tracking.persistencia.mongodb.ISessionRepository;
import com.docomo.fraudwall.tracking.service.IServiceTracking;

//@SpringBootTest
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//@ContextConfiguration(classes = ConfigTest.class)
//@DataMongoTest
public class ServicesTrackingsTest {

	// @MockBean
	private ISessionRepository repositorySessionMck;

	@Value("${msg.sessionBadPartnerName}")
	private String sessionBadPartnerName;

	@MockBean
	private ITrackingBusiness bussinesTrackingMck;

	@MockBean
	private IPartnerRespository repoSession;
	
	
	@Autowired
	private IServiceTracking service;
	
	


//	@Test
//	public void testGetFraudLevel() {
//		Mockito.when(bussinesTrackingMck.getFraudLevel("sakura_2", "FWca6113c000159")).thenReturn(initListFraudInfo());
//		FraudInfo fraud =service.getFraudLevel("sakura_2", "FWca6113c000159");		
//		assertEquals(initListFraudInfo(), fraud);		
//
//	}
	
//	@Test
//	public void testSendKpiSuccess() {	
//		service.createFwtrid("sakura_2");
//	}
//	
	
//	@Test
//	public void testGetFraudLevelError() {
//		Mockito.when(bussinesTrackingMck.getFraudLevel("sakura_2", "FWca6113c000159")).thenReturn(initListFraudInfo());
//		FraudInfo fraud =service.getFraudLevel("sakura_2", "FWca6113c000159");		
//		assertNotEquals(null, fraud);		
//
//	}	

	private FraudInfo initListFraudInfo() {
		List<String> lstTags = new ArrayList<>();
		lstTags.add("click movil");
		lstTags.add("many clicks");
		lstTags.add("facebook");

		FraudInfo fraudinfo = new FraudInfo(2, Float.valueOf("3.1"), lstTags);
		return fraudinfo;
	}

}
