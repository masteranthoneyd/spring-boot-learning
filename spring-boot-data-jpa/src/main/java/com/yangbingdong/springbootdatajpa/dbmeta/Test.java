package com.yangbingdong.springbootdatajpa.dbmeta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * @author ybd
 * @date 19-1-9
 * @contact yangbingdong1994@gmail.com
 */
public class Test {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DATA_URL = "jdbc:mysql://localhost:3306/jpa_test?useSSL=false";

	// 数据库的用户名与密码，需要根据自己的设置
	static final String USER = "root";
	static final String PASS = "root";

	public static void main(String[] args) throws Exception {
		Class.forName(JDBC_DRIVER);

		// 打开链接
		System.out.println("连接数据库...");
		Connection conn = DriverManager.getConnection(DATA_URL,USER,PASS);
		ResultSet rs = conn.getMetaData().getIndexInfo(null, null, "test_table", false, false);
		while (rs.next()) {
			String idxName = rs.getString("INDEX_NAME");
			if (idxName.equals("PRIMARY")) {
				continue;
			}
			String colName = rs.getString("COLUMN_NAME");
			System.out.println(colName);
		}
		rs.close();
	}
}
