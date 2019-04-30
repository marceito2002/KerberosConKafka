package com.docomo.fraudwall.tracking.business.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReadHBase implements IReadHbase {

	private static final Logger logger = LoggerFactory.getLogger(ReadHBase.class);

	private static final byte[] CF = "cf1".getBytes();
	private static final byte[] CQ1 = "json".getBytes();

	@Autowired
	Connection connectHdf;

	@Value("${hbase.table.name:testalf}")
	private String tableName;

	@Value("${hbase.table.nameSpace:fraudwall}")
	private String nameSpace;

	@Override
	public String readDataHdfs(String sessionId) {
		try {
			
			String data = "";
			
			logger.info("readHdfs.: Asccessing to namespace.:{} and table name.:{} and SESSIONid is.:{}", nameSpace, tableName, sessionId);
			Table table = connectHdf.getTable(TableName.valueOf(nameSpace, tableName));
			logger.info("readHdfs.: Succesfull");
	
			Result result = table.get(new Get(Bytes.toBytes(sessionId)).addColumn(CF, CQ1));
			logger.info("readHdfs.: reading data");

			if (result != null && result.getRow() != null) {
				data = Bytes.toString(result.getValue(CF, CQ1));
				return data;
			}
			logger.info("readHdfs.: there is not data");
			return data;

		} catch (IOException e) {
			logger.info("readDataHdfs: Error access HBase.:{}", e);
		}
		return null;
	}

}
