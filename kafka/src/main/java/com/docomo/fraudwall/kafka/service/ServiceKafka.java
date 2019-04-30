package com.docomo.fraudwall.kafka.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docomo.fraudwall.kafka.business.IKafkaBusiness;
import com.docomo.fraudwall.kafka.generated.model.MsgProducer;



@Service
public class ServiceKafka implements IServiceKafka {

	@Autowired
	private IKafkaBusiness kafkaBusiness;

	@Override
	public String sendMsgKafka(MsgProducer msgProducer) {

		return kafkaBusiness.sendMsgKafka(msgProducer);
	}



}
