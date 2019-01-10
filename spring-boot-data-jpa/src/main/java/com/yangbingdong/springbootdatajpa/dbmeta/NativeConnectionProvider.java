package com.yangbingdong.springbootdatajpa.dbmeta;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ybd
 * @date 19-1-10
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class NativeConnectionProvider implements ConnectionProvider {

	private static final String MYSQL = "mysql";
	private Connection connection;

	public NativeConnectionProvider(String url, String user, String password) {
		if (!url.contains(MYSQL)) {
			throw new IllegalArgumentException("Only support MySQL!");
		}
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Connection provideConnection() {
		return connection;
	}

	@Override
	public void close() throws SQLException {
		if (connection != null) {
//			log.info("Closing connection......");
			connection.close();
		}
	}
}
