package com.yangbingdong.docker.pubsub.disruptor.handler;

import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.domain.repository.AccessLogRepository;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AopLogEventPersistHandler extends AbstractShardingHandler<AopLogEvent> {
	public static final AtomicInteger HANDLE_ATOMIC_INTEGER = new AtomicInteger(0);
	private final AccessLogRepository accessLogRepository;

	@Override
	public void onShardingEvent(AopLogEvent event, long sequence, boolean endOfBatch, int currentShard) throws Exception {
		HANDLE_ATOMIC_INTEGER.incrementAndGet();
		AccessLog accessLog = event.getAccessLog()
								   .parseAndFillReqData();
		accessLogRepository.save(accessLog);
		log.info("Success persist access log! sequence: {}, current shard: {}", sequence, this.currentShard);
	}

	@Override
	public AbstractShardingHandler addShardingQuantity() {
		this.shardingQuantity = 3;
		return this;
	}


	@Override
	public int horizontalOrder() {
		return 0;
	}
}
