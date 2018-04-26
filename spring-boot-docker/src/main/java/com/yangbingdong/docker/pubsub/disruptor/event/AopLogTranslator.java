package com.yangbingdong.docker.pubsub.disruptor.event;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class AopLogTranslator implements EventTranslatorOneArg<AopLogEvent, AccessLog> {
	@Override
	public void translateTo(AopLogEvent event, long sequence, AccessLog accessLog) {
		event.setAccessLog(accessLog);
	}
}
