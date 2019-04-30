package com.docomo.fraudwall.tracking.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer.ControlFlag;

import com.docomo.fraudwall.tracking.business.TrackingBusiness;

@Configuration
public class ConfigTrackingKafka {
	@Value(value = "${kafka.bootstrap-servers:localhost}")
	private String bootstrapAddress;

	@Value(value = "${kafka.clientIdConfig:fraudwall-spring}")
	private String clientIdConfig;

	@Value(value = "${kafka.security:SASL_PLAINTEXT}")
	private String security;

	@Value(value = "${kafka.useKeyTab:true}")
	private String useKeyTab;

	@Value(value = "${kafka.storeKey:true}")
	private String storeKey;

	@Value(value = "${kafka.keyTabPlace:C:\\\\Docomo\\\\proyectos\\\\fraudwall\\\\hdp_b2bp_app.keytab}")
	private String keyTabPlace;

	@Value(value = "${kafka.keyTabUser:hdp_b2bp_app@AD.DOCOMODIGITAL.COM}")
	private String keyTabUser;

	@Value(value = "${kafka.kerberosServiceName:kafka}")
	private String kerberosServiceName;


	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientIdConfig);
		configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, security);
		configProps.put("sasl.kerberos.service.name", kerberosServiceName);
		configProps.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, null);

		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * 
	 *                     This values is the configuration of jaas.conf
	 */
	@Bean
	public KafkaJaasLoginModuleInitializer jaasConfig() throws IOException {
		KafkaJaasLoginModuleInitializer jaasConfig = new KafkaJaasLoginModuleInitializer();

		jaasConfig.setControlFlag(ControlFlag.REQUIRED);
		Map<String, String> options = new HashMap<>();
		options.put("useKeyTab", useKeyTab);
		options.put("storeKey", storeKey);
		options.put("keyTab", keyTabPlace);
		options.put("principal", keyTabUser);

		jaasConfig.setOptions(options);
		return jaasConfig;
	}

}
