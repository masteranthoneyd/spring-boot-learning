package com.yangbingdong.springbootdatajpa.dbmeta;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author ybd
 * @date 19-1-10
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class NativeConnectionProvider implements ConnectionProvider {

	private static final String MYSQL = "mysql";
	private String url;
	private String user;
	private String password;

	public NativeConnectionProvider(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
		if (!url.contains(MYSQL)) {
			throw new IllegalArgumentException("Only support MySQL!");
		}
	}

	@Override
	public Connection provideConnection() {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
