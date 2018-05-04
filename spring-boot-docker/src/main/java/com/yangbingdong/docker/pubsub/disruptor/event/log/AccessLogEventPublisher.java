package com.yangbingdong.docker.pubsub.disruptor.event.log;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.ProducerType;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.pubsub.disruptor.core.AbstractDisruptorPublisher;
import com.yangbingdong.docker.pubsub.disruptor.core.DefaultDisruptorCommonComponents;
import com.yangbingdong.docker.pubsub.disruptor.core.DisruptorEventHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class AccessLogEventPublisher extends AbstractDisruptorPublisher<AccessLog, AccessLogEvent> {

	@Resource
	private List<DisruptorEventHandler<AccessLogEvent>> eventHandlers = new ArrayList<>(16);

	@Override
	protected void customDisruptorComponents(DefaultDisruptorCommonComponents components) {
		components.setProducerType(ProducerType.MULTI);
	}

	@Override
	protected EventFactory<AccessLogEvent> provideEventFactory() {
		return AccessLogEvent::new;
	}

	@Override
	protected List<DisruptorEventHandler<AccessLogEvent>> provideDisruptorEventHandlers() {
		return eventHandlers;
	}

	@Override
	protected EventTranslatorOneArg<AccessLogEvent, AccessLog> provideTranslatorOneArg() {
		return (event, sequence, accessLog) -> event.setSource(accessLog);
	}
}
