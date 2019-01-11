package com.yangbingdong.springbootdatajpa.dbmeta;

import java.sql.Connection;

/**
 * @author ybd
 * @date 19-1-10
 * @contact yangbingdong1994@gmail.com
 */
public interface ConnectionProvider {

	Connection provideConnection();

}
