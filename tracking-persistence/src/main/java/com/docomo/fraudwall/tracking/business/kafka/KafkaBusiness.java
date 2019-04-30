package com.docomo.fraudwall.tracking.business.kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaBusiness implements IKafkaBusiness {


	@Value("${kafka.topic:HDP_FW_TRACK}")
	private String kafkaTopic;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	

	@Override
	public String sendMsgKafka(String msgProducer) {		
		kafkaTemplate.send(kafkaTopic, msgProducer);
		return null;
	}

	
	

}
