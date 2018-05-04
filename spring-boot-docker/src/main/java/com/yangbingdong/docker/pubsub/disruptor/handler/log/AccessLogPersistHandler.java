package com.yangbingdong.docker.pubsub.disruptor.handler.log;

import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.domain.repository.AccessLogRepository;
import com.yangbingdong.docker.pubsub.disruptor.core.DisruptorEventHandler;
import com.yangbingdong.docker.pubsub.disruptor.event.log.AccessLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessLogPersistHandler implements DisruptorEventHandler<AccessLogEvent> {
	private final AccessLogRepository accessLogRepository;

	@Override
	public int order() {
		return Integer.MIN_VALUE;
	}

	@Override
	public void onEvent(AccessLogEvent event, long sequence, boolean endOfBatch, int currentShard) throws Exception {
		AccessLog accessLog = event.getSource()
								   .parseAndFillReqData();
		accessLogRepository.save(accessLog);
		log.info("Success persist access log! sequence: {}, current shard: {}", sequence, currentShard);
	}

	@Override
	public boolean enableSharding() {
		return true;
	}

	@Override
	public int shardingQuantity() {
		return 3;
	}
}
