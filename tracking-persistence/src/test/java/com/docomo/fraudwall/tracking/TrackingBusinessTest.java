package com.docomo.fraudwall.tracking;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.docomo.fraudwall.domain.EventDomain;
import com.docomo.fraudwall.domain.PartnerDomain;
import com.docomo.fraudwall.domain.SessionDomain;
import com.docomo.fraudwall.tracking.bean.FraudInfo;
import com.docomo.fraudwall.tracking.bean.KpiData;
import com.docomo.fraudwall.tracking.bean.KpiUaData;
import com.docomo.fraudwall.tracking.business.ITrackingBusiness;
import com.docomo.fraudwall.tracking.business.TrackingBusiness;
import com.docomo.fraudwall.tracking.business.kafka.KafkaBusiness;
import com.docomo.fraudwall.tracking.service.IServiceTracking;

@SpringBootTest // (webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = ConfigTest.class)
public class TrackingBusinessTest {

	@Value("${msg.sessionBadPartnerName:There are some partned with the same name}")
	private String sessionBadPartnerName;
	

	@MockBean
	private TrackingBusiness bussinesTrackingMck;

	@MockBean
	private KafkaBusiness kafkaBusinessMck;

	@Autowired
	private IServiceTracking service;

	@Test
	public void testGetFraudLevelBussiness() {

		Mockito.when(bussinesTrackingMck.getFraudLevel("sakura_2", "FWca6113c000159")).thenReturn(initListFraudInfo());
		assertEquals(initListFraudInfo(), bussinesTrackingMck.getFraudLevel("sakura_2", "FWca6113c000159"));

	}

	@Test
	public void testGetFraudLevelService() {
		Mockito.when(bussinesTrackingMck.getFraudLevel("sakura_2", "FWca6113c000159")).thenReturn(initListFraudInfo());
		FraudInfo fraud = service.getFraudLevel("sakura_2", "FWca6113c000159");
		assertEquals(initListFraudInfo(), fraud);

	}

	@Test
	public void testGetAllDataSession() {

		Mockito.when(bussinesTrackingMck.getAllDataSession("sakura_2", "FWca6113c000159"))
				.thenReturn(initListFraudInfo().toString());
		assertEquals(initListFraudInfo().toString(),
				bussinesTrackingMck.getAllDataSession("sakura_2", "FWca6113c000159"));

	}

	@Test
	public void testSendKpiSuccess() {
		Mockito.when(bussinesTrackingMck.sendKpi(initKpiData())).thenReturn(null);
		assertEquals(null, bussinesTrackingMck.sendKpi(initKpiData()));
	}

	@Test
	public void testSendKpiSuccessErrorMorePartnerWithSameName() {

		Mockito.when(bussinesTrackingMck.findPartnerByName("sakura_2")).thenReturn(returnPartnerDomainSome());
		assertEquals(null, bussinesTrackingMck.sendKpi(initKpiData()));
	}

	@Test
	public void testSendKpiSuccessOn() {

		Mockito.when(bussinesTrackingMck.findPartnerByName("sakura_2")).thenReturn(returnPartnerDomain());
		assertEquals(null, bussinesTrackingMck.sendKpi(initKpiData()));
	}
	
	
	private List<PartnerDomain> returnPartnerDomain() {
		List<PartnerDomain> list = new ArrayList<PartnerDomain>();
		list.add(new PartnerDomain("partnerId", new Date(), "sakura_2", "nivel 1", null));
		return list;
	}
	
	private List<PartnerDomain> returnPartnerDomainSome() {
		List<PartnerDomain> list =returnPartnerDomain();		
		list.add(new PartnerDomain("partnerId2", new Date(), "sakura_3", "nivel 2", null));
		return list;
	}	

//	@Test
//	public void testSendKpiSuccessService() {
//		
//		service.createFwtrid("sakura_2");
//	}

	@Test
	public void testSendKpiError() {			
		Mockito.when(bussinesTrackingMck.existSessionnId("sakura_2", "sesionid")).thenReturn(true);
		System.out.println("sessionBadPartnerName.:" + sessionBadPartnerName);
		assertEquals(null, bussinesTrackingMck.sendKpi(initKpiData()));
	}

	private FraudInfo initListFraudInfo() {
		List<String> lstTags = new ArrayList<>();
		lstTags.add("click movil");
		lstTags.add("many clicks");
		lstTags.add("facebook");

		FraudInfo fraudinfo = new FraudInfo(2, Float.valueOf("3.1"), lstTags);
		return fraudinfo;
	}

	private List<SessionDomain> initListSession() {
		List<SessionDomain> lstSession = new ArrayList<>();
		lstSession.add(new SessionDomain("FWre223423421", "sakura_2", new Date()));
		lstSession.add(new SessionDomain("FWca6113c000159", "telefonica", new Date()));
		lstSession.add(new SessionDomain("FWca6123c002345", "vodafone", new Date()));
		lstSession.add(new SessionDomain("FWca233423c002345", "patn1", new Date()));
		lstSession.add(new SessionDomain("FWca6123cdfasdfa5", "patn2", new Date()));
		return lstSession;
	}

	private List<PartnerDomain> initOneListPartnerDomain() {
		List<PartnerDomain> lstPartner = new ArrayList<>();
		lstPartner.add(new PartnerDomain("5ca6119f47943c00015ccb09", new Date(), "sakura_2", "fraudwallModel_1", null));
		return lstPartner;
	}

	private KpiData initKpiData() {
		KpiData kpiData = new KpiData();
		kpiData.setAffiliate("afiliado");
		kpiData.setApp("appname");
		kpiData.setCountry("country");
		kpiData.setEventName("click1");
		// kpiData.setFraudinfo(initListFraudInfo());
		kpiData.setFraudwallModel("Model_1");
		kpiData.setIp("123.123.342.12");
		kpiData.setMediaChannel("media channel");
		kpiData.setMerchant("merchant");
		kpiData.setNetwork("red");
		kpiData.setOperator("operador2");
		kpiData.setPartnerName("sakura_2");
		kpiData.setPartnerTrid("5ca6119f47943c00015ccb09");
		kpiData.setService("service name");
		kpiData.setTimestampUtc("20171216 10:20:34:653");
		kpiData.setUa(initKpiUaData());
		kpiData.setUserId("id usu");

		return kpiData;
	}

	private KpiUaData initKpiUaData() {
		return new KpiUaData("uaGroup", "usSTring", "uaBrand", "uaModel", "uaBrowser", "uaBrowserVersion");
	}
}
