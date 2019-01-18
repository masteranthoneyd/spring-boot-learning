package com.yangbingdong.docker.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

/**
 * @author ybd
 * @date 19-1-16
 * @contact yangbingdong1994@gmail.com
 */
public class PrettySqlMultiLineFormat implements MessageFormattingStrategy {
	private static final Formatter FORMATTER = new BasicFormatterImpl();

	@Override
	public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
		return "#" + now + " | took " + elapsed + "ms | " + category + " | connection " + connectionId + " | " + FORMATTER.format(sql) +";";
	}
}
