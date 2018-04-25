package com.yangbingdong.docker.pubsub.application.listener;

import com.yangbingdong.docker.pubsub.application.event.PersistAccessLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author ybd
 * @date 18-4-20
 * @contact yangbingdong1994@gmail.com
 */
@Component
@Slf4j
public class DomainEventListener {
	@Async
	@TransactionalEventListener(PersistAccessLogEvent.class)
	public void processSaveMsgEvent(PersistAccessLogEvent persistAccessLogEvent) throws InterruptedException {
//		TimeUnit.MILLISECONDS.sleep(1000);
//		log.info("Listening PersistAccessLogEvent: {}", persistAccessLogEvent);
		log.info("Listening PersistAccessLogEvent");
	}
}
