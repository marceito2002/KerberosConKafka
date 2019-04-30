package com.docomo.fraudwall.kafka.business;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class interceptaKafka implements ProducerInterceptor<String, String>{

	private static final Logger logger = LoggerFactory.getLogger(interceptaKafka.class);

	
	
	@Override
	public void close() {
		logger.info("TrackingBusiness: writting message in kafka");
	}

	@Override
	public void configure(Map<String, ?> configs) {
		// TODO Auto-generated method stub
		logger.info("TrackingBusiness: writting message in kafka");
		
	}

	@Override
	public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
		logger.info("TrackingBusiness: writting message topic.: {}, Data.:{}", record.topic(), record.value() );
		return record;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		logger.info("TrackingBusiness: Error writting message in kafka");		
	}
}
