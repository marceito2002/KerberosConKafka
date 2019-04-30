package com.docomo.fraudwall.tracking.configuration;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigTrackingHbase {

	private static final Logger logger = LoggerFactory.getLogger(ConfigTrackingHbase.class);
	@Value("${hbase.zookeeper.quorum:localhost}")
	private String zookeeper;

	@Value("${hbase.zookeeper.clientPort:2181}")
	private String zookeeperPort;

	@Value("${hbase.kerberos.user:}")
	private String kerberosUser;

	@Value("${hbase.kerberos.principal:}")
	private String kerberosPrincipal;

	@Value("${hbase.kerberos.name:}")
	private String kerberosName;

	@Value("${hbase.kerberos.keytab:}")
	private String kerberosKeytab;

	@Bean
	public Connection connectHdfs() throws IOException {

		logger.info("readHdfs.: Establishing connection...");

		org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();

		conf.set("hbase.zookeeper.quorum", zookeeper);
		conf.set("hbase.zookeeper.property.clientPort", zookeeperPort);
		conf.set("hbase.cluster.distributed", "true");

		if (kerberosName.length()>0) {
			conf.set("hadoop.security.authentication", kerberosName);
			conf.set("hbase.security.authentication", kerberosName);
			conf.set("hbase.rpc.protection", "authentication");
			conf.set("hbase.master.kerberos.principal", kerberosPrincipal);
			conf.set("hbase.master.keytab.file", kerberosKeytab);
			conf.set("hbase.regionserver.kerberos.principal", kerberosPrincipal);
			conf.set("hbase.regionserver.keytab.file", kerberosKeytab);
		}

		logger.info("kerberosKeytab NAME{}, file keytab in.: {}.. kerberosName.: {}", kerberosPrincipal, kerberosKeytab,
				kerberosName);
		UserGroupInformation.setConfiguration(conf);
		if (kerberosName.length()>0) {
			UserGroupInformation.loginUserFromKeytab(kerberosUser, kerberosKeytab);
		}

		logger.info("readHdfs.: User configuration establixh");
		return ConnectionFactory.createConnection(HBaseConfiguration.create(conf));

	}

}
