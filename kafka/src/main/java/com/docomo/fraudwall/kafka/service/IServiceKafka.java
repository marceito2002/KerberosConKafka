package com.docomo.fraudwall.kafka.service;

import com.docomo.fraudwall.kafka.generated.model.MsgProducer;

public interface IServiceKafka {
	String sendMsgKafka(MsgProducer msgProducer);
}
