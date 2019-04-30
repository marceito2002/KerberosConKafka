package com.docomo.fraudwall.kafka.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.docomo.fraudwall.kafka.generated.api.KafkaApi;
import com.docomo.fraudwall.kafka.generated.model.MsgProducer;
import com.docomo.fraudwall.kafka.generated.model.ResponseData;
import com.docomo.fraudwall.kafka.service.IServiceKafka;



@RestController
public class KafkaController implements KafkaApi{

	private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);
	
	
	@Autowired
	private IServiceKafka serviceKafka;
	
	
	@Override
	public ResponseEntity<ResponseData> producermsg(@RequestBody @Valid MsgProducer msgProducer) {

		logger.info("KafkaController: I am going to write in kafka the topic.: {} Msg.: {}", msgProducer.getTopic(), msgProducer.getMsg());

			
		String resul = serviceKafka.sendMsgKafka(msgProducer);

		ResponseData responseData = new ResponseData();
		responseData.setResponseMessage(resul);
		if (resul != null) {			
			return new ResponseEntity<>(responseData, HttpStatus.NOT_FOUND);			

		} else {
			return new ResponseEntity<>(responseData, HttpStatus.OK);

		}
	}


}
