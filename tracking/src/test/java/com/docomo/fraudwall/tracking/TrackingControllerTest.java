package com.docomo.fraudwall.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.docomo.fraudwall.domain.PartnerDomain;
import com.docomo.fraudwall.domain.SessionDomain;
import com.docomo.fraudwall.tracking.business.ITrackingBusiness;
import com.docomo.fraudwall.tracking.business.TrackingBusiness;
import com.docomo.fraudwall.tracking.business.kafka.KafkaBusiness;
import com.docomo.fraudwall.tracking.controller.TrackingController;
import com.docomo.fraudwall.tracking.generated.model.Kpis;
import com.docomo.fraudwall.tracking.generated.model.KpisUa;
import com.docomo.fraudwall.tracking.persistencia.mongodb.ISessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class TrackingControllerTest {

	@Autowired
	private TrackingController controller;

	@MockBean
	private ISessionRepository repositorySessionMck;

	@MockBean
	private TrackingBusiness bussinesTrackingMck;

	@MockBean
	private KafkaBusiness kafkaBusinessMck;

	@Autowired
	private MockMvc mockMvc;

	@Test
	/**
	 * Comprobar si esta el controlador en el contexto de spring
	 */
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
		// insertar();
	}

	@Test
	public void testCreateSessionIdError() throws Exception {
		this.mockMvc.perform(post("/partner/prueba").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isNotFound());
	}	
	
	@Test
	public void testCreateSessionPartnerNoExist() throws Exception {
		this.mockMvc.perform(post("/partner/1234").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isNotFound());
	}	
	
	@Test
	public void testGetFraudLevelNoExist() throws Exception {
		this.mockMvc.perform(get("/partner/fffffffffff/session/234234").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().is4xxClientError());
	}	
	
	@Test
	public void testGetSessionIdInfoNoExist() throws Exception {
		this.mockMvc.perform(get("/partner/partnernoexiste/session/234234").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().is4xxClientError());
	}	
	
	@Test
	public void testSendKpi() throws Exception {
	
		final ObjectMapper mapper = new ObjectMapper();

		this.mockMvc
				.perform(put("/partner").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(getDatosIn())))
				.andDo(print()).andExpect(status().isNotFound());

		
	}	
	
	@Test
	public void putMsgKafka() throws Exception {
		assertEquals(null,kafkaBusinessMck.sendMsgKafka("Mensaje Prueba"));
	}	


	private Kpis getDatosIn() {
		Kpis kpis = new Kpis();
		kpis.setAffiliate("name afiilate");
		kpis.setApp("nombre app");
		kpis.setCountry("spain");
		kpis.setIp("192.12.43.12");
		kpis.setMediaChannel("channel");
		kpis.setMerchant("merchan");
		kpis.setNetwork("ethernet");
		kpis.setOperator("orange");
		kpis.setPartnerTrid("22342");
		kpis.setPurchaseFlow("flow");
		kpis.setService("service2");
		kpis.setTimestampUtc("20171216 10:20:34:653");
		kpis.setUserId("aherreros");
		KpisUa  ua = new KpisUa();
		ua.setUaBrand("uaBrand");
		ua.setUaBrowser("uaBrowser");
		ua.setUaBrowserVersion("uaBrowserVersion");
		ua.setUaGroup("uaGroup");
		ua.setUaModel("uaModel");
		kpis.setUa(ua);
		
		return kpis;
	}
}
