package com.docomo.fraudwall.kafka.business;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.docomo.fraudwall.kafka.generated.model.MsgProducer;

@Service
public class KafkaBusiness implements IKafkaBusiness {

	private static final Logger logger = LoggerFactory.getLogger(KafkaBusiness.class);


	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	

	@Override
	public String sendMsgKafka(MsgProducer msgProducer) {
		logger.info("TrackingBusiness: writting message in kafka {}.  {}", msgProducer.getMsg(), msgProducer.getTopic());
		kafkaTemplate.send(msgProducer.getTopic(),  msgProducer.getMsg());
		return null;
	}

	
	

}
