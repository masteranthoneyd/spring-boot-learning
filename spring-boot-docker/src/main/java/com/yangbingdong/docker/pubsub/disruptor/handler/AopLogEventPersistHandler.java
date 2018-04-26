package com.yangbingdong.docker.pubsub.disruptor.handler;

import com.lmax.disruptor.EventHandler;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.domain.repository.AccessLogRepository;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AopLogEventPersistHandler implements EventHandler<AopLogEvent> {
	private final AccessLogRepository accessLogRepository;

	@Override
	public void onEvent(AopLogEvent event, long sequence, boolean endOfBatch) {
		AccessLog accessLog = event.getAccessLog()
								   .parseAndFillReqData();
		accessLogRepository.save(accessLog);
		log.info("Success persist access log!");
	}
}
