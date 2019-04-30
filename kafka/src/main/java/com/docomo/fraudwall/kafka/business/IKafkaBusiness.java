package com.docomo.fraudwall.kafka.business;

import com.docomo.fraudwall.kafka.generated.model.MsgProducer;

public interface IKafkaBusiness {
	String sendMsgKafka(MsgProducer msgProducer);
			
}
